package com.adamratzman.endpoints.priv.playlists

import com.adamratzman.main.SpotifyClientAPI
import com.adamratzman.main.clientApi
import org.junit.Test
import java.io.File

class ClientPlaylistsAPITest {
    @Test
    fun createPlaylist() {
        println(clientApi.clientPlaylists.createPlaylist("adamratzman1", "test", "test ;)"))
    }

    @Test
    fun addTrackToPlaylist() {
        println(clientApi.clientPlaylists.addTrackToPlaylist("adamratzman1", "53Sr94VRrDgvLCh86lbMVx", "3ghmFXEXTP6DdVOyvuPHpr"))
    }

    @Test
    fun changePlaylistDescription() {
        println(clientApi.clientPlaylists.changePlaylistDescription("adamratzman1", "53Sr94VRrDgvLCh86lbMVx",
                name = "test2", description = "this is a modified description", public = false))
    }

    @Test
    fun getClientPlaylists() {
        println(clientApi.clientPlaylists.getClientPlaylists())
    }

    @Test
    fun reorderTracks() {
        println(clientApi.clientPlaylists.reorderTracks("adamratzman1", "53Sr94VRrDgvLCh86lbMVx", 1, 1, 0))
    }

    @Test
    fun replaceTracks() {
        println(clientApi.clientPlaylists.replaceTracks("adamratzman1", "53Sr94VRrDgvLCh86lbMVx", "3ghmFXEXTP6DdVOyvuPHpr"))
    }

}