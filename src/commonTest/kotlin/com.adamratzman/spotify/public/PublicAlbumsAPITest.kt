/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.BadRequestException
import com.neovisionaries.i18n.CountryCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicAlbumsAPITest : Spek({
    describe("Public Albums test") {
        val a = api.albums
        describe("get albums") {
            it("singular album") {
                assertNull(a.getAlbum("asdf", CountryCode.FR).complete())
                assertNull(a.getAlbum("asdf").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2").complete())
                assertNotNull(a.getAlbum("1f1C1CjidKcWQyiIYcMvP2", CountryCode.US).complete())
            }
            it("multiple albums") {
                assertThrows<BadRequestException> { a.getAlbums(market = CountryCode.US).complete() }
                assertThrows<BadRequestException> { a.getAlbums().complete() }
                assertEquals(listOf(true, false),
                        a.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc", market = CountryCode.US)
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
                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, CountryCode.US).complete().items.isNotEmpty())

                assertTrue(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3).complete().items.isNotEmpty())
                assertFalse(a.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, CountryCode.US).complete().items[0].isRelinked())
            }
        }
    }
})