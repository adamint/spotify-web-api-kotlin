package com.adamratzman.spotify.kotlin.endpoints.pub.browse

import com.adamratzman.spotify.endpoints.pub.browse.TuneableTrackAttribute
import com.adamratzman.spotify.main.api
import com.adamratzman.spotify.main.clientApi
import com.adamratzman.spotify.utils.Market
import junit.framework.TestCase

class BrowseAPITest : TestCase() {
    fun testGetAvailableGenreSeeds() {
        println(api.browse.getAvailableGenreSeeds().complete())
    }

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
        println(api.browse.getCategory("chill", market = Market.FR, locale = "fr_FR").complete())
    }

    fun testGetPlaylistsForCategory() {
        println(api.browse.getPlaylistsForCategory("party").complete())
    }

    fun testGetRecommendations() {
        api.browse.getRecommendations(seedTracks = listOf("43ehiuXyqIdLyZ4eEH47nw"),
                targetAttributes = hashMapOf(Pair(TuneableTrackAttribute.DANCEABILITY, 1.0)),
                market = Market.FR)
                .complete().tracks.let {
                    println(it.map { it.name })
            clientApi.player.startPlayback(tracksToPlay = *it.map { it.id }.toTypedArray()).complete() }
    }

}