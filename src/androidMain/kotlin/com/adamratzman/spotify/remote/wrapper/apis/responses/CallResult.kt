package com.adamratzman.spotify.remote.wrapper.apis.responses

import com.spotify.protocol.client.ErrorCallback
import com.spotify.protocol.client.PendingResult
import com.spotify.protocol.client.Result
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal fun <T, R> com.spotify.protocol.client.CallResult<T>.toLibraryCallResult(
    rProducer: (T) -> R,
    callback: ((R) -> Unit)?
): CallResult<T, R> {
    return CallResult(
        this,
        rProducer,
        callback
    )
}

/**
 * Represents a wrapped Spotify CallResult that produces a wrapped response and that can be obtained asynchronously or
 * synchronous via coroutines through [execute].
 *
 * This is separated from the base type as eventually the dependency on the remote api will be removed.
 *
 * @param T The original type upon which this CallResult is based.
 * @param R The new type to be returned
 * @param innerCallResult The call result with which this is associated.
 * @param rProducer The producer that maps T -> R.
 */
public class CallResult<T, R>(
    private val innerCallResult: com.spotify.protocol.client.CallResult<T>,
    private val rProducer: (T) -> R,
    callback: ((R) -> Unit)? = null
) : PendingResult<R> {
    /**
     * The callback to execute, if any, after the value is obtained.
     */
    public var callback: ((R) -> Unit)? = callback
        set(value) {
            field = value
            innerCallResult.setResultCallback { tData -> value?.invoke(rProducer.invoke(tData)) }
        }

    /**
     * Obtain the value in a coroutine without blocking.
     */
    public suspend fun execute(): R =
        suspendCoroutine { continuation ->
            innerCallResult.setResultCallback {
                continuation.resume(rProducer.invoke(it))
            }

            setErrorCallback {
                continuation.resumeWithException(it as Exception);
            }
        }

    /**
     * Cancel the call. This should not be used.
     */
    override fun cancel(): Unit = innerCallResult.cancel()

    /**
     * Whether the call has been cancelled.
     */
    override fun isCanceled(): Boolean = innerCallResult.isCanceled

    /**
     * Whether to synchronously block and wait for the result. [execute] is recommended instead.
     */
    override fun await(): Result<R> {
        val innerResult = innerCallResult.await()
        return WrappedResult<T, R>(
            innerResult,
            rProducer.invoke(innerResult.data)
        )
    }

    /**
     * Whether to synchronously block and wait for the result, with a given timeout. [execute] is recommended instead.
     *
     * @param interval The amount of [timeUnit] to wait before timing out.
     * @param timeUnit The time unit to use with [interval].
     */
    override fun await(interval: Long, timeUnit: TimeUnit): Result<R> {
        val innerResult = innerCallResult.await(interval, timeUnit)
        return WrappedResult<T, R>(
            innerResult,
            rProducer.invoke(innerResult.data)
        )
    }

    /**
     * Set the error callback, if using callbacks for result, to be executed on call fail.
     *
     * @param errorCallback Code to be synchronously run on request fail.
     */
    override fun setErrorCallback(errorCallback: ErrorCallback): PendingResult<R> {
        innerCallResult.setErrorCallback(errorCallback)
        return this
    }
}

internal class WrappedResult<T, R>(
    private val innerResult: Result<T>,
    private val newData: R
) : Result<R> {
    override fun getData(): R = newData
    override fun isSuccessful(): Boolean = innerResult.isSuccessful
    override fun getErrorMessage(): String = innerResult.errorMessage
    override fun getError(): Throwable = innerResult.error
}