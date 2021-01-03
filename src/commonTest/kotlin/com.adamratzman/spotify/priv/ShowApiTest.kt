/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ShowApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
            println("Built API")
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testGetShow() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val show = api.shows.getShow("1iohmBNlRooIVtukKeavRa")!!
            assertEquals("Love Letters", show.name)
            assertTrue(show.episodes.isNotEmpty())

            assertNull(api.shows.getShow("nonexistant show"))
        }
    }

    @Test
    fun testGetShows() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<BadRequestException> { api.shows.getShows("hi", "dad", market = Market.US) }
            assertFailsWithSuspend<BadRequestException> {
                api.shows.getShows("1iohmBNlRooIVtukKeavRa", "j").map { it?.name }
            }
            assertEquals(
                listOf("Love Letters", "Freakonomics Radio"),
                api.shows.getShows("1iohmBNlRooIVtukKeavRa", "6z4NLXyHPga1UmSJsPK7G1").map { it?.name })
        }
    }

    @Test
    fun testGetShowEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.shows.getShowEpisodes("1iohmBNlRooIVtukKeavRa").items.isNotEmpty())
            assertFailsWithSuspend<BadRequestException> { api.shows.getShowEpisodes("adskjfjkasdf") }
        }
    }
}
