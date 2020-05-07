/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientFollowingApiTest : Spek({
    describe("Client following tests") {
        if (api !is SpotifyClientApi) return@describe
        it("following/unfollowing artists") {
            val testArtistId = "7eCmccnRwPmRnWPw61x6jM"

            if (api.following.isFollowingArtist(testArtistId).complete()) {
                api.following.unfollowArtist(testArtistId).complete()
            }

            assertTrue(!api.following.isFollowingArtist(testArtistId).complete())

            val beforeFollowing = api.following.getFollowedArtists().getAllItemsNotNull().complete()

            assertNull(beforeFollowing.find { it.id == testArtistId })

            api.following.followArtist(testArtistId).complete()
            api.following.followArtist(testArtistId).complete()

            assertTrue(api.following.isFollowingArtist(testArtistId).complete())

            assertEquals(1, api.following.getFollowedArtists().getAllItems().complete().size - beforeFollowing.size)

            api.following.unfollowArtist(testArtistId).complete()
            api.following.unfollowArtist(testArtistId).complete()

            assertEquals(beforeFollowing.size, api.following.getFollowedArtists().getAllItems().complete().size)

            assertTrue(!api.following.isFollowingArtist(testArtistId).complete())

            assertFailsWith<SpotifyException.BadRequestException> { api.following.isFollowingArtist("no u").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.following.followArtist("no u").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.following.followArtists(testArtistId, "no u").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.following.unfollowArtist("no u").complete() }
        }

        it("follow/unfollow users") {
            val testUserId = "adamratzman"

            if (api.following.isFollowingUser(testUserId).complete()) {
                api.following.unfollowUser(testUserId).complete()
            }

            api.following.followUser(testUserId).complete()

            assertTrue(api.following.isFollowingUser(testUserId).complete())

            api.following.unfollowUser(testUserId).complete()

            assertFalse(api.following.isFollowingUser(testUserId).complete())
        }

        it("follow/unfollow playlists") {
            val playlistId = "37i9dQZF1DXcBWIGoYBM5M"
            if (api.following.isFollowingPlaylist(playlistId).complete()) {
                api.following.unfollowPlaylist(playlistId).complete()
            }

            assertFalse(api.following.isFollowingPlaylist(playlistId).complete())

            api.following.followPlaylist(playlistId).complete()

            assertTrue(api.following.isFollowingPlaylist(playlistId).complete())

            assertFailsWith<SpotifyException.BadRequestException> { api.following.isFollowingPlaylist(" no u", "no u").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.following.unfollowPlaylist("no-u").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.following.followPlaylist("nou").complete() }
        }
    }
})
