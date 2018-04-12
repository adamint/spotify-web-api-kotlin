package com.adamratzman.spotify.kotlin.endpoints.pub.search

import com.adamratzman.spotify.main.api
import org.junit.Test

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