/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify

import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class AbstractTest<T : GenericSpotifyApi> {
    lateinit var api: T
    var apiInitialized: Boolean = false

    suspend inline fun <reified Z : T> buildApi() {
        if (apiInitialized) return

        val api = buildSpotifyApi()
        if (api != null && api is Z) {
            this.api = api
            apiInitialized = true
        }
    }

    suspend fun isApiInitialized(): Boolean {
        return if (apiInitialized) true
        else {
            println("Api is not initialized. buildSpotifyApi returns ${buildSpotifyApi()}")
            false
        }
    }
}