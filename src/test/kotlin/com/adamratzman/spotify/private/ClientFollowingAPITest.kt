package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.utils.Artist
import com.adamratzman.spotify.utils.BadRequestException
import org.junit.jupiter.api.Assertions.*
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

            assert(!api.following.isFollowingArtist(testArtistId).complete())

            val beforeFollowing = api.following.getFollowedArtists().complete().getAllItems<Artist>().complete()

            assertNull(beforeFollowing.find { it.id == testArtistId })

            api.following.followArtist(testArtistId).complete()

            assert(api.following.isFollowingArtist(testArtistId).complete())

            assertEquals(1, api.following.getFollowedArtists().complete().getAllItems<Artist>().complete().size - beforeFollowing.size)

            api.following.unfollowArtist(testArtistId).complete()

            assert(beforeFollowing.size == api.following.getFollowedArtists().complete().getAllItems<Artist>().complete().size)

            assert(!api.following.isFollowingArtist(testArtistId).complete())

            assertThrows<BadRequestException> { api.following.isFollowingArtist("no u").complete() }
            assertThrows<BadRequestException> { api.following.followArtist("no u").complete() }
            assertThrows<BadRequestException> { api.following.followArtists(testArtistId,"no u").complete() }
            assertThrows<BadRequestException> { api.following.unfollowArtist("no u").complete() }
        }
    }
})