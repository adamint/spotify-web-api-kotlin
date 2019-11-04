/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.AlbumUri
import com.adamratzman.spotify.models.AlbumsResponse
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch

typealias AlbumAPI = AlbumApi

/**
 * Endpoints for retrieving information about one or more albums from the Spotify catalog.
 */
class AlbumApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single album.
     *
     * @param album The spotify id or uri for the album.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return Full [Album] object if the provided id is found, otherwise null
     */
    fun getAlbum(album: String, market: Market? = null): SpotifyRestAction<Album?> {
        return toAction {
            catch {
                get(
                    EndpointBuilder("/albums/${AlbumUri(album).id}").with(
                        "market",
                        market?.name
                    ).toString()
                ).toObject(Album.serializer(), api)
            }
        }
    }

    /**
     * Get Spotify catalog information for multiple albums identified by their Spotify IDs.
     * **Albums not found are returned as null inside the ordered list**
     *
     * @param albums The spotify ids or uris for the albums.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return List of [Album] objects or null if the album could not be found, in the order requested
     */
    fun getAlbums(vararg albums: String, market: Market? = null): SpotifyRestAction<List<Album?>> {
        return toAction {
            get(
                EndpointBuilder("/albums").with("ids", albums.joinToString(",") { AlbumUri(it).id.encodeUrl() })
                    .with("market", market?.name).toString()
            ).toObject(AlbumsResponse.serializer(), api).albums
        }
    }

    /**
     * Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.
     *
     * @param album The spotify id or uri for the album.
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the [album] is not found, or positioning of [limit] or [offset] is illegal.
     * @return [PagingObject] of [SimpleTrack] objects
     */
    fun getAlbumTracks(
        album: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimpleTrack, PagingObject<SimpleTrack>> {
        return toActionPaging {
            get(
                EndpointBuilder("/albums/${AlbumUri(album).id.encodeUrl()}/tracks").with("limit", limit).with(
                    "offset",
                    offset
                ).with("market", market?.name)
                    .toString()
            ).toPagingObject(SimpleTrack.serializer(), endpoint = this)
        }
    }
}
