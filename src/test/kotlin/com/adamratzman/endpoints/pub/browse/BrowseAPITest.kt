package com.adamratzman.endpoints.pub.browse

import junit.framework.TestCase
import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.api

class BrowseAPITest : TestCase() {
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
        println(api.browse.getCategory("pop"))
    }

    fun testGetPlaylistsForCategory() {
        println(api.browse.getPlaylistsForCategory("party"))
    }

    fun testGetRecommendations() {
        println(api.browse.getRecommendations(seedArtists = listOf("3TVXtAsR1Inumwj472S9r4"), seedGenres = listOf("pop", "country"), targets = hashMapOf(Pair("speechiness", 1.0), Pair("danceability", 1.0))))
    }

}