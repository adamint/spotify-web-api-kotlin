/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.Market
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class EpisodeApiTest : Spek({
    describe("Episode API  test") {
        val t = api.episodes
        describe("get episode") {
            it("known episode") {
                val episode = t.getEpisode("1cfOhXP4GQCd5ZFHoSF8gg").complete()!!

                assertEquals("417. Reasons to Be Cheerful", episode.name)
            }
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
                assertEquals(listOf("417. Reasons to Be Cheerful", "The 'Murder Hornets' And The Honey Bees"), t.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "4IhgnOc8rwMW70agMWVVfh").complete().map { it?.name })
            }
        }
    }
})
