/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.getEnvironmentVariable
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UtilityTests {
    lateinit var api: GenericSpotifyApi

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testPagingObjectTakeItemsSize() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertEquals(60, api.browse.getNewReleases(limit = 12).take(60).size)
        }
    }

            @Test
    fun testInvalidApiBuilderParameters() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<IllegalArgumentException> {
                spotifyAppApi { }.build()
            }

            assertFailsWithSuspend<IllegalArgumentException> {
                spotifyClientApi {
                    credentials {
                        clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
                    }
                }.build()
            }
            assertFailsWithSuspend<IllegalArgumentException> {
                spotifyClientApi { }.build()
            }

            if (api is SpotifyClientApi) {
                assertFailsWithSuspend<IllegalArgumentException> {
                    spotifyClientApi {
                        credentials {
                            clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
                            clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
                        }
                    }.build()
                }
            }
        }
    }

    @Test
    fun testValidAppApiBuilderParameters() {
        runBlockingTest {
            if (com.adamratzman.spotify.clientId != null && com.adamratzman.spotify.clientSecret != null) {
                val testApi = spotifyAppApi {
                    credentials {
                        clientId = com.adamratzman.spotify.clientId
                        clientSecret = com.adamratzman.spotify.clientSecret
                    }
                }

                testApi.build()
            }
        }
    }

    @Test
    fun testAutomaticRefresh() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            var test = false
            val api = spotifyAppApi {
                credentials {
                    clientId = com.adamratzman.spotify.clientId
                    clientSecret = com.adamratzman.spotify.clientSecret
                }

                options {
                    onTokenRefresh = { test = true }
                }
            }.build()

            api.token = api.token.copy(expiresIn = -1)
            val currentToken = api.token

            api.browse.getAvailableGenreSeeds()

            assertTrue(test)
            assertTrue(api.token.accessToken != currentToken.accessToken)
        }
    }

    @Test
    fun testRequiredScopes() {
        runBlockingTest {
            if (!testPrereq() || api !is SpotifyClientApi) return@runBlockingTest

            assertFailsWithSuspend<IllegalStateException> {
                spotifyClientApi(
                    api.clientId,
                    api.clientSecret,
                    (api as SpotifyClientApi).redirectUri,
                    SpotifyUserAuthorization(token = api.token.copy(scopeString = null))
                ) { requiredScopes = listOf(SpotifyScope.PLAYLIST_READ_PRIVATE) }.build()
            }
        }
    }
}
