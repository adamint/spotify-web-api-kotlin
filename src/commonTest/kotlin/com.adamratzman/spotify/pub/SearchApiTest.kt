/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.models.SearchFilter
import com.adamratzman.spotify.models.SearchFilterType.Artist
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import com.adamratzman.spotify.utils.Market
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SearchApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testSearchMultiple(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchMultiple.name)

        val query = api.search.search("lo", *SearchApi.SearchType.entries.toTypedArray(), market = Market.US)
        assertTrue(
            query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                query.playlists?.items?.isNotEmpty() == true && query.shows?.items?.isNotEmpty() == true && query.episodes?.items?.isNotEmpty() == true
        )
        val query2 = api.search.search("lo", SearchApi.SearchType.Artist, SearchApi.SearchType.Playlist)
        assertTrue(
            query2.albums == null && query2.tracks == null && query2.shows == null && query2.episodes == null &&
                query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true
        )
        val query3 =
            api.search.search("lo", SearchApi.SearchType.Show, SearchApi.SearchType.Episode, market = Market.US)
        assertTrue(query3.episodes?.items?.isNotEmpty() == true && query3.shows?.items?.isNotEmpty() == true)
    }

    @Test
    fun testSearchTrack(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchTrack.name)

        assertTrue(api.search.searchTrack("hello", listOf(SearchFilter(Artist, "Lionel Ritchie")), 1, 1, Market.US).items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchTrack("").items.size }
    }

    @Test
    fun testSearchAlbum(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchAlbum.name)

        assertTrue(api.search.searchAlbum("le début").items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchAlbum("").items.size }
    }

    @Test
    fun testSearchPlaylist(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchPlaylist.name)

        assertTrue(api.search.searchPlaylist("test").items.isNotEmpty())
        assertFailsWithSuspend<SpotifyException.BadRequestException> { api.search.searchPlaylist("").items.size }
    }

    @Test
    fun testSearchArtist(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchArtist.name)

        assertTrue(api.search.searchArtist("amir").items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.search.searchArtist("").items.size }
    }

    @Test
    fun testSearchShow(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchShow.name)

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
    fun testSearchEpisode(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testSearchEpisode.name)

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
