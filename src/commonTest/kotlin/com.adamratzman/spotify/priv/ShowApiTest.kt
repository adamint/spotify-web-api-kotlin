/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class ShowApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetShow() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val show = api.shows.getShow("1iohmBNlRooIVtukKeavRa")!!
        assertEquals("Love Letters", show.name)
        assertTrue(show.episodes.isNotEmpty())

        assertNull(api.shows.getShow("nonexistant show"))
    }

    @Test
    fun testGetShows() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertFailsWith<BadRequestException> { api.shows.getShows("hi", "dad", market = Market.US) }
        assertFailsWith<BadRequestException> {
            api.shows.getShows("1iohmBNlRooIVtukKeavRa", "j").map { it?.name }
        }
        assertEquals(
            listOf("Love Letters", "Freakonomics Radio"),
            api.shows.getShows("1iohmBNlRooIVtukKeavRa", "6z4NLXyHPga1UmSJsPK7G1").map { it?.name })
    }

    @Test
    fun testGetShowEpisodes() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(api.shows.getShowEpisodes("1iohmBNlRooIVtukKeavRa").items.isNotEmpty())
        assertFailsWith<BadRequestException> { api.shows.getShowEpisodes("adskjfjkasdf") }
    }
}
