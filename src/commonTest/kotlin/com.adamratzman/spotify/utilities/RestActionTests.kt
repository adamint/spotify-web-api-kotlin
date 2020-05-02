/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.SpotifyException.TimeoutException
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.public.SearchApi.SearchType.TRACK
import com.adamratzman.spotify.utils.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpotifyExperimentalHttpApi
class RestActionTests : Spek({
    describe("Paging Object") {
        it("next test") {
            /*runBlocking {
                assertEquals(3, api.search.search("I", TRACK, limit = 10).complete().tracks!!.getWithNext(3).complete().toList().size)
                assertEquals(45, api.search.searchTrack("I", limit = 15).getWithNextItems(3).complete().toList().size)
            }*/
        }
    }

    describe("request timeout") {
        it("request timeout of 0ms (must fail)") {
            val prevTimeout = api.requestTimeoutMillis

            api.requestTimeoutMillis = 1

            runBlocking {
                assertFailsWith<TimeoutException> {
                    api.search.searchTrack("fail").complete()
                }
            }

            api.requestTimeoutMillis = prevTimeout
        }
    }
})
