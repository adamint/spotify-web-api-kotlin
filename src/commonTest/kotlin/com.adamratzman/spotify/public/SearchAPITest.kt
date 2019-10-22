/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.utils.Market
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SearchAPITest : Spek({
    describe("Search API test") {
        val s = api.search
        describe("search multiple") {
            it("valid request") {
                val query = s.search("lo", *SearchApi.SearchType.values()).complete()
                assertTrue(query.albums?.items?.isNotEmpty() == true && query.tracks?.items?.isNotEmpty() == true && query.artists?.items?.isNotEmpty() == true &&
                        query.playlists?.items?.isNotEmpty() == true)
                val query2 = s.search("lo", SearchApi.SearchType.ARTIST, SearchApi.SearchType.PLAYLIST).complete()
                assertTrue(query2.albums == null && query2.tracks == null && query2.artists?.items?.isNotEmpty() == true && query2.playlists?.items?.isNotEmpty() == true)
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
    }
})
