/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class BrowseAPITest : Spek({
    describe("Browse test") {
        val b = api.browse
        it("get genre seeds") {
            assertTrue(b.getAvailableGenreSeeds().complete().isNotEmpty())
        }

        it("get category list") {
            assertEquals(
                b.getCategoryList(locale = "BAD_LOCALE").complete().items[0],
                b.getCategoryList().complete().items[0]
            )
            assertTrue(b.getCategoryList(4, 3, locale = "fr_FR", market = Market.CA).complete().items.isNotEmpty())
        }

        it("get category") {
            assertNotNull(b.getCategory("pop").complete())
            assertNotNull(b.getCategory("pop", Market.FR).complete())
            assertNotNull(b.getCategory("pop", Market.FR, locale = "en_US").complete())
            assertNotNull(b.getCategory("pop", Market.FR, locale = "KSDJFJKSJDKF").complete())
            assertFailsWith<SpotifyException.BadRequestException> { b.getCategory("no u", Market.US).complete() }
        }

        it("get playlists by category") {
            assertFailsWith<SpotifyException.BadRequestException> { b.getPlaylistsForCategory("no u", limit = 4).complete() }
            assertTrue(b.getPlaylistsForCategory("pop", 10, 0, Market.FR).complete().items.isNotEmpty())
        }

        it("get featured playlists") {
            assertTrue(
                b.getFeaturedPlaylists(
                    5,
                    4,
                    market = Market.US,
                    timestamp = getCurrentTimeMs() - 1000000
                ).complete().playlists.total > 0
            )
            assertTrue(b.getFeaturedPlaylists(offset = 32).complete().playlists.total > 0)
        }

        it("get new releases") {
            assertTrue(b.getNewReleases(market = Market.CA).complete().items.isNotEmpty())
            assertTrue(b.getNewReleases(limit = 1, offset = 3).complete().items.isNotEmpty())
            assertTrue(b.getNewReleases(limit = 6, offset = 44, market = Market.US).complete().items.isNotEmpty())
        }

        describe("get recommendations") {
            it("no parameters") {
                assertFailsWith<SpotifyException.BadRequestException> { b.getTrackRecommendations().complete() }
            }
            it("seed artists") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    b.getTrackRecommendations(seedArtists = listOf("abc")).complete()
                }
                assertTrue(b.getTrackRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89")).complete().tracks.isNotEmpty())
                assertTrue(
                    b.getTrackRecommendations(
                        seedArtists = listOf(
                            "2C2sVVXanbOpymYBMpsi89",
                            "7lMgpN1tEBQKpRoUMKB8iw"
                        )
                    ).complete().tracks.isNotEmpty()
                )
            }
            it("seed tracks") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    b.getTrackRecommendations(seedTracks = listOf("abc")).complete()
                }
                assertTrue(b.getTrackRecommendations(seedTracks = listOf("3Uyt0WO3wOopnUBCe9BaXl")).complete().tracks.isNotEmpty())
                assertTrue(
                    b.getTrackRecommendations(
                        seedTracks = listOf(
                            "6d9iYQG2JvTTEgcndW81lt",
                            "3Uyt0WO3wOopnUBCe9BaXl"
                        )
                    ).complete().tracks.isNotEmpty()
                )
            }
            it("seed genres") {
                b.getTrackRecommendations(seedGenres = listOf("abc")).complete()
                assertTrue(b.getTrackRecommendations(seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
                assertTrue(
                    b.getTrackRecommendations(
                        seedGenres = listOf(
                            "pop",
                            "latinx"
                        )
                    ).complete().tracks.isNotEmpty()
                )
            }
            it("multiple seed types") {

                b.getTrackRecommendations(
                    seedArtists = listOf("2C2sVVXanbOpymYBMpsi89"),
                    seedTracks = listOf("6d9iYQG2JvTTEgcndW81lt", "3Uyt0WO3wOopnUBCe9BaXl"),
                    seedGenres = listOf("pop")
                ).complete()
            }
            it("target attributes") {
                assertFailsWith<IllegalArgumentException> {
                    b.getTrackRecommendations(
                        targetAttributes = listOf(
                            TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(
                                3f
                            )
                        )
                    ).complete()
                }
                assertTrue(
                    b.getTrackRecommendations(
                        targetAttributes = listOf(
                            TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(1f),
                            TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.5f)
                        ),
                        seedGenres = listOf("pop")
                    ).complete().tracks.isNotEmpty()
                )
            }
            it("min attributes") {
                assertFailsWith<IllegalArgumentException> {
                    b.getTrackRecommendations(
                        minAttributes = listOf(
                            TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(
                                3f
                            )
                        )
                    ).complete()
                }
                assertTrue(
                    b.getTrackRecommendations(
                        minAttributes = listOf(
                            TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(0.5f),
                            TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.5f)
                        ),
                        seedGenres = listOf("pop")
                    ).complete().tracks.isNotEmpty()
                )
            }
            it("max attributes") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    b.getTrackRecommendations(
                        maxAttributes = listOf(
                            TuneableTrackAttribute.SPEECHINESS.asTrackAttribute(
                                0.9f
                            )
                        )
                    ).complete()
                }
                assertTrue(
                    b.getTrackRecommendations(
                        maxAttributes = listOf(
                            TuneableTrackAttribute.ACOUSTICNESS.asTrackAttribute(0.9f),
                            TuneableTrackAttribute.DANCEABILITY.asTrackAttribute(0.9f)
                        ),
                        seedGenres = listOf("pop")
                    ).complete().tracks.isNotEmpty()
                )
            }
        }
    }
})
