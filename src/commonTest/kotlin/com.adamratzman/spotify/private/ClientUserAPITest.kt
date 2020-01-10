/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientUserAPITest : Spek({
    describe("Client profile test") {
        val cp = (api as? SpotifyClientAPI)?.users
        it("valid user") {
            cp?.getClientProfile()?.complete()?.birthdate
        }
    }
})
