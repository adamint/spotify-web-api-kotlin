/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import com.adamratzman.spotify.spotifyApi
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UtilityTests : Spek({
    describe("Utility tests") {
        describe("Builder tests") {
            it("API invalid parameters") {
                assertThrows<IllegalArgumentException> {
                    spotifyApi { }.buildCredentialed()
                }

                assertThrows<IllegalArgumentException> {
                    spotifyApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                        }
                    }.buildCredentialed()
                }
                assertThrows<IllegalArgumentException> {
                    spotifyApi { }.buildClient()
                }

                if (api is SpotifyClientAPI) {
                    assertThrows<IllegalArgumentException> {
                        spotifyApi {
                            credentials {
                                clientId = System.getProperty("clientId")
                                clientSecret = System.getProperty("clientSecret")
                            }
                        }.buildClient()
                    }
                }
            }

            it("App API valid parameters") {
                assertDoesNotThrow {
                    val api = spotifyApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                            clientSecret = System.getProperty("clientSecret")
                        }
                    }
                    api.buildCredentialed()
                    api.buildCredentialedAsync { }
                }
            }
        }
    }
})
