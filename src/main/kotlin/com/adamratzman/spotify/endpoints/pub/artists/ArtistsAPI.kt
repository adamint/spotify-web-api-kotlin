package com.adamratzman.spotify.endpoints.pub.artists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier
import java.util.stream.Collectors

class ArtistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getArtist(artistId: String): SpotifyRestAction<Artist> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/artists/${artistId.encode()}").toObject<Artist>(api)
        })

    }

    fun getArtists(vararg artistIds: String): SpotifyRestAction<List<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/artists?ids=${artistIds.map { it.encode() }.toList().stream().collect(Collectors.joining(","))}")
                    .toObject<ArtistPNList>(api).artists
        })
    }

    fun getArtistAlbums(artistId: String): SpotifyRestAction<LinkedResult<SimpleAlbum>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/artists/${artistId.encode()}/albums").toLinkedResult<SimpleAlbum>(api)
        })
    }

    fun getArtistTopTracks(artistId: String, market: Market): SpotifyRestAction<List<Track>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/artists/${artistId.encode()}/top-tracks?country=${market.code}").toObject<TrackList>(api).tracks
        })

    }

    fun getRelatedArtists(artistId: String): SpotifyRestAction<List<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/artists/${artistId.encode()}/related-artists").toObject<ArtistList>(api).artists
        })

    }
}