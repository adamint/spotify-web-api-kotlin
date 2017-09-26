package endpoints.search

import main.SpotifyAPI
import obj.Artist
import obj.Market
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
    }

    @Test
    fun searchArtists() {
        println((api.artists.getArtistTopTracks(api.search.searchArtist("amir", limit = 7).items[0].id, Market.US).tracks.sortedByDescending { it.disc_number }))
    }

    @Test
    fun searchPlaylists() {
        println(api.search.searchPlaylist("Meant to be"))
    }
}