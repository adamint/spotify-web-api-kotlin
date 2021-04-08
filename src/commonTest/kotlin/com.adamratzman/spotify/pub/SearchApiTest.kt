/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.ClientSearchApi
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertTrue

class SearchApiTest {
    lateinit var api: GenericSpotifyApi

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testSearchMultiple() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val query = api.search.search("lo", *SearchApi.SearchType.values())
            assertTrue(
                query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                        query.playlists?.items?.isNotEmpty() == true && query.shows?.items?.isNotEmpty() == true && query.episodes?.items?.isNotEmpty() == true
            )
            val query2 = api.search.search("lo", SearchApi.SearchType.ARTIST, SearchApi.SearchType.PLAYLIST)
            assertTrue(
                query2.albums == null && query2.tracks == null && query2.shows == null && query2.episodes == null &&
                        query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true
            )
            val query3 = api.search.search("lo", SearchApi.SearchType.SHOW, SearchApi.SearchType.EPISODE)
            assertTrue(query3.episodes?.items?.isNotEmpty() == true && query3.shows?.items?.isNotEmpty() == true)
        }
    }

    @Test
    fun testSearchTrack() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.search.searchTrack("hello", 1, 1, Market.US).items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchTrack("").items.size }
        }
    }

    @Test
    fun testSearchAlbum() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.search.searchAlbum("le début").items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchAlbum("").items.size }
        }
    }

    @Test
    fun testSearchPlaylist() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.search.searchPlaylist("test").items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchPlaylist("").items.size }
        }
    }

    @Test
    fun testSearchArtist() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.search.searchArtist("amir").items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchArtist("").items.size }
        }
    }

    @Test
    fun testSearchShow() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            (api.search as? ClientSearchApi)?.let { clientSearchApi ->
                assertTrue(clientSearchApi.searchShow("f").items.isNotEmpty())
                assertFailsWithSuspend<SpotifyException.BadRequestException> { clientSearchApi.searchShow("").items.size }
            }
        }
    }

    @Test
    fun testSearchEpisode() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            (api.search as? ClientSearchApi)?.let { clientSearchApi ->
                assertTrue(clientSearchApi.searchEpisode("f").items.isNotEmpty())
                assertFailsWithSuspend<SpotifyException.BadRequestException> { clientSearchApi.searchEpisode("").items.size }
            }
        }
    }
}
