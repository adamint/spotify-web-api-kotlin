/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an album saved in a user's library
 *
 * @param addedAt The date and time the album was saved.
 * @param album Information about the album.
 */
@Serializable
public data class SavedAlbum(
    @SerialName("added_at") val addedAt: String,
    val album: Album
)

/**
 * Represents a track saved in a user's library
 *
 * @param addedAt The date and time the track was saved.
 * @param track The track object.
 */
@Serializable
public data class SavedTrack(
    @SerialName("added_at") val addedAt: String,
    val track: Track
)
