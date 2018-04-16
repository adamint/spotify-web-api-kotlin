package com.adamratzman.spotify.endpoints.pub.search

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.net.URLEncoder
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
    fun searchPlaylist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<Playlist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.PLAYLIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<Playlist>("playlists", api)
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
    fun searchArtist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.ARTIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<Artist>("artists", api)
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
    fun searchAlbum(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<SimpleAlbum>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.ALBUM.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<SimpleAlbum>("albums", api)
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
    fun searchTrack(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<SimpleTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.TRACK.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<SimpleTrack>("tracks", api)
        })
    }
}