package com.adamratzman.spotify.public

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientProfileAPITest : Spek({
    describe("Client profile test") {
        val cp = (api as? SpotifyClientAPI)?.clientProfile
        it("valid user") {
            cp?.let { assertDoesNotThrow { it.getUserProfile().complete().birthdate } }
        }
    }
})