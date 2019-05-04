/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.beust.klaxon.Json

/**
 * @property birthdate The user’s date-of-birth. This field is only available when the current user
 * has granted access to the user-read-birthdate scope.
 * @property country The country of the user, as set in the user’s account profile. An ISO 3166-1 alpha-2
 * country code. This field is only available when the current user has granted access to the user-read-private scope.
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property email The user’s email address, as entered by the user when creating their account. Important! This email
 * address is unverified; there is no proof that it actually belongs to the user. This field is only
 * available when the current user has granted access to the user-read-email scope.
 * @property externalUrls Known external URLs for this user.
 * @property followers Information about the followers of the user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for the user
 * @property images The user’s profile image.
 * @property product The user’s Spotify subscription level: “premium”, “free”, etc.
 * (The subscription level “open” can be considered the same as “free”.) This field is only available when the
 * current user has granted access to the user-read-private scope.
 * @property type The object type: “user”
 * @property uri The Spotify URI for the user.
 */
data class SpotifyUserInformation(
    val birthdate: String? = null,
    val country: String? = null,
    @Json(name = "display_name") val displayName: String? = null,
    val email: String? = null,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val product: String?,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: UserURI = UserURI(_uri)
)

/**
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property externalUrls Known public external URLs for this user.
 * @property followers Information about the followers of this user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for this user.
 * @property images The user’s profile image.
 * @property type The object type: “user”
 * @property uri The Spotify URI for this user.
 */
data class SpotifyPublicUser(
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers = Followers(null, -1),
    val href: String,
    val id: String,
    val images: List<SpotifyImage> = listOf(),
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: UserURI = UserURI(_uri)
)

/**
 * Spotify user's followers
 *
 * @property href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @property total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */
data class Followers(
    val href: String?,
    val total: Int
)