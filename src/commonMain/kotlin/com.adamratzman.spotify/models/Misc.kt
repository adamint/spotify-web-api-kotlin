/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable

/**
 * A Spotify image
 *
 * @property height The image height in pixels. If unknown: null or not returned.
 * @property url The source URL of the image.
 * @property width The image width in pixels. If unknown: null or not returned.
 */
@Serializable
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
@Serializable
data class Restrictions(val reason: String)