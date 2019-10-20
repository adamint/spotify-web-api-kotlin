/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an album saved in a user's library
 *
 * @property addedAt The date and time the album was saved.
 * @property album Information about the album.
 */
@Serializable
data class SavedAlbum(
    @SerialName("added_at") val addedAt: String,
    val album: Album
)

/**
 * Represents a track saved in a user's library
 *
 * @property addedAt The date and time the track was saved.
 * @property track The track object.
 */
@Serializable
data class SavedTrack(
    @SerialName("added_at") val addedAt: String,
    val track: Track
)