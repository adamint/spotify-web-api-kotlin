package com.adamratzman.spotify.public

import com.adamratzman.spotify.endpoints.public.PublicArtistsAPI
import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicArtistsAPITest : Spek({
    describe("Public Artists test") {
        val api by memoized {
            SpotifyAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret")).build()
        }
        val a = api.artists
        describe("get artists") {
            it("invalid artist") {
                assertNull(a.getArtist("adkjlasdf").complete())
            }
            it("valid artist") {
                assertNotNull(a.getArtist("66CXWjxzNUsdJxJ2JdwvnR").complete())
            }
            it("multiple artists") {
                assertThrows<BadRequestException> { a.getArtists().complete() }
                assertEquals(listOf(true, true),
                        a.getArtists("66CXWjxzNUsdJxJ2JdwvnR", "7wjeXCtRND2ZdKfMJFu6JC")
                                .complete().map { it != null })
                assertEquals(listOf(false, true),
                        a.getArtists("dskjafjkajksdf", "66CXWjxzNUsdJxJ2JdwvnR").complete()
                                .map { it != null })
            }
        }

        describe("get artist albums") {
            it("invalid artist") {
                assertThrows<BadRequestException> { a.getArtistAlbums("asfasdf").complete() }
            }
            it("valid artist") {
                assertTrue(a.getArtistAlbums("7wjeXCtRND2ZdKfMJFu6JC", limit = 10, include = listOf(PublicArtistsAPI.AlbumInclusionStrategy.ALBUM))
                        .complete().items.asSequence().map { it.name }.contains("Louane"))
            }
        }

        describe("get related artists") {
            it("invalid artist") {
                assertThrows<BadRequestException> { a.getRelatedArtists("").complete() }
                assertThrows<BadRequestException> { a.getRelatedArtists("no").complete() }
            }
            it("valid artist") {
                assertTrue(a.getRelatedArtists("0X2BH1fck6amBIoJhDVmmJ").complete().isNotEmpty())
            }
        }

        describe("get artist top tracks by market") {
            it("invalid artist") {
                assertThrows<BadRequestException> { a.getArtistTopTracks("no").complete() }
            }
            it("valid artist") {
                assertTrue(a.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW").complete().isNotEmpty())
                assertTrue(a.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW", Market.FR).complete().isNotEmpty())
            }
        }
    }
})