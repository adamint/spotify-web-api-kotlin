package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicPlaylistsAPITest : Spek({
    describe("Public playlists test") {
        val p = api.playlists
        describe("get user's playlists") {
            it("available user should return playlists") {
                assert(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
            }
            it("unknown user should throw exception but doesn't. if the id is valid, the list should be empty") {
                assertEquals(0, p.getPlaylists("non-existant-user").complete().items.size)
            }
        }
        describe("get playlist") {
            it("valid user, valid playlist id") {
                assertEquals("run2", p.getPlaylist("adamratzman1", "78eWnYKwDksmCHAjOUNPEj").complete()?.name)
            }
            it("invalid user, invalid playlist id") {
                assertNull(p.getPlaylist("salut-mes-amis-adjfasdf", "nope").complete())
            }
        }
        describe("get playlist tracks") {
            it("valid playlist") {
                assert(p.getPlaylistTracks("adamratzman1", "78eWnYKwDksmCHAjOUNPEj", offset = 1).complete().items.isNotEmpty())
            }
            it("invalid playlist") {
                assertThrows<BadRequestException> { p.getPlaylistTracks("asdjfasjkdfjasjdf", "adskjfjkasdf").complete() }
            }
        }
        describe("get playlist cover") {
            it("valid playlist") {
                assert(p.getPlaylistCovers("adamratzman1", "78eWnYKwDksmCHAjOUNPEj").complete().isNotEmpty())
            }
            it("invalid playlist") {
                assertThrows<BadRequestException> { p.getPlaylistCovers("asdjfasjkdfjasjdf", "adskjfjkasdf").complete() }
            }
        }
    }
})