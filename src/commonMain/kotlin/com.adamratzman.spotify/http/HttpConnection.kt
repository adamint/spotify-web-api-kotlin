/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.AuthenticationException
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyException.ParseException
import com.adamratzman.spotify.models.AuthenticationError
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
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

public enum class HttpRequestMethod(internal val externalMethod: HttpMethod) {
    GET(HttpMethod.Get),
    POST(HttpMethod.Post),
    PUT(HttpMethod.Put),
    DELETE(HttpMethod.Delete);
}

@Serializable
public data class HttpHeader(val key: String, val value: String)

@Serializable
public data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)

/**
 * Provides a fast, easy, and slim way to execute and retrieve HTTP GET, POST, PUT, and DELETE requests
 */
public class HttpConnection constructor(
    public val url: String,
    public val method: HttpRequestMethod,
    public val bodyMap: Map<*, *>?,
    public val bodyString: String?,
    contentType: String?,
    public val headers: List<HttpHeader> = listOf(),
    public val api: GenericSpotifyApi? = null
) {
    public val contentType: ContentType = contentType?.let { ContentType.parse(it) } ?: ContentType.Application.Json

    public fun String?.toByteArrayContent(): ByteArrayContent? {
        return if (this == null) null else ByteArrayContent(this.toByteArray(), contentType)
    }

    public fun buildRequest(additionalHeaders: List<HttpHeader>?): HttpRequestBuilder = HttpRequestBuilder().apply {
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

        // let additionalHeaders overwrite headers
        val allHeaders = this@HttpConnection.headers + (additionalHeaders ?: listOf())

        allHeaders.forEach { (key, value) ->
            header(key, value)
        }
    }

    public suspend fun execute(
        additionalHeaders: List<HttpHeader>? = null,
        retryIfInternalServerError: Boolean = true
    ): HttpResponse {
        val httpRequest = buildRequest(additionalHeaders)

        try {
            return HttpClient().request<io.ktor.client.statement.HttpResponse>(httpRequest).let { response ->
                val respCode = response.status.value

                if (respCode in 500..599 && retryIfInternalServerError) {
                    api?.logger?.logError(
                        false,
                        "Received $respCode (Internal Server Error) for URL $url and $this (${response.readText()})\nRetrying..",
                        null
                    )
                    return@let execute(additionalHeaders, retryIfInternalServerError = false)
                } else if (respCode in 500..599 && !retryIfInternalServerError) {
                    api?.logger?.logWarning("Received $respCode (Internal Server Error) for URL $url and $this\nNot retrying")
                }

                if (respCode == 429) {
                    val ratelimit = response.headers["Retry-After"]!!.toLong() + 1L
                    if (api?.spotifyApiOptions?.retryWhenRateLimited == true) {
                        api.logger.logError(
                            false,
                            "The request ($url) was ratelimited for $ratelimit seconds at ${getCurrentTimeMs()}",
                            null
                        )

                        delay(ratelimit * 1000)
                        return@let execute(additionalHeaders, retryIfInternalServerError = retryIfInternalServerError)
                    } else throw SpotifyRatelimitedException(ratelimit)
                }

                val body = response.readText()
                if (respCode == 401 && body.contains("access token") &&
                    api != null && api.spotifyApiOptions.automaticRefresh
                ) {
                    api.refreshToken()
                    val newAdditionalHeaders = additionalHeaders?.toMutableList() ?: mutableListOf()
                    newAdditionalHeaders.add(0, HttpHeader("Authorization", "Bearer ${api.token.accessToken}"))
                    return execute(newAdditionalHeaders, retryIfInternalServerError)
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
            try {
                if (e.response.status.value == 429) {
                    val ratelimit = e.response.headers["Retry-After"]!!.toLong() + 1L
                    if (api?.spotifyApiOptions?.retryWhenRateLimited == true) {
                        api.logger.logError(
                            false,
                            "The request ($url) was ratelimited for $ratelimit seconds at ${getCurrentTimeMs()}",
                            null
                        )

                        delay(ratelimit * 1000)
                        return execute(additionalHeaders, retryIfInternalServerError = retryIfInternalServerError)
                    } else throw SpotifyRatelimitedException(ratelimit)
                }

                if (e.response.status.value == 401 && errorBody.contains("access token") &&
                    api != null && api.spotifyApiOptions.automaticRefresh
                ) {
                    api.refreshToken()
                    val newAdditionalHeaders = additionalHeaders?.toMutableList() ?: mutableListOf()
                    newAdditionalHeaders.add(0, HttpHeader("Authorization", "Bearer ${api.token.accessToken}"))
                    return execute(newAdditionalHeaders, retryIfInternalServerError)
                }

                val error = errorBody.toObject(
                    ErrorResponse.serializer(),
                    api,
                    api?.spotifyApiOptions?.json ?: nonstrictJson
                ).error
                throw BadRequestException(error.copy(reason = (error.reason ?: "") + " URL: $url"))
            } catch (ignored: ParseException) {
                try {
                    val error = errorBody.toObject(
                        AuthenticationError.serializer(),
                        api,
                        api?.spotifyApiOptions?.json ?: nonstrictJson
                    )
                    throw AuthenticationException(error)
                } catch (ignored: ParseException) {
                    throw BadRequestException(e)
                }
            }
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

public enum class HttpConnectionStatus(public val code: Int) {
    HTTP_NOT_MODIFIED(304);
}
