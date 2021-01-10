/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClientFollowingApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
            println("Built API")
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testFollowUnfollowArtists() {
        println("a")
        runBlockingTest {
            println("b")
            if (!testPrereq()) {
                println("d")
                return@runBlockingTest
            }
            println("c")
            val testArtistId = "7eCmccnRwPmRnWPw61x6jM"
            println("1")
            if (api.following.isFollowingArtist(testArtistId)) {
                println("2")
                api.following.unfollowArtist(testArtistId)
            }
            println("3")

            assertTrue(!api.following.isFollowingArtist(testArtistId))

            val beforeFollowing = api.following.getFollowedArtists().getAllItemsNotNull()

            assertNull(beforeFollowing.find { it.id == testArtistId })

            api.following.followArtist(testArtistId)
            api.following.followArtist(testArtistId)

            assertTrue(api.following.isFollowingArtist(testArtistId))

            assertEquals(1, api.following.getFollowedArtists().getAllItems().size - beforeFollowing.size)

            api.following.unfollowArtist(testArtistId)
            api.following.unfollowArtist(testArtistId)

            assertEquals(beforeFollowing.size, api.following.getFollowedArtists().getAllItems().size)

            assertTrue(!api.following.isFollowingArtist(testArtistId))

            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.following.isFollowingArtist("no u") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.following.followArtist("no u") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.followArtists(
                    testArtistId,
                    "no u"
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.following.unfollowArtist("no u") }
        }
    }

    @Test
    fun testFollowUnfollowUsers() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val testUserId = "adamratzman"

            if (api.following.isFollowingUser(testUserId)) {
                api.following.unfollowUser(testUserId)
            }

            api.following.followUser(testUserId)

            assertTrue(api.following.isFollowingUser(testUserId))

            api.following.unfollowUser(testUserId)

            assertFalse(api.following.isFollowingUser(testUserId))
        }
    }

    @Test
    fun testFollowUnfollowPlaylists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val playlistId = "37i9dQZF1DXcBWIGoYBM5M"
            if (api.following.isFollowingPlaylist(playlistId)) {
                api.following.unfollowPlaylist(playlistId)
            }

            assertFalse(api.following.isFollowingPlaylist(playlistId))

            api.following.followPlaylist(playlistId)

            assertTrue(api.following.isFollowingPlaylist(playlistId))

            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.following.isFollowingPlaylist(
                    " no u",
                    "no u"
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.following.unfollowPlaylist("no-u") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.following.followPlaylist("nou") }
        }
    }
}
