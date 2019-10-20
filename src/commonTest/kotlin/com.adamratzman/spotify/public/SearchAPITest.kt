/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.SearchAPI
import com.adamratzman.spotify.models.BadRequestException

class SearchAPITest : Spek({
    describe("Search API test") {
        val s = api.search
        describe("search multiple") {
            it("valid request") {
                val query = s.search("lo", *SearchAPI.SearchType.values()).complete()
                assertTrue(query.albums?.isNotEmpty() == true && query.tracks?.isNotEmpty() == true && query.artists?.isNotEmpty() == true &&
                        query.playlists?.isNotEmpty() == true)

                val query2 = s.search("lo", SearchAPI.SearchType.ARTIST, SearchAPI.SearchType.PLAYLIST).complete()
                assertTrue(query2.albums == null && query2.tracks == null && query2.artists?.isNotEmpty() == true && query2.playlists?.isNotEmpty() == true)
            }
        }
        describe("search track") {
            it("valid request") {
                assertTrue(s.searchTrack("hello", 1, 1, CountryCode.US).complete().isNotEmpty())
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchTrack("").complete().size }
            }
        }
        describe("search album") {
            it("valid request") {
                assertTrue(s.searchAlbum("le début").complete().size > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchAlbum("").complete().size }
            }
        }
        describe("search playlist") {
            it("valid request") {
                assertTrue(s.searchPlaylist("test").complete().size > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchPlaylist("").complete().size }
            }
        }
        describe("search artist") {
            it("valid request") {
                assertTrue(s.searchArtist("amir").complete().size > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchArtist("").complete().size }
            }
        }
    }
})