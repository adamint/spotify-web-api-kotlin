/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models.serialization

import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.NeedsApi
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.instantiatePagingObjects
import com.beust.klaxon.JsonBase
import com.beust.klaxon.Klaxon

internal inline fun <reified T> String.toObjectNullable(o: SpotifyAPI?): T? = try {
    toObject<T>(o)
} catch (e: Exception) {
    null
}

internal inline fun <reified T> String.toObject(o: SpotifyAPI?): T {
    val klaxon = o?.klaxon ?: Klaxon()
    val obj = klaxon.parse<T>(this) ?: throw SpotifyException(
            "Unable to parse $this",
            IllegalArgumentException("$this not found")
    )
    o?.let {
        if (obj is NeedsApi) obj.api = o
        if (obj is AbstractPagingObject<*>) obj.endpoint = o.tracks
        obj.instantiatePagingObjects(o)
    }
    return obj
}

internal inline fun <reified T> String.toArray(o: SpotifyAPI?): List<T> {
    val klaxon = o?.klaxon ?: Klaxon()
    return klaxon.parseArray<T>(this)?.apply {
        if (o != null) {
            forEach { obj ->
                if (obj is NeedsApi) obj.api = o
                if (obj is AbstractPagingObject<*>) obj.endpoint = o.tracks
            }
        }
    } ?: throw SpotifyException(
            "Unable to parse $this",
            IllegalArgumentException("$this not found")
    )
}

internal inline fun <reified T> String.toPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): PagingObject<T> {
    val jsonObject = endpoint.api.klaxon.parseJsonObject(this.reader())
            .let { if (innerObjectName != null) it.obj(innerObjectName)!! else it }

    return PagingObject(
            jsonObject.string("href")!!,
            (jsonObject["items"] as JsonBase).toJsonString().toArray<T>(endpoint.api),
            jsonObject.int("limit")!!,
            jsonObject.string("next"),
            jsonObject.int("offset")!!,
            jsonObject.string("previous"),
            jsonObject.int("total")!!
    ).apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
    }
}

internal inline fun <reified T> String.toCursorBasedPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): CursorBasedPagingObject<T> {
    val jsonObject = endpoint.api.klaxon.parseJsonObject(this.reader())
            .let { if (innerObjectName != null) it.obj(innerObjectName)!! else it }

    return CursorBasedPagingObject(
            jsonObject.string("href")!!,
            (jsonObject["items"] as JsonBase).toJsonString().toArray<T>(endpoint.api),
            jsonObject.int("limit")!!,
            jsonObject.string("next"),
            endpoint.api.klaxon.parseFromJsonObject(jsonObject.obj("cursors")!!)!!,
            if (jsonObject.containsKey("total")) jsonObject.int("total")!! else -1
    ).apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
    }
}

internal inline fun <reified T> String.toInnerObject(innerName: String, api: SpotifyAPI): T {
    val jsonObject = api.klaxon.parseJsonObject(this.reader())

    return jsonObject.obj(innerName)?.let { api.klaxon.parseFromJsonObject<T>(it) }
            ?: throw SpotifyException("Unable to parse $this into $innerName (${T::class})", IllegalArgumentException())
}

internal inline fun <reified T> String.toInnerArray(innerName: String, api: SpotifyAPI): List<T> {
    val jsonObject = api.klaxon.parseJsonObject(this.reader())

    return jsonObject.array(innerName)
            ?: throw SpotifyException("Unable to parse $this into $innerName (${T::class})", IllegalArgumentException())
}
