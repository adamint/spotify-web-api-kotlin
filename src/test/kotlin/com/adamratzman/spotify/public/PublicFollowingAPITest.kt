package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicFollowingAPITest : Spek({
    describe("Public Following test") {
        val f = api.publicFollowing
        describe("do users follow playlist") {
            it("invalid users, valid playlist") {
                assertFalse(f.doUsersFollowPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "udontexist89").complete()[0])
            }
            it("no users, valid playlist") {
                assertThrows<BadRequestException> {
                    f.doUsersFollowPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M").complete()
                }
            }
            it("valid users, invalid playlist") {
                assertThrows<BadRequestException> {
                    f.doUsersFollowPlaylist("spotify", "asdkfjajksdfjkasdf", "adamratzman1").complete()
                }
            }
            it("valid users, valid playlist") {
                assertEquals(listOf(true, false),
                        f.doUsersFollowPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "adamratzman1", "adamratzman").complete())
            }
            it("mix of valid and invalid users, valid playlist") {
                assertEquals(listOf(false, true),
                        f.doUsersFollowPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "udontexist89", "adamratzman1").complete())
            }
        }
    }
})