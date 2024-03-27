/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClientEpisodeApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetEpisode(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetEpisode.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertNull(api.episodes.getEpisode("nonexistant episode"))
        assertNotNull(api.episodes.getEpisode("3lMZTE81Pbrp0U12WZe27l"))
    }

    @Test
    fun testGetEpisodes(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetEpisodes.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertFailsWith<BadRequestException> { api.episodes.getEpisodes("hi", "dad") }
        assertFailsWith<BadRequestException> { api.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j")[1] }

        assertEquals(
            listOf("The Great Inflation (Classic)"),
            api.episodes.getEpisodes("3lMZTE81Pbrp0U12WZe27l").map { it?.name }
        )
    }
}
