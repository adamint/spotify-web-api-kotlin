/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.Market
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicTracksAPITest : Spek({
    describe("Track API (Public View) test") {
        val t = api.tracks
        describe("get track") {
            it("known track should return author name") {
                assertEquals("Bénabar", t.getTrack("5OT3k9lPxI2jkaryRK3Aop").complete()!!.artists[0].name)
            }
            it("unknown track id should be null") {
                assertNull(t.getTrack("nonexistant track").complete())
            }
        }
        describe("get tracks") {
            it("unknown tracks") {
                assertEquals(listOf(null, null), t.getTracks("hi", "dad", market = Market.US).complete())
            }
            it("mix of known tracks") {
                assertEquals(listOf("Alors souris", null), t.getTracks("0o4jSZBxOQUiDKzMJSqR4x", "j").complete().map { it?.name })
            }
        }
        describe("audio analysis") {
            it("unknown track") {
                assertFailsWith<SpotifyException.BadRequestException> { t.getAudioAnalysis("bad track").complete() }
            }
            it("known track") {
                assertEquals("165.61333", t.getAudioAnalysis("0o4jSZBxOQUiDKzMJSqR4x").complete().track.duration.toString())
            }
        }
        describe("audio features") {
            it("unknown track") {
                assertFailsWith<SpotifyException.BadRequestException> { t.getAudioFeatures("bad track").complete() }
            }
            it("known track") {
                assertEquals("0.0589", t.getAudioFeatures("6AH3IbS61PiabZYKVBqKAk").complete().acousticness.toString())
            }
            it("multiple tracks (all known)") {
                assertEquals(listOf(null, "0.0589"), t.getAudioFeatures("hkiuhi", "6AH3IbS61PiabZYKVBqKAk").complete().map { it?.acousticness?.toString() })
            }
            it("mix of known and unknown tracks") {
                assertTrue(t.getAudioFeatures("bad track", "0o4jSZBxOQUiDKzMJSqR4x").complete().let {
                    it[0] == null && it[1] != null
                })
            }
        }
    }
})
