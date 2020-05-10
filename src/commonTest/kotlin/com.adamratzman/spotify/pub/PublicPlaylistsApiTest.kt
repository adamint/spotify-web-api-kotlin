/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.LocalTrack
import com.adamratzman.spotify.models.Track
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicPlaylistsApiTest : Spek({
    describe("Public playlists test") {
        val p = api?.playlists?: return@describe
        describe("get user's playlists") {
            it("available user should return playlists") {
                assertTrue(p.getUserPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getUserPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getUserPlaylists("adamratzman1").complete().items.isNotEmpty())
                assertTrue(p.getUserPlaylists("adamratzman1").complete().items.isNotEmpty())
            }
            it("unknown user should throw exception") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getUserPlaylists("non-existant-user").complete().items.size }
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
            it("valid playlist, get tracks") {
                assertTrue(p.getPlaylistTracks("78eWnYKwDksmCHAjOUNPEj").complete().items.isNotEmpty())
            }

            it("valid playlist, get tracks including one local track") {
                val playlist = p.getPlaylistTracks("0vzdw0N41qZLbRDqyx2cE0").complete()
                assertEquals(LocalTrack::class, playlist[0].track!!::class)
                assertEquals(Track::class, playlist[1].track!!::class)
            }

            it("invalid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getPlaylistTracks("adskjfjkasdf").complete() }
            }
        }
        describe("get playlist cover") {
            it("valid playlist, get cover") {
                assertTrue(p.getPlaylistCovers("37i9dQZF1DXcBWIGoYBM5M").complete().isNotEmpty())
            }
            it("invalid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> { p.getPlaylistCovers("adskjfjkasdf").complete() }
            }
        }
    }
})
