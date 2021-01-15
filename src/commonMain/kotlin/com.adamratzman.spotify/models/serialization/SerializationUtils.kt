/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

internal val nonstrictJson =
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }

// Parse function that catches on parse exception
internal fun <T> String.parseJson(producer: String.() -> T): T =
    try {
        producer(this)
    } catch (e: Exception) {
        throw SpotifyException.ParseException(
            "Unable to parse $this (${e.message})",
            e
        )
    }

// Generic object deserialization
internal fun <T : Any> String.toObject(serializer: KSerializer<T>, api: GenericSpotifyApi?, json: Json): T {
    return parseJson {
        val obj = json.decodeFromString(serializer, this)
        obj.initSpotifyObject(api)
        obj
    }
}

internal fun <T> String.toList(serializer: KSerializer<List<T>>, api: GenericSpotifyApi?, json: Json): List<T> {
    return parseJson {
        json.decodeFromString(serializer, this).onEach { obj -> obj?.initSpotifyObject(api) }
    }
}

internal fun Any.initSpotifyObject(api: GenericSpotifyApi?) {
    if (api == null) return

    if (this is NeedsApi) this.api = api
    if (this is PagingObjectBase<*, *>) this.endpoint = api.tracks
    this.instantiatePagingObjects(api)
}

// Inner object deserialization
internal inline fun <reified T> String.toInnerObject(serializer: KSerializer<T>, innerName: String, json: Json): T {
    val map = this.parseJson {
        val t = (String.serializer() to serializer)
        json.decodeFromString(MapSerializer(t.first, t.second), this)
    }
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map"))
}

internal inline fun <reified T> String.toInnerArray(
    serializer: KSerializer<List<T>>,
    innerName: String,
    json: Json
): List<T> {
    val map = this.parseJson {
        val t = (String.serializer() to serializer)
        json.decodeFromString(MapSerializer(t.first, t.second), this)
    }
    return (map[innerName] ?: error("Inner object with name $innerName doesn't exist in $map")).toList()
}


// Paging Object deserialization
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
        val jsonObjectRoot = (json.parseToJsonElement(this) as JsonObject)
        val jsonElement =
            innerObjectName?.let { jsonObjectRoot[it] } ?: jsonObjectRoot.keys.firstOrNull()?.let { jsonObjectRoot[it] }
            ?: throw SpotifyException.ParseException("Json element was null for class $tClazz (json $this)")

        val objectString = jsonElement.toString()
        val pagingObject = objectString.parseJson {
            json.decodeFromString(CursorBasedPagingObject.serializer(tSerializer), this)
        }
        pagingObject.initPagingObject(tClazz, endpoint)

        return pagingObject
    }
    try {
        val pagingObject = parseJson { json.decodeFromString(CursorBasedPagingObject.serializer(tSerializer), this) }
        pagingObject.initPagingObject(tClazz, endpoint)

        return pagingObject
    } catch (jde: SpotifyException.ParseException) {
        if (!arbitraryInnerNameAllowed && jde.message?.contains("unable to parse", true) == true) {
            return toCursorBasedPagingObject(
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

internal fun <T : Any> String.toNonNullablePagingObject(
    tClazz: KClass<T>,
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): NullablePagingObject<T> {
    if (innerObjectName != null || (arbitraryInnerNameAllowed && !skipInnerNameFirstIfPossible)) {
        val jsonObjectRoot = (json.parseToJsonElement(this) as JsonObject)
        val jsonElement =
            innerObjectName?.let { jsonObjectRoot[it] } ?: jsonObjectRoot.keys.firstOrNull()?.let { jsonObjectRoot[it] }
            ?: throw SpotifyException.ParseException("Json element was null for class $tClazz (json $this)")

        val objectString = jsonElement.toString()
        val pagingObject = objectString.parseJson {
            json.decodeFromString(NullablePagingObject.serializer(tSerializer), this)
        }
        pagingObject.initPagingObject(tClazz, endpoint)

        return pagingObject
    }

    try {
        val pagingObject = this.parseJson { json.decodeFromString(NullablePagingObject.serializer(tSerializer), this) }
        pagingObject.initPagingObject(tClazz, endpoint)
        return pagingObject
    } catch (jde: SpotifyException.ParseException) {
        if (arbitraryInnerNameAllowed && jde.message?.contains("unable to parse", true) == true) {
            return toNonNullablePagingObject(
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

internal inline fun <reified T : Any> String.toNonNullablePagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): PagingObject<T> = toNullablePagingObject(
    tSerializer,
    innerObjectName,
    endpoint,
    json,
    arbitraryInnerNameAllowed,
    skipInnerNameFirstIfPossible
).toPagingObject()

internal inline fun <reified T : Any> String.toNullablePagingObject(
    tSerializer: KSerializer<T>,
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    json: Json,
    arbitraryInnerNameAllowed: Boolean = false,
    skipInnerNameFirstIfPossible: Boolean = true
): NullablePagingObject<T> = toNonNullablePagingObject(
    T::class,
    tSerializer,
    innerObjectName,
    endpoint,
    json,
    arbitraryInnerNameAllowed,
    skipInnerNameFirstIfPossible
)

// Kotlin object -> JSON string transformations
internal fun Map<String, JsonElement>.mapToJsonString() = JsonObject(this).toString()
