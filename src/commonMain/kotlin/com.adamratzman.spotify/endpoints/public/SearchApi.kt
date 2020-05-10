/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimpleEpisode
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleShow
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.SpotifySearchResult
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.serialization.createMapSerializer
import com.adamratzman.spotify.models.serialization.toNullablePagingObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("SearchApi"))
typealias SearchAPI = SearchApi

/**
 * Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
 */
open class SearchApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * Describes which object to search for
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param id The internal spotify id
     */
    enum class SearchType(internal val id: String) {
        ALBUM("album"),
        TRACK("track"),
        ARTIST("artist"),
        PLAYLIST("playlist"),
        SHOW("show"),
        EPISODE("episode");
    }

    /**
     * Get Spotify Catalog information about artists, albums, tracks and/or playlists that match a keyword string.
     *
     * **Information from Spotify**:
     * Writing a Query - Guidelines
     *
     * Keyword matching:
     *
     * Matching of search keywords is not case-sensitive. Operators, however, should be specified in uppercase. Unless surrounded by double quotation marks, keywords are matched in any order. For example: q=roadhouse&20blues matches both “Blues Roadhouse” and “Roadhouse of the Blues”. q="roadhouse&20blues" matches “My Roadhouse Blues” but not “Roadhouse of the Blues”.
     *
     * Searching for playlists returns results where the query keyword(s) match any part of the playlist’s name or description. Only popular public playlists are returned.
     *
     * Operator: The operator NOT can be used to exclude results.
     *
     * For example: q=roadhouse%20NOT%20blues returns items that match “roadhouse” but excludes those that also contain the keyword “blues”.
     *
     * Similarly, the OR operator can be used to broaden the search: q=roadhouse%20OR%20blues returns all the results that include either of the terms. Only one OR operator can be used in a query.
     *
     * Note: Operators must be specified in uppercase. Otherwise, they are handled as normal keywords to be matched.
     *
     * Wildcards: The asterisk (*) character can, with some limitations, be used as a wildcard (maximum: 2 per query). It matches a variable number of non-white-space characters. It cannot be used:
    - in a quoted phrase
    - in a field filter
    - when there is a dash (“-“) in the query
    - or as the first character of the keyword string Field filters: By default, results are returned when a match is found in any field of the target object type. Searches can be made more specific by specifying an album, artist or track field filter.
     *
     * For example: The query q=album:gold%20artist:abba&type=album returns only albums with the text “gold” in the album name and the text “abba” in the artist name.
     *
     * To limit the results to a particular year, use the field filter year with album, artist, and track searches.
     *
     * For example: q=bob%20year:2014
     *
     * Or with a date range. For example: q=bob%20year:1980-2020 To retrieve only albums released in the last two weeks, use the field filter tag:new in album searches.
     *
     * To retrieve only albums with the lowest 10% popularity, use the field filter tag:hipster in album searches. Note: This field filter only works with album searches.
     *
     * Depending on object types being searched for, other field filters, include genre (applicable to tracks and artists), upc, and isrc. For example: q=lil%20genre:%22southern%20hip%20hop%22&type=artist. Use double quotation marks around the genre keyword string if it contains spaces.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param searchTypes A list of item types to search across. Search results include hits from all the specified item types.
     * @param limit Maximum number of results to return.
    Default: 20
    Minimum: 1
    Maximum: 50
    Note: The limit is applied within each type, not on the total response.
    For example, if the limit value is 3 and the type is artist,album, the response contains 3 artists and 3 albums.
     * @param offset The index of the first result to return.
    Default: 0 (the first result).
    Maximum offset (including limit): 10,000.
    Use with limit to get the next page of search results.
     * @param market If a country code is specified, only artists, albums, and tracks with content that is playable in that market is returned. Note:
    - Playlist results are not affected by the market parameter.
    - If market is set to from_token, and a valid access token is specified in the request header, only content playable in the country associated with the user account, is returned.
    - Users can view the country that is associated with their account in the account settings. A user must grant access to the [SpotifyScope.USER_READ_PRIVATE] scope prior to when the access token is issued.
     * @param includeExternal If true, the response will include any relevant audio content that is hosted externally. By default external content is filtered out from responses.
     */
    fun search(
        query: String,
        vararg searchTypes: SearchType,
        limit: Int? = api.defaultLimit,
        offset: Int? = null,
        market: Market? = null,
        includeExternal: Boolean? = null
    ): SpotifyRestAction<SpotifySearchResult> {
        require(searchTypes.isNotEmpty()) { "At least one search type must be provided" }
        return toAction {
            val jsonString = get(build(query, market, limit, offset, *searchTypes, includeExternal = includeExternal))
            val map = json.parse(createMapSerializer(String.serializer(), JsonObject.serializer()), jsonString)

            SpotifySearchResult(
                    map["albums"]?.toString()?.toPagingObject(SimpleAlbum.serializer(), endpoint = this, json = json),
                    map["artists"]?.toString()?.toPagingObject(Artist.serializer(), endpoint = this, json = json),
                    map["playlists"]?.toString()?.toPagingObject(SimplePlaylist.serializer(), endpoint = this, json = json),
                    map["tracks"]?.toString()?.toPagingObject(Track.serializer(), endpoint = this, json = json),
                    map["episodes"]?.toString()?.toNullablePagingObject(SimpleEpisode.serializer(), endpoint = this, json = json),
                    map["shows"]?.toString()?.toNullablePagingObject(SimpleShow.serializer(), endpoint = this, json = json)
            )
        }
    }

    /**
     * Get Spotify Catalog information about playlists that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of full [Playlist] objects ordered by likelihood of correct match
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchPlaylist(
        query: String,
        limit: Int? = api.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging {
            get(build(query, market, limit, offset, SearchType.PLAYLIST))
                    .toPagingObject(SimplePlaylist.serializer(), "playlists", this, json)
        }
    }

    /**
     * Get Spotify Catalog information about artists that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of full [Artist] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchArtist(
        query: String,
        limit: Int? = api.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<Artist, PagingObject<Artist>> {
        return toActionPaging {
            get(build(query, market, limit, offset, SearchType.ARTIST))
                    .toPagingObject(Artist.serializer(), "artists", this, json)
        }
    }

    /**
     * Get Spotify Catalog information about albums that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleAlbum] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchAlbum(
        query: String,
        limit: Int? = api.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimpleAlbum, PagingObject<SimpleAlbum>> {
        return toActionPaging {
            get(build(query, market, limit, offset, SearchType.ALBUM))
                    .toPagingObject(SimpleAlbum.serializer(), "albums", this, json)
        }
    }

    /**
     * Get Spotify Catalog information about tracks that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleTrack] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    fun searchTrack(
        query: String,
        limit: Int? = api.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<Track, PagingObject<Track>> {
        return toActionPaging {
            get(build(query, market, limit, offset, SearchType.TRACK))
                    .toPagingObject(Track.serializer(), "tracks", this, json)
        }
    }

    protected fun build(
        query: String,
        market: Market?,
        limit: Int?,
        offset: Int?,
        vararg types: SearchType,
        includeExternal: Boolean? = null
    ): String {
        return EndpointBuilder("/search").with("q", query.encodeUrl()).with("type", types.joinToString(",") { it.id })
                .with("market", market?.name).with("limit", limit).with("offset", offset)
                .with("include_external", if (includeExternal == true) "audio" else null).toString()
    }
}
