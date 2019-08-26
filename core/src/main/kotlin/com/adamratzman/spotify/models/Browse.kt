/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.squareup.moshi.Json

/**
 * Spotify music category
 *
 * @property href A link to the Web API endpoint returning full details of the category.
 * @property icons The category icon, in various sizes.
 * @property id The Spotify category ID of the category.
 * @property name The name of the category.
 */
data class SpotifyCategory(
    @Json(name = "href") private val _href: String,
    @Json(name = "id") private val _id: String,

    val icons: List<SpotifyImage>,
    val name: String
) : Identifiable(_href, _id)

/**
 * Seed from which the recommendation was constructed
 *
 * @property initialPoolSize The number of recommended tracks available for this seed.
 * @property afterFilteringSize The number of tracks available after min_* and max_* filters have been applied.
 * @property afterRelinkingSize The number of tracks available after relinking for regional availability.
 * @property href A link to the full track or artist data for this seed. For tracks this will be a link to a Track
 * Object. For artists a link to an Artist Object. For genre seeds, this value will be null.
 * @property id The id used to select this seed. This will be the same as the string used in the
 * seed_artists , seed_tracks or seed_genres parameter.
 * @property type The entity type of this seed. One of artist , track or genre.
 */
data class RecommendationSeed(
    @Json(name = "href") private val _href: String?,
    @Json(name = "id") private val _id: String,

    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int?,
    val type: String
) : Identifiable(_href, _id)

/**
 * @property seeds An array of recommendation seed objects.
 * @property tracks An array of track object (simplified) ordered according to the parameters supplied.
 */
data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<SimpleTrack>)

/**
 * Spotify featured playlists (on the Browse tab)
 *
 * @property message the featured message in "Overview"
 * @property playlists [PagingObject] of returned items
 */
data class FeaturedPlaylists(val message: String, val playlists: PagingObject<SimplePlaylist>)
