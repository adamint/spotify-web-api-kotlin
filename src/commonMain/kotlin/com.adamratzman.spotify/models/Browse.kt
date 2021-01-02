/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Spotify music category
 *
 * @param href A link to the Web API endpoint returning full details of the category.
 * @param icons The category icon, in various sizes.
 * @param id The Spotify category ID of the category.
 * @param name The name of the category.
 */
@Serializable
public data class SpotifyCategory(
    override val href: String,
    override val id: String,

    val icons: List<SpotifyImage>,
    val name: String
) : Identifiable()

/**
 * Seed from which the recommendation was constructed
 *
 * @param initialPoolSize The number of recommended tracks available for this seed.
 * @param afterFilteringSize The number of tracks available after min_* and max_* filters have been applied.
 * @param afterRelinkingSize The number of tracks available after relinking for regional availability.
 * @param href A link to the full track or artist data for this seed. For tracks this will be a link to a Track
 * Object. For artists a link to an Artist Object. For genre seeds, this value will be null.
 * @param id The id used to select this seed. This will be the same as the string used in the
 * seed_artists , seed_tracks or seed_genres parameter.
 * @param type The entity type of this seed. One of artist , track or genre.
 */
@Serializable
public data class RecommendationSeed(
    @SerialName("href") override val href: String? = null,
    @SerialName("id") override val id: String,

    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int? = null,
    val type: String
) : Identifiable()

/**
 * @param seeds An array of recommendation seed objects.
 * @param tracks An array of track object (simplified) ordered according to the parameters supplied.
 */
@Serializable
public data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<Track>)

/**
 * Spotify featured playlists (on the Browse tab)
 *
 * @param message the featured message in "Overview"
 * @param playlists [PagingObject] of returned items
 */
@Serializable
public data class FeaturedPlaylists(val message: String, val playlists: PagingObject<SimplePlaylist>)
