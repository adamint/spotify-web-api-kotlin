/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simplified Artist object that can be used to retrieve a full [Artist]
 *
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property name The name of the artist
 * @property type The object type: "artist"
 */
@Serializable
data class SimpleArtist(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,
    @SerialName("uri") private val uriString: String,

    val name: String,
    val type: String
) : CoreObject(href, id, ArtistUri(uriString), externalUrlsString) {
    /**
     * Converts this [SimpleArtist] into a full [Artist] object
     */
    fun toFullArtist() = api.artists.getArtist(id)
}

/**
 * Represents an Artist (distinct from a regular user) on Spotify
 *
 * @property followers Information about the followers of the artist.
 * @property genres A list of the genres the artist is associated with. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property images Images of the artist in various sizes, widest first.
 * @property name The name of the artist
 * @property popularity The popularity of the artist. The value will be between 0 and 100, with 100 being the most
 * popular. The artist’s popularity is calculated from the popularity of all the artist’s tracks.
 * @property type The object type: "artist"
 */
@Serializable
data class Artist(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,
    @SerialName("uri") private val uriString: String,

    val followers: Followers,
    val genres: List<String>,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String
) : CoreObject(href, id, ArtistUri(uriString), externalUrlsString)

@Serializable
internal data class ArtistList(val artists: List<Artist?>)
