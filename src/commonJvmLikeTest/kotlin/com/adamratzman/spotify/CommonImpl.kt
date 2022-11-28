/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpRequest
import com.adamratzman.spotify.http.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

val cacheLocation: String? = System.getenv("RESPONSE_CACHE_DIR")
val shouldRecacheRequests: Boolean = System.getenv("SHOULD_RECACHE_RESPONSES")?.toBoolean() == true

actual fun getTestClientId(): String? = System.getenv("SPOTIFY_CLIENT_ID")
actual fun getTestClientSecret(): String? = System.getenv("SPOTIFY_CLIENT_SECRET")
actual fun getTestRedirectUri(): String? = System.getenv("SPOTIFY_REDIRECT_URI")
actual fun getTestTokenString(): String? = System.getenv("SPOTIFY_TOKEN_STRING")
actual fun isHttpLoggingEnabled(): Boolean = System.getenv("SPOTIFY_LOG_HTTP") == "true"
actual fun arePlayerTestsEnabled(): Boolean = System.getenv("SPOTIFY_ENABLE_PLAYER_TESTS")?.toBoolean() == true
actual fun areLivePkceTestsEnabled(): Boolean = System.getenv("VERBOSE_TEST_ENABLED")?.toBoolean() ?: false

actual suspend fun buildSpotifyApi(testClassQualifiedName: String, testName: String): GenericSpotifyApi? {
    val clientId = getTestClientId()
    val clientSecret = getTestClientSecret()
    val tokenString = getTestTokenString()
    val logHttp = isHttpLoggingEnabled()

    val optionsCreator: (SpotifyApiOptions.() -> Unit) = {
        this.enableDebugMode = logHttp
        this.enableLogger = logHttp
    }

    return when {
        tokenString?.isNotBlank() == true -> {
            spotifyClientApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                    this.redirectUri = getTestRedirectUri()
                }
                authorization {
                    this.tokenString = tokenString
                }
                options(optionsCreator)
            }.build()
        }

        clientId?.isNotBlank() == true -> {
            spotifyAppApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                }
                options(optionsCreator)
            }.build()
        }

        else -> null
    }
}

object JvmResponseCacher : ResponseCacher {
    override val cachedResponsesDirectoryPath: String = cacheLocation ?: ""
    private val json = Json { prettyPrint = true }
    private val baseDirectory = File(cacheLocation)

    init {
        if (baseDirectory.exists()) baseDirectory.deleteRecursively()
        baseDirectory.mkdirs()
    }

    override fun cacheResponse(
        className: String,
        testName: String,
        responseNumber: Int,
        request: HttpRequest,
        response: HttpResponse
    ) {
        val testDirectory = File(baseDirectory.absolutePath + "/$className/$testName")
        if (!testDirectory.exists()) testDirectory.mkdirs()

        val responseFile = File(testDirectory.absolutePath + "/http_request_$responseNumber.txt")
        if (responseFile.exists()) responseFile.delete()
        responseFile.createNewFile()

        val objToWrite = CachedResponse(
            Request(
                request.url,
                request.method.toString(),
                request.bodyString
            ),
            Response(
                response.responseCode,
                response.headers.associate { it.key to it.value },
                response.body
            )
        )

        responseFile.appendText(json.encodeToString(objToWrite))
    }
}

actual fun getResponseCacher(): ResponseCacher? {
    if (cacheLocation == null || !shouldRecacheRequests) return null
    return JvmResponseCacher
}
