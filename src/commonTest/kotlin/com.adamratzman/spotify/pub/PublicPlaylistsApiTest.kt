/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.models.LocalTrack
import com.adamratzman.spotify.models.PodcastEpisodeTrack
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicPlaylistsApiTest {
    var api: GenericSpotifyApi? = null

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = api != null

    @Test
    fun testGetUserPlaylists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
            assertTrue(api!!.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
            assertTrue(api!!.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
            assertTrue(api!!.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api!!.playlists.getUserPlaylists("non-existant-user").items.size }
        }
    }

    @Test
    fun testGetPlaylist() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertEquals("run2", api!!.playlists.getPlaylist("78eWnYKwDksmCHAjOUNPEj")?.name)
            assertNull(api!!.playlists.getPlaylist("nope"))
            assertTrue(api!!.playlists.getPlaylist("78eWnYKwDksmCHAjOUNPEj")!!.tracks.isNotEmpty())
            val playlistWithLocalAndNonLocalTracks = api!!.playlists.getPlaylist("0vzdw0N41qZLbRDqyx2cE0")!!.tracks
            assertEquals(LocalTrack::class, playlistWithLocalAndNonLocalTracks[0].track!!::class)
            assertEquals(Track::class, playlistWithLocalAndNonLocalTracks[1].track!!::class)

            if (api is SpotifyClientApi) {
                val playlistWithPodcastsTracks = api!!.playlists.getPlaylist("37i9dQZF1DX8tN3OFXtAqt")!!.tracks
                assertEquals(PodcastEpisodeTrack::class, playlistWithPodcastsTracks[0].track!!::class)
            }
        }
    }

    @Test
    fun testGetPlaylistTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.playlists.getPlaylistTracks("78eWnYKwDksmCHAjOUNPEj").items.isNotEmpty())
            val playlist = api!!.playlists.getPlaylistTracks("0vzdw0N41qZLbRDqyx2cE0")
            assertEquals(LocalTrack::class, playlist[0].track!!::class)
            assertEquals(Track::class, playlist[1].track!!::class)
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api!!.playlists.getPlaylistTracks("adskjfjkasdf") }

            if (api is SpotifyClientApi) {
                val playlistWithPodcasts = api!!.playlists.getPlaylistTracks("37i9dQZF1DX8tN3OFXtAqt")
                assertEquals(PodcastEpisodeTrack::class, playlistWithPodcasts[0].track!!::class)
            }
        }
    }

    @Test
    fun testGetPlaylistCover() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.playlists.getPlaylistCovers("37i9dQZF1DXcBWIGoYBM5M").isNotEmpty())
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api!!.playlists.getPlaylistCovers("adskjfjkasdf") }
        }
    }

    @Test
    fun testConvertSimplePlaylistToPlaylist() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!
            val simplePlaylist = api!!.playlists.getUserPlaylists("adamratzman1").first()!!
            assertEquals(simplePlaylist.id, simplePlaylist.toFullPlaylist()?.id)
        }
    }
}
