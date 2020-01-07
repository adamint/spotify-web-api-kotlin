/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicPlaylistsAPITest : Spek({
    describe("Public playlists test") {
        val p = api.playlists
        describe("get user's playlists") {
            it("available user should return playlists") {
                assertTrue(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getPlaylists("adamratzman1").complete().items.isNotEmpty())
            }
            it("unknown user should throw exception") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getPlaylists("non-existant-user").complete().items.size }
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
                try {
                    assertTrue(p.getPlaylistTracks("37i9dQZF1DXcBWIGoYBM5M", offset = 1).complete().items.isNotEmpty())
                } catch (e: SpotifyException.ParseException) {
                    e.message!!.removePrefix("Invalid JSON at ").let { println(it) }
                    println("msg: " + e.message)
                    throw e
                }
            }
            it("invalid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getPlaylistTracks("adskjfjkasdf").complete() }
            }
        }
        describe("get playlist cover") {
            it("valid playlist") {
                assertTrue(p.getPlaylistCovers("37i9dQZF1DXcBWIGoYBM5M").complete().isNotEmpty())
            }
            it("invalid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getPlaylistCovers("adskjfjkasdf").complete() }
            }
        }
    }
})
