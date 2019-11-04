/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.SpotifyRatelimitedException
import com.adamratzman.spotify.utils.getCurrentTimeMs
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.response.readText
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import kotlinx.coroutines.delay
import kotlinx.io.core.toByteArray
import kotlinx.io.core.use

enum class HttpRequestMethod(internal val externalMethod: HttpMethod) {
    GET(HttpMethod.Get),
    POST(HttpMethod.Post),
    PUT(HttpMethod.Put),
    DELETE(HttpMethod.Delete);
}

internal data class HttpHeader(val key: String, val value: String)

internal data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)

internal class HttpConnection constructor(
    private val url: String,
    private val method: HttpRequestMethod,
    private val bodyMap: Map<*, *>?,
    private val bodyString: String?,
    contentType: String?,
    private val headers: List<HttpHeader> = listOf(),
    val api: SpotifyApi<*, *>? = null
) {
    private val contentType: ContentType = contentType?.let { ContentType.parse(it) } ?: ContentType.Application.Json

    companion object {
        private val client = HttpClient()
    }

    private fun String?.toByteArrayContent(): ByteArrayContent? {
        return if (this == null) null else ByteArrayContent(this.toByteArray(), contentType)
    }

    private fun buildRequest(additionalHeaders: List<HttpHeader>?): HttpRequestBuilder = HttpRequestBuilder().apply {
        url(this@HttpConnection.url)
        method = this@HttpConnection.method.externalMethod

        body = when (this@HttpConnection.method) {
            HttpRequestMethod.DELETE -> {
                bodyString.toByteArrayContent() ?: body
            }
            HttpRequestMethod.PUT, HttpRequestMethod.POST -> {
                val contentString = if (contentType == ContentType.Application.FormUrlEncoded) {
                    bodyMap?.map { "${it.key}=${it.value}" }?.joinToString("&") ?: bodyString
                } else bodyString

                contentString.toByteArrayContent() ?: ByteArrayContent("".toByteArray(), contentType)
            }
            else -> body
        }

        if (body === EmptyContent && this@HttpConnection.method != HttpRequestMethod.POST) {
            body = TextContent("", contentType)
        }

        // let additionalHeaders overwrite headers
        val allHeaders = this@HttpConnection.headers + (additionalHeaders ?: listOf())

        allHeaders.forEach { (key, value) ->
            header(key, value)
        }
    }

    internal suspend fun execute(
        additionalHeaders: List<HttpHeader>? = null,
        retryIf502: Boolean = true
    ): HttpResponse {
        val httpRequest = buildRequest(additionalHeaders)

        try {
            return client.execute(httpRequest).use {
                val resp = it.response
                val respCode = resp.status.value

                if (respCode == 502 && retryIf502) {
                    api?.logger?.logError(
                        false,
                        "Received 502 (Invalid response) for URL $url and $this\nRetrying..",
                        null
                    )
                    return@use execute(additionalHeaders, retryIf502 = false)
                } else if (respCode == 502 && !retryIf502) {
                    api?.logger?.logWarning("Recieved 502 (Invalid response) for URL $url and $this\nNot retrying")
                }

                if (respCode == 429) {
                    val ratelimit = resp.headers["Retry-After"]!!.toLong() + 1L
                    if (api?.retryWhenRateLimited == true) {
                        api.logger.logError(
                            false,
                            "The request ($url) was ratelimited for $ratelimit seconds at ${getCurrentTimeMs()}",
                            null
                        )

                        delay(ratelimit * 1000)
                        return@use execute(additionalHeaders, retryIf502 = retryIf502)
                    } else throw SpotifyRatelimitedException(ratelimit)
                }

                val body = resp.readText()

                if (respCode == 401 && body.contains("access token") &&
                    api != null && api.automaticRefresh
                ) {
                    api.suspendRefreshToken()
                    val newAdditionalHeaders = additionalHeaders?.toMutableList() ?: mutableListOf()
                    newAdditionalHeaders.add(0, HttpHeader("Authorization", "Bearer ${api.token.accessToken}"))
                    return execute(newAdditionalHeaders, retryIf502)
                }

                return HttpResponse(
                    responseCode = respCode,
                    body = body,
                    headers = resp.headers.entries().map { (key, value) ->
                        HttpHeader(
                            key,
                            value.getOrNull(0) ?: "null"
                        )
                    }
                )
            }
        } catch (e: ResponseException) {
            throw SpotifyException.BadRequestException(e)
        }
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

internal enum class HttpConnectionStatus(val code: Int) {
    HTTP_NOT_MODIFIED(304);
}
