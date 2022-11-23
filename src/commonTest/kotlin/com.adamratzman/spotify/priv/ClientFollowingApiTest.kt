/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class ClientFollowingApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testFollowUnfollowArtists() = runTestOnDefaultDispatcher {
        return@runTestOnDefaultDispatcher // TODO https://github.com/adamint/spotify-web-api-kotlin/issues/309

        /*buildApi(SpotifyClientApi::class)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testArtistId = "7eCmccnRwPmRnWPw61x6jM"
        if (api!!.following.isFollowingArtist(testArtistId)) {
            api!!.following.unfollowArtist(testArtistId)
        }

        assertTrue(!api!!.following.isFollowingArtist(testArtistId))

        val beforeFollowing = api!!.following.getFollowedArtists().getAllItemsNotNull()

        assertNull(beforeFollowing.find { it.id == testArtistId })

        api!!.following.followArtist(testArtistId)
        api!!.following.followArtist(testArtistId)

        assertTrue(api!!.following.isFollowingArtist(testArtistId))

        assertEquals(1, api!!.following.getFollowedArtists().getAllItems().size - beforeFollowing.size)

        api!!.following.unfollowArtist(testArtistId)
        api!!.following.unfollowArtist(testArtistId)

        assertEquals(beforeFollowing.size, api!!.following.getFollowedArtists().getAllItems().size)

        assertTrue(!api!!.following.isFollowingArtist(testArtistId))

        assertFailsWith<SpotifyException.BadRequestException> { api!!.following.isFollowingArtist("no u") }
        assertFailsWith<SpotifyException.BadRequestException> { api!!.following.followArtist("no u") }
        assertFailsWith<SpotifyException.BadRequestException> {
            api!!.following.followArtists(
                testArtistId,
                "no u"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> { api!!.following.unfollowArtist("no u") }*/
    }

    @Test
    fun testFollowUnfollowUsers() = runTestOnDefaultDispatcher {
        return@runTestOnDefaultDispatcher // TODO https://github.com/adamint/spotify-web-api-kotlin/issues/309

        /*buildApi(SpotifyClientApi::class)
                if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testUserId = "adamratzman"

        if (api!!.following.isFollowingUser(testUserId)) {
            api!!.following.unfollowUser(testUserId)
        }

        api!!.following.followUser(testUserId)

        assertTrue(api!!.following.isFollowingUser(testUserId))

        api!!.following.unfollowUser(testUserId)

        assertFalse(api!!.following.isFollowingUser(testUserId))*/
    }

    @Test
    fun testFollowUnfollowPlaylists() = runTestOnDefaultDispatcher {
        return@runTestOnDefaultDispatcher // TODO https://github.com/adamint/spotify-web-api-kotlin/issues/309

        /*buildApi(SpotifyClientApi::class)
                if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val playlistId = "37i9dQZF1DXcBWIGoYBM5M"
        if (api!!.following.isFollowingPlaylist(playlistId)) {
            api!!.following.unfollowPlaylist(playlistId)
        }

        assertFalse(api!!.following.isFollowingPlaylist(playlistId))

        api!!.following.followPlaylist(playlistId)

        assertTrue(api!!.following.isFollowingPlaylist(playlistId))

        assertFailsWith<SpotifyException.BadRequestException> {
            api!!.following.isFollowingPlaylist(
                " no u",
                "no u"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> { api!!.following.unfollowPlaylist("no-u") }
        assertFailsWith<SpotifyException.BadRequestException> { api!!.following.followPlaylist("nou") }*/
    }
}
