package com.adamratzman.spotify.utils

import java.net.HttpURLConnection
import java.net.URL

enum class HttpRequestMethod { GET, POST, PUT, DELETE, }
data class HttpHeader(val key: String, val value: String)

class HttpConnection(
    private val url: String,
    private val method: HttpRequestMethod,
    private val body: String?,
    private val contentType: String?,
    vararg headersVararg: HttpHeader,
    val headers: MutableList<HttpHeader> = headersVararg.toMutableList()
) {

    fun execute(): HttpResponse {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method.toString()

        contentType?.let { connection.setRequestProperty("Content-Type", contentType) }
        headers.forEach { (key, value) -> connection.setRequestProperty(key, value) }

        if (body != null || method != HttpRequestMethod.GET){
            connection.doOutput = true
            connection.setFixedLengthStreamingMode(body?.length ?: 0)
        }

        body?.let { _ ->
            connection.outputStream.bufferedWriter().also { it.write(body) }.let { it.close() }
        }

        connection.connect()

        val responseCode = connection.responseCode

        val outputBody = (connection.errorStream ?: connection.inputStream).bufferedReader().let {
            val text = it.readText()
            it.close()
            text
        }

        val headers = connection.headerFields.keys.filterNotNull().map { HttpHeader(it, connection.getHeaderField(it)) }
        connection.disconnect()

        return HttpResponse(responseCode, outputBody, headers)
    }
}

data class HttpResponse(val responseCode: Int, val body: String, val headers: List<HttpHeader>)