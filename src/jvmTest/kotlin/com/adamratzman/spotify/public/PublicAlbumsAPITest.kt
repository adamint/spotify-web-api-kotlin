/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicAlbumsAPITest : Spek({
    describe("Public Albums test") {
        val a = api.albums
        describe("get albums") {
            it("singular album") {
                assertNull(a.getAlbum("asdf", Market.FR).complete())
                assertNull(a.getAlbum("asdf").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2", Market.US).complete())
            }
            it("multiple albums") {
                assertFailsWith<BadRequestException> { a.getAlbums(market = Market.US).complete() }
                assertFailsWith<BadRequestException> { a.getAlbums().complete() }
                assertEquals(listOf(true, false),
                        a.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc", market = Market.US)
                                .complete().map { it != null })
                assertEquals(listOf(true, false),
                        a.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc").complete().map { it != null })
            }
        }

        describe("get album tracks. market is optional here for relinking support") {
            it("invalid album") {
                assertFailsWith<BadRequestException> { a.getAlbumTracks("no").complete() }
            }
            it("valid album") {
                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).complete().items.isNotEmpty())

                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3).complete().items.isNotEmpty())
                assertFalse(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).complete().items[0].isRelinked())
            }
        }
    }
})