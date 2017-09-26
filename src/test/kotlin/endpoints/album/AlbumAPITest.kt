package endpoints.album

import junit.framework.TestCase
import main.SpotifyAPI

class AlbumAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()

    fun testGetAlbum() {
        println(api.albums.getAlbum("0dzeoQhVNzKkwM5ieOJC54"))
    }

    fun testGetAlbums() {
        println(api.albums.getAlbums(null, "4wHI7bZSdSQAbiVElWBlSO", "0dzeoQhVNzKkwM5ieOJC54"))
    }

    fun testGetAlbumTracks() {
        println(api.albums.getAlbumTracks("4wHI7bZSdSQAbiVElWBlSO"))
    }

}