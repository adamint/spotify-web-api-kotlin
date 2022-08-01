package com.adamratzman.spotify.remote.wrapper.apis.responses

import com.spotify.protocol.client.ErrorCallback
import com.spotify.protocol.client.PendingResult
import com.spotify.protocol.client.Result
import com.spotify.protocol.client.Subscription.LifecycleCallback
import java.util.concurrent.TimeUnit

/**
 * Wrapped [com.spotify.protocol.client.Subscription], which represents a value for which you can subscribe to changes.
 *
 * This is separated from the base type as eventually the dependency on the remote api will be removed.
 *
 * @param innerSubscription The subscription with which this is associated.
 * @param rProducer The producer that maps T -> R.
 */
public class Subscription<T, R>(
    private val innerSubscription: com.spotify.protocol.client.Subscription<T>,
    private val rProducer: (T) -> R
) : PendingResult<T> {
    private var onStopCallback: (() -> Unit)? = null
    private var onStartCallback: (() -> Unit)? = null

    /**
     * Cancel the subscription.
     */
    public override fun cancel(): Unit = innerSubscription.cancel()

    /**
     * Return whether the subscription has been cancelled.
     */
    public override fun isCanceled(): Boolean = innerSubscription.isCanceled

    /**
     * Synchronously wait for a value to be produced. Instead, please use [setEventCallback] where possible.
     */
    public override fun await(): Result<T> = innerSubscription.await()

    /**
     * Synchronously wait for a value to be produced, with a specified timeout. Instead, please use [setEventCallback] where possible.
     *
     * @param interval The amount of [timeUnit] to wait before timing out.
     * @param timeUnit The time unit to use with [interval].
     */
    public override fun await(interval: Long, timeUnit: TimeUnit): Result<T> =
        innerSubscription.await(interval, timeUnit)

    /**
     * Set the error callback for this subscription. Please use [setEventCallback] instead.
     */
    override fun setErrorCallback(errorCallback: ErrorCallback?): PendingResult<T> =
        innerSubscription.setErrorCallback(errorCallback)

    /**
     * Set the event callback for this subscription.
     *
     * @param callback The callback to invoke on value receive.
     */
    public fun setEventCallback(callback: (R) -> Unit) {
        innerSubscription.setEventCallback { t ->
            callback(
                rProducer.invoke(t)
            )
        }
    }

    /**
     * Set the error callback for this subscription.
     *
     * @param callback The error callback to invoke on value receive.
     */
    public fun setErrorCallback(callback: (exception: Throwable) -> Unit) {
        setErrorCallback(ErrorCallback { callback(it) })
    }

    /**
     * Set the callback to be invoked when the subscription begins.
     *
     * @param onStartCallback The callback to be invoked.
     */
    public fun setOnStartCallback(onStartCallback: () -> Unit) {
        this.onStartCallback = onStartCallback
        setLifecycleCallback()
    }

    /**
     * Set the callback to be invoked when the subscription ends.
     *
     * @param onStopCallback The callback to be invoked.
     */
    public fun setOnStopCallback(onStopCallback: () -> Unit) {
        this.onStopCallback = onStopCallback
        setLifecycleCallback()
    }

    private fun setLifecycleCallback() {
        innerSubscription.setLifecycleCallback(object : LifecycleCallback {
            override fun onStart() {
                this@Subscription.onStartCallback?.invoke()
            }

            override fun onStop() {
                this@Subscription.onStopCallback?.invoke()
            }
        })
    }
}