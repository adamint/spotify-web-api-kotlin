package com.adamratzman.spotify.public

import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SearchAPITest : Spek({
    describe("Search API test") {
        val s = api.search
        describe("search track") {
            it("valid request") {
                assert(s.searchTrack("hello", 1, 1, Market.US).complete().total > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchTrack("").complete().total }
            }
        }
        describe("search album") {
            it("valid request") {
                assert(s.searchAlbum("le début").complete().total > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchAlbum("").complete().total }
            }
        }
        describe("search playlist") {
            it("valid request") {
                assert(s.searchPlaylist("run2").complete().total > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchPlaylist("").complete().total }
            }
        }
        describe("search artist") {
            it("valid request") {
                assert(s.searchArtist("amir").complete().total > 0)
            }
            it("invalid request") {
                assertThrows<BadRequestException> { s.searchArtist("").complete().total }
            }
        }
    }
})