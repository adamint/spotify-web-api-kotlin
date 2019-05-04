/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.AlbumURI
import com.adamratzman.spotify.models.AlbumsResponse
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.Market
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.catch
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about one or more albums from the Spotify catalog.
 */
class AlbumAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single album.
     * @param album the spotify id or uri for the album.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return full [Album] object if the provided id is found, otherwise null
     */
    fun getAlbum(album: String, market: Market? = null): SpotifyRestAction<Album?> {
        return toAction(Supplier {
            catch {
                get(EndpointBuilder("/albums/${AlbumURI(album).id}").with("market", market?.code).toString())
                        .toObject<Album>(api)
            }
        })
    }

    /**
     * Get Spotify catalog information for multiple albums identified by their Spotify IDs. **Albums not found are returned as null inside the ordered list**
     * @param albums the spotify ids or uris for the albums.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     */
    fun getAlbums(vararg albums: String, market: Market? = null): SpotifyRestAction<List<Album?>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/albums").with("ids", albums.joinToString(",") { AlbumURI(it).id.encode() })
                            .with("market", market?.code).toString()
            ).toObject<AlbumsResponse>(api).albums
        })
    }

    /**
     * Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.
     * @param album the spotify id or uri for the album.
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the [album] is not found, or positioning of [limit] or [offset] is illegal.
     */
    fun getAlbumTracks(
        album: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimpleTrack, PagingObject<SimpleTrack>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/albums/${AlbumURI(album).id.encode()}/tracks").with("limit", limit).with(
                            "offset",
                            offset
                    ).with("market", market?.code)
                            .toString()
            ).toPagingObject<SimpleTrack>(endpoint = this)
        })
    }
}
