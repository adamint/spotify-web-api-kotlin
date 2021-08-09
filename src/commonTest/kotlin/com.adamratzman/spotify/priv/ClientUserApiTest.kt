/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test

class ClientUserApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testClientProfile() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            api!!.users.getClientProfile().displayName
        }
    }
}
