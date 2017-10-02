package com.adamratzman.endpoints.public.artists

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.*
import java.util.stream.Collectors

class ArtistsAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getArtist(artistId: String): Artist? {
        return get("https://api.spotify.com/v1/artists/$artistId").toObject()
    }

    fun getArtists(vararg artistIds: String): ArtistPNList {
        return get("https://api.spotify.com/v1/artists?ids=${artistIds.toList().stream().collect(Collectors.joining(","))}").toObject()
    }

    fun getArtistAlbums(artistId: String): SimpleAlbumLinkedResult {
        return get("https://api.spotify.com/v1/artists/$artistId/albums").toObject()
    }

    fun getArtistTopTracks(artistId: String, country: Market): TrackList {
        return get("https://api.spotify.com/v1/artists/$artistId/top-tracks?country=${country.code}").toObject()
    }

    fun getRelatedArtists(artistId: String): ArtistList {
        return get("https://api.spotify.com/v1/artists/$artistId/related-artists").toObject()
    }
}