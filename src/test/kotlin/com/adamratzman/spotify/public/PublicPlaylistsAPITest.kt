package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicPlaylistsAPITest : Spek({
    describe("Public playlists test") {
        val p = api.playlists
        describe("get user's playlists") {
            it("available user should return playlists") {
                assertTrue(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
            }
            it("unknown user should throw exception but doesn't. if the id is valid, the list should be empty") {
                assertEquals(0, p.getPlaylists("non-existant-user").complete().items.size)
            }
        }
        describe("get playlist") {
            it("valid user, valid playlist id") {
                assertEquals("run2", p.getPlaylist("78eWnYKwDksmCHAjOUNPEj").complete()?.name)
            }
            it("invalid user, invalid playlist id") {
                assertNull(p.getPlaylist("nope").complete())
            }
        }
        describe("get playlist tracks") {
            it("valid playlist") {
                assertTrue(p.getPlaylistTracks("78eWnYKwDksmCHAjOUNPEj", offset = 1).complete().items.isNotEmpty())
            }
            it("invalid playlist") {
                assertThrows<BadRequestException> { p.getPlaylistTracks("adskjfjkasdf").complete() }
            }
        }
        describe("get playlist cover") {
            it("valid playlist") {
                assertTrue(p.getPlaylistCovers("78eWnYKwDksmCHAjOUNPEj").complete().isNotEmpty())
            }
            it("invalid playlist") {
                assertThrows<BadRequestException> { p.getPlaylistCovers("adskjfjkasdf").complete() }
            }
        }
    }
})