/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicFollowingAPITest : Spek({
    describe("Public Following test") {
        val f = api.following
        describe("do users follow playlist") {
            it("invalid users, valid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> { f.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M", "udontexist89").complete()[0] }
            }
            it("no users, valid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    f.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M").complete()
                }
            }
            it("valid users, invalid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    f.areFollowingPlaylist("asdkfjajksdfjkasdf", "adamratzman1").complete()
                }
            }
            it("valid users, valid playlist") {
                assertEquals(listOf(true, false),
                        f.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M", "adamratzman1", "adamratzman").complete())
            }
            it("mix of valid and invalid users, valid playlist") {
                assertFailsWith<SpotifyException.BadRequestException> {
                    f.areFollowingPlaylist("37i9dQZF1DXcBWIGoYBM5M", "udontexist89", "adamratzman1").complete()
                }
            }
        }
    }
})
