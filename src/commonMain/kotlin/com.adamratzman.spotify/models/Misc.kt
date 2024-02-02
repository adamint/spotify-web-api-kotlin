/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable

/**
 * A Spotify image
 *
 * @param height The image height in pixels. If unknown: null or not returned.
 * @param url The source URL of the image.
 * @param width The image width in pixels. If unknown: null or not returned.
 */
@Serializable
public data class SpotifyImage(
    val height: Double? = null,
    val url: String,
    val width: Double? = null
)

/**
 * Contains an explanation of why a track is not available
 *
 * @param reason why the track is not available
 */
@Serializable
public data class Restrictions(val reason: String)
