/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.Market
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class EpisodeApiTest : Spek({
    describe("Episode API  test") {
        if (api !is SpotifyClientApi) return@describe
        val t = api.episodes
        describe("get episode") {
            it("unknown episode id should be null") {
                assertNull(t.getEpisode("nonexistant episode").complete())
            }
        }
        describe("get episodes") {
            it("unknown episodes") {
                assertFailsWith<BadRequestException> { t.getEpisodes("hi", "dad", market = Market.US).complete() }
            }
            it("mix of known episodes") {
                assertFailsWith<BadRequestException> { t.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j").complete().map { it?.name } }
            }
            it("known episodes") {
                assertEquals(listOf("The 'Murder Hornets' And The Honey Bees"), t.getEpisodes( "4IhgnOc8rwMW70agMWVVfh").complete().map { it?.name })
            }
        }
    }
})
