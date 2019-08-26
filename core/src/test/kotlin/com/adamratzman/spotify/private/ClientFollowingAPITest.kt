/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientFollowingAPITest : Spek({
    describe("Client following tests") {
        if (api !is SpotifyClientAPI) return@describe
        it("following/unfollowing artists") {
            val testArtistId = "7eCmccnRwPmRnWPw61x6jM"

            if (api.following.isFollowingArtist(testArtistId).complete()) {
                api.following.unfollowArtist(testArtistId).complete()
            }

            assertTrue(!api.following.isFollowingArtist(testArtistId).complete())

            val beforeFollowing = api.following.getFollowedArtists().getAllItems().complete()

            assertNull(beforeFollowing.find { it.id == testArtistId })

            api.following.followArtist(testArtistId).complete()
            api.following.followArtist(testArtistId).complete()

            assertTrue(api.following.isFollowingArtist(testArtistId).complete())

            assertEquals(1, api.following.getFollowedArtists().getAllItems().complete().size - beforeFollowing.size)

            api.following.unfollowArtist(testArtistId).complete()
            api.following.unfollowArtist(testArtistId).complete()

            assertTrue(beforeFollowing.size == api.following.getFollowedArtists().getAllItems().complete().size)

            assertTrue(!api.following.isFollowingArtist(testArtistId).complete())

            assertThrows<BadRequestException> { api.following.isFollowingArtist("no u").complete() }
            assertThrows<BadRequestException> { api.following.followArtist("no u").complete() }
            assertThrows<BadRequestException> { api.following.followArtists(testArtistId, "no u").complete() }
            assertThrows<BadRequestException> { api.following.unfollowArtist("no u").complete() }
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
            val playlistOwnerId = "spotify"
            if (api.following.isFollowingPlaylist(playlistOwnerId, playlistId).complete()) {
                api.following.unfollowPlaylist(playlistId).complete()
            }

            assertFalse(api.following.isFollowingPlaylist(playlistOwnerId, playlistId).complete())

            api.following.followPlaylist(playlistId).complete()

            assertTrue(api.following.isFollowingPlaylist(playlistOwnerId, playlistId).complete())

            assertThrows<BadRequestException> { api.following.isFollowingPlaylist(" no u", "no u").complete() }
            assertThrows<BadRequestException> { api.following.unfollowPlaylist("no-u").complete() }
            assertThrows<BadRequestException> { api.following.followPlaylist("nou").complete() }
        }
    }
})