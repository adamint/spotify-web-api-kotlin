package com.adamratzman.spotify.endpoints.priv.playlists

import clientApi
import org.junit.Test

internal class ClientPlaylistAPITest {
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
        clientApi.clientPlaylists.uploadPlaylistCover("adamratzman1", "24LoWIwSxOTzaq44qLabiV",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg").complete()
    }

    @Test
    fun removeAllOccurances() {
        clientApi.clientPlaylists.addTracksToPlaylist("adamratzman1", "53Sr94VRrDgvLCh86lbMVx", "3ghmFXEXTP6DdVOyvuPHpr")
                .complete()
        clientApi.playlists.getPlaylist("adamratzman1", "53Sr94VRrDgvLCh86lbMVx").complete()
                .tracks.items.map { it.track.id }.let { println(it) }
        //clientApi.clientPlaylists.removeAllOccurances("adamratzman1", "53Sr94VRrDgvLCh86lbMVx",
          //      "3ghmFXEXTP6DdVOyvuPHpr").complete()
    }
}