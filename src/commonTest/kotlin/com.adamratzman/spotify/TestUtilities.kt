/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

abstract class AbstractTest<T : GenericSpotifyApi> {
    var api: T? = null

    suspend fun build(): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            (buildSpotifyApi() as? T)?.let { api = it }
            api != null
        } catch (cce: ClassCastException) {
            false
        }
    }
}

typealias GenericSpotifyApiTest = AbstractTest<GenericSpotifyApi>
typealias SpotifyClientApiTest = AbstractTest<SpotifyClientApi>
