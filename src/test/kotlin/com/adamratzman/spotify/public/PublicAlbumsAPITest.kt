package com.adamratzman.spotify.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicAlbumsAPITest : Spek({
    describe("Public Albums test") {
        val api by memoized {
            SpotifyAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret")).build()
        }
        val a = api.albums
        describe("get albums") {
            it("singular album") {
                assertNull(a.getAlbum("asdf", Market.FR).complete())
                assertNull(a.getAlbum("asdf").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2", Market.US).complete())
            }
            it("multiple albums") {
                assertThrows<BadRequestException> { a.getAlbums(market = Market.US).complete() }
                assertThrows<BadRequestException> { a.getAlbums().complete() }
                assertEquals(listOf(true, false),
                        a.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc", market = Market.US)
                                .complete().map { it != null })
                assertEquals(listOf(true, false),
                        a.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc").complete().map { it != null })
            }
        }

        describe("get album tracks. market is optional here for relinking support") {
            it("invalid album") {
                assertThrows<BadRequestException> { a.getAlbumTracks("no").complete() }
            }
            it("valid album") {
                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).complete().items.isNotEmpty())
                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3).complete().items.isNotEmpty())
                assertFalse(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).complete().items[0].isRelinked())
            }
        }

    }
})