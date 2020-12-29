/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicTracksApiTest {
    lateinit var api: GenericSpotifyApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { api = it }
        return ::api.isInitialized
    }

    @Test
    fun testGetTrack() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertEquals("Bénabar", api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop")!!.artists[0].name)
            assertNull(api.tracks.getTrack("nonexistant track"))
        }
    }

    @Test
    fun testGetTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertEquals(listOf(null, null), api.tracks.getTracks("hi", "dad", market = Market.US))
            assertEquals(listOf("Alors souris", null), api.tracks.getTracks("0o4jSZBxOQUiDKzMJSqR4x", "j").map { it?.name })
        }
    }

    @Test
    fun testAudioAnalysis() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.tracks.getAudioAnalysis("bad track") }
            assertEquals("165.61333", api.tracks.getAudioAnalysis("0o4jSZBxOQUiDKzMJSqR4x").track.duration.toString())
        }
    }

    @Test
    fun testAudioFeatures() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.tracks.getAudioFeatures("bad track") }
            assertEquals("0.0592", api.tracks.getAudioFeatures("6AH3IbS61PiabZYKVBqKAk").acousticness.toString())
            assertEquals(listOf(null, "0.0592"), api.tracks.getAudioFeatures("hkiuhi", "6AH3IbS61PiabZYKVBqKAk").map { it?.acousticness?.toString() })
            assertTrue(api.tracks.getAudioFeatures("bad track", "0o4jSZBxOQUiDKzMJSqR4x").let {
                it[0] == null && it[1] != null
            })
        }
    }
}
