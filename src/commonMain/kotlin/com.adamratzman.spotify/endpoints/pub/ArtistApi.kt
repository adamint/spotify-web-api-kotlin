/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.ArtistList
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.serialization.toInnerArray
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encodeUrl
import kotlinx.serialization.builtins.ListSerializer

/**
 * Endpoints for retrieving information about one or more artists from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/)**
 */
public class ArtistApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-artist/)**
     *
     * @param artist The id or uri for the artist.
     *
     * @return [Artist] if valid artist id is provided, otherwise null
     */
    public suspend fun getArtist(artist: String): Artist? = catch {
        get(endpointBuilder("/artists/${ArtistUri(artist).id.encodeUrl()}").toString()).toObject(
            Artist.serializer(),
            api,
            json
        )
    }

    /**
     * Get Spotify catalog information for several artists based on their Spotify IDs. **Artists not found are returned as null inside the ordered list**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-several-artists/)**
     *
     * @param artists The ids or uris representing the artists. Maximum **50**.
     *
     * @return List of [Artist] objects or null if the artist could not be found, in the order requested
     */
    public suspend fun getArtists(vararg artists: String): List<Artist?> {
        checkBulkRequesting(50, artists.size)

        return bulkStatelessRequest(50, artists.toList()) { chunk ->
            get(
                endpointBuilder("/artists").with(
                    "ids",
                    chunk.joinToString(",") { ArtistUri(it).id.encodeUrl() }).toString()
            ).toObject(ArtistList.serializer(), api, json).artists
        }.flatten()
    }

    /**
     * Get Spotify catalog information about an artist’s albums.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-artists-albums/)**
     *
     * @param artist The artist id or uri
     * @param market Supply this parameter to limit the response to one particular geographical market.
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param include List of keywords that will be used to filter the response. If not supplied, all album groups will be returned.
     *
     * @throws BadRequestException if [artist] is not found, or filter parameters are illegal
     * @return [PagingObject] of [SimpleAlbum] objects
     */
    public suspend fun getArtistAlbums(
        artist: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null,
        vararg include: AlbumInclusionStrategy
    ): PagingObject<SimpleAlbum> = get(
        endpointBuilder("/artists/${ArtistUri(artist).id.encodeUrl()}/albums").with("limit", limit).with(
            "offset",
            offset
        ).with("market", market?.name)
            .with("include_groups", include.joinToString(",") { it.keyword }).toString()
    ).toNonNullablePagingObject(SimpleAlbum.serializer(), null, api, json)

    /**
     * Describes object types to include when finding albums
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-artists-albums/)**
     *
     * @param keyword The id of the strategy
     */
    public enum class AlbumInclusionStrategy(public val keyword: String) {
        ALBUM("album"),
        SINGLE("single"),
        APPEARS_ON("appears_on"),
        COMPILATION("compilation");
    }

    /**
     * Get Spotify catalog information about an artist’s top tracks **by country**.
     *
     * Contains only up to **10** tracks with *no* [CursorBasedPagingObject] to go between top track pages. Thus, only the top
     * 10 are exposed
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-artists-top-tracks/)**
     *
     * @param artist The id or uri for the artist.
     * @param market The country ([Market]) to search. Unlike endpoints with optional Track Relinking, the Market is **not** optional.
     *
     * @throws BadRequestException if tracks are not available in the specified [Market] or the [artist] is not found
     * @return List of the top [Track]s of an artist in the given market
     */
    public suspend fun getArtistTopTracks(artist: String, market: Market = Market.US): List<Track> = get(
        endpointBuilder("/artists/${ArtistUri(artist).id.encodeUrl()}/top-tracks").with(
            "country",
            market.name
        ).toString()
    ).toInnerArray(ListSerializer(Track.serializer()), "tracks", json)

    /**
     * Get Spotify catalog information about artists similar to a given artist.
     * Similarity is based on analysis of the Spotify community’s listening history.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/artists/get-related-artists/)**
     *
     * @param artist The id or uri for the artist.
     *
     * @throws BadRequestException if the [artist] is not found
     * @return List of *never-null*, but possibly empty [Artist]s representing similar artists
     */
    public suspend fun getRelatedArtists(artist: String): List<Artist> =
        get(endpointBuilder("/artists/${ArtistUri(artist).id.encodeUrl()}/related-artists").toString())
            .toObject(ArtistList.serializer(), api, json).artists.filterNotNull()
}
