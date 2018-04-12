package com.adamratzman.spotify.kotlin.endpoints.pub.artists

import com.adamratzman.spotify.main.api
import com.adamratzman.spotify.obj.Market
import junit.framework.TestCase

class ArtistsAPITest : TestCase() {
    fun testGetArtist() {
        println(api.artists.getArtist("0C8ZW7ezQVs4URX5aX7Kqx"))
    }

    fun testGetArtists() {
        println(api.artists.getArtists("0C8ZW7ezQVs4URX5aX7Kqx", "0C8ZW7ezQVs4URX5aX7Kqx"))
    }

    fun testGetArtistAlbums() {
        println(api.artists.getArtistAlbums("0C8ZW7ezQVs4URX5aX7Kqx"))
    }

    fun testGetArtistTopTracks() {
        println(api.artists.getArtistTopTracks("0C8ZW7ezQVs4URX5aX7Kqx", Market.US))
    }

    fun testGetRelatedArtists() {
        println(api.artists.getRelatedArtists("0C8ZW7ezQVs4URX5aX7Kqx"))
    }

}