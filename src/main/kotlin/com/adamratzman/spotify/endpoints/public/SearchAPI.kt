/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.neovisionaries.i18n.CountryCode
import java.util.function.Supplier

/**
 * Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string.
 */
class SearchAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Describes which object to search for
     *
     * @param id The internal spotify id
     */
    enum class SearchType(internal val id: String) {
        ALBUM("album"), TRACK("track"), ARTIST("artist"), PLAYLIST("playlist")
    }

    /**
     * Get Spotify Catalog information about playlists that match the keyword string.
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of full [Playlist] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchPlaylist(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging(Supplier {
            get(build(SearchType.PLAYLIST, query, limit, offset, market)).toPagingObject<SimplePlaylist>(
                    "playlists", this
            )
        })
    }

    /**
     * Get Spotify Catalog information about artists that match the keyword string.
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of full [Artist] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchArtist(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<Artist, PagingObject<Artist>> {
        return toActionPaging(Supplier {
            get(build(SearchType.ARTIST, query, limit, offset, market)).toPagingObject<Artist>(
                    "artists", this
            )
        })
    }

    /**
     * Get Spotify Catalog information about albums that match the keyword string.
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of non-full [SimpleAlbum] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchAlbum(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<SimpleAlbum, PagingObject<SimpleAlbum>> {
        return toActionPaging(Supplier {
            get(build(SearchType.ALBUM, query, limit, offset, market)).toPagingObject<SimpleAlbum>(
                    "albums", this
            )
        })
    }

    /**
     * Get Spotify Catalog information about tracks that match the keyword string.
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of non-full [SimpleTrack] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchTrack(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<Track, PagingObject<Track>> {
        return toActionPaging(Supplier {
            get(build(SearchType.TRACK, query, limit, offset, market)).toPagingObject<Track>(
                    "tracks", this
            )
        })
    }

    private fun build(type: SearchType, query: String, limit: Int?, offset: Int?, market: CountryCode?): String {
        return EndpointBuilder("/search").with("q", query.encode()).with("type", type.id).with("market", market?.name)
                .with("limit", limit).with("offset", offset).toString()
    }
}