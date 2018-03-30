package com.adamratzman.endpoints.pub.album

import junit.framework.TestCase
import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.api

class AlbumAPITest : TestCase() {
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