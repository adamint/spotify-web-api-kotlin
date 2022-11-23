/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class ClientUserApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testClientProfile() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        api.users.getClientProfile().displayName
    }
}
