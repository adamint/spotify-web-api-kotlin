package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SearchAPITest : Spek({
    describe("Search API test") {
        val s = api.search
        describe("search track") {
            it("valid request") {
                assertTrue(s.searchTrack("hello", 1, 1, Market.US).complete().isNotEmpty())
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