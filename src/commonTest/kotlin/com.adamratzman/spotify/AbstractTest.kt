/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify

import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class AbstractTest<T : GenericSpotifyApi> {
    lateinit var api: T
    var apiInitialized: Boolean = false
    val testClassQualifiedName = this::class.simpleName!!

    suspend inline fun <reified Z : T> buildApi(testName: String) {
        if (apiInitialized) return
        var requestNumber = 0 // local to the specific test. used to fake request responses

        val api = buildSpotifyApi(testClassQualifiedName, testName)
        if (api != null && api is Z) {
            api.spotifyApiOptions.retryOnInternalServerErrorTimes = 10
            api.spotifyApiOptions.httpResponseSubscriber = { request, response ->
                getResponseCacher()?.cacheResponse(
                    testClassQualifiedName,
                    testName,
                    requestNumber,
                    request,
                    response
                )
                requestNumber++
            }

            this.api = api
            apiInitialized = true
        }
    }

    suspend fun isApiInitialized(): Boolean {
        return if (apiInitialized) {
            true
        } else {
            println("Api is not initialized or does not match the expected type. buildSpotifyApi returns ${buildSpotifyApi(testClassQualifiedName, "n/a")}")
            false
        }
    }
}
