package com.adamratzman.spotify.models

import com.beust.klaxon.Json

/**
 * @property addedAt The date and time the album was saved.
 * @property track Information about the album.
 */
data class SavedAlbum(
        @Json(name = "added_at") val addedAt: String,
        val album: Album
)

/**
 * @property addedAt The date and time the track was saved.
 * @property track The track object.
 */
data class SavedTrack(
        @Json(name = "added_at") val addedAt: String,
        val track: Track
)