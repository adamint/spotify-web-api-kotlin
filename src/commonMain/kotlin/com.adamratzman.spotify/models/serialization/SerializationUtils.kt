/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models.serialization

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.NeedsApi
import com.adamratzman.spotify.models.NullablePagingObject
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PagingObjectBase
import com.adamratzman.spotify.models.instantiatePagingObjects
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Suppress("EXPERIMENTAL_API_USAGE")
internal val nonstrictJson =
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = true
        }

internal inline fun <reified T : Any> String.toObjectNullable(serializer: KSerializer<T>, api: GenericSpotifyApi?, json: Json): T? = try {
    toObject(serializer, api, json)
} catch (e: Exception) {
    null
}

internal inline fun <reified T : Any> String.toObject(serializer: KSerializer<T>, api: GenericSpotifyApi?, json: Json): T {
    return this.parseJson {
        val obj = json.decodeFromString(serializer, this)
        api?.let {
            if (obj is NeedsApi) obj.api = api
            if (obj is PagingObjectBase<*>) obj.endpoint = api.tracks
            obj.instantiatePagingObjects(api)
        }
        obj
    }
}

internal inline fun <reified T> String.toList(serializer: KSerializer<List<T>>, api: GenericSpotifyApi?, json: Json): List<T> {
    return this.parseJson {
        json.decodeFromString(serializer, this).apply {
            if (api != null) {
                forEach { obj ->
                    if (obj is NeedsApi) obj.api = api
                    if (obj is PagingObjectBase<*>) obj.endpoint = api.tracks
                }
            }
        }
    }
}

internal fun <T : Any> String.toPagingObject(
    tClazz: KClass<T>,
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): NullablePagingObject<T> {
    if (innerObjectName != null || (arbitraryInnerNameAllowed && !skipInnerNameFirstIfPossible)) {
        val map = this.parseJson {
            val t = (String.serializer() to NullablePagingObject.serializer(tSerializer))
            json.decodeFromString(MapSerializer(t.first, t.second), this)
        }
        return (map[innerObjectName] ?: if (arbitraryInnerNameAllowed) map.keys.firstOrNull()?.let { map[it] }
                ?: error("") else error(""))
                .apply {
                    this.endpoint = endpoint
                    this.itemClazz = tClazz
                    this.items.map { obj ->
                        if (obj is NeedsApi) obj.api = endpoint.api
                        if (obj is PagingObjectBase<*>) obj.endpoint = endpoint
                    }
                }
    }

    return try {
        val pagingObject = this.parseJson { json.decodeFromString(NullablePagingObject.serializer(tSerializer), this) }

        pagingObject.apply {
            this.endpoint = endpoint
            this.itemClazz = tClazz
            this.items.map { obj ->
                if (obj is NeedsApi) obj.api = endpoint.api
                if (obj is PagingObjectBase<*>) obj.endpoint = endpoint
            }
        }
    } catch (jde: SpotifyException.ParseException) {
        if (arbitraryInnerNameAllowed && jde.message?.contains("unable to parse", true) == true) {
            toPagingObject(
                    tClazz,
                    tSerializer,
                    innerObjectName,
                    endpoint,
                    json,
                    true,
                    false
            )
        } else throw jde
    }
}

internal fun <T : Any> initPagingObject(tClazz: KClass<T>, pagingObject: PagingObjectBase<T>, endpoint: SpotifyEndpoint) {
    pagingObject.apply {
        this.endpoint = endpoint
        this.itemClazz = tClazz
        this.items.map { obj ->
            if (obj is NeedsApi) obj.api = endpoint.api
            if (obj is PagingObjectBase<*>) obj.endpoint = endpoint
        }
    }
}

internal inline fun <reified T : Any> String.toPagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): PagingObject<T> = toNullablePagingObject(tSerializer, innerObjectName, endpoint, json, arbitraryInnerNameAllowed, skipInnerNameFirstIfPossible).toPagingObject()

internal inline fun <reified T : Any> String.toNullablePagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): NullablePagingObject<T> = toPagingObject(T::class, tSerializer, innerObjectName, endpoint, json, arbitraryInnerNameAllowed, skipInnerNameFirstIfPossible)

internal fun <T : Any> String.toCursorBasedPagingObject(
    tClazz: KClass<T>,
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): CursorBasedPagingObject<T> {
    if (innerObjectName != null || (arbitraryInnerNameAllowed && !skipInnerNameFirstIfPossible)) {
        val map = this.parseJson {
            val t = (String.serializer() to CursorBasedPagingObject.serializer(tSerializer))
            json.decodeFromString(MapSerializer(t.first, t.second), this)
        }
        return (map[innerObjectName] ?: if (arbitraryInnerNameAllowed) map.keys.firstOrNull()?.let { map[it] }
                ?: error("") else error(""))
                .apply { initPagingObject(tClazz, this, endpoint) }
    }
    return try {
        val pagingObject = this.parseJson { json.decodeFromString(CursorBasedPagingObject.serializer(tSerializer), this) }

        initPagingObject(tClazz, pagingObject, endpoint)

        pagingObject
    } catch (jde: SpotifyException.ParseException) {
        if (!arbitraryInnerNameAllowed && jde.message?.contains("unable to parse", true) == true) {
            toCursorBasedPagingObject(
                    tClazz,
                    tSerializer,
                    innerObjectName,
                    endpoint,
                    json,
                    arbitraryInnerNameAllowed = true,
                    skipInnerNameFirstIfPossible = false
            )
        } else throw jde
    }
}

internal inline fun <reified T : Any> String.toCursorBasedPagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): CursorBasedPagingObject<T> =
        toCursorBasedPagingObject(T::class, tSerializer, innerObjectName, endpoint, json, arbitraryInnerNameAllowed, skipInnerNameFirstIfPossible)

internal inline fun <reified T> String.toInnerObject(serializer: KSerializer<T>, innerName: String, json: Json): T {
    val map = this.parseJson {
        val t = (String.serializer() to serializer)
        json.decodeFromString(MapSerializer(t.first, t.second), this)
    }
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map"))
}

internal inline fun <reified T> String.toInnerArray(serializer: KSerializer<List<T>>, innerName: String, json: Json): List<T> {
    val map = this.parseJson {
        val t = (String.serializer() to serializer)
        json.decodeFromString(MapSerializer(t.first, t.second), this)
    }
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map")).toList()
}

internal fun Map<String, JsonElement>.toJson() = JsonObject(this).toString()

internal fun <A, B> createMapSerializer(aSerializer: KSerializer<A>, bSerializer: KSerializer<B>): KSerializer<Map<A, B>> {
    val t = (aSerializer to bSerializer)
    return MapSerializer(t.first, t.second)
}

internal fun <T> String.parseJson(producer: String.() -> T): T =
        try {
            producer(this)
        } catch (e: Exception) {
            throw SpotifyException.ParseException(
                    "Unable to parse $this",
                    e
            )
        }
