package endpoints.playlists

import junit.framework.TestCase
import main.SpotifyAPI

class PlaylistsAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()
    fun testGetPlaylists() {
        println(api.playlists.getPlaylists("adamratzman1"))
    }

    fun testGetPlaylist() {
        println(api.playlists.getPlaylist("adamratzman1", "2XbZmN597sRXP8XPIWcqOt"))
    }

    fun testGetPlaylistTracks() {
        println(api.playlists.getPlaylistTracks("adamratzman1", "2XbZmN597sRXP8XPIWcqOt"))
    }

}