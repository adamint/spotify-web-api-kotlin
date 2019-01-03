/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.Artist
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.PagingObject
import com.adamratzman.spotify.utils.Playlist
import com.adamratzman.spotify.utils.SimpleAlbum
import com.adamratzman.spotify.utils.SimplePlaylist
import com.adamratzman.spotify.utils.SimpleTrack
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.Track
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toPagingObject
import java.util.function.Supplier

/**
 * Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string.
 */
class SearchAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    enum class SearchType(val id: String) {
        ALBUM("album"), TRACK("track"), ARTIST("artist"), PLAYLIST("playlist")
    }

    /**
     * Get Spotify Catalog information about playlists that match the keyword string.
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of full [Playlist] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchPlaylist(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
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
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of full [Artist] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchArtist(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<Artist>> {
        return toAction(Supplier {
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
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of non-full [SimpleAlbum] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchAlbum(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SimpleAlbum>> {
        return toAction(Supplier {
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
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of non-full [SimpleTrack] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchTrack(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<Track>> {
        return toAction(Supplier {
            get(build(SearchType.TRACK, query, limit, offset, market)).toPagingObject<Track>(
                "tracks", this
            )
        })
    }

    private fun build(type: SearchType, query: String, limit: Int?, offset: Int?, market: Market?): String {
        return EndpointBuilder("/search").with("q", query.encode()).with("type", type.id).with("market", market?.code)
            .with("limit", limit).with("offset", offset).toString()
    }
}