package com.adamratzman.spotify.kotlin.endpoints.priv.personalization

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*

class PersonalizationAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getTopArtists(): PagingObject<Artist> {
        return get("https://api.spotify.com/v1/me/top/artists").toPagingObject()
    }

    fun getTopTracks(): PagingObject<Track> {
        return get("https://api.spotify.com/v1/me/top/tracks").toPagingObject()
    }
}