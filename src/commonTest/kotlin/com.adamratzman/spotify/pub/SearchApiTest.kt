/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SearchApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testSearchMultiple() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!
            val query = api!!.search.search("lo", *SearchApi.SearchType.values())
            assertTrue(
                query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                        query.playlists?.items?.isNotEmpty() == true && query.shows?.items?.isNotEmpty() == true && query.episodes?.items?.isNotEmpty() == true
            )
            val query2 = api!!.search.search("lo", SearchApi.SearchType.ARTIST, SearchApi.SearchType.PLAYLIST)
            assertTrue(
                query2.albums == null && query2.tracks == null && query2.shows == null && query2.episodes == null &&
                        query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true
            )
            val query3 = api!!.search.search("lo", SearchApi.SearchType.SHOW, SearchApi.SearchType.EPISODE)
            assertTrue(query3.episodes?.items?.isNotEmpty() == true && query3.shows?.items?.isNotEmpty() == true)
        }
    }

    @Test
    fun testSearchTrack() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.search.searchTrack("hello", 1, 1, Market.US).items.isNotEmpty())
            assertFailsWith<SpotifyException.BadRequestException> { api!!.search.searchTrack("").items.size }
        }
    }

    @Test
    fun testSearchAlbum() {
        return runBlockingTest {
            println("h1...")
            super.build<GenericSpotifyApi>()
            println("2..")
            if (!testPrereq()) return@runBlockingTest else api!!
            println("3...")
            assertTrue(api!!.search.searchAlbum("le début").items.isNotEmpty())
            println("4...")
            assertFailsWith<SpotifyException.BadRequestException> { api!!.search.searchAlbum("").items.size }
            println("5...")
        }
    }

    @Test
    fun testSearchPlaylist() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.search.searchPlaylist("test").items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api!!.search.searchPlaylist("").items.size }
        }
    }

    @Test
    fun testSearchArtist() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.search.searchArtist("amir").items.isNotEmpty())
            assertFailsWith<SpotifyException.BadRequestException> { api!!.search.searchArtist("").items.size }
        }
    }

    @Test
    fun testSearchShow() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            (api!!.search as? SearchApi)?.let { clientSearchApi ->
                assertTrue(clientSearchApi.searchShow("f", market = Market.US).items.isNotEmpty())
                assertFailsWith<SpotifyException.BadRequestException> {
                    clientSearchApi.searchShow(
                        "",
                        market = Market.US
                    ).items.size
                }
            }
        }
    }

    @Test
    fun testSearchEpisode() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            (api!!.search as? SearchApi)?.let { clientSearchApi ->
                assertTrue(clientSearchApi.searchEpisode("f", market = Market.US).items.isNotEmpty())
                assertFailsWith<SpotifyException.BadRequestException> {
                    clientSearchApi.searchEpisode(
                        "",
                        market = Market.US
                    ).items.size
                }
            }
        }
    }
}
