/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.utils.encodeUrl
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.AlbumUri
import com.adamratzman.spotify.models.AlbumsResponse
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch

/**
 * Endpoints for retrieving information about one or more albums from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/albums/)**
 */
public class AlbumApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single album.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/albums/get-album/)**
     *
     * @param album The id or uri for the album.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @return Full [Album] object if the provided id is found, otherwise null
     */
    public suspend fun getAlbum(album: String, market: Market? = null): Album? = catch {
        get(
            endpointBuilder("/albums/${AlbumUri(album).id}").with(
                "market",
                market?.name
            ).toString()
        ).toObject(Album.serializer(), api, json)
    }

    /**
     * Get Spotify catalog information for multiple albums identified by their Spotify IDs.
     * **Albums not found are returned as null inside the ordered list**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/albums/get-several-albums/)**
     *
     * @param albums The ids or uris for the albums. Maximum **20**.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @return List of [Album] objects or null if the album could not be found, in the order requested
     */
    public suspend fun getAlbums(vararg albums: String, market: Market? = null): List<Album?> {
        checkBulkRequesting(20, albums.size)
        return bulkRequest(20, albums.toList()) { chunk ->
            get(
                endpointBuilder("/albums").with("ids", chunk.joinToString(",") { AlbumUri(it).id.encodeUrl() })
                    .with("market", market?.name).toString()
            ).toObject(AlbumsResponse.serializer(), api, json).albums
        }.flatten()
    }

    /**
     * Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/albums/get-albums-tracks/)**
     *
     * @param album The id or uri for the album.
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @throws [BadRequestException] if the [album] is not found, or positioning of [limit] or [offset] is illegal.
     * @return [PagingObject] of [SimpleTrack] objects
     */
    public suspend fun getAlbumTracks(
        album: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SimpleTrack> = get(
        endpointBuilder("/albums/${AlbumUri(album).id.encodeUrl()}/tracks").with("limit", limit).with(
            "offset",
            offset
        ).with("market", market?.name)
            .toString()
    ).toNonNullablePagingObject(SimpleTrack.serializer(), api = api, json = json)
}
