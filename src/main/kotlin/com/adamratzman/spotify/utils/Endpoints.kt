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
import java.net.HttpURLConnection
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

        val spotifyRequest = SpotifyRequest(url, method, body, data)
        val cacheState = if (api.useCache) cache[spotifyRequest] else null

        if (cacheState?.isStillValid() == true) return cacheState.data
        else if (cacheState?.let { it.eTag == null } == true) {
            cache -= spotifyRequest
        }

        val document = createConnection(url, body, method, contentType, data).apply {
            if (cacheState?.eTag != null) header("If-None-Match", cacheState.eTag)
        }.execute()

        return handleResponse(document, cacheState, spotifyRequest, retry202) ?: execute(
            url,
            body,
            method,
            false,
            contentType,
            data
        )
    }

    private fun handleResponse(
        document: Connection.Response,
        cacheState: CacheState?,
        spotifyRequest: SpotifyRequest,
        retry202: Boolean
    ): String? {
        val statusCode = document.statusCode()

        if (statusCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
            if (cacheState?.eTag == null) throw BadRequestException("304 status only allowed on Etag-able endpoints")
            return cacheState.data
        }

        val responseBody = document.body()

        document.header("Cache-Control")?.also {
            if (api.useCache) {
                cache[spotifyRequest] = (cacheState ?: CacheState(responseBody, document.header("ETag"))).update(it)
            }
        }

        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) {
            val message = try {
                api.gson.fromJson(responseBody, ErrorResponse::class.java).error
            } catch (e: JsonParseException) {
                ErrorObject(400, "malformed request (likely spaces)")
            }
            throw BadRequestException(message)
        } else if (document.statusCode() == 202 && retry202) return null
        return responseBody
    }

    private fun createConnection(
        url: String,
        body: String? = null,
        method: Connection.Method = Connection.Method.GET,
        contentType: String? = null,
        data: List<Pair<String, String>>? = null
    ) = Jsoup.connect(url).also { connection ->

        connection
            .ignoreContentType(true)
            .header("Authorization", "Bearer ${api.token.access_token}")
            .method(method)
            .ignoreHttpErrors(true)

        data?.forEach { connection.data(it.first, it.second) }

        if (contentType != null) {
            connection.header("Content-Type", contentType)
            body?.also { connection.requestBody(it) }
        } else if (body != null) {
            if (method == Connection.Method.DELETE) {
                val key = JSONObject(body).keySet().toList()[0]
                connection.data(key, JSONObject(body).getJSONArray(key).toString())
            } else connection.requestBody(body)
        }
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
    private val cachedRequests = hashMapOf<SpotifyRequest, CacheState>()

    internal operator fun get(spotifyRequest: SpotifyRequest): CacheState? {
        return cachedRequests[spotifyRequest]
    }

    internal operator fun set(request: SpotifyRequest, state: CacheState) {
        cachedRequests[request] = state
    }

    internal operator fun minusAssign(spotifyRequest: SpotifyRequest) {
        cachedRequests.remove(spotifyRequest)
    }

    fun clear() = cachedRequests.clear()
}

internal data class SpotifyRequest(
    val url: String,
    val method: Connection.Method,
    val body: String?,
    val params: List<Pair<String, String>>?
)

internal data class CacheState(val data: String, val eTag: String?, val expireBy: Long = 0) {
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
