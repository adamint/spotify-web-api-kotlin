package endpoints.search

import main.SpotifyAPI
import obj.Artist
import obj.Playlist
import org.junit.Test

internal class SearchAPITest {
    val api = SpotifyAPI.Builder().build()
    @Test
    fun searchTrack() {
        println(api.search.searchTrack("Meant to be"))
    }
    @Test
    fun searchAlbums() {
        println(api.search.searchAlbum("Meant to be").items.sortedByDescending { it.id })
    }

    @Test
    fun searchArtists() {
        println((api.search.searchArtist("amir", limit = 7).items.sortedByDescending { it.popularity }))
    }

    @Test
    fun searchPlaylists() {
        println(api.search.searchPlaylist("Meant to be"))
    }
}