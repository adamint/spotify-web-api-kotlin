/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShowApiTest {
    var api: GenericSpotifyApi? = null

    val market = Market.US

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = api != null

    @Test
    fun testGetShow() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertNull(api!!.shows.getShow("invalid-show", market = market))
            assertEquals("Freakonomics Radio", api!!.shows.getShow("spotify:show:6z4NLXyHPga1UmSJsPK7G1", market = market)?.name)
        }
    }

    @Test
    fun testGetShows() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWithSuspend<BadRequestException> { api!!.shows.getShows("hi", "dad", market = market) }
            assertFailsWithSuspend<BadRequestException> {
                api!!.shows.getShows("78nWZk9ikQrOJX7OTRE2h7", "j", market = market).map { it?.name }
            }
            assertEquals(
                listOf("Freakonomics Radio"),
                api!!.shows.getShows("6z4NLXyHPga1UmSJsPK7G1", market = market).map { it?.name }
            )
        }
    }

    @Test
    fun testGetShowEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWithSuspend<BadRequestException> { api!!.shows.getShowEpisodes("hi", market = market) }
            val show = api!!.shows.getShow("6z4NLXyHPga1UmSJsPK7G1", market = market)!!
            assertEquals(show.id, api!!.shows.getShowEpisodes(show.id, market = market).first()?.toFullEpisode(market)?.show?.id)
        }
    }
}
