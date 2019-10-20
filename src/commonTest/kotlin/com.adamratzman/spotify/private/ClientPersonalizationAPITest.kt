/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationAPI
import com.adamratzman.spotify.SpotifyClientAPI
import org.junit.jupiter.api.Assertions.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPersonalizationAPITest : Spek({
    describe("personalization endpoints") {
        if (api !is SpotifyClientAPI) return@describe
        it("top artists") {
            assertTrue(api.personalization.getTopArtists(5, timeRange = ClientPersonalizationAPI.TimeRange.MEDIUM_TERM)
                    .complete().isNotEmpty())
        }

        it("top tracks") {
            assertTrue(api.personalization.getTopTracks(5).complete().isNotEmpty())
        }
    }
})