/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.endpoints.pub.ArtistApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicArtistsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetArtists() {
        return runBlockingTest {
            println("here")
            super.build<GenericSpotifyApi>()
            println("here2")
            println(api)
            if (!testPrereq()) return@runBlockingTest else api!!
            println("here3")
            assertNull(api!!.artists.getArtist("adkjlasdf"))
            assertNotNull(api!!.artists.getArtist("66CXWjxzNUsdJxJ2JdwvnR"))
            assertFailsWith<SpotifyException.BadRequestException> { api!!.artists.getArtists() }
            assertEquals(
                listOf(true, true),
                api!!.artists.getArtists("66CXWjxzNUsdJxJ2JdwvnR", "7wjeXCtRND2ZdKfMJFu6JC")
                    .map { it != null }
            )
            assertEquals(
                listOf(false, true),
                api!!.artists.getArtists("dskjafjkajksdf", "66CXWjxzNUsdJxJ2JdwvnR")
                    .map { it != null }
            )
        }
    }

    @Test
    fun testGetArtistAlbums() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<SpotifyException.BadRequestException> { api!!.artists.getArtistAlbums("asfasdf") }
            assertTrue(
                api!!.artists.getArtistAlbums(
                    "7wjeXCtRND2ZdKfMJFu6JC", 10,
                    include = arrayOf(ArtistApi.AlbumInclusionStrategy.ALBUM)
                )
                    .items.asSequence().map { it.name }.contains("Louane")
            )
        }
    }

    @Test
    fun testGetRelatedArtists() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<SpotifyException.BadRequestException> { api!!.artists.getRelatedArtists("") }
            assertFailsWith<SpotifyException.BadRequestException> { api!!.artists.getRelatedArtists("no") }
            assertTrue(api!!.artists.getRelatedArtists("0X2BH1fck6amBIoJhDVmmJ").isNotEmpty())
        }
    }

    @Test
    fun testGetArtistTopTracksByMarket() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<SpotifyException.BadRequestException> { api!!.artists.getArtistTopTracks("no") }
            assertTrue(api!!.artists.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW").isNotEmpty())
            assertTrue(api!!.artists.getArtistTopTracks("4ZGK4hkNX6pilPpyy4YJJW", Market.FR).isNotEmpty())
        }
    }
}
