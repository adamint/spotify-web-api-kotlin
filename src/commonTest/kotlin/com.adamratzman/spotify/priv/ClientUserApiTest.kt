/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import kotlin.test.Test

class ClientUserApiTest {
    lateinit var api: SpotifyClientApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { it as? SpotifyClientApi }?.let { api = it }
        return ::api.isInitialized
    }

    @Test
    fun testClientProfile() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            api.users.getClientProfile().displayName
        }
    }
}
