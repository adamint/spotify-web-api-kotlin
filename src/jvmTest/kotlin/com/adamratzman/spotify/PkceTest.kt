package com.adamratzman.spotify

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port
import kotlin.random.Random

class PkceTest : Spek({
    if (getEnvironmentVariable("VERBOSE_TEST_ENABLED")?.toBoolean() == true
            && _clientId != null) {
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
                    val api = spotifyClientPkceApi(_clientId, serverRedirectUri, code, pkceCodeVerifier).build()
                    val username = api.users.getClientProfile().complete().displayName
                    stop = true
                    "Successfully authenticated $username with PKCE."
                } else "err."
            }

            while (!stop) {
                Thread.sleep(2000)
                println("Waiting...")
            }

        }
    }
})