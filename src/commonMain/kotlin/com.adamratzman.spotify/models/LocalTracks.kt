/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Local artist object (goes with [LocalTrack]) representing an artist on a local track
 *
 * @property name The name of the artist
 * @property type The object type: "artist"
 */
@Serializable
data class SimpleLocalArtist(
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
data class SimpleLocalAlbum(
    @SerialName("album_type") val albumType: String? = null,
    val artists: List<SimpleLocalArtist> = listOf(),
    val name: String,
    @SerialName("release_date") private val releaseDate: String? = null,
    @SerialName("release_date_precision") val releaseDatePrecision: String? = null,
    val type: String
)

@Serializable
data class LocalTrack(
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
) : IdentifiableNullable(), Playable
