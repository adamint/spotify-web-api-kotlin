package com.adamratzman.endpoints.pub.playlists

import com.adamratzman.main.api
import org.junit.Test

internal class PlaylistsAPITest {
    @Test
    fun getPlaylists() {
        println(api.playlists.getPlaylists("wizzler"))
    }

    @Test
    fun getPlaylist() {
        println(api.playlists.getPlaylist("spotify", "59ZbFPES4DQwEjBpWHzrtC"))
    }

    @Test
    fun getPlaylistTracks() {
        println(api.playlists.getPlaylistTracks("spotify_espa%C3%B1a", "21THa8j9TaSGuXYNBU5tsC"))
    }

    @Test
    fun getPlaylistCovers() {
        println(api.playlists.getPlaylistCovers("spotify", "59ZbFPES4DQwEjBpWHzrtC"))
    }
}
