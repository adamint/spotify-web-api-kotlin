/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

abstract class AbstractTest<T : GenericSpotifyApi> {
    lateinit var api: T

    suspend fun build(): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            (buildSpotifyApi() as? T)?.let { api = it }
            println(api as? T)
            ::api.isInitialized
        } catch (cce: ClassCastException) {
            false
        }
    }
}

typealias GenericSpotifyApiTest = AbstractTest<GenericSpotifyApi>
typealias SpotifyClientApiTest = AbstractTest<SpotifyClientApi>
