package com.adamratzman.spotify.endpoints.pub.album

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * Endpoints for retrieving information about one or more albums from the Spotify catalog.
 */
class AlbumAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single album.
     * @param albumId The Spotify ID for the album.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the [albumId] is not found
     */
    fun getAlbum(albumId: String, market: Market? = null): SpotifyRestAction<Album> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums/$albumId${if (market != null) "?market=${market.code}" else ""}").toObject<Album>(api)
        })
    }

    /**
     * Get Spotify catalog information for multiple albums identified by their Spotify IDs. **Albums not found are returned as null inside the ordered list**
     * @param albumIds List of the Spotify IDs for the albums.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     */
    fun getAlbums(vararg albumIds: String, market: Market? = null): SpotifyRestAction<List<Album?>> {
        if (albumIds.isEmpty()) throw BadRequestException(ErrorObject(404, "You cannot send a request with no album ids!"))
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums?ids=${albumIds.map { it.encode() }.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=${market.code}" else ""}")
                    .toObject<AlbumsResponse>(api).albums
        })
    }

    /**
     * Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.
     * @param albumId The Spotify ID for the album.
     * @param limit The maximum number of tracks to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first track to return. Default: 0 (the first object). Use with limit to get the next set of tracks.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the [albumId] is not found, or positioning of [limit] or [offset] is illegal.
     */
    fun getAlbumTracks(albumId: String, limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<LinkedResult<SimpleTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums/${albumId.encode()}/tracks?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}").toLinkedResult<SimpleTrack>(api)
        })
    }
}