/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.*

class UtilityTests {
    var api: GenericSpotifyApi? = null

    @Test
    fun testPagingObjectGetAllItems(): TestResult = runTestOnDefaultDispatcher {
        buildSpotifyApi(this::class.simpleName!!, ::testPagingObjectGetAllItems.name)?.let { api = it }

        val spotifyWfhPlaylist = api!!.playlists.getPlaylist("37i9dQZF1DWTLSN7iG21yC")!!
        val totalTracks = spotifyWfhPlaylist.tracks.total
        val allTracks = spotifyWfhPlaylist.tracks.getAllItemsNotNull()
        assertEquals(totalTracks, allTracks.size)
    }

    @Test
    fun testGeneratePkceCodeChallenge() {
        assertEquals(
            "c7jV_d4sQ658HgwINAR77Idumz1ik1lIb1JNlOva75E",
            getSpotifyPkceCodeChallenge("thisisaveryrandomalphanumericcodeverifierandisgreaterthan43characters")
        )
        assertEquals(
            "9Y__uhKapn7GO_ElcaQpd8C3hdOyqTzAU4VXyR2iEV0",
            getSpotifyPkceCodeChallenge("12345678901234567890123456789012345678901234567890")
        )
    }

    @Test
    fun testPagingObjectTakeItemsSize(): TestResult = runTestOnDefaultDispatcher {
        buildSpotifyApi(this::class.simpleName!!, ::testPagingObjectTakeItemsSize.name)?.let { api = it }
        assertEquals(24, api!!.browse.getNewReleases(limit = 12).take(24).size)
    }

    @Test
    fun testInvalidApiBuilderParameters() = runTestOnDefaultDispatcher {
        assertFailsWith<IllegalArgumentException> {
            spotifyAppApi { }.build()
        }

        assertFailsWith<IllegalArgumentException> {
            spotifyClientApi { }.build()
        }

        if (!PlatformUtils.IS_JVM) return@runTestOnDefaultDispatcher

        assertFailsWith<IllegalArgumentException> {
            spotifyClientApi {
                credentials {
                    clientId = getTestClientId()
                }
            }.build()
        }

        if (api is SpotifyClientApi) {
            assertFailsWith<IllegalArgumentException> {
                spotifyClientApi {
                    credentials {
                        clientId = getTestClientId()
                        clientSecret = getTestClientSecret()
                    }
                }.build()
            }
        }
    }

    @Test
    fun testValidAppApiBuilderParameters() = runTestOnDefaultDispatcher {
        if (!PlatformUtils.IS_JVM) return@runTestOnDefaultDispatcher

        if (getTestClientId() != null && getTestClientSecret() != null) {
            val testApi = spotifyAppApi {
                credentials {
                    clientId = getTestClientId()
                    clientSecret = getTestClientSecret()
                }
            }

            testApi.build()
        }
    }

    @Test
    fun testAutomaticRefresh() = runTestOnDefaultDispatcher {
        if (!PlatformUtils.IS_JVM) return@runTestOnDefaultDispatcher

        var test = false
        val api = spotifyAppApi {
            credentials {
                clientId = getTestClientId()
                clientSecret = getTestClientSecret()
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

    @Test
    fun testRequiredScopes(): TestResult = runTestOnDefaultDispatcher {
        buildSpotifyApi(this::class.simpleName!!, ::testRequiredScopes.name)?.let { api = it }
        if (api !is SpotifyClientApi) return@runTestOnDefaultDispatcher

        assertFailsWith<IllegalStateException> {
            spotifyClientApi(
                api!!.clientId,
                api!!.clientSecret,
                (api as SpotifyClientApi).redirectUri,
                SpotifyUserAuthorization(token = api!!.token.copy(scopeString = null))
            ) { requiredScopes = listOf(SpotifyScope.PlaylistReadPrivate) }.build()
        }
    }

    @Test
    fun testResponseSubscriber(): TestResult = runTestOnDefaultDispatcher {
        buildSpotifyApi(this::class.simpleName!!, ::testPagingObjectGetAllItems.name)?.let { api = it }
        val options = api!!.spotifyApiOptions
        val oldSubscriber = options.httpResponseSubscriber
        options.httpResponseSubscriber = { request, response ->
            assertNotNull(
                api!!.getCache().entries.singleOrNull { it.key.url == request.url }
            )
            oldSubscriber?.invoke(request, response)
        }
        
        api!!.tracks.getTrack("6DrcMKnfMByc3RhhIvEw0F")
        options.httpResponseSubscriber = oldSubscriber
    }
}
