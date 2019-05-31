/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.api
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class HttpConnectionTests : Spek({
    describe("http connection testing") {
        describe("get request") {
            val (response, body) = HttpConnection(
                    "https://httpbin.org/get?query=string",
                    HttpRequestMethod.GET,
                    null,
                    "text/html"
            ).execute().let { it to JSONObject(it.body) }

            it("get request response code") {
                assertEquals(200, response.responseCode)
            }

            it("get request header") {
                val requestHeader = body.getJSONObject("headers")
                assertEquals(
                        mapOf(
                                "Accept" to "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",
                                "Host" to "httpbin.org",
                                "Content-Type" to "text/html"
                        ).toSortedMap(),
                        // ignore the user-agent because of the version in it
                        requestHeader.toMap().filterKeys { it.length >= 3 && it != "User-Agent" }.toSortedMap()
                )
            }

            it("get request query string") {
                assertEquals("string", body.getJSONObject("args").getString("query"))
            }
        }

        describe("post request") {
            val (response, body) = HttpConnection(
                    "https://httpbin.org/post?query=string",
                    HttpRequestMethod.POST,
                    "body",
                    "text/html"
            ).execute().let { it to JSONObject(it.body) }

            it("post request response code") {
                assertEquals(200, response.responseCode)
            }

            it("post request header") {
                val requestHeader = body.getJSONObject("headers")
                assertEquals(
                        mapOf(
                                "Accept" to "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",
                                "Host" to "httpbin.org",
                                "Content-Type" to "text/html",
                                "Content-Length" to "4"
                        ).toSortedMap(),
                        // ignore the user-agent because of the version in it
                        requestHeader.toMap().filterKeys { it.length >= 4 && it != "User-Agent" }.toSortedMap()
                )
            }

            it("post request query string") {
                assertEquals("string", body.getJSONObject("args").getString("query"))
            }

            it("post request body") {
                assertEquals("body", body.getString("data"))
            }
        }

        describe("delete request") {
            val (response, body) = HttpConnection(
                    "https://httpbin.org/delete?query=string",
                    HttpRequestMethod.DELETE,
                    "body",
                    "text/html"
            ).execute().let { it to JSONObject(it.body) }

            it("delete request response code") {
                assertEquals(200, response.responseCode)
            }

            it("delete request header") {
                val requestHeader = body.getJSONObject("headers")
                assertEquals(
                        mapOf(
                                "Accept" to "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2",
                                "Host" to "httpbin.org",
                                "Content-Type" to "text/html",
                                "Content-Length" to "4"
                        ).toSortedMap(),
                        // ignore the user-agent because of the version in it
                        requestHeader.toMap().filterKeys { it.length >= 4 && it != "User-Agent" }.toSortedMap()
                )
            }

            it("delete request query string") {
                assertEquals("string", body.getJSONObject("args").getString("query"))
            }

            it("delete request body") {
                assertEquals("body", body.getString("data"))
            }
        }

        it("status code") {
            assertEquals(
                    200,
                    HttpConnection(
                            "https://apple.com",
                            HttpRequestMethod.GET,
                            null,
                            null
                    ).execute().responseCode
            )
        }

        it("retry") {
            api.useCache = false
            api.retryWhenRateLimited = true
            api.clearCache()
            (1..250).forEach {
                api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop").complete()
            }
            api.useCache = true
            api.retryWhenRateLimited = false
        }

        /* it("thrown exception when can't retry") {
             api.retryWhenRateLimited = false
             api.useCache = false
             assertThrows<SpotifyRatelimitedException> {
                 (1..50000).forEach {
                     println(it + 1)
                     println(System.currentTimeMillis())
                     println(api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop").complete()?.name)
                 }
             }
             api.useCache = true
         } */
    }
})
