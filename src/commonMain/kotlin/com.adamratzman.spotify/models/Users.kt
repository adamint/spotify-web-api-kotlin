/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Private information about a Spotify user. Each field may require a specific scope.
 *
 * @property birthdate The user’s date-of-birth. This field is only available when the current user
 * has granted access to the user-read-birthdate scope.
 * @property country The country of the user, as set in the user’s account profile. An ISO 3166-1 alpha-2
 * country code. This field is only available when the current user has granted access to the user-read-private scope.
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property email The user’s email address, as entered by the user when creating their account. Important! This email
 * address is unverified; there is no proof that it actually belongs to the user. This field is only
 * available when the current user has granted access to the user-read-email scope.
 * @property followers Information about the followers of the user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for the user
 * @property images The user’s profile image.
 * @property product The user’s Spotify subscription level: “premium”, “free”, etc.
 * (The subscription level “open” can be considered the same as “free”.) This field is only available when the
 * current user has granted access to the user-read-private scope.
 * @property type The object type: “user”
 */
@Serializable
data class SpotifyUserInformation(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,
    @SerialName("uri") private val uriString: String,

    val birthdate: String? = null,
    val country: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val email: String? = null,
    val followers: Followers,
    val images: List<SpotifyImage>,
    val product: String?,
    @SerialName("explicit_content") val explicitContentSettings: ExplicitContentSettings?,
    val type: String
) : CoreObject(href, id, UserUri(uriString), externalUrlsString)

/**
 * Public information about a Spotify user
 *
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property followers Information about the followers of this user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for this user.
 * @property images The user’s profile image.
 * @property type The object type: “user”
 */
@Serializable
data class SpotifyPublicUser(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,
    @SerialName("uri") private val uriString: String,

    @SerialName("display_name") val displayName: String? = null,
    val followers: Followers = Followers(null, -1),
    val images: List<SpotifyImage> = listOf(),
    val type: String
) : CoreObject(href, id, UserUri(uriString), externalUrlsString)

/**
 * Information about a Spotify user's followers
 *
 * @property href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @property total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */
@Serializable
data class Followers(
    val href: String?,
    @SerialName("total") val total: Int
)

@Serializable
data class ExplicitContentSettings(
    @SerialName("filter_enabled") val filterEnabled: Boolean,
    @SerialName("filter_locked") val filterLocked: Boolean
)
