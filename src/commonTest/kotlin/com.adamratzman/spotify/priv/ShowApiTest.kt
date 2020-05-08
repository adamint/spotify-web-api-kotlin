/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.Market
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ShowApiTest : Spek({
    describe("Show API  test") {
        if (api !is SpotifyClientApi) return@describe
        val t = api.shows

        describe("get show") {
            it("known show") {
                val show = t.getShow("38bS44xjbVVZ3No3ByF1dJ").complete()!!
                assertEquals("Love Letters", show.name)
                assertTrue(show.episodes.isNotEmpty())
            }
            it("unknown show id should be null") {
                assertNull(t.getShow("nonexistant show").complete())
            }
        }
        describe("get shows") {
            it("unknown shows") {
                assertFailsWith<BadRequestException> { t.getShows("hi", "dad", market = Market.US).complete() }
            }
            it("mix of known shows") {
                assertFailsWith<BadRequestException> { t.getShows("1iohmBNlRooIVtukKeavRa", "j").complete().map { it?.name } }
            }
            it("known shows") {
                assertEquals(listOf("Love Letters", "Freakonomics Radio"), t.getShows("1iohmBNlRooIVtukKeavRa", "6z4NLXyHPga1UmSJsPK7G1").complete().map { it?.name })
            }
        }

        describe("get show episodes") {
            it("valid show, get episodes") {
                assertTrue(t.getShowEpisodes("1iohmBNlRooIVtukKeavRa").complete().items.isNotEmpty())
            }

            it("invalid show") {
                assertFailsWith<BadRequestException> { t.getShowEpisodes("adskjfjkasdf").complete() }
            }
        }
    }
})
