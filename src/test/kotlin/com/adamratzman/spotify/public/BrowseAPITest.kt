package com.adamratzman.spotify.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe

class BrowseAPITest : Spek({
    describe("Browse test") {
        val api by memoized {
            SpotifyAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret")).build()
        }
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
            it("seed artists") {

            }
            it("seed tracks") {

            }
            it("seed genres") {

            }
            it("target attributes") {

            }
            it("min attributes") {

            }
            it("max attributes") {

            }

        }
    }
})