package com.adamratzman.endpoints.public.search

import com.adamratzman.main.SpotifyAPI
import org.junit.Test

val api = SpotifyAPI.Builder("79d455af5aea45c094c5cea04d167ac1", "b81441a80aeb435aa545949c880853dd").build()

internal class SearchAPITest {
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
        println((api.search.searchArtist("amir")))
    }

    @Test
    fun searchPlaylists() {
        println(api.search.searchPlaylist("Meant to be"))
    }
}