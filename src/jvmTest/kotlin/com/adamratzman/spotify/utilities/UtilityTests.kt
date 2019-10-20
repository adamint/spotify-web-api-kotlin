/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertFailsWith

class UtilityTests : Spek({
    describe("Utility tests") {
        describe("Builder tests") {
            it("API invalid parameters") {
                assertFailsWith<IllegalArgumentException> {
                    spotifyAppApi { }.build()
                }

                assertFailsWith<IllegalArgumentException> {
                    spotifyClientApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                        }
                    }.build()
                }
                assertFailsWith<IllegalArgumentException> {
                    spotifyClientApi { }.build()
                }

                if (api is SpotifyClientAPI) {
                    assertFailsWith<IllegalArgumentException> {
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
})
