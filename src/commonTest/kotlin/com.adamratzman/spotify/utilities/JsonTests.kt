/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.runBlockingTest
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonTests {
    lateinit var api: GenericSpotifyApi

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
            println("Built API")
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testArtistSerialization() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(
                Json.encodeToString(
                    Artist.serializer().nullable,
                    api.artists.getArtist("spotify:artist:5WUlDfRSoLAfcVSX1WnrxN")
                ).isNotEmpty()
            )
        }
    }

    @Test
    fun testTrackSerialization() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(
                Json.encodeToString(
                    Track.serializer().nullable,
                    api.tracks.getTrack("spotify:track:6kcHg7XL6SKyPNd78daRBL")
                ).isNotEmpty()
            )
        }
    }

    @Test
    fun testAlbumSerialization() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(
                Json.encodeToString(
                    Album.serializer().nullable,
                    api.albums.getAlbum("spotify:album:6ggQNps98xaXMY0OZWevEH")
                ).isNotEmpty()
            )
        }
    }

    @Test
    fun testArtistDeserialization() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val json =
                """{"external_urls":{"spotify":"https://open.spotify.com/artist/5WUlDfRSoLAfcVSX1WnrxN"},"href":"https://api.spotify.com/v1/artists/5WUlDfRSoLAfcVSX1WnrxN","id":"5WUlDfRSoLAfcVSX1WnrxN","uri":"spotify:artist:5WUlDfRSoLAfcVSX1WnrxN","followers":{"href":null,"total":14675484},"genres":["australian dance","australian pop","dance pop","pop"],"images":[{"height":1333,"url":"https://i.scdn.co/image/652b6bb0dfaf8aa444f4414ee018699260e74306","width":1000},{"height":853,"url":"https://i.scdn.co/image/a82822ab211cbe28a0a1dbcb16902a1a8a2ea791","width":640},{"height":267,"url":"https://i.scdn.co/image/dd3e336d456172bbda56b543c5389e1490903a30","width":200},{"height":85,"url":"https://i.scdn.co/image/95a2aa98384b31336b8d56f8b470c45b12dcd550","width":64}],"name":"Sia","popularity":88,"type":"artist"}"""
            val artist = Json.decodeFromString<Artist>(json)
            assertEquals(ArtistUri("spotify:artist:5WUlDfRSoLAfcVSX1WnrxN"), artist.uri)
            assertEquals("5WUlDfRSoLAfcVSX1WnrxN", artist.id)
            assertEquals("Sia", artist.name)
            assertEquals(88, artist.popularity)
            assertEquals("artist", artist.type)
        }
    }

    @Test
    fun testPagingObjectDeserialization() {
        runBlockingTest {
            val json =
                """{"href": "href", "items": [], "limit": 50, "next": "nextHref", "offset": 3, "previous": "previousHref", "total": 5}"""
            val pagingObject = Json.decodeFromString(PagingObject.serializer(Artist.serializer()), json)
            assertEquals("href", pagingObject.href)
            assertEquals(emptyList(), pagingObject.items)
            assertEquals(50, pagingObject.limit)
            assertEquals("nextHref", pagingObject.next)
            assertEquals(3, pagingObject.offset)
            assertEquals("previousHref", pagingObject.previous)
            assertEquals(5, pagingObject.total)
        }
    }

    @Test
    fun testCursorBasedPagingObjectDeserialization() {
        runBlockingTest {
            val json =
                """{"href": "href", "items": [], "limit": 50, "next": "nextHref", "cursors": {"after": "afterHref"}, "total": 5}"""
            val pagingObject = Json.decodeFromString(CursorBasedPagingObject.serializer(Artist.serializer()), json)
            assertEquals("href", pagingObject.href)
            assertEquals(emptyList(), pagingObject.items)
            assertEquals(50, pagingObject.limit)
            assertEquals("nextHref", pagingObject.next)
            assertEquals("afterHref", pagingObject.cursor.after)
            assertEquals(5, pagingObject.total)
        }
    }
}
