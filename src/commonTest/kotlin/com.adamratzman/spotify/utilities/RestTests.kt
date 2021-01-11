/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.TimeoutException
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyAppApi
import kotlin.test.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@SpotifyExperimentalHttpApi
class RestTests {
    lateinit var api: GenericSpotifyApi

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testRequestTimeoutFailure() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val testApi = spotifyAppApi(null, null, SpotifyUserAuthorization(token = api.token)).build()
            val prevTimeout = testApi.spotifyApiOptions.requestTimeoutMillis

            testApi.spotifyApiOptions.requestTimeoutMillis = 1
            assertFailsWithSuspend<TimeoutException> {
                testApi.search.searchTrack("fail")
            }

            testApi.spotifyApiOptions.requestTimeoutMillis = prevTimeout
        }
    }
}
