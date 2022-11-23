/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class SearchApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testSearchMultiple() = runTestOnDefaultDispatcher {
        buildApi()

        val query = api.search.search("lo", *SearchApi.SearchType.values(), market = Market.US)
        assertTrue(
            query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                    query.playlists?.items?.isNotEmpty() == true && query.shows?.items?.isNotEmpty() == true && query.episodes?.items?.isNotEmpty() == true
        )
        val query2 = api.search.search("lo", SearchApi.SearchType.ARTIST, SearchApi.SearchType.PLAYLIST)
        assertTrue(
            query2.albums == null && query2.tracks == null && query2.shows == null && query2.episodes == null &&
                    query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true
        )
        val query3 =
            api.search.search("lo", SearchApi.SearchType.SHOW, SearchApi.SearchType.EPISODE, market = Market.US)
        assertTrue(query3.episodes?.items?.isNotEmpty() == true && query3.shows?.items?.isNotEmpty() == true)
    }

    @Test
    fun testSearchTrack() = runTestOnDefaultDispatcher {
        buildApi()

        assertTrue(api.search.searchTrack("hello", 1, 1, Market.US).items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchTrack("").items.size }
    }

    @Test
    fun testSearchAlbum() = runTestOnDefaultDispatcher {
        buildApi()

        assertTrue(api.search.searchAlbum("le début").items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchAlbum("").items.size }
    }

    @Test
    fun testSearchPlaylist() = runTestOnDefaultDispatcher {
        buildApi()

        assertTrue(api.search.searchPlaylist("test").items.isNotEmpty())
        assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchPlaylist("").items.size }
    }

    @Test
    fun testSearchArtist() = runTestOnDefaultDispatcher {
        buildApi()

        assertTrue(api.search.searchArtist("amir").items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchArtist("").items.size }
    }

    @Test
    fun testSearchShow() = runTestOnDefaultDispatcher {
        buildApi()

        (api.search as? SearchApi)?.let { clientSearchApi ->
            assertTrue(clientSearchApi.searchShow("f", market = Market.US).items.isNotEmpty())
            assertFailsWith<SpotifyException.BadRequestException> {
                clientSearchApi.searchShow(
                    "",
                    market = Market.US
                ).items.size
            }
        }
    }

    @Test
    fun testSearchEpisode() = runTestOnDefaultDispatcher {
        buildApi()

        (api.search as? SearchApi)?.let { clientSearchApi ->
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
