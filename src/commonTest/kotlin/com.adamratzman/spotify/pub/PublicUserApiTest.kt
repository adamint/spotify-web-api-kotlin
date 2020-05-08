/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.api
import com.adamratzman.spotify.utils.catch
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class PublicUserApiTest : Spek({
    describe("Public User test") {
        describe("get user") {
            it("available user should return author name") {
                assertTrue { catch { api.users.getProfile("adamratzman1").complete()!!.followers.total } != null }
            }
            it("unknown user should throw exception") {
                assertNull(api.users.getProfile("non-existant-user").complete())
            }
        }
    }
})
