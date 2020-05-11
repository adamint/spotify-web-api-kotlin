/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.ErrorResponse
import com.adamratzman.spotify.models.SpotifyRatelimitedException
import com.adamratzman.spotify.models.serialization.nonstrictJson
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.getCurrentTimeMs
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.readText
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

enum class HttpRequestMethod(internal val externalMethod: HttpMethod) {
    GET(HttpMethod.Get),
    POST(HttpMethod.Post),
    PUT(HttpMethod.Put),
    DELETE(HttpMethod.Delete);
}

data class HttpHeader(val key: String, val value: String)

data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)

/**
 * Provides a fast, easy, and slim way to execute and retrieve HTTP GET, POST, PUT, and DELETE requests
 */
class HttpConnection constructor(
        val url: String,
        val method: HttpRequestMethod,
        val bodyMap: Map<*, *>?,
        val bodyString: String?,
        contentType: String?,
        val headers: List<HttpHeader> = listOf(),
        val api: GenericSpotifyApi? = null
) {
    val contentType: ContentType = contentType?.let { ContentType.parse(it) } ?: ContentType.Application.Json

    companion object {
        private val client = HttpClient()
    }

    fun String?.toByteArrayContent(): ByteArrayContent? {
        return if (this == null) null else ByteArrayContent(this.toByteArray(), contentType)
    }

    fun buildRequest(additionalHeaders: List<HttpHeader>?): HttpRequestBuilder = HttpRequestBuilder().apply {
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

    suspend fun execute(
            additionalHeaders: List<HttpHeader>? = null,
            retryIf502: Boolean = true
    ): HttpResponse {
        val httpRequest = buildRequest(additionalHeaders)

        try {
            return client.request<io.ktor.client.statement.HttpResponse>(httpRequest).let { response ->
                val respCode = response.status.value

                if (respCode == 502 && retryIf502) {
                    api?.logger?.logError(
                            false,
                            "Received 502 (Invalid response) for URL $url and $this (${response.readText()})\nRetrying..",
                            null
                    )
                    return@let execute(additionalHeaders, retryIf502 = false)
                } else if (respCode == 502 && !retryIf502) {
                    api?.logger?.logWarning("Recieved 502 (Invalid response) for URL $url and $this\nNot retrying")
                }

                if (respCode == 429) {
                    val ratelimit = response.headers["Retry-After"]!!.toLong() + 1L
                    if (api?.retryWhenRateLimited == true) {
                        api.logger.logError(
                                false,
                                "The request ($url) was ratelimited for $ratelimit seconds at ${getCurrentTimeMs()}",
                                null
                        )

                        delay(ratelimit * 1000)
                        return@let execute(additionalHeaders, retryIf502 = retryIf502)
                    } else throw SpotifyRatelimitedException(ratelimit)
                }

                val body = response.readText()

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
                        headers = response.headers.entries().map { (key, value) ->
                            HttpHeader(
                                    key,
                                    value.getOrNull(0) ?: "null"
                            )
                        }
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: ResponseException) {
            val errorBody = e.response.readText()
            val error = errorBody.toObject(ErrorResponse.serializer(), api, api?.json ?: nonstrictJson).error
            throw SpotifyException.BadRequestException(error)
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

enum class HttpConnectionStatus(val code: Int) {
    HTTP_NOT_MODIFIED(304);
}
