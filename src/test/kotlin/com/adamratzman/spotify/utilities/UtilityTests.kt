/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.main.spotifyApi
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
                    spotifyApi {
                        credentials {
                            clientId = System.getProperty("clientId")
                            clientSecret = System.getProperty("clientSecret")
                        }
                    }.buildCredentialed()
                }
            }
        }
    }
})
