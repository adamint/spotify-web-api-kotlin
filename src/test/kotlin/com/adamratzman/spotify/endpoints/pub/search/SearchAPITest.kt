package com.adamratzman.spotify.kotlin.endpoints.pub.search

import com.adamratzman.spotify.main.api
import com.adamratzman.spotify.utils.Market
import org.junit.Test

internal class SearchAPITest {
    @Test
    fun searchTrack() {
        println(api.search.searchTrack("Meant to be").complete())
    }

    @Test
    fun searchAlbums() {
        println(api.search.searchAlbum("Meant to be").complete())
    }

    @Test
    fun searchArtists() {
        println((api.search.searchArtist("amir", market = Market.FR)).complete())
    }

    @Test
    fun searchPlaylists() {
        println(api.search.searchPlaylist("Meant to be").complete())
    }
}