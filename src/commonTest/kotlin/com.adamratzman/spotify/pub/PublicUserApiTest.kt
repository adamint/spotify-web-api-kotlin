/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.utils.catch
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.test.TestResult

class PublicUserApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testPublicUser(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testPublicUser.name)

        assertTrue(catch { api.users.getProfile("adamratzman1")!!.followers.total } != null)
        assertNull(api.users.getProfile("non-existant-user"))
    }
}
