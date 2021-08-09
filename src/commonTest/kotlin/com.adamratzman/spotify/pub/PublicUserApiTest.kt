/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.catch
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicUserApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testPublicUser() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(catch { api!!.users.getProfile("adamratzman1")!!.followers.total } != null)
            assertNull(api!!.users.getProfile("non-existant-user"))
        }
    }
}
