package com.adamratzman.endpoints.public.browse

import junit.framework.TestCase
import com.adamratzman.main.SpotifyAPI

class BrowseAPITest : TestCase() {
    val api = SpotifyAPI.Builder("79d455af5aea45c094c5cea04d167ac1", "").build()
    fun testGetNewReleases() {
        println(api.browse.getNewReleases())
    }

    fun testGetFeaturedPlaylists() {
        println(api.browse.getFeaturedPlaylists())
    }

    fun testGetCategoryList() {
        println(api.browse.getCategoryList())
    }
    fun testGetCategory() {
        println(api.browse.getCategory(" "))
    }

    fun testGetPlaylistsForCategory() {
        println(api.browse.getPlaylistsForCategory("party"))
    }

    fun testGetRecommendations() {
        println(api.browse.getRecommendations(seedArtists = listOf("3TVXtAsR1Inumwj472S9r4"), seedGenres = listOf("pop", "country"), targets = hashMapOf(Pair("speechiness", 1.0), Pair("danceability", 1.0))))
    }

}