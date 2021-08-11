/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPersonalizationApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetTopArtists() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(
                api!!.personalization
                    .getTopArtists(5, timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM)
                    .items
                    .isNotEmpty()
            )
        }
    }

    @Test
    fun testGetTopTracks() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.personalization.getTopTracks(5).items.isNotEmpty())
        }
    }
}
