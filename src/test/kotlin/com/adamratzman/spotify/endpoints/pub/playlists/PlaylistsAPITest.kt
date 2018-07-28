package com.adamratzman.spotify.kotlin.endpoints.pub.playlists

import api
import com.adamratzman.spotify.utils.SimplePlaylist
import org.junit.Test

internal class PlaylistsAPITest {
    @Test
    fun getPlaylists() {
        println(api.playlists.getPlaylists("wizzler", limit = 3).complete().getNext<SimplePlaylist>().complete())
    }

    @Test
    fun getPlaylist() {
        println(api.playlists.getPlaylist("spotify", "5dsaf9ZbFPES4DQwEjBpWHzrtC").complete())
    }

    @Test
    fun getPlaylistTracks() {
        println(api.playlists.getPlaylistTracks("adamratzman1", "53Sr94VRrDgvLCh86lbMVx").complete().toPlaylistParams())
    }

    @Test
    fun getPlaylistCovers() {
        println(api.playlists.getPlaylistCovers("spotify", "59ZbFPES4DQwEjBpWHzrtC").complete())
    }
}
