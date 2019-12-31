/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Spotify music category
 *
 * @property href A link to the Web API endpoint returning full details of the category.
 * @property icons The category icon, in various sizes.
 * @property id The Spotify category ID of the category.
 * @property name The name of the category.
 */
@Serializable
data class SpotifyCategory(
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,

    val icons: List<SpotifyImage>,
    val name: String
) : Identifiable(href, id)

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
@Serializable
data class RecommendationSeed(
    @SerialName("href") override val href: String? = null,
    @SerialName("id") override val id: String,

    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int? = null,
    val type: String
) : Identifiable(href, id)

/**
 * @property seeds An array of recommendation seed objects.
 * @property tracks An array of track object (simplified) ordered according to the parameters supplied.
 */
@Serializable
data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<Track>)

/**
 * Spotify featured playlists (on the Browse tab)
 *
 * @property message the featured message in "Overview"
 * @property playlists [PagingObject] of returned items
 */
@Serializable
data class FeaturedPlaylists(val message: String, val playlists: PagingObject<SimplePlaylist>)
