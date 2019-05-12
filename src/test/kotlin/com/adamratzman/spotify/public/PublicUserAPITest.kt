/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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