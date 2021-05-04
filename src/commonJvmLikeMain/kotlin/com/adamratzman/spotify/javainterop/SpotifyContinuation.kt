/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:JvmName("SpotifyContinuation")

package com.adamratzman.spotify.javainterop

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A [Continuation] wrapper to allow you to directly implement [onSuccess] and [onFailure], when exceptions are hidden
 * on JVM via traditional continuations. **Please use this class as a callback anytime you are using Java code with this library.**
 *
 */
public abstract class SpotifyContinuation<in T> : Continuation<T> {
    /**
     * Invoke a function with the callback [value]
     *
     * @param value The value retrieved from the Spotify API.
     */
    public abstract fun onSuccess(value: T)

    /**
     * Handle exceptions during this API call.
     *
     * @param exception The exception that was thrown during the call.
     */
    public abstract fun onFailure(exception: Throwable)

    override fun resumeWith(result: Result<T>) {
        result.fold(::onSuccess, ::onFailure)
    }

    override val context: CoroutineContext = EmptyCoroutineContext
}
