/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.ArtistApi
import com.adamratzman.spotify.utils.Market
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicArtistsApiTest : Spek({
    describe("Public Artists test") {
        val a = api?.artists ?: return@describe
        describe("get artists") {
            it("invalid artist") {
                assertNull(a.getArtist("adkjlasdf").complete())
            }
            it("valid artist") {
                assertNotNull(a.getArtist("66CXWjxzNUsdJxJ2JdwvnR").complete())
            }
            it("multiple artists") {
                assertFailsWith<SpotifyException.BadRequestException> { a.getArtists().complete() }
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
                assertFailsWith<SpotifyException.BadRequestException> { a.getArtistAlbums("asfasdf").complete() }
            }
            it("valid artist") {
                assertTrue(a.getArtistAlbums("7wjeXCtRND2ZdKfMJFu6JC", 10,
                    include = *arrayOf(ArtistApi.AlbumInclusionStrategy.ALBUM)
                )
                        .complete().items.asSequence().map { it.name }.contains("Louane"))
            }
        }

        describe("get related artists") {
            it("invalid artist") {
                assertFailsWith<SpotifyException.BadRequestException> { a.getRelatedArtists("").complete() }
                assertFailsWith<SpotifyException.BadRequestException> { a.getRelatedArtists("no").complete() }
            }
            it("valid artist") {
                assertTrue(a.getRelatedArtists("0X2BH1fck6amBIoJhDVmmJ").complete().isNotEmpty())
            }
        }

        describe("get artist top tracks by market") {
            it("invalid artist") {
                assertFailsWith<SpotifyException.BadRequestException> { a.getArtistTopTracks("no").complete() }
            }
            it("valid artist") {
                assertTrue(a.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW").complete().isNotEmpty())
                assertTrue(a.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW", Market.FR).complete().isNotEmpty())
            }
        }
    }
})
