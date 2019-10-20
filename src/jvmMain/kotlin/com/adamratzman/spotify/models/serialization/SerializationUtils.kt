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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.lang.reflect.Type

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

internal actual inline fun <reified T : Any> String.toObjectNullable(o: SpotifyAPI?): T? = try {
    toObject(o)
} catch (e: Exception) {
    null
}

internal actual inline fun <reified T : Any> String.toObject(o: SpotifyAPI?): T {
    try {
        val obj = moshi.adapter(T::class.java).fromJson(this)!!
        o?.let {
            if (obj is NeedsApi) obj.api = o
            if (obj is AbstractPagingObject<*>) obj.endpoint = o.tracks
            obj.instantiatePagingObjects(o)
        }
        return obj
    } catch (e: Exception) {
        throw SpotifyException(
                "Unable to parse $this",
                e
        )
    }
}

internal actual inline fun <reified T> String.toList(o: SpotifyAPI?): List<T> {
    return moshi.adapter(Array<T>::class.java).fromJson(this)?.toList()?.apply {
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
    if (innerObjectName != null) {
        val map = fromJsonMap<String, PagingObject<T>>(
                String::class.java,
                Types.newParameterizedType(PagingObject::class.java, T::class.java),
                this)!!
        return map[innerObjectName]!!
                .apply {
                    this.endpoint = endpoint
                    this.itemClazz = T::class.java
                    this.items.map { obj ->
                        if (obj is NeedsApi) obj.api = endpoint.api
                        if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
                    }
                }
    }

    val pagingType = Types.newParameterizedType(PagingObject::class.java, T::class.java)
    val adapter = moshi.adapter<PagingObject<T>>(pagingType)
    val pagingObject = adapter.fromJson(this)!!

    return pagingObject.apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
        this.items.map { obj ->
            if (obj is NeedsApi) obj.api = endpoint.api
            if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
        }
    }
}

internal inline fun <reified T> String.toCursorBasedPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): CursorBasedPagingObject<T> {
    if (innerObjectName != null) {
        val map = fromJsonMap<String, CursorBasedPagingObject<T>>(
                String::class.java,
                Types.newParameterizedType(CursorBasedPagingObject::class.java, T::class.java),
                this)!!
        return map[innerObjectName]!!
                .apply {
                    this.endpoint = endpoint
                    this.itemClazz = T::class.java
                    this.items.map { obj ->
                        if (obj is NeedsApi) obj.api = endpoint.api
                        if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
                    }
                }
    }

    val pagingType = Types.newParameterizedType(CursorBasedPagingObject::class.java, T::class.java)
    val adapter = endpoint.api.moshi.adapter<CursorBasedPagingObject<T>>(pagingType)
    val pagingObject = adapter.fromJson(this)!!

    return pagingObject.apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
        this.items.map { obj ->
            if (obj is NeedsApi) obj.api = endpoint.api
            if (obj is AbstractPagingObject<*>) obj.endpoint = endpoint
        }
    }
}

internal fun String.asJSONObject() = JSONObject(this)
internal fun JSONObject.asJsonString(key: String): String? = if (has(key)) getJSONObject(key)?.toString() else null

internal inline fun <reified T> String.toInnerObject(innerName: String): T {
    val map = fromJsonMap<String, T>(
            String::class.java,
            T::class.java,
            this)!!

    return map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map")
}

internal inline fun <reified T> String.toInnerArray(innerName: String): List<T> {
    val map = fromJsonMap<String, Array<T>>(
            String::class.java,
            Array<T>::class.java,
            this)!!

    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map")).toList()
}

private fun <K, V> fromJsonMap(keyType: Type, valueType: Type, json: String): Map<K, V>? {
    val mapJsonAdapter = mapAdapter<K, V>(keyType, valueType)
    return mapJsonAdapter.fromJson(json)
}

internal fun <K, V> mapAdapter(keyType: Type, valueType: Type): JsonAdapter<Map<K, V>> {
    return moshi.adapter(Types.newParameterizedType(Map::class.java, keyType, valueType))
}

internal fun mapAdapterJson(): JsonAdapter<Map<String, Any>> = mapAdapter(String::class.java, Any::class.java)

internal fun Map<String, Any>.toJson() = mapAdapterJson().toJson(this)