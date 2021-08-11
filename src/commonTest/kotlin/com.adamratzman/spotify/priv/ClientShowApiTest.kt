/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ClientShowApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetShow() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertNull(api!!.shows.getShow("invalid-show"))
            assertEquals("Freakonomics Radio", api!!.shows.getShow("spotify:show:6z4NLXyHPga1UmSJsPK7G1")?.name)
        }
    }

    @Test
    fun testGetShows() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<BadRequestException> { api!!.shows.getShows("hi", "dad") }
            assertFailsWith<BadRequestException> {
                api!!.shows.getShows("78nWZk9ikQrOJX7OTRE2h7", "j").map { it?.name }
            }
            assertEquals(
                listOf("Freakonomics Radio"),
                api!!.shows.getShows("6z4NLXyHPga1UmSJsPK7G1").map { it?.name }
            )
        }
    }

    @Test
    fun testGetShowEpisodes() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<BadRequestException> { api!!.shows.getShowEpisodes("hi") }
            val show = api!!.shows.getShow("6z4NLXyHPga1UmSJsPK7G1")!!
            assertEquals(
                show.id,
                api!!.shows.getShowEpisodes(show.id).first()?.toFullEpisode(market = Market.US)?.show?.id
            )
        }
    }
}
