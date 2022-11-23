/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class PublicTracksApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetTrack() = runTestOnDefaultDispatcher {
        buildApi()

        assertEquals("Bénabar", api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop")!!.artists[0].name)
        assertNull(api.tracks.getTrack("nonexistant track"))
    }

    @Test
    fun testGetTracks() = runTestOnDefaultDispatcher {
        buildApi()

        assertEquals(listOf(null, null), api.tracks.getTracks("hi", "dad", market = Market.US))
        assertEquals(
            listOf("Alors souris", null),
            api.tracks.getTracks("0o4jSZBxOQUiDKzMJSqR4x", "j").map { it?.name })
    }

    @Test
    fun testAudioAnalysis() = runTestOnDefaultDispatcher {
        buildApi()

        assertFailsWith<SpotifyException.BadRequestException> { api.tracks.getAudioAnalysis("bad track") }
        assertEquals("165.61333", api.tracks.getAudioAnalysis("0o4jSZBxOQUiDKzMJSqR4x").track.duration.toString())
    }

    @Test
    fun testAudioFeatures() = runTestOnDefaultDispatcher {
        buildApi()

        assertFailsWith<SpotifyException.BadRequestException> { api.tracks.getAudioFeatures("bad track") }
        assertEquals("0.0592", api.tracks.getAudioFeatures("6AH3IbS61PiabZYKVBqKAk").acousticness.toString())
        assertEquals(
            listOf(null, "0.0592"),
            api.tracks.getAudioFeatures("hkiuhi", "6AH3IbS61PiabZYKVBqKAk").map { it?.acousticness?.toString() })
        assertTrue(api.tracks.getAudioFeatures("bad track", "0o4jSZBxOQUiDKzMJSqR4x").let {
            it[0] == null && it[1] != null
        })
    }
}
