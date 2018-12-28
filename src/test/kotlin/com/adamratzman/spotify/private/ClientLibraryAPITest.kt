package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientLibraryAPITest: Spek({
    describe("Client Library tests") {
        if (api !is SpotifyClientAPI) return@describe

    }
})