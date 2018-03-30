package com.adamratzman.endpoints.priv.personalization

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toPagingObject
import com.adamratzman.obj.Artist
import com.adamratzman.obj.SpotifyEndpoint
import com.adamratzman.obj.PagingObject
import com.adamratzman.obj.Track

class PersonalizationAPI(api: SpotifyAPI): SpotifyEndpoint(api) {
    fun getTopArtists(): PagingObject<Artist> {
        return get("https://api.spotify.com/v1/me/top/artists").toPagingObject()
    }
    fun getTopTracks(): PagingObject<Track> {
        return get("https://api.spotify.com/v1/me/top/tracks").toPagingObject()
    }
}