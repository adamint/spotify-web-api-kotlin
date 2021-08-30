/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyException.TimeoutException
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.ErrorResponse
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.ConcurrentHashMap
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlin.math.ceil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

public abstract class SpotifyEndpoint(public val api: GenericSpotifyApi) {
    public val cache: SpotifyCache = SpotifyCache()
    internal val json get() = api.spotifyApiOptions.json

    internal fun endpointBuilder(path: String) = EndpointBuilder(path, api)

    protected fun checkBulkRequesting(maxSize: Int, itemSize: Int) {
        if (itemSize > maxSize && !api.spotifyApiOptions.allowBulkRequests) {
            throw BadRequestException(
                "Too many items ($itemSize) provided, only $maxSize allowed",
                IllegalArgumentException("Bulk requests (SpotifyApi.spotifyApiOptions.allowBulkRequests) are not turned on, and too many items were provided")
            )
        }
        if (itemSize == 0) throw BadRequestException("No items provided!")
    }

    protected fun requireScopes(vararg requiredScopes: SpotifyScope, anyOf: Boolean = false) {
        val scopes = api.token.scopes ?: return
        val notFoundScopes = requiredScopes.filter { it !in scopes }
        if ((!anyOf && notFoundScopes.isNotEmpty()) || (anyOf && scopes.none { it in requiredScopes })) {
            throw SpotifyException.SpotifyScopesNeededException(missingScopes = notFoundScopes)
        }
    }

    protected suspend fun <T, R> bulkRequest(
        chunkSize: Int,
        items: List<T>,
        producer: suspend (List<T>) -> R
    ): List<R> {
        return coroutineScope {
            items.chunked(chunkSize).map { chunk ->
                async {
                    producer(chunk)
                }
            }.awaitAll()
        }
    }

    internal suspend fun get(url: String): String {
        return execute(url)
    }

    internal suspend fun post(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, HttpRequestMethod.POST, contentType = contentType)
    }

    internal suspend fun put(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, HttpRequestMethod.PUT, contentType = contentType)
    }

    internal suspend fun delete(
        url: String,
        body: String? = null,
        contentType: String? = null
    ): String {
        return execute(url, body, HttpRequestMethod.DELETE, contentType = contentType)
    }

    private suspend fun execute(
        url: String,
        body: String? = null,
        method: HttpRequestMethod = HttpRequestMethod.GET,
        retry202: Boolean = true,
        contentType: String? = null,
        attemptedRefresh: Boolean = false
    ): String {
        if (api.token.shouldRefresh()) {
            if (!api.spotifyApiOptions.automaticRefresh) throw SpotifyException.ReAuthenticationNeededException(message = "The access token has expired.")
            else api.refreshToken()
        }

        val spotifyRequest = SpotifyRequest(url, method, body, api)
        val cacheState = if (api.useCache) cache[spotifyRequest] else null

        if (cacheState?.isStillValid() == true) return cacheState.data
        else if (cacheState?.let { it.eTag == null } == true) {
            cache -= spotifyRequest
        }

        try {
            return withTimeout(api.spotifyApiOptions.requestTimeoutMillis ?: 100 * 1000L) {
                try {
                    val document = createConnection(url, body, method, contentType).execute(
                        additionalHeaders = cacheState?.eTag?.let {
                            listOf(HttpHeader("If-None-Match", it))
                        },
                        retryIfInternalServerErrorLeft = api.spotifyApiOptions.retryOnInternalServerErrorTimes
                    )

                    handleResponse(document, cacheState, spotifyRequest, retry202) ?: execute(
                        url,
                        body,
                        method,
                        false,
                        contentType
                    )
                } catch (e: BadRequestException) {
                    if (e.statusCode == 401 && !attemptedRefresh) {
                        api.refreshToken()

                        execute(
                            url,
                            body,
                            method,
                            retry202,
                            contentType,
                            true
                        )
                    } else throw e
                }
            }
        } catch (e: CancellationException) {
            throw TimeoutException(
                e.message
                    ?: "The request $spotifyRequest timed out after (${api.spotifyApiOptions.requestTimeoutMillis ?: (100_000)}ms.",
                e
            )
        }
    }

    private fun handleResponse(
        document: HttpResponse,
        cacheState: CacheState?,
        spotifyRequest: SpotifyRequest,
        retry202: Boolean
    ): String? {
        val statusCode = document.responseCode

        if (statusCode == HttpConnectionStatus.HTTP_NOT_MODIFIED.code) {
            requireNotNull(cacheState?.eTag) { "304 status only allowed on Etag-able endpoints" }
            return cacheState?.data
        }

        val responseBody = document.body

        document.headers.find { it.key.equals("Cache-Control", true) }?.also { cacheControlHeader ->
            if (api.useCache) {
                cache[spotifyRequest] = (cacheState ?: CacheState(
                    responseBody, document.headers
                        .find { it.key.equals("ETag", true) }?.value
                )).update(cacheControlHeader.value)
            }
        }

        if (document.responseCode !in 200..399 /* Check if status is not 2xx or 3xx */) {
            val response = try {
                document.body.toObject(ErrorResponse.serializer(), api, api.spotifyApiOptions.json)
            } catch (e: Exception) {
                ErrorResponse(ErrorObject(400, "malformed request sent"), e)
            }
            throw BadRequestException(response.error)
        } else if (document.responseCode == 202 && retry202) return null
        return responseBody
    }

    private fun createConnection(
        url: String,
        body: String? = null,
        method: HttpRequestMethod = HttpRequestMethod.GET,
        contentType: String? = null
    ) = HttpConnection(
        url,
        method,
        null,
        body,
        contentType,
        listOf(HttpHeader("Authorization", "Bearer ${api.token.accessToken}")),
        api
    )
}

internal class EndpointBuilder(private val path: String, api: GenericSpotifyApi) {
    val base = api.spotifyApiOptions.proxyBaseUrl ?: api.spotifyApiBase
    private val builder = StringBuilder(base + path)

    fun with(key: String, value: Any?): EndpointBuilder {
        if (value != null && (value !is String || value.isNotEmpty())) {
            if (builder.toString() == base + path) builder.append("?")
            else builder.append("&")
            builder.append(key).append("=").append(value.toString())
        }
        return this
    }

    override fun toString() = builder.toString()
}

public class SpotifyCache {
    public val cachedRequests: ConcurrentHashMap<SpotifyRequest, CacheState> = ConcurrentHashMap()

    internal operator fun get(request: SpotifyRequest): CacheState? {
        checkCache(request)
        return cachedRequests[request]
    }

    internal operator fun set(request: SpotifyRequest, state: CacheState) {
        if (request.api.useCache) cachedRequests.put(request, state)

        checkCache(request)
    }

    internal operator fun minusAssign(request: SpotifyRequest) {
        checkCache(request)
        cachedRequests.remove(request)
    }

    public fun clear(): Unit = cachedRequests.clear()

    private fun checkCache(request: SpotifyRequest) {
        if (!request.api.useCache) clear()
        else {
            cachedRequests.entries.removeAll { !it.value.isStillValid() }

            val cacheLimit = request.api.spotifyApiOptions.cacheLimit
            val cacheUse = cachedRequests.size

            if (cacheLimit != null && cacheUse > cacheLimit) {
                val amountRemoveFromEach = ceil((cacheUse - cacheLimit).toDouble() / request.api.endpoints.size).toInt()

                val entries = cachedRequests.entries

                val toRemove = entries.sortedBy { it.value.expireBy }.take(amountRemoveFromEach)

                if (toRemove.isNotEmpty()) entries.removeAll(toRemove)
            }
        }
    }
}

public data class SpotifyRequest(
    val url: String,
    val method: HttpRequestMethod,
    val body: String?,
    val api: GenericSpotifyApi
)

@Serializable
public data class CacheState(val data: String, val eTag: String?, val expireBy: Long = 0) {
    @Transient
    private val cacheRegex = "max-age=(\\d+)".toRegex()
    internal fun isStillValid(): Boolean = getCurrentTimeMs() <= this.expireBy

    internal fun update(expireBy: String): CacheState {
        val group = cacheRegex.find(expireBy)?.groupValues
        val time =
            group?.getOrNull(1)?.toLongOrNull() ?: throw BadRequestException("Unable to match regex")

        return this.copy(
            expireBy = getCurrentTimeMs() + 1000 * time
        )
    }
}
