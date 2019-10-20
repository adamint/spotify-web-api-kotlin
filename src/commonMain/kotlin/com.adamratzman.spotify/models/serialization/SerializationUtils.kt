/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models.serialization

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.http.SpotifyEndpoint

internal expect inline fun <reified T : Any> String.toObjectNullable(o: SpotifyAPI?): T?

internal expect inline fun <reified T : Any> String.toObject(o: SpotifyAPI?): T

internal expect inline fun <reified T> String.toList(o: SpotifyAPI?): List<T>

internal expect inline fun <reified T> String.toPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): PagingObject<T>

internal expect inline fun <reified T> String.toCursorBasedPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): CursorBasedPagingObject<T>

internal expect inline fun <reified T> String.toInnerObject(innerName: String): T

internal expect inline fun <reified T> String.toInnerArray(innerName: String): List<T>

internal expect fun Map<String, Any>.toJson(): String