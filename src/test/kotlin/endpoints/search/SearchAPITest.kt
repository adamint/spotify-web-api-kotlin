package endpoints.search

import main.SpotifyAPI
import org.junit.Test

internal class SearchAPITest {
    val api = SpotifyAPI.Builder().build()
    @Test
    fun searchTrack() {
        println(api.search.searchTrack("Meant to be"))
    }
    @Test
    fun searchAlbums() {
        println(api.search.searchAlbum("Meant to be"))
    }

    @Test
    fun searchArtists() {
        println(api.search.searchArtist("Meant to be"))
    }

    @Test
    fun searchPlaylists() {
        println(api.search.searchPlaylist("Meant to be"))
    }
}