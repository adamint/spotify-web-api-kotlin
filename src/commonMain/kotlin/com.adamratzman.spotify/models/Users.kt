/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Private information about a Spotify user. Each field may require a specific scope.
 *
 * @param country The country of the user, as set in the user’s account profile. An ISO 3166-1 alpha-2
 * country code. This field is only available when the current user has granted access to the [SpotifyScope.USER_READ_PRIVATE] scope.
 * @param displayName The name displayed on the user’s profile. null if not available.
 * @param email The user’s email address, as entered by the user when creating their account. Important! This email
 * address is unverified; there is no proof that it actually belongs to the user. This field is only
 * available when the current user has granted access to the [SpotifyScope.USER_READ_EMAIL] scope.
 * @param followers Information about the followers of the user.
 * @param href A link to the Web API endpoint for this user.
 * @param id The Spotify user ID for the user
 * @param images The user’s profile image.
 * @param product The user’s Spotify subscription level: “premium”, “free”, etc.
 * (The subscription level “open” can be considered the same as “free”.) This field is only available when the
 * current user has granted access to the [SpotifyScope.USER_READ_PRIVATE] scope.
 * @param type The object type: “user”
 */
@Serializable
public data class SpotifyUserInformation(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: UserUri,

    val country: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val email: String? = null,
    val followers: Followers,
    val images: List<SpotifyImage>,
    val product: String? = null,
    @SerialName("explicit_content") val explicitContentSettings: ExplicitContentSettings? = null,
    val type: String
) : CoreObject()

/**
 * Public information about a Spotify user
 *
 * @param displayName The name displayed on the user’s profile. null if not available.
 * @param followers Information about the followers of this user.
 * @param href A link to the Web API endpoint for this user.
 * @param id The Spotify user ID for this user.
 * @param images The user’s profile image.
 * @param type The object type: “user”
 */
@Serializable
public data class SpotifyPublicUser(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: UserUri,

    @SerialName("display_name") val displayName: String? = null,
    val followers: Followers = Followers(null, -1),
    val images: List<SpotifyImage> = listOf(),
    val type: String
) : CoreObject()

/**
 * Information about a Spotify user's followers
 *
 * @param href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @param total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */
@Serializable
public data class Followers(
    val href: String? = null,
    @SerialName("total") val total: Int
)

@Serializable
public data class ExplicitContentSettings(
    @SerialName("filter_enabled") val filterEnabled: Boolean,
    @SerialName("filter_locked") val filterLocked: Boolean
)
