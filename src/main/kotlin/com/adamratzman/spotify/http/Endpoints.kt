/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyAppAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.base
import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.ErrorResponse
import com.adamratzman.spotify.models.serialization.toObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.util.Base64
import java.util.function.Supplier
import kotlin.math.ceil

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    val cache = SpotifyCache()

    internal fun get(url: String): String {
        return execute(url)
    }

    internal fun post(url: String, body: String? = null): String {
        return execute(url, body, HttpRequestMethod.POST)
    }

    internal fun put(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, HttpRequestMethod.PUT, contentType = contentType)
    }

    internal fun delete(
            url: String,
            body: String? = null,
            contentType: String? = null
    ): String {
        return execute(url, body, HttpRequestMethod.DELETE, contentType = contentType)
    }

    private fun execute(
            url: String,
            body: String? = null,
            method: HttpRequestMethod = HttpRequestMethod.GET,
            retry202: Boolean = true,
            contentType: String? = null
    ): String {
        if (api is SpotifyAppAPI && System.currentTimeMillis() >= api.expireTime) api.refreshToken()

        val spotifyRequest = SpotifyRequest(url, method, body, api)
        val cacheState = if (api.useCache) cache[spotifyRequest] else null

        if (cacheState?.isStillValid() == true) return cacheState.data
        else if (cacheState?.let { it.eTag == null } == true) {
            cache -= spotifyRequest
        }

        val document = createConnection(url, body, method, contentType).execute(
                cacheState?.eTag?.let {
                    listOf(HttpHeader("If-None-Match", it))
                }
        )

        return handleResponse(document, cacheState, spotifyRequest, retry202) ?: execute(
                url,
                body,
                method,
                false,
                contentType
        )
    }

    private fun handleResponse(
            document: HttpResponse,
            cacheState: CacheState?,
            spotifyRequest: SpotifyRequest,
            retry202: Boolean
    ): String? {
        val statusCode = document.responseCode

        if (statusCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
            if (cacheState?.eTag == null) throw BadRequestException("304 status only allowed on Etag-able endpoints")
            return cacheState.data
        }

        val responseBody = document.body

        document.headers.find { it.key == "Cache-Control" }?.also { cacheControlHeader ->
            if (api.useCache) {
                cache[spotifyRequest] = (cacheState ?: CacheState(
                        responseBody, document.headers
                        .find { it.key == "ETag" }?.value
                )).update(cacheControlHeader.value)
            }
        }

        if (document.responseCode / 200 != 1 /* Check if status is 2xx or 3xx */) {
            val message = try {
                document.body.toObject<ErrorResponse>(api).error
            } catch (e: Exception) {
                ErrorObject(400, "malformed request sent")
            }
            throw BadRequestException(message)
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
            body,
            contentType,
            listOf(HttpHeader("Authorization", "Bearer ${api.token.accessToken}")),
            api = api
    )

    internal fun <T> toAction(supplier: Supplier<T>) = SpotifyRestAction(api, supplier)
    internal fun <Z, T : AbstractPagingObject<Z>> toActionPaging(supplier: Supplier<T>) = SpotifyRestActionPaging(api, supplier)
}

internal class EndpointBuilder(private val path: String) {
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

class SpotifyCache {
    val cachedRequests = mutableMapOf<SpotifyRequest, CacheState>()

    internal operator fun get(request: SpotifyRequest): CacheState? {
        checkCache(request)
        return cachedRequests[request]
    }

    internal operator fun set(request: SpotifyRequest, state: CacheState) {
        if (request.api.useCache) cachedRequests[request] = state

        checkCache(request)
    }

    internal operator fun minusAssign(request: SpotifyRequest) {
        checkCache(request)
        cachedRequests.remove(request)
    }

    fun clear() = cachedRequests.clear()

    private fun checkCache(request: SpotifyRequest) {
        if (!request.api.useCache) clear()
        else {
            cachedRequests.entries.removeIf { !it.value.isStillValid() }

            val cacheLimit = request.api.cacheLimit
            val cacheUse = request.api.endpoints.sumBy { it.cache.cachedRequests.size }

            if (cacheLimit != null && cacheUse > cacheLimit) {
                val amountRemoveFromEach = ceil((cacheUse - cacheLimit).toDouble() / request.api.endpoints.size).toInt()

                request.api.endpoints.forEach { endpoint ->
                    val entries = endpoint.cache.cachedRequests.entries
                    val toRemove = entries.sortedBy { it.value.expireBy }.take(amountRemoveFromEach)

                    if (toRemove.isNotEmpty()) entries.removeAll(toRemove)
                }
            }
        }
    }
}

data class SpotifyRequest(
        val url: String,
        val method: HttpRequestMethod,
        val body: String?,
        val api: SpotifyAPI
)

data class CacheState(val data: String, val eTag: String?, val expireBy: Long = 0) {
    private val cacheRegex = "max-age=(\\d+)".toRegex()
    internal fun isStillValid(): Boolean = System.currentTimeMillis() <= this.expireBy

    internal fun update(expireBy: String): CacheState {
        val group = cacheRegex.find(expireBy)?.groupValues
        val time = group?.getOrNull(1)?.toLongOrNull() ?: throw BadRequestException("Unable to match regex")

        return this.copy(
                expireBy = System.currentTimeMillis() + 1000 * time
        )
    }
}

internal fun String.byteEncode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

internal fun String.encode() = URLEncoder.encode(this, "UTF-8")!!
