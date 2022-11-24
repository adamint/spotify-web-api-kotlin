/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpRequest
import com.adamratzman.spotify.http.HttpResponse
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val cacheLocation = System.getenv("RESPONSE_CACHE_DIR")
actual fun getTestClientId(): String? = System.getenv("SPOTIFY_CLIENT_ID")
actual fun getTestClientSecret(): String? = System.getenv("SPOTIFY_CLIENT_SECRET")
actual fun getTestRedirectUri(): String? = System.getenv("SPOTIFY_REDIRECT_URI")
actual fun getTestTokenString(): String? = System.getenv("SPOTIFY_TOKEN_STRING")
actual fun isHttpLoggingEnabled(): Boolean = System.getenv("SPOTIFY_LOG_HTTP") == "true"
actual fun arePlayerTestsEnabled(): Boolean = System.getenv("SPOTIFY_ENABLE_PLAYER_TESTS")?.toBoolean() == true
actual fun areLivePkceTestsEnabled(): Boolean = System.getenv("VERBOSE_TEST_ENABLED")?.toBoolean() ?: false

actual suspend fun buildSpotifyApi(): GenericSpotifyApi? {
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

private val json = Json { prettyPrint = true }

actual fun getResponseCacher(): ResponseCacher? {
    if (cacheLocation == null) return null

    return object : ResponseCacher {
        override val cachedResponsesDirectoryPath: String = cacheLocation

        override fun cacheResponse(
            className: String,
            testName: String,
            responseNumber: Int,
            request: HttpRequest,
            response: HttpResponse
        ) {
            val baseDirectory = File(cacheLocation)
            if (!baseDirectory.exists()) baseDirectory.mkdirs()
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
}

@Serializable
data class CachedResponse(val request: Request, val response: Response)

@Serializable
data class Request(val url: String, val method: String, val body: String? = null)

@Serializable
data class Response(val responseCode: Int, val headers: Map<String, String>, val body: String)