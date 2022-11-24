/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.LocalTrack
import com.adamratzman.spotify.models.PodcastEpisodeTrack
import com.adamratzman.spotify.models.Track
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.test.TestResult

class PublicPlaylistsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetUserPlaylists(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetUserPlaylists.name)

        assertTrue(api.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
        assertTrue(api.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
        assertTrue(api.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
        assertTrue(api.playlists.getUserPlaylists("adamratzman1").items.isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.playlists.getUserPlaylists("non-existant-user").items.size }
    }

    @Test
    fun testGetPlaylist(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetPlaylist.name)

        assertEquals("run2", api.playlists.getPlaylist("78eWnYKwDksmCHAjOUNPEj")?.name)
        assertNull(api.playlists.getPlaylist("nope"))
        assertTrue(api.playlists.getPlaylist("78eWnYKwDksmCHAjOUNPEj")!!.tracks.isNotEmpty())
        val playlistWithLocalAndNonLocalTracks = api.playlists.getPlaylist("627gNjNzj3sOrSiDm5acc2")!!.tracks
        assertEquals(LocalTrack::class, playlistWithLocalAndNonLocalTracks[0].track!!::class)
        assertEquals(Track::class, playlistWithLocalAndNonLocalTracks[1].track!!::class)

        if (api is SpotifyClientApi) {
            val playlistWithPodcastsTracks = api.playlists.getPlaylist("37i9dQZF1DX8tN3OFXtAqt")!!.tracks
            assertEquals(PodcastEpisodeTrack::class, playlistWithPodcastsTracks[0].track!!::class)
        }
    }

    @Test
    fun testGetPlaylistTracks(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetPlaylistTracks.name)

        assertTrue(api.playlists.getPlaylistTracks("78eWnYKwDksmCHAjOUNPEj").items.isNotEmpty())
        val playlist = api.playlists.getPlaylistTracks("627gNjNzj3sOrSiDm5acc2")
        assertEquals(LocalTrack::class, playlist[0].track!!::class)
        assertEquals(Track::class, playlist[1].track!!::class)
        assertFailsWith<SpotifyException.BadRequestException> { api.playlists.getPlaylistTracks("adskjfjkasdf") }

        if (api is SpotifyClientApi) {
            val playlistWithPodcasts = api.playlists.getPlaylistTracks("37i9dQZF1DX8tN3OFXtAqt")
            assertEquals(PodcastEpisodeTrack::class, playlistWithPodcasts[0].track!!::class)
        }
    }

    @Test
    fun testGetPlaylistCover(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetPlaylistCover.name)

        assertTrue(api.playlists.getPlaylistCovers("37i9dQZF1DXcBWIGoYBM5M").isNotEmpty())
        assertFailsWith<SpotifyException.BadRequestException> { api.playlists.getPlaylistCovers("adskjfjkasdf") }
    }

    @Test
    fun testConvertSimplePlaylistToPlaylist(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testConvertSimplePlaylistToPlaylist.name)

        val simplePlaylist = api.playlists.getUserPlaylists("adamratzman1").first()!!
        assertEquals(simplePlaylist.id, simplePlaylist.toFullPlaylist()?.id)
    }
}
