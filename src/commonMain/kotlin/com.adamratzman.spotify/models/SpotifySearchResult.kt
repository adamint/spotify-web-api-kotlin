/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable

/**
 * Available filters that Spotify allows in search, in addition to filtering by object type.
 */
public enum class SearchFilterType(public val id: String) {
    Album("album"),
    Artist("artist"),
    Track("track"),
    Year("year"),
    Upc("upc"),
    Hipster("tag:hipster"),
    New("tag:new"),
    Isrc("isrc"),
    Genre("genre")
}

/**
 * A filter of type [SearchFilterType]. Should be unique by type.
 *
 * @param filterValue A string to match, or in the case of [SearchFilterType.Year] can be a range of years in the form
 * A-B. Example: 2000-2010
 */
public data class SearchFilter(val filterType: SearchFilterType, val filterValue: String)

@Serializable
public data class SpotifySearchResult(
    val albums: PagingObject<SimpleAlbum>? = null,
    val artists: PagingObject<Artist>? = null,
    val playlists: PagingObject<SimplePlaylist>? = null,
    val tracks: PagingObject<Track>? = null,
    val episodes: NullablePagingObject<SimpleEpisode>? = null,
    val shows: NullablePagingObject<SimpleShow>? = null
)
