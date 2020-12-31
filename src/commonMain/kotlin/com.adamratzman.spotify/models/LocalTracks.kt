/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Local artist object (goes with [LocalTrack]) representing an artist on a local track
 *
 * @property name The name of the artist
 * @property type The object type: "artist"
 */
@Serializable
public data class SimpleLocalArtist(
    val name: String,
    val type: String
)

/**
 * Local album object that goes with [LocalTrack] - represents the local album it was obtained from (likely "Local Files")
 *
 * @property artists The artists of the album.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property type The object type: “album”
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property albumType The type of the album: one of “album”, “single”, or “compilation”.
 */
@Serializable
public data class SimpleLocalAlbum(
    @SerialName("album_type") val albumType: String? = null,
    val artists: List<SimpleLocalArtist> = listOf(),
    val name: String,
    @SerialName("release_date") private val releaseDate: String? = null,
    @SerialName("release_date_precision") val releaseDatePrecision: String? = null,
    val type: String
)

/**
 * Local track object that representing a song uploaded from a client locally
 *
 * @property artists The artists who performed the track.
 * @property discNumber The disc number.
 * @property durationMs The track length in milliseconds.
 * @property explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @property href A link to the Web API endpoint providing full details of the track.
 * @property id The Spotify ID for the track.
 * @property name The name of the track.
 * @property trackNumber The number of the track. If an album has several discs, the track number
 * is the number on the specified disc.
 * @property type The object type: “track”.
 * @property isLocal Whether or not the track is from a local file.
 * @property popularity the popularity of this track. possibly null
 */
@Serializable
public data class LocalTrack(
    val album: SimpleLocalAlbum,
    val artists: List<SimpleLocalArtist>,
    override val href: String? = null,
    override val id: String? = null,
    @SerialName("disc_number") val discNumber: String? = null,
    @SerialName("duration_ms") val durationMs: Int? = null,
    @SerialName("explicit") val explicit: Boolean? = null,
    @SerialName("is_local") val isLocal: Boolean = true,
    val name: String,
    val popularity: Int? = null,
    @SerialName("track_number") val trackNumber: Int? = null,
    override val type: String,
    override val uri: LocalTrackUri
) : IdentifiableNullable(), Playable {

    /**
     * Search for this local track by name in Spotify's track catalog.
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     */
    public suspend fun searchForSpotifyTrack(limit: Int? = null, offset: Int? = null, market: Market? = null): PagingObject<Track> = api.search.searchTrack(name, limit, offset, market)
}
