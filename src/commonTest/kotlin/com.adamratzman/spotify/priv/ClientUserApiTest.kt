/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test

class ClientUserApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testClientProfile(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testClientProfile.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        api.users.getClientProfile().displayName
    }
}
