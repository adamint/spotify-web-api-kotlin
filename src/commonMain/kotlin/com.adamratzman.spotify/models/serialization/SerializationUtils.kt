/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models.serialization

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.NeedsApi
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.instantiatePagingObjects
import com.adamratzman.spotify.models.spotifyUriSerializersModule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.map
import kotlinx.serialization.serializer

val json = Json(JsonConfiguration.Stable, spotifyUriSerializersModule)

internal inline fun <reified T : Any> String.toObjectNullable(serializer: KSerializer<T>, api: SpotifyAPI?): T? = try {
    toObject(serializer, api)
} catch (e: Exception) {
    null
}

@Serializable
data class Tt(val d: String)

internal inline fun <reified T : Any> String.toObject(serializer: KSerializer<T>, api: SpotifyAPI?): T {
    try {
        val obj = json.parse(serializer, this)
        api?.let {
            if (obj is NeedsApi) obj.api = api
            if (obj is AbstractPagingObject<*>) obj.endpoint = api.tracks
            obj.instantiatePagingObjects(api)
        }
        return obj
    } catch (e: Exception) {
        throw SpotifyException(
            "Unable to parse $this",
            e
        )
    }
}

internal inline fun <reified T> String.toList(serializer: KSerializer<List<T>>, api: SpotifyAPI?): List<T> {
    return try {
        json.parse(serializer, this).apply {
            if (api != null) {
                forEach { obj ->
                    if (obj is NeedsApi) obj.api = api
                    if (obj is AbstractPagingObject<*>) obj.endpoint = api.tracks
                }
            }
        }
    } catch (e: Exception) {
        throw SpotifyException(
            "Unable to parse $this",
            e
        )
    }
}

internal inline fun <reified T : Any> String.toPagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): PagingObject<T> {
    if (innerObjectName != null) {
        val map = json.parse((String.serializer() to PagingObject.serializer(tSerializer)).map, this)

        return map[innerObjectName]!!
            .apply {
                this.endpoint = endpoint
                this.itemClazz = T::class
                this.items.map { obj ->
                    if (obj is NeedsApi) obj.api = endpoint.api
                    if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
                }
            }
    }

    val pagingObject = json.parse(PagingObject.serializer(tSerializer), this)

    return pagingObject.apply {
        this.endpoint = endpoint
        this.itemClazz = T::class
        this.items.map { obj ->
            if (obj is NeedsApi) obj.api = endpoint.api
            if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
        }
    }
}

internal inline fun <reified T : Any> String.toCursorBasedPagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): CursorBasedPagingObject<T> {
    if (innerObjectName != null) {
        val map = json.parse((String.serializer() to CursorBasedPagingObject.serializer(tSerializer)).map, this)

        return map[innerObjectName]!!
            .apply {
                this.endpoint = endpoint
                this.itemClazz = T::class
                this.items.map { obj ->
                    if (obj is NeedsApi) obj.api = endpoint.api
                    if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
                }
            }
    }

    val pagingObject = json.parse(CursorBasedPagingObject.serializer(tSerializer), this)

    return pagingObject.apply {
        this.endpoint = endpoint
        this.itemClazz = T::class
        this.items.map { obj ->
            if (obj is NeedsApi) obj.api = endpoint.api
            if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
        }
    }
}

internal inline fun <reified T> String.toInnerObject(serializer: KSerializer<T>, innerName: String): T {
    val map = json.parse((String.serializer() to serializer).map, this)
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map"))
}

internal inline fun <reified T> String.toInnerArray(serializer: KSerializer<List<T>>, innerName: String): List<T> {
    val map = json.parse((String.serializer() to serializer).map, this)
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map")).toList()
}

internal fun Map<String, JsonElement>.toJson() = JsonObject(this).toString()