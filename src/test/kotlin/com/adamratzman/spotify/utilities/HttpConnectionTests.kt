package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.utils.HttpConnection
import com.adamratzman.spotify.utils.HttpRequestMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class HttpConnectionTests : Spek({
    describe("http connection testing") {
        it("get request") {
            assertEquals(
                200, HttpConnection("https://apple.com", HttpRequestMethod.GET, null, null)
                    .execute().responseCode
            )
        }
    }
})