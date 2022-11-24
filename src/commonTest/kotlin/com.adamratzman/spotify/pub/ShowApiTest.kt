/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.test.TestResult

class ShowApiTest : AbstractTest<GenericSpotifyApi>() {
    private val market = Market.US

    @Test
    fun testGetShow(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShow.name)

        assertNull(api.shows.getShow("invalid-show", market = market))
        assertEquals(
            "Freakonomics Radio",
            api.shows.getShow("spotify:show:6z4NLXyHPga1UmSJsPK7G1", market = market)?.name
        )
    }

    @Test
    fun testGetShows(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShows.name)

        assertFailsWith<BadRequestException> { api.shows.getShows("hi", "dad", market = market) }
        assertFailsWith<BadRequestException> {
            api.shows.getShows("78nWZk9ikQrOJX7OTRE2h7", "j", market = market).map { it?.name }
        }
        assertEquals(
            listOf("Freakonomics Radio"),
            api.shows.getShows("6z4NLXyHPga1UmSJsPK7G1", market = market).map { it?.name }
        )
    }

    @Test
    fun testGetShowEpisodes(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetShowEpisodes.name)

        assertFailsWith<BadRequestException> { api.shows.getShowEpisodes("hi", market = market) }
        val show = api.shows.getShow("6z4NLXyHPga1UmSJsPK7G1", market = market)!!
        assertEquals(
            show.id,
            api.shows.getShowEpisodes(show.id, market = market).first()?.toFullEpisode(market)?.show?.id
        )
    }
}
