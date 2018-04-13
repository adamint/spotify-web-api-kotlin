package com.adamratzman.spotify.kotlin.endpoints.pub.playlists

import com.adamratzman.spotify.main.api
import org.junit.Test

internal class PlaylistsAPITest {
    @Test
    fun getPlaylists() {
        println(api.playlists.getPlaylists("wizzler").complete())
    }

    @Test
    fun getPlaylist() {
        println(api.playlists.getPlaylist("spotify", "59ZbFPES4DQwEjBpWHzrtC").complete())
    }

    @Test
    fun getPlaylistTracks() {
        println(api.playlists.getPlaylistTracks("adamratzman1", "53Sr94VRrDgvLCh86lbMVx").complete())
    }

    @Test
    fun getPlaylistCovers() {
        println(api.playlists.getPlaylistCovers("spotify", "59ZbFPES4DQwEjBpWHzrtC").complete())
    }
}
