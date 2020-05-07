/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.utils.Market
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SearchApiTest : Spek({
    describe("Search API test") {
        val s = api.search
        describe("search multiple") {
            it("valid request") {
                val query = s.search("lo", *SearchApi.SearchType.values()).complete()
                assertTrue(query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                        query.playlists?.items?.isNotEmpty() == true && query.shows?.items?.isNotEmpty() == true && query.episodes?.items?.isNotEmpty() == true)
                val query2 = s.search("lo", SearchApi.SearchType.ARTIST, SearchApi.SearchType.PLAYLIST).complete()
                assertTrue(query2.albums == null && query2.tracks == null && query2.shows == null && query2.episodes == null &&
                        query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true)

                val query3 = s.search("lo", SearchApi.SearchType.SHOW, SearchApi.SearchType.EPISODE).complete()
                assertTrue(query3.episodes?.items?.isNotEmpty() == true && query3.shows?.items?.isNotEmpty() == true)
            }
        }
        describe("search track") {
            it("valid request") {
                assertTrue(s.searchTrack("hello", 1, 1, Market.US).complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchTrack("").complete().items.size }
            }
        }
        describe("search album") {
            it("valid request") {
                assertTrue(s.searchAlbum("le début").complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchAlbum("").complete().items.size }
            }
        }
        describe("search playlist") {
            it("valid request") {
                assertTrue(s.searchPlaylist("test").complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchPlaylist("").complete().items.size }
            }
        }
        describe("search artist") {
            it("valid request") {
                assertTrue(s.searchArtist("amir").complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchArtist("").complete().items.size }
            }
        }
        describe("search show") {
            it("valid request") {
                assertTrue(s.searchShow("f").complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchShow("").complete().items.size }
            }
        }

        describe("search episode") {
            it("valid request") {
                assertTrue(s.searchEpisode("f").complete().items.isNotEmpty())
            }
            it("invalid request") {
                assertFailsWith<SpotifyException.BadRequestException> { s.searchEpisode("").complete().items.size }
            }
        }
    }
})
