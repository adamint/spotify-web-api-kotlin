package com.adamratzman.spotify.public

import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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
                assertThrows<BadRequestException> { t.getAudioAnalysis("bad track").complete() }
            }
            it("known track") {
                assertEquals("165.61333", t.getAudioAnalysis("0o4jSZBxOQUiDKzMJSqR4x").complete().track.duration.toString())
            }
        }
        describe("audio features") {
            it("unknown track") {
                assertThrows<BadRequestException> { t.getAudioFeatures("bad track").complete() }
            }
            it("known track") {
                assertEquals("0.0589", t.getAudioFeatures("6AH3IbS61PiabZYKVBqKAk").complete().acousticness.toString())
            }
            it("multiple tracks (all known)") {
                assertEquals(listOf(null, "0.0589"), t.getAudioFeatures("", "6AH3IbS61PiabZYKVBqKAk").complete().map { it?.acousticness?.toString() })
            }
            it("mix of known and unknown tracks") {
                assert(t.getAudioFeatures("bad track", "0o4jSZBxOQUiDKzMJSqR4x").complete().let {
                    it[0] == null && it[1] != null
                })
            }
        }
    }
})