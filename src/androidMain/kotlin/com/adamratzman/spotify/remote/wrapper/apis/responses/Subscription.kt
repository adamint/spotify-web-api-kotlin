package com.adamratzman.spotify.remote.wrapper.apis.responses

import com.spotify.protocol.client.ErrorCallback
import com.spotify.protocol.client.PendingResult
import com.spotify.protocol.client.Result
import com.spotify.protocol.client.Subscription.LifecycleCallback
import java.util.concurrent.TimeUnit

public class Subscription<T, R>(
    private val innerSubscription: com.spotify.protocol.client.Subscription<T>,
    private val rProducer: (T) -> R
) : PendingResult<T> {
    private var onStopCallback: (() -> Unit)? = null
    private var onStartCallback: (() -> Unit)? = null

    public override fun cancel(): Unit = innerSubscription.cancel()
    public override fun isCanceled(): Boolean = innerSubscription.isCanceled
    public override fun await(): Result<T> = innerSubscription.await()
    public override fun await(interval: Long, timeUnit: TimeUnit): Result<T> =
        innerSubscription.await(interval, timeUnit)

    public override fun setErrorCallback(errorCallback: ErrorCallback?): PendingResult<T> =
        innerSubscription.setErrorCallback(errorCallback)

    public fun setEventCallback(callback: (R) -> Unit) {
        innerSubscription.setEventCallback { t ->
            callback(
                rProducer.invoke(t)
            )
        }
    }

    public fun setOnStartCallback(onStartCallback: () -> Unit) {
        this.onStartCallback = onStartCallback
        setLifecycleCallback()
    }

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