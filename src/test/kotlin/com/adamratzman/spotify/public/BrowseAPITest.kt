package com.adamratzman.spotify.public

import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.*
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
            assertEquals(b.getCategoryList(locale = "BAD_LOCALE").complete().items[0], b.getCategoryList().complete().items[0])
            assertTrue(b.getCategoryList(4, 3, locale = "fr_FR", market = Market.CA).complete().total > 0)
        }

        it("get category") {
            assertNotNull(b.getCategory("pop").complete())
            assertNotNull(b.getCategory("pop", Market.FR).complete())
            assertNotNull(b.getCategory("pop", Market.FR, locale = "en_US").complete())
            assertNotNull(b.getCategory("pop", Market.FR, locale = "KSDJFJKSJDKF").complete())
            assertThrows<BadRequestException> { b.getCategory("no u", Market.US).complete() }
        }

        it("get playlists by category") {
            assertThrows<BadRequestException> { b.getPlaylistsForCategory("no u", limit = 4).complete() }
            assert(b.getPlaylistsForCategory("pop", 10, 500, Market.FR).complete().total > 0)
        }

        it("get featured playlists") {
            assert(b.getFeaturedPlaylists(5, 4, market = Market.US, timestamp = System.currentTimeMillis() - 1000000).complete().playlists.total > 0)
            assert(b.getFeaturedPlaylists(offset = 32).complete().playlists.total > 0)
        }

        it("get new releases") {
            assert(b.getNewReleases(market = Market.CA).complete().total > 0)
            assert(b.getNewReleases(limit = 1, offset = 3).complete().total > 0)
            assert(b.getNewReleases(limit = 6, offset = 44, market = Market.US).complete().total > 0)
        }

        describe("get recommendations") {
            it("no parameters") {
                assertThrows<BadRequestException> { b.getRecommendations().complete() }
            }
            it("seed artists") {
                assertThrows<BadRequestException> { b.getRecommendations(seedArtists = listOf("abc")).complete() }
                assert(b.getRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89")).complete().tracks.isNotEmpty())
                assert(b.getRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89", "7lMgpN1tEBQKpRoUMKB8iw")).complete().tracks.isNotEmpty())
            }
            it("seed tracks") {
                assertThrows<BadRequestException> { b.getRecommendations(seedTracks = listOf("abc")).complete() }
                assert(b.getRecommendations(seedTracks = listOf("3Uyt0WO3wOopnUBCe9BaXl")).complete().tracks.isNotEmpty())
                assert(b.getRecommendations(seedTracks = listOf("6d9iYQG2JvTTEgcndW81lt", "3Uyt0WO3wOopnUBCe9BaXl")).complete().tracks.isNotEmpty())
            }
            it("seed genres") {
                assertDoesNotThrow { b.getRecommendations(seedGenres = listOf("abc")).complete() }
                assert(b.getRecommendations(seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
                assert(b.getRecommendations(seedGenres = listOf("pop", "latinx")).complete().tracks.isNotEmpty())
            }
            it("multiple seed types") {
                assertDoesNotThrow {
                    b.getRecommendations(seedArtists = listOf("2C2sVVXanbOpymYBMpsi89"),
                            seedTracks = listOf("6d9iYQG2JvTTEgcndW81lt", "3Uyt0WO3wOopnUBCe9BaXl"),
                            seedGenres = listOf("pop")).complete()
                }
            }
            it("target attributes") {
                assertThrows<BadRequestException> {
                    b.getRecommendations(targetAttributes = hashMapOf(TuneableTrackAttribute.ACOUSTICNESS to 3)).complete()
                }
                assert(b.getRecommendations(
                        targetAttributes = hashMapOf(TuneableTrackAttribute.ACOUSTICNESS to 1.0, TuneableTrackAttribute.DANCEABILITY to .5),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
            }
            it("min attributes") {
                assertThrows<BadRequestException> {
                    b.getRecommendations(minAttributes = hashMapOf(TuneableTrackAttribute.ACOUSTICNESS to 3)).complete()
                }
                assert(b.getRecommendations(
                        minAttributes = hashMapOf(TuneableTrackAttribute.ACOUSTICNESS to 0.5, TuneableTrackAttribute.DANCEABILITY to .5),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())
            }
            it("max attributes") {
                assertThrows<BadRequestException> {
                    b.getRecommendations(maxAttributes = hashMapOf(TuneableTrackAttribute.SPEECHINESS to 3)).complete()
                }
                assert(b.getRecommendations(
                        maxAttributes = hashMapOf(TuneableTrackAttribute.ACOUSTICNESS to 0.9, TuneableTrackAttribute.DANCEABILITY to 0.9),
                        seedGenres = listOf("pop")).complete().tracks.isNotEmpty())

            }

        }
    }
})