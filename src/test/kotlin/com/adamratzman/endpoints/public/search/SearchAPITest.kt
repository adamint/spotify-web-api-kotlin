package com.adamratzman.endpoints.public.search

import com.adamratzman.main.SpotifyAPI
import org.junit.Test

internal class SearchAPITest {
    val api = SpotifyAPI.Builder("79d455af5aea45c094c5cea04d167ac1", "").build()
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
        println((api.artists.getArtist("amir")))
    }

    @Test
    fun searchPlaylists() {
        Thread.sleep(5000)
        println(api.search.searchPlaylist("Meant to be"))
    }
}