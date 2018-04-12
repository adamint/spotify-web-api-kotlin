package com.adamratzman.spotify.kotlin.endpoints.pub.artists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*
import java.util.stream.Collectors

class ArtistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getArtist(artistId: String): Artist? {
        return get("https://api.spotify.com/v1/artists/$artistId").toObject()
    }

    fun getArtists(vararg artistIds: String): ArtistPNList {
        return get("https://api.spotify.com/v1/artists?ids=${artistIds.toList().stream().collect(Collectors.joining(","))}").toObject()
    }

    fun getArtistAlbums(artistId: String): LinkedResult<SimpleAlbum> {
        return get("https://api.spotify.com/v1/artists/$artistId/albums").toObject()
    }

    fun getArtistTopTracks(artistId: String, country: Market): TrackList {
        return get("https://api.spotify.com/v1/artists/$artistId/top-tracks?country=${country.code}").toObject()
    }

    fun getRelatedArtists(artistId: String): ArtistList {
        return get("https://api.spotify.com/v1/artists/$artistId/related-artists").toObject()
    }
}