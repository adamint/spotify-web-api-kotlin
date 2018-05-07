package com.adamratzman.spotify.endpoints.priv.playlists

import clientApi
import org.junit.Test

internal class UserPlaylistAPITest {
    @Test
    fun createPlaylist() {
    }

    @Test
    fun addTracksToPlaylist() {
    }

    @Test
    fun changePlaylistDescription() {
    }

    @Test
    fun getClientPlaylists() {
    }

    @Test
    fun reorderTracks() {
    }

    @Test
    fun replaceTracks() {
    }

    @Test
    fun uploadPlaylistCover() {
        println(clientApi.token.access_token)
        println(clientApi.playlists.getPlaylist("adamratzman1", "24LoWIwSxOTzaq44qLabiV").complete().name)
        clientApi.userPlaylists.uploadPlaylistCover("adamratzman1", "24LoWIwSxOTzaq44qLabiV",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg").complete()
    }
}