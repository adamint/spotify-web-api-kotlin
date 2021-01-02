/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HttpConnectionTests {
    @Test
    fun testGetRequest() {
        runBlockingTest {
            val (response, body) = HttpConnection(
                    "https://httpbin.org/get?query=string",
                    HttpRequestMethod.GET,
                    null,
                    null,
                    "text/html"
            ).execute().let { it to Json.decodeFromString(JsonObject.serializer(), it.body) }

            assertEquals(200, response.responseCode)
            val requestHeader = body["headers"]

            assertTrue {
                // ignore the user-agent because of the version in it
                requestHeader!!.jsonObject.map { it.key to it.value.jsonPrimitive.content }.containsAll(
                        mapOf(
                                "Accept" to "*/*",
                                "Host" to "httpbin.org"
                        ).toList()
                )
            }

            assertEquals("string", body["args"]!!.jsonObject.getValue("query").jsonPrimitive.content)
        }
    }

    @Test
    fun testPostRequest() {
        runBlockingTest {
            val (response, body) = HttpConnection(
                    "https://httpbin.org/post?query=string",
                    HttpRequestMethod.POST,
                    null,
                    "body",
                    "text/html"
            ).execute().let { it to Json.decodeFromString(JsonObject.serializer(), it.body) }

            assertEquals(200, response.responseCode)

            val requestHeader = body["headers"]
            assertTrue {
                requestHeader!!.jsonObject.map { it.key to it.value.jsonPrimitive.content }.containsAll(
                        mapOf(
                                "Accept" to "*/*",
                                "Host" to "httpbin.org",
                                "Content-Type" to "text/html",
                                "Content-Length" to "4"
                        ).toList()
                )
            }

            assertEquals("string", body["args"]!!.jsonObject.getValue("query").jsonPrimitive.content)
            assertEquals("body", body.jsonObject.getValue("data").jsonPrimitive.content)
        }
    }

    @Test
    fun testDeleteRequest() {
        runBlockingTest {
            val (response, _) = HttpConnection(
                    "https://httpbin.org/delete?query=string",
                    HttpRequestMethod.DELETE,
                    null,
                    null,
                    "text/html"
            ).execute().let { it to Json.decodeFromString(JsonObject.serializer(), it.body) }

            assertEquals(200, response.responseCode)
        }
    }

    /*
    @Test
    fun testRetry() = runBlockingTest {
        spotifyApi.await()?.let { api ->
            api.useCache = false
            api.retryWhenRateLimited = true
            api.clearCache()
            for (it in 1..2500) {
                api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop")
            }
            api.useCache = true
            api.retryWhenRateLimited = false
        }
    }

    @Test
    fun testThrowExceptionWhenCantRetry() = runBlockingTest {
        spotifyApi.await()?.let { api ->
            api.retryWhenRateLimited = false
            api.useCache = false
            assertFailsWithSuspend<SpotifyRatelimitedException> {
                repeat((1..50000).count()) {
                    println(api.tracks.getTrack("5OT3k9lPxI2jkaryRK3Aop")?.name)
                }
            }
            api.useCache = true
        }
    }*/
}
