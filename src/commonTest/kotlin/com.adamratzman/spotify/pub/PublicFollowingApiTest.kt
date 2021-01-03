/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PublicFollowingApiTest {
    lateinit var api: GenericSpotifyApi

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
            println("Built API")
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testUsersFollowingPlaylist() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.areFollowingPlaylist(
                    "37i9dQZF1DXcBWIGoYBM5M",
                    "udontexist89"
                )[0]
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M")
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.areFollowingPlaylist("asdkfjajksdfjkasdf", "adamratzman1")
            }
            assertEquals(
                listOf(true, false),
                api.following.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M", "adamratzman1", "adamratzman")
            )
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M", "udontexist89", "adamratzman1")
            }
        }
    }
}
