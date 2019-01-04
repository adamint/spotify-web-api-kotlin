/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import java.net.HttpURLConnection
import java.net.URL

enum class HttpRequestMethod { GET, POST, PUT, DELETE }
data class HttpHeader(val key: String, val value: String)

class HttpConnection(
    private val url: String,
    private val method: HttpRequestMethod,
    private val body: String?,
    private val contentType: String?,
    private vararg val headers: HttpHeader
) {

    fun execute(vararg additionalHeaders: HttpHeader?): HttpResponse {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method.toString()

        contentType?.let { connection.setRequestProperty("Content-Type", contentType) }
        headers.union(additionalHeaders.filterNotNull()).forEach { (key, value) ->
            connection.setRequestProperty(key, value)
        }

        if (body != null || method != HttpRequestMethod.GET) {
            connection.doOutput = true
            connection.setFixedLengthStreamingMode(body?.toByteArray()?.size ?: 0)
            connection.outputStream.bufferedWriter().use {
                body?.also(it::write)
                it.close()
            }
        }

        connection.connect()

        val responseCode = connection.responseCode
        val body = (connection.errorStream ?: connection.inputStream).bufferedReader().use {
            val text = it.readText()
            it.close()
            text
        }

        return HttpResponse(
            responseCode = responseCode,
            body = body,
            headers = connection.headerFields.keys.filterNotNull().map { HttpHeader(it, connection.getHeaderField(it)) }
        ).also { connection.disconnect() }
    }
}

data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)
