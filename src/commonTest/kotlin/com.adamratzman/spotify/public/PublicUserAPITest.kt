/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.catch
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicUserAPITest : Spek({
    describe("Public User test") {
        describe("get user") {
            it("available user should return author name") {
                assertTrue { catch {  api.users.getProfile("adamratzman1").complete()!!.followers.total } != null }
            }
            it("unknown user should throw exception") {
                assertNull(api.users.getProfile("non-existant-user").complete())
            }
        }
    }
})
