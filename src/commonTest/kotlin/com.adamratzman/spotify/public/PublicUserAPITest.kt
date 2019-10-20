/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api

class PublicUserAPITest : Spek({
    describe("Public User test") {
        describe("get user") {
            it("available user should return author name") {
                assertEquals(0, api.users.getProfile("adamratzman1").complete()!!.followers.total)
            }
            it("unknown user should throw exception") {
                assertNull(api.users.getProfile("non-existant-user").complete())
            }
        }
    }
})