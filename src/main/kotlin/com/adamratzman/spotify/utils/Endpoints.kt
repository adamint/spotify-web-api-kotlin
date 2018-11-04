package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.base
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.function.Supplier

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        return execute(url)
    }

    fun post(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.POST)
    }

    fun put(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, Connection.Method.PUT, contentType = contentType)
    }

    fun delete(url: String, body: String? = null, data: List<Pair<String, String>>? = null, contentType: String? = null): String {
        return execute(url, body, Connection.Method.DELETE, data = data, contentType = contentType)
    }

    private fun execute(url: String, body: String? = null, method: Connection.Method = Connection.Method.GET,
                        retry202: Boolean = true, contentType: String? = null, data: List<Pair<String, String>>? = null): String {
        if (api !is SpotifyClientAPI && System.currentTimeMillis() >= api.expireTime) {
            api.refreshClient()
            api.expireTime = System.currentTimeMillis() + api.token.expires_in * 1000
        }
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
        connection = connection.header("Authorization", "Bearer ${api.token.access_token}")
        val document = connection.method(method).ignoreHttpErrors(true).execute()
        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) throw BadRequestException(api.gson.fromJson(document.body(), ErrorResponse::class.java).error)
        else if (document.statusCode() == 202 && retry202) return execute(url, body, method, false)
        return document.body()
    }

    fun <T> toAction(supplier: Supplier<T>) = SpotifyRestAction(api, supplier)
}

internal class EndpointBuilder(val path: String) {
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