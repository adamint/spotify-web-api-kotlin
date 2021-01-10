/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test

class ClientUserApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testClientProfile() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            api.users.getClientProfile().displayName
        }
    }
}
