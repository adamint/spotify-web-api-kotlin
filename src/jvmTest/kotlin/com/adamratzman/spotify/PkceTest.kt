/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.AuthenticationException
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port

class PkceTest {

    @Test
    fun testPkce() = runTestOnDefaultDispatcher {
        val clientId = getTestClientId()

        if (areLivePkceTestsEnabled() && clientId != null) {
            val serverRedirectUri = "http://localhost:1337"

            val pkceCodeVerifier = (1..100).joinToString("") { "1" }
            val state = Random.nextLong().toString()

            println(
                getSpotifyPkceAuthorizationUrl(
                    *SpotifyScope.values(),
                    clientId = clientId,
                    redirectUri = serverRedirectUri,
                    codeChallenge = getSpotifyPkceCodeChallenge(pkceCodeVerifier),
                    state = state
                )
            )

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
                            SpotifyUserAuthorization(
                                authorizationCode = code,
                                pkceCodeVerifier = pkceCodeVerifier
                            )
                        ) {
                            onTokenRefresh = { println("refreshed token") }
                            testTokenValidity = true
                        }.build()
                        val token = api.token.copy(expiresIn = -1)
                        api.refreshToken()
                        // test that using same token will fail with auth exception

                        assertFailsWith<AuthenticationException> {
                            spotifyClientPkceApi(
                                clientId,
                                serverRedirectUri,
                                SpotifyUserAuthorization(
                                    token = token,
                                    pkceCodeVerifier = pkceCodeVerifier
                                )
                            ).build().library.getSavedTracks()
                        }

                        val username = api.users.getClientProfile().displayName

                        stop = true
                        "Successfully authenticated $username with PKCE and refreshed the token."
                    } else "err."
                }
            }

            println("Waiting...")

            while (!stop) {
                Thread.sleep(2000)
            }
        }
    }
}
