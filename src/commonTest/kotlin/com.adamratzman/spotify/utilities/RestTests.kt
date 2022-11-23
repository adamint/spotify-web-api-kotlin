/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.TimeoutException
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.spotifyAppApi
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

@ExperimentalTime
@SpotifyExperimentalHttpApi
class RestTests {
    var api: GenericSpotifyApi? = null

    fun testPrereq() = api != null

    @Test
    fun testRequestTimeoutFailure() = runTestOnDefaultDispatcher {
        buildSpotifyApi()?.let { api = it }

        val testApi = spotifyAppApi(null, null, SpotifyUserAuthorization(token = api!!.token)).build()
        val prevTimeout = testApi.spotifyApiOptions.requestTimeoutMillis

        testApi.spotifyApiOptions.requestTimeoutMillis = 1
        assertFailsWith<TimeoutException> {
            testApi.search.searchTrack("fail")
        }

        testApi.spotifyApiOptions.requestTimeoutMillis = prevTimeout
    }
}
