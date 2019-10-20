/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UtilityTests : Spek({
    describe("Utility tests") {
        describe("Builder tests") {
            it("API invalid parameters") {
                assertThrows<IllegalArgumentException> {
                    spotifyAppApi { }.build()
                }

                assertThrows<IllegalArgumentException> {
                    spotifyClientApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                        }
                    }.build()
                }
                assertThrows<IllegalArgumentException> {
                    spotifyClientApi { }.build()
                }

                if (api is SpotifyClientAPI) {
                    assertThrows<IllegalArgumentException> {
                        spotifyClientApi {
                            credentials {
                                clientId = System.getProperty("clientId")
                                clientSecret = System.getProperty("clientSecret")
                            }
                        }.build()
                    }
                }
            }

            it("App API valid parameters") {
                assertDoesNotThrow {
                    val api = spotifyAppApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                            clientSecret = System.getProperty("clientSecret")
                        }
                    }
                    api.build()
                    api.buildAsync { }
                }
            }
        }
    }
})
