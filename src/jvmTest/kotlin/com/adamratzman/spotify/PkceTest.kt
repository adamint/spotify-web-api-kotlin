/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.AuthenticationException
import kotlin.random.Random
import kotlin.test.assertFailsWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port

class PkceTest : Spek({
    if (getEnvironmentVariable("VERBOSE_TEST_ENABLED")?.toBoolean() == true &&
            _clientId != null) {
        val serverRedirectUri = "http://localhost:1337"
        describe("verbose pkce test. print auth, wait for redirect. requires VERBOSE_TEST_ENABLED=true AND http://localhost:1337 as a redirect uri") {
            val pkceCodeVerifier = (1..100).map { "1" }.joinToString("")
            val state = Random.nextLong().toString()

            println(
                    getPkceAuthorizationUrl(
                            *SpotifyScope.values(),
                            clientId = _clientId,
                            redirectUri = serverRedirectUri,
                            codeChallenge = getSpotifyPkceCodeChallenge(pkceCodeVerifier),
                            state = state
                    ))
            // spotifyClientApi(_clientId,)
            var stop = false

            port(1337)

            exception(Exception::class.java) { exception, request, response -> exception.printStackTrace() }

            get("/") { request, _ ->
                val code = request.queryParams("code")
                val actualState = request.queryParams("state")
                if (code != null && actualState == state) {
                    val api = spotifyClientPkceApi(
                            _clientId,
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

                    assertFailsWith<AuthenticationException> {
                        spotifyClientPkceApi(
                                _clientId,
                                serverRedirectUri,
                                token,
                                pkceCodeVerifier
                        ).build().library.getSavedTracks().complete()
                    }

                    val username = api.users.getClientProfile().complete().displayName

                    stop = true
                    "Successfully authenticated $username with PKCE and refreshed the token."
                } else "err."
            }

            println("Waiting...")

            while (!stop) {
                Thread.sleep(2000)
            }
        }
    }
})
