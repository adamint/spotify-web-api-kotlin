package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientProfileAPITest : Spek({
    describe("Client profile test") {
        val cp = (api as? SpotifyClientAPI)?.clientProfile
        it("valid user") {
            println(cp?.getUserProfile()?.complete()?.birthdate)
            cp?.let { Assertions.assertDoesNotThrow { it.getUserProfile().complete().birthdate } }
        }
    }
})