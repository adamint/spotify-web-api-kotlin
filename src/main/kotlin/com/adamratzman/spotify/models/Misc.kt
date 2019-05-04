package com.adamratzman.spotify.models

/**
 * A Spotify image
 *
 * @property height The image height in pixels. If unknown: null or not returned.
 * @property url The source URL of the image.
 * @property width The image width in pixels. If unknown: null or not returned.
 */
data class SpotifyImage(
        val height: Int? = null,
        val url: String,
        val width: Int? = null
)

/**
 * Contains an explanation of why a track is not available
 *
 * @property reason why the track is not available
 */
data class Restrictions(val reason: String)