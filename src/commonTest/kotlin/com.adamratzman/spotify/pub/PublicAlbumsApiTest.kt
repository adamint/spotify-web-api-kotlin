/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicAlbumsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetAlbums() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertNull(api!!.albums.getAlbum("asdf", Market.FR))
            assertNull(api!!.albums.getAlbum("asdf"))
            assertNotNull(api!!.albums.getAlbum("1f1C1CjidKcWQyiIYcMvP2"))
            assertNotNull(api!!.albums.getAlbum("1f1C1CjidKcWQyiIYcMvP2", Market.US))

            assertFailsWith<SpotifyException.BadRequestException> { api!!.albums.getAlbums(market = Market.US) }
            assertFailsWith<SpotifyException.BadRequestException> { api!!.albums.getAlbums() }
            assertEquals(listOf(true, false),
                api!!.albums.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc", market = Market.US)
                    .map { it != null })
            assertEquals(listOf(true, false),
                api!!.albums.getAlbums("1f1C1CjidKcWQyiIYcMvP2", "abc").map { it != null })
        }
    }

    @Test
    fun testGetAlbumsTracks() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<SpotifyException.BadRequestException> { api!!.albums.getAlbumTracks("no") }

            assertTrue(api!!.albums.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).items.isNotEmpty())
            assertTrue(api!!.albums.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3).items.isNotEmpty())
            assertFalse(api!!.albums.getAlbumTracks("29ct57rVIi3MIFyKJYUWrZ", 4, 3, Market.US).items[0].isRelinked())
        }
    }

    @Test
    fun testConvertSimpleAlbumToAlbum() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!
            val simpleAlbum = api!!.tracks.getTrack("53BHUFdQphHiZUUG3nx9zn")!!.album
            assertEquals(simpleAlbum.id, simpleAlbum.toFullAlbum()?.id)
        }
    }
}
