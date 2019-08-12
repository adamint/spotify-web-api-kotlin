/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.models.SpotifyRatelimitedException
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import java.util.concurrent.TimeUnit

enum class HttpRequestMethod {
    GET,
    POST,
    PUT,
    DELETE;
}

data class HttpHeader(val key: String, val value: String)

internal class HttpConnection(
    private val url: String,
    private val method: HttpRequestMethod,
    private val bodyMap: Map<*, *>?,
    private val bodyString: String?,
    private val contentType: String?,
    private val headers: List<HttpHeader> = listOf(),
    val api: SpotifyAPI? = null
) {

    companion object {
        private val requestFactory = NetHttpTransport().createRequestFactory()
    }

    fun execute(additionalHeaders: List<HttpHeader>? = null, retryIf502: Boolean = true): HttpResponse {
        val genericUrl = GenericUrl(url)
        val request = when (method) {
            HttpRequestMethod.GET -> requestFactory.buildGetRequest(genericUrl)
            HttpRequestMethod.DELETE -> requestFactory.buildDeleteRequest(genericUrl)
            HttpRequestMethod.PUT, HttpRequestMethod.POST -> {
                val content = if (contentType == "application/x-www-form-urlencoded") {
                    bodyMap?.map { "${it.key}=${it.value}" }?.joinToString("&")?.let { ByteArrayContent.fromString(contentType, it) }
                            ?: ByteArrayContent.fromString(contentType, bodyString)
                } else bodyString?.let { ByteArrayContent.fromString(contentType, bodyString) }

                if (method == HttpRequestMethod.PUT) requestFactory.buildPutRequest(genericUrl, content)
                else requestFactory.buildPostRequest(genericUrl, content)
            }
        }

        if (request.content != null) request.headers.contentLength = request.content.length
        else if (request.content == null && method in listOf(HttpRequestMethod.POST, HttpRequestMethod.PUT)) {
            request.content = ByteArrayContent.fromString(null, "")
        }

        if (method == HttpRequestMethod.DELETE && bodyString != null) {
            // request.content = ByteArrayContent.fromString(contentType, bodyString)
        }

        if (contentType != null && method != HttpRequestMethod.POST) request.headers.contentType = contentType

        val allHeaders = (additionalHeaders ?: listOf()) + headers

        allHeaders.filter { !it.key.equals("Authorization", true) }.forEach { (key, value) ->
            request.headers[key] = value
        }

        allHeaders.find { it.key.equals("Authorization", true) }?.let { authHeader ->
            request.headers.authorization = authHeader.value
        }

        request.throwExceptionOnExecuteError = false

        val response = request.execute()

        val responseCode = response.statusCode

        if (responseCode == 502 && retryIf502) {
            api?.logger?.logError(false, "Received 502 (Invalid response) for URL $url and $this\nRetrying..", null)
            return execute(additionalHeaders, retryIf502 = false)
        }

        if (responseCode == 502 && !retryIf502) api?.logger?.logWarning("502 retry successful for $this")

        if (responseCode == 429) {
            val ratelimit = response.headers["Retry-After"]!!.toString().toLong() + 1L
            if (api?.retryWhenRateLimited == true) {
                api.logger.logError(false, "The request ($url) was ratelimited for $ratelimit seconds at ${System.currentTimeMillis()}", null)

                var retryResponse: HttpResponse? = null
                api.executor.schedule({
                    retryResponse = try {
                        execute(additionalHeaders, retryIf502 = retryIf502)
                    } catch (e: Throwable) {
                        throw e
                    }
                }, ratelimit, TimeUnit.SECONDS).get()

                return retryResponse!!
            } else throw SpotifyRatelimitedException(ratelimit)
        }

        val contentReader = response.content.bufferedReader()
        val body = contentReader.readText()

        contentReader.close()
        response.content.close()

        if (responseCode == 401 && body.contains("access token") &&
                api != null && api.automaticRefresh) {
            api.refreshToken()
            return execute(additionalHeaders)
        }

        return HttpResponse(
                responseCode = responseCode,
                body = body,
                headers = response.headers.map { HttpHeader(it.key, it.value?.toString() ?: "null") }
        ).also { response.disconnect() }
    }

    override fun toString(): String {
        return """HttpConnection  (
            |url=$url,
            |method=$method,
            |body=${bodyString ?: bodyMap},
            |contentType=$contentType,
            |headers=${headers.toList()}
            |  )""".trimMargin()
    }
}

internal data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)