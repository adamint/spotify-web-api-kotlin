package endpoints.browse

import junit.framework.TestCase
import main.SpotifyAPI

class BrowseAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()
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