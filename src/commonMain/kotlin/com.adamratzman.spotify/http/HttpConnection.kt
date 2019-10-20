/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.SpotifyRatelimitedException
import com.adamratzman.spotify.utils.getCurrentTimeMs
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import io.ktor.util.toMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.io.core.toByteArray

enum class HttpRequestMethod {
    GET,
    POST,
    PUT,
    DELETE;
}

data class HttpHeader(val key: String, val value: String)

data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)


class HttpConnection constructor(
    private val url: String,
    private val method: HttpRequestMethod,
    private val bodyMap: Map<*, *>?,
    private val bodyString: String?,
    private val contentType: String?,
    private val headers: List<HttpHeader> = listOf(),
    val api: SpotifyApi? = null
) {

    companion object {
        private val client = HttpClient()
    }

    fun execute(additionalHeaders: List<HttpHeader>? = null, retryIf502: Boolean = true): HttpResponse {
        val ktorContentType = contentType?.let { ContentType.parse(contentType) }

        val httpRequestBuilder = HttpRequestBuilder().apply {
            url(this@HttpConnection.url)

            when (this@HttpConnection.method) {
                HttpRequestMethod.GET -> method = HttpMethod.Get
                HttpRequestMethod.DELETE -> {
                    method = HttpMethod.Delete
                    bodyString?.let { body = ByteArrayContent(bodyString.toByteArray(), ktorContentType) }
                }
                HttpRequestMethod.PUT, HttpRequestMethod.POST -> {
                    val content = if (contentType == "application/x-www-form-urlencoded") {
                        bodyMap?.map { "${it.key}=${it.value}" }?.joinToString("&")?.let {
                            ByteArrayContent(it.toByteArray(), ktorContentType)
                        } ?: bodyString?.let { ByteArrayContent(bodyString.toByteArray(), ktorContentType ?: ContentType.parse("application/json")) }
                    } else bodyString?.let { ByteArrayContent(bodyString.toByteArray(), ktorContentType ?: ContentType.parse("application/json")) }

                    method = if (this@HttpConnection.method == HttpRequestMethod.PUT) HttpMethod.Put
                    else HttpMethod.Post

                    content?.let { body = content }
                }
            }

            if (body !is ByteArrayContent && this@HttpConnection.method in listOf(
                    HttpRequestMethod.POST,
                    HttpRequestMethod.PUT
                )
            ) {
                body = ByteArrayContent("".toByteArray(), ktorContentType)
            }

            if (ktorContentType != null && body !is ByteArrayContent && this@HttpConnection.method != HttpRequestMethod.POST) {
                body = TextContent("", ktorContentType)
            }


            val allHeaders = (additionalHeaders ?: listOf()) + this@HttpConnection.headers

            allHeaders.filter { !it.key.equals("Authorization", true) }.forEach { (key, value) ->
                header(key, value)
            }

            allHeaders.find { it.key.equals("Authorization", true) }?.let { authHeader ->
                header("Authorization", authHeader.value)
            }
        }

        return runBlocking {
            try {
                val call = client.execute(httpRequestBuilder)
                val responseCode = call.response.status.value

                if (responseCode == 502 && retryIf502) {
                    api?.logger?.logError(
                        false,
                        "Received 502 (Invalid response) for URL $url and $this\nRetrying..",
                        null
                    )
                    return@runBlocking execute(additionalHeaders, retryIf502 = false)
                }

                if (responseCode == 502 && !retryIf502) api?.logger?.logWarning("502 retry successful for $this")

                if (responseCode == 429) {
                    val ratelimit = call.response.headers["Retry-After"]!!.toLong() + 1L
                    if (api?.retryWhenRateLimited == true) {
                        api.logger.logError(
                            false,
                            "The request ($url) was ratelimited for $ratelimit seconds at ${getCurrentTimeMs()}",
                            null
                        )

                        delay(ratelimit * 1000)
                        val retryResponse = try {
                            execute(additionalHeaders, retryIf502 = retryIf502)
                        } catch (e: Throwable) {
                            throw e
                        }

                        return@runBlocking retryResponse
                    } else throw SpotifyRatelimitedException(ratelimit)
                }

                val body = call.response.readText()

                if (responseCode == 401 && body.contains("access token") &&
                    api != null && api.automaticRefresh
                ) {
                    api.refreshToken()
                    return@runBlocking execute(additionalHeaders)
                }

                return@runBlocking HttpResponse(
                    responseCode = responseCode,
                    body = body,
                    headers = call.response.headers.toMap().toList().map {
                        HttpHeader(
                            it.first,
                            it.second.getOrNull(0) ?: "null"
                        )
                    }
                ).also { call.close() }
            } catch (e: ResponseException) {
                throw BadRequestException(e)
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

enum class HttpConnectionStatus(val code: Int) {
    HTTP_NOT_MODIFIED(304);

    fun getStatusCode() = code
}

expect fun <T> runBlocking(coroutineCode: suspend CoroutineScope.() -> T): T
