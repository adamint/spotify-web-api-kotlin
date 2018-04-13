package com.adamratzman.spotify.kotlin.endpoints.pub.browse

import com.adamratzman.spotify.main.api
import junit.framework.TestCase

class BrowseAPITest : TestCase() {
    fun testGetNewReleases() {
        val action = api.browse.getNewReleases()
        println(action.complete())
    }

    fun testGetFeaturedPlaylists() {
        println(api.browse.getFeaturedPlaylists().complete())
    }

    fun testGetCategoryList() {
        println(api.browse.getCategoryList().complete())
    }

    fun testGetCategory() {
        println(api.browse.getCategory("pop").complete())
    }

    fun testGetPlaylistsForCategory() {
        println(api.browse.getPlaylistsForCategory("party").complete())
    }

    fun testGetRecommendations() {
        println(api.browse.getRecommendations(seedArtists = listOf("3TVXtAsR1Inumwj472S9r4"), seedGenres = listOf("pop", "country"), targets = hashMapOf(Pair("speechiness", 1.0), Pair("danceability", 1.0))).complete())
    }

}