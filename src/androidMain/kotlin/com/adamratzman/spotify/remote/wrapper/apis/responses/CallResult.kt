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

public class CallResult<T, R>(
    private val innerCallResult: com.spotify.protocol.client.CallResult<T>,
    private val rProducer: (T) -> R,
    callback: ((R) -> Unit)? = null
) : PendingResult<R> {
    public var callback: ((R) -> Unit)? = callback
        set(value) {
            field = value
            innerCallResult.setResultCallback { tData -> value?.invoke(rProducer.invoke(tData)) }
        }

    public suspend fun execute(): R =
        suspendCoroutine { continuation ->
            innerCallResult.setResultCallback {
                continuation.resume(rProducer.invoke(it))
            }

            setErrorCallback {
                continuation.resumeWithException(it as Exception);
            }
        }

    override fun cancel(): Unit = innerCallResult.cancel()

    override fun isCanceled(): Boolean = innerCallResult.isCanceled

    override fun await(): Result<R> {
        val innerResult = innerCallResult.await()
        return WrappedResult<T, R>(
            innerResult,
            rProducer.invoke(innerResult.data)
        )
    }

    override fun await(interval: Long, timeUnit: TimeUnit): Result<R> {
        val innerResult = innerCallResult.await(interval, timeUnit)
        return WrappedResult<T, R>(
            innerResult,
            rProducer.invoke(innerResult.data)
        )
    }

    override fun setErrorCallback(errorCallback: ErrorCallback?): PendingResult<R> {
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