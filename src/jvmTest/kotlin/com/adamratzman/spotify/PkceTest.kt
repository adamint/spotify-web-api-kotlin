/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.AuthenticationException
import kotlin.random.Random
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port

class PkceTest {

    @Test
    fun testPkce() = runBlockingTest {
        if (getEnvironmentVariable("VERBOSE_TEST_ENABLED")?.toBoolean() == true &&
                clientId != null) {
            val serverRedirectUri = "http://localhost:1337"

            val pkceCodeVerifier = (1..100).joinToString("") { "1" }
            val state = Random.nextLong().toString()

            println(
                    getPkceAuthorizationUrl(
                            *SpotifyScope.values(),
                            clientId = clientId,
                            redirectUri = serverRedirectUri,
                            codeChallenge = getSpotifyPkceCodeChallenge(pkceCodeVerifier),
                            state = state
                    ))

            var stop = false

            port(1337)

            exception(Exception::class.java) { exception, _, _ -> exception.printStackTrace() }

            get("/") { request, _ ->
                runBlocking {
                    val code = request.queryParams("code")
                    val actualState = request.queryParams("state")
                    if (code != null && actualState == state) {
                        val api = spotifyClientPkceApi(
                                clientId,
                                serverRedirectUri,
                                code,
                                pkceCodeVerifier,
                                SpotifyApiOptionsBuilder(
                                        onTokenRefresh = { println("refreshed token") },
                                        testTokenValidity = true
                                )
                        ).build()
                        val token = api.token.copy(expiresIn = -1)
                        api.refreshToken()
                        // test that using same token will fail with auth exception

                        assertFailsWithSuspend<AuthenticationException> {
                            spotifyClientPkceApi(
                                    clientId,
                                    serverRedirectUri,
                                    token,
                                    pkceCodeVerifier
                            ).build().library.getSavedTracks()
                        }

                        val username = api.users.getClientProfile().displayName

                        stop = true
                        "Successfully authenticated $username with PKCE and refreshed the token."
                    } else "err."
                }
            }

            println("Waiting...")

            while (!stop) { Thread.sleep(2000) }
        }
    }
}
