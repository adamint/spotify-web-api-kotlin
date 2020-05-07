/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPersonalizationApiTest : Spek({
    describe("personalization endpoints") {
        if (api !is SpotifyClientApi) return@describe
        it("top artists") {
            assertTrue(api.personalization.getTopArtists(5, timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM)
                    .complete().items.isNotEmpty())
        }

        it("top tracks") {
            assertTrue(api.personalization.getTopTracks(5).complete().items.isNotEmpty())
        }
    }
})
