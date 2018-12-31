/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.main.spotifyApi
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@Serializable
data class TokenTest(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("scope") val scopeString: String,
    @Optional @SerialName("refresh_token") val refreshToken: String? = null
)

class UtilityTests : Spek({
    describe("Utility tests") {
        describe("Serialization tests") {
            val json = """
                {"access_token":"token", "token_type":"Bearer","expires_in":3600,"scope":""}
            """.trimIndent()
            val token = JSON.parse(TokenTest.serializer(), json)
            assertEquals(3600, token.expiresIn)

            assertEquals(
                """{"access_token":"token","token_type":"Bearer","expires_in":3600,"scope":"","refresh_token":null}""",
                JSON.stringify(TokenTest.serializer(), token)
            )
        }
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
