/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.serialization.toInnerArray
import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

public class MarketsApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get the list of markets where Spotify is available.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#category-markets)**
     *
     * @return List of [Market]
     */
    public suspend fun getAvailableMarkets(): List<Market> {
        return get(endpointBuilder("/markets").toString()).toInnerArray(
            ListSerializer(String.serializer()),
            "markets",
            json
        ).map { Market.valueOf(it.uppercase()) }
    }
}
