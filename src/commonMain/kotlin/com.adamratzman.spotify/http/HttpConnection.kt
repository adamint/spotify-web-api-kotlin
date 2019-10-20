/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyApi

enum class HttpRequestMethod {
    GET,
    POST,
    PUT,
    DELETE;
}

data class HttpHeader(val key: String, val value: String)

expect class HttpConnection(
    url: String,
    method: HttpRequestMethod,
    bodyMap: Map<*, *>?,
    bodyString: String?,
    contentType: String?,
    headers: List<HttpHeader> = listOf(),
    api: SpotifyApi? = null
) {
    fun execute(additionalHeaders: List<HttpHeader>? = null, retryIf502: Boolean = true): HttpResponse
}

data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)

expect enum class HttpConnectionStatus {
    HTTP_NOT_MODIFIED;

    fun getStatusCode(): Int
}
