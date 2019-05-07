/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.beust.klaxon.Json

/**
 * Represents an album saved in a user's library
 *
 * @property addedAt The date and time the album was saved.
 * @property album Information about the album.
 */
data class SavedAlbum(
    @Json(name = "added_at") val addedAt: String,
    val album: Album
)

/**
 * Represents a track saved in a user's library
 *
 * @property addedAt The date and time the track was saved.
 * @property track The track object.
 */
data class SavedTrack(
    @Json(name = "added_at") val addedAt: String,
    val track: Track
)