/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.LocalTrack
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.utils.Market
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ShowApiTest : Spek({
    describe("Show API  test") {
        val t = api.shows
        describe("get show") {
            it("known show") {
                val show = t.getShow("1iohmBNlRooIVtukKeavRa").complete()!!
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
                assertFailsWith<BadRequestException> {   t.getShows("1iohmBNlRooIVtukKeavRa", "j").complete().map { it?.name } }
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
