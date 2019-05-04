/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.BadRequestException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicFollowingAPITest : Spek({
    describe("Public Following test") {
        val f = api.following
        describe("do users follow playlist") {
            it("invalid users, valid playlist") {
                assertThrows<BadRequestException> { f.areFollowingPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "udontexist89").complete()[0] }
            }
            it("no users, valid playlist") {
                assertThrows<BadRequestException> {
                    f.areFollowingPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M").complete()
                }
            }
            it("valid users, invalid playlist") {
                assertThrows<BadRequestException> {
                    f.areFollowingPlaylist("spotify", "asdkfjajksdfjkasdf", "adamratzman1").complete()
                }
            }
            it("valid users, valid playlist") {
                assertEquals(listOf(true, false),
                        f.areFollowingPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "adamratzman1", "adamratzman").complete())
            }
            it("mix of valid and invalid users, valid playlist") {
                assertThrows<BadRequestException> {
                    f.areFollowingPlaylist("spotify", "37i9dQZF1DXcBWIGoYBM5M", "udontexist89", "adamratzman1").complete()
                }
            }
        }
    }
})