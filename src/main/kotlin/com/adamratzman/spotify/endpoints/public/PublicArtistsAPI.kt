package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about one or more artists from the Spotify catalog.
 */
class PublicArtistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
     * @param artistId The Spotify ID for the artist.
     *
     * @throws BadRequestException if the [artistId] is not found
     */
    fun getArtist(artistId: String): SpotifyRestAction<Artist> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${artistId.encode()}").toString()).toObject<Artist>(api)
        })

    }

    /**
     * Get Spotify catalog information for several artists based on their Spotify IDs. **Artists not found are returned as null inside the ordered list**
     * @param artistIds List of the Spotify IDs representing the artists.
     */
    fun getArtists(vararg artistIds: String): SpotifyRestAction<List<Artist?>> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists").with("ids", artistIds.joinToString(",") { it.encode() }).toString())
                    .toObject<ArtistList>(api).artists
        })
    }

    /**
     * Get Spotify catalog information about an artist’s albums.
     * @param artistId Spotify ID for the artist
     * @param market Supply this parameter to limit the response to one particular geographical market.
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     * @param include List of keywords that will be used to filter the response. If not supplied, all album groups will be returned.
     *
     * @throws BadRequestException if [artistId] is not found, or filter parameters are illegal
     */
    fun getArtistAlbums(artistId: String, limit: Int? = null, offset: Int? = null, market: Market? = null, include: List<AlbumInclusionStrategy> = listOf()): SpotifyRestAction<LinkedResult<SimpleAlbum>> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${artistId.encode()}/albums").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .with("include_groups", include.joinToString(",") { it.keyword }).toString()).toLinkedResult<SimpleAlbum>(api)
        })
    }

    enum class AlbumInclusionStrategy(val keyword: String) {
        ALBUM("album"), SINGLE("single"), APPEARS_ON("appears_on"), COMPILATION("compilation")
    }

    /**
     * Get Spotify catalog information about an artist’s top tracks **by country**.
     * @param artistId    The Spotify ID for the artist.
     * @param market The country ([Market]) to search. Unlike endpoints with optional Track Relinking, the Market is **not** optional.
     *
     * @throws BadRequestException if tracks are not available in the specified [Market] or the [artistId] is not found
     */
    fun getArtistTopTracks(artistId: String, market: Market = Market.US): SpotifyRestAction<List<Track>> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${artistId.encode()}/top-tracks").with("country", market.code).toString())
                    .toObject<TrackList>(api).tracks.map { it!! }
        })

    }

    /**
     * Get Spotify catalog information about artists similar to a given artist.
     * Similarity is based on analysis of the Spotify community’s listening history.
     *
     * @param artistId    The Spotify ID for the artist.
     *
     * @return List of *never-null*, but possibly empty Artist objects representing similar artists
     * @throws BadRequestException if the [artistId] is not found
     */
    fun getRelatedArtists(artistId: String): SpotifyRestAction<List<Artist>> {
        return toAction(Supplier {
            get(EndpointBuilder("/artists/${artistId.encode()}/related-artists").toString()).toObject<ArtistList>(api).artists.map { it!! }
        })

    }
}