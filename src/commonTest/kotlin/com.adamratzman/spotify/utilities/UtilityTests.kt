/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.api
import com.adamratzman.spotify.getEnvironmentalVariable
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UtilityTests {
    @Test
    fun builderApiInvalidParameters() {
        assertFailsWith<IllegalArgumentException> {
            spotifyAppApi { }.build()
        }

        assertFailsWith<IllegalArgumentException> {
            spotifyClientApi {
                credentials {
                    clientId = getEnvironmentalVariable("clientId")
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
                        clientId = getEnvironmentalVariable("clientId")
                        clientSecret = getEnvironmentalVariable("clientSecret")
                    }
                }.build()
            }
        }
    }

    @Test
    fun builderApiValidParameters() {
        val api = spotifyAppApi {
            credentials {
                clientId = getEnvironmentalVariable("clientId")
                clientSecret = getEnvironmentalVariable("clientSecret")
            }
        }
        api.build()
        api.buildAsync { }

    }
}
