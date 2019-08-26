/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.models.BadRequestException
import com.neovisionaries.i18n.CountryCode
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class BrowseAPITest : Spek({
    describe("Browse test") {
        val b = api.browse
        it("get genre seeds") {
            assertTrue(b.getAvailableGenreSeeds().complete().isNotEmpty())
        }

        it("get category list") {
            assertEquals(b.getCategoryList(locale = "BAD_LOCALE").complete()[0], b.getCategoryList().complete()[0])
            assertTrue(b.getCategoryList(4, 3, locale = "fr_FR", market = CountryCode.CA).complete().isNotEmpty())
        }

        it("get category") {
            assertNotNull(b.getCategory("pop").complete())
            assertNotNull(b.getCategory("pop", CountryCode.FR).complete())
            assertNotNull(b.getCategory("pop", CountryCode.FR, locale = "en_US").complete())
            assertNotNull(b.getCategory("pop", CountryCode.FR, locale = "KSDJFJKSJDKF").complete())
            assertThrows<BadRequestException> { b.getCategory("no u", CountryCode.US).complete() }
        }

        it("get playlists by category") {
            assertThrows<BadRequestException> { b.getPlaylistsForCategory("no u", limit = 4).complete() }
            assertTrue(b.getPlaylistsForCategory("pop", 10, 0, CountryCode.FR).complete().isNotEmpty())
        }

        it("get featured playlists") {
            assertTrue(b.getFeaturedPlaylists(5, 4, market = CountryCode.US, timestamp = System.currentTimeMillis() - 1000000).complete().playlists.total > 0)
            assertTrue(b.getFeaturedPlaylists(offset = 32).complete().playlists.total > 0)
        }

        it("get new releases") {
            assertTrue(b.getNewReleases(market = CountryCode.CA).complete().isNotEmpty())
            assertTrue(b.getNewReleases(limit = 1, offset = 3).complete().isNotEmpty())
            assertTrue(b.getNewReleases(limit = 6, offset = 44, market = CountryCode.US).complete().isNotEmpty())
        }

        describe("get recommendations") {
            it("no parameters") {
                assertThrows<BadRequestException> { b.getTrackRecommendations().complete() }
            }
            it("seed artists") {
                assertThrows<BadRequestException> { b.getTrackRecommendations(seedArtists = listOf("abc")).complete() }
                assertTrue(b.getTrackRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89")).complete().tracks.isNotEmpty())
                assertTrue(b.getTrackRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89", "7lMgpN1tEBQKpRoUMKB8iw")).complete().tracks.isNotEmpty())
            }
            it("seed tracks") {
                assertThrows<BadRequestException> { b.getTrackRecommendations(seedTracks = listOf("abc")).complete() }
                assertTrue(b.getTrackRecommendations(seedTracks = listOf("3Uyt0WO3wOopnUBCe9BaXl")).complete().tracks.isNotEmpty())
                assertTrue(b.getTrackRecommendations(seedTracks = listOf("6d9iYQG2JvTTEgcndW81lt", "3Uyt0WO3wOopnUBCe9BaXl")).complete().tracks.isNotEmpty())
            }
            it("seed genres") {
                assertDoesNotThrow { b.getTrackRecommendations(seedGenres = listOf("abc")).complete() }
                assertTrue(b.getTrackRecommendations(seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
                assertTrue(b.getTrackRecommendations(seedGenres = listOf("pop", "latinx")).complete().tracks.isNotEmpty())
            }
            it("multiple seed types") {
                assertDoesNotThrow {
                    b.getTrackRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89"),
                            seedTracks = listOf("6d9iYQG2JvTTEgcndW81lt", "3Uyt0WO3wOopnUBCe9BaXl"),
                            seedGenres = listOf("pop")).complete()
                }
            }
            it("target attributes") {
                assertThrows<IllegalArgumentException> {
                    b.getTrackRecommendations(targetAttributes = listOf(TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(3f))).complete()
                }
                assertTrue(b.getTrackRecommendations(
                        targetAttributes = listOf(TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(1f), TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.5f)),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
            }
            it("min attributes") {
                assertThrows<IllegalArgumentException> {
                    b.getTrackRecommendations(minAttributes = listOf(TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(3f))).complete()
                }
                assertTrue(b.getTrackRecommendations(
                        minAttributes = listOf(TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(0.5f), TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.5f)),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
            }
            it("max attributes") {
                assertThrows<BadRequestException> {
                    b.getTrackRecommendations(maxAttributes = listOf(TuneableTrackAttribute.SPEECHINESS.asTrackAttribute(0.9f))).complete()
                }
                assertTrue(b.getTrackRecommendations(
                        maxAttributes = listOf(TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(0.9f), TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.9f)),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
            }
        }
    }
})