/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import com.adamratzman.spotify.utils.catch
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicUserApiTest {
    lateinit var api: GenericSpotifyApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { api = it }
        return ::api.isInitialized
    }

    @Test
    fun testPublicUser() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(catch { api.users.getProfile("adamratzman1")!!.followers.total } != null)
            assertNull(api.users.getProfile("non-existant-user"))
        }
    }
}
