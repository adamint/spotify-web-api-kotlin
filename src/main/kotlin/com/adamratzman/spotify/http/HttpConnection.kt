/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.models.SpotifyRatelimitedException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

enum class HttpRequestMethod { GET, POST, PUT, DELETE }
data class HttpHeader(val key: String, val value: String)

internal class HttpConnection(
        private val url: String,
        private val method: HttpRequestMethod,
        private val body: String?,
        private val contentType: String?,
        private val headers: List<HttpHeader> = listOf(),
        val api: SpotifyAPI? = null
) {

    fun execute(additionalHeaders: List<HttpHeader>? = null, retryIf502: Boolean = true): HttpResponse {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method.toString()

        contentType?.let { connection.setRequestProperty("Content-Type", contentType) }

        ((additionalHeaders ?: listOf()) + headers).forEach { (key, value) ->
            connection.setRequestProperty(key, value)
        }

        if (body != null && method != HttpRequestMethod.GET) {
            connection.doOutput = true
            // connection.setFixedLengthStreamingMode(body.toByteArray().size)
            val os = connection.outputStream
            val osw = OutputStreamWriter(os)
            osw.write(body)
            osw.apply {
                flush()
                close()
            }
            os.close()
        }

        connection.connect()

        val responseCode = connection.responseCode

        if (responseCode == 502 && retryIf502) {
            api?.logger?.logError(false, "Received 502 (Invalid response) for URL $url and $this\nRetrying..", null)
            return execute(additionalHeaders, retryIf502 = false)
        }

        if (responseCode == 502 && !retryIf502) api?.logger?.logWarning("502 retry successful for $this")

        if (responseCode == 429) {
            val ratelimit = connection.getHeaderField("Retry-After").toLong() + 1L
            if (api?.retryWhenRateLimited == true) {
                api.logger.logError(false, "The request ($url) was ratelimited for $ratelimit seconds at ${System.currentTimeMillis()}", null)

                var response: HttpResponse? = null
                api.executor.schedule({
                    response = try {
                        execute(additionalHeaders, retryIf502 = retryIf502)
                    } catch (e: Throwable) {
                        throw e
                    }
                }, ratelimit, TimeUnit.SECONDS).get()

                return response!!
            } else throw SpotifyRatelimitedException(ratelimit)
        }

        val body = (connection.errorStream ?: connection.inputStream).bufferedReader().use {
            val text = it.readText()
            it.close()
            text
        }

        if (responseCode == 401 && body.contains("access token") &&
                api != null && api.automaticRefresh) {
            api.refreshToken()
            return execute(additionalHeaders)
        }

        return HttpResponse(
                responseCode = responseCode,
                body = body,
                headers = connection.headerFields.keys.filterNotNull().map { HttpHeader(it, connection.getHeaderField(it)) }
        ).also { connection.disconnect() }
    }

    override fun toString(): String {
        return """HttpConnection  (
            |url=$url,
            |method=$method,
            |body=$body,
            |contentType=$contentType,
            |headers=${headers.toList()}
            |  )""".trimMargin()
    }
}

internal data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)
