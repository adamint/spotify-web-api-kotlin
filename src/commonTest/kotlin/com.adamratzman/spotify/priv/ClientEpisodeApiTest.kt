/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class ClientEpisodeApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetEpisode() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertNull(api.episodes.getEpisode("nonexistant episode"))
        assertNotNull(api.episodes.getEpisode("3lMZTE81Pbrp0U12WZe27l"))
    }

    @Test
    fun testGetEpisodes() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertFailsWith<BadRequestException> { api.episodes.getEpisodes("hi", "dad") }
        assertFailsWith<BadRequestException> {
            api.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j").map { it?.name }
        }
        assertEquals(
            listOf("The Great Inflation (Classic)"),
            api.episodes.getEpisodes("3lMZTE81Pbrp0U12WZe27l").map { it?.name }
        )
    }
}
