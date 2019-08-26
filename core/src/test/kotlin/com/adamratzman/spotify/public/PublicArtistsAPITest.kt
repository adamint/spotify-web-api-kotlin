/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.ArtistsAPI
import com.adamratzman.spotify.models.BadRequestException
import com.neovisionaries.i18n.CountryCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicArtistsAPITest : Spek({
    describe("Public Artists test") {
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
                assertTrue(a.getArtistAlbums("7wjeXCtRND2ZdKfMJFu6JC", 10,
                    include = *arrayOf(ArtistsAPI.AlbumInclusionStrategy.ALBUM)
                )
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
                assertTrue(a.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW", CountryCode.FR).complete().isNotEmpty())
            }
        }
    }
})