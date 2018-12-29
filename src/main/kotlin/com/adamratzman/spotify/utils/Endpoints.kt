/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyAppAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.SpotifyRestPagingAction
import com.adamratzman.spotify.main.base
import com.google.gson.JsonParseException
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.function.Supplier

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    internal val cache = SpotifyCache()

    fun get(url: String): String {
        return execute(url)
    }

    fun post(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.POST)
    }

    fun put(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, Connection.Method.PUT, contentType = contentType)
    }

    fun delete(
        url: String,
        body: String? = null,
        data: List<Pair<String, String>>? = null,
        contentType: String? = null
    ): String {
        return execute(url, body, Connection.Method.DELETE, data = data, contentType = contentType)
    }

    private fun execute(
        url: String,
        body: String? = null,
        method: Connection.Method = Connection.Method.GET,
        retry202: Boolean = true,
        contentType: String? = null,
        data: List<Pair<String, String>>? = null
    ): String {
        if (api is SpotifyAppAPI && System.currentTimeMillis() >= api.expireTime) api.refreshToken()

        var connection = Jsoup.connect(url).ignoreContentType(true)
        data?.forEach { connection.data(it.first, it.second) }
        if (contentType != null) connection.header("Content-Type", contentType)
        if (body != null) {
            if (contentType != null) connection.requestBody(body)
            else
                connection = if (method == Connection.Method.DELETE) {
                    val key = JSONObject(body).keySet().toList()[0]
                    connection.data(key, JSONObject(body).getJSONArray(key).toString())
                } else connection.requestBody(body)
        }

        val spotifyRequest = SpotifyRequest(url, method, body, data)
        val requestOrder = cache.shouldRequest(spotifyRequest)

        if (requestOrder == SpotifyCache.RequestOrder.NO) {
            return cache.getData(spotifyRequest)
        } else if (requestOrder == SpotifyCache.RequestOrder.YES_WITH_ETAG) {
            connection = connection.header("If-None-Match", cache.getEtag(spotifyRequest))
        }

        connection = connection.header("Authorization", "Bearer ${api.token.access_token}")

        val document = connection.method(method).ignoreHttpErrors(true).execute()

        if (document.statusCode() == 304) {
            if (requestOrder != SpotifyCache.RequestOrder.YES_WITH_ETAG) throw BadRequestException("304 status only allowed on Etag-able endpoints")
            return cache.getData(spotifyRequest)
        } else {
            val cacheHeaders = document.header("Cache-Control")
            // this api is single-user and thus can ignore the public/private cache distinction
            val maxAge: Int = cache.cacheRegex
                .find(cacheHeaders)
                ?.groupValues?.let {
                it.getOrNull(1)?.toIntOrNull()
            } ?: throw BadRequestException("Unable to match regex")

            if (requestOrder == SpotifyCache.RequestOrder.YES_WITH_ETAG) cache.remove(spotifyRequest)

            cache.add(
                spotifyRequest,
                CacheResponse(System.currentTimeMillis() + 1000 * maxAge, document.header("ETag"), document.body())
            )
        }

        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) {
            val message = try {
                api.gson.fromJson(document.body(), ErrorResponse::class.java).error
            } catch (e: JsonParseException) {
                ErrorObject(400, "malformed request (likely spaces)")
            }
            throw BadRequestException(message)
        } else if (document.statusCode() == 202 && retry202) return execute(url, body, method, false)
        return document.body()
    }

    fun <T> toAction(supplier: Supplier<T>) = SpotifyRestAction(api, supplier)
    fun <Z, T : PagingObject<Z>> toPagingObjectAction(supplier: Supplier<T>) = SpotifyRestPagingAction(api, supplier)
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

internal class SpotifyCache {
    private val cachedRequests = hashMapOf<SpotifyRequest, CacheResponse>()
    internal val cacheRegex = "max-age=(\\d+)".toRegex()

    internal fun shouldRequest(spotifyRequest: SpotifyRequest): RequestOrder {
        return cachedRequests[spotifyRequest]?.let {
            if (System.currentTimeMillis() <= it.expireBy) RequestOrder.NO
            else {
                if (it.eTag == null) {
                    cachedRequests.remove(spotifyRequest)
                    RequestOrder.YES
                } else RequestOrder.YES_WITH_ETAG
            }
        } ?: RequestOrder.YES
    }

    internal fun add(spotifyRequest: SpotifyRequest, cacheResponse: CacheResponse) {
        if (cacheResponse.eTag != null || cacheResponse.expireBy > System.currentTimeMillis() + 1000) {
            cachedRequests[spotifyRequest] = cacheResponse
        }
    }

    internal fun remove(spotifyRequest: SpotifyRequest) = cachedRequests.remove(spotifyRequest)

    internal fun getData(spotifyRequest: SpotifyRequest) = cachedRequests[spotifyRequest]!!.data

    internal fun getEtag(spotifyRequest: SpotifyRequest) = cachedRequests[spotifyRequest]!!.eTag

    fun clear() = cachedRequests.clear()

    internal enum class RequestOrder { YES, NO, YES_WITH_ETAG }
}

internal data class SpotifyRequest(
    val url: String,
    val method: Connection.Method,
    val body: String?,
    val params: List<Pair<String, String>>?
)

internal data class CacheResponse(val expireBy: Long, val eTag: String?, val data: String)