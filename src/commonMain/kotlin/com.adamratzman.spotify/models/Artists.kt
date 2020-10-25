/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
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
    override val href: String,
    override val id: String,
    override val uri: ArtistUri,

    val name: String,
    val type: String
) : CoreObject() {

    /**
     * This [SimpleArtist] into a full [Artist] object
     */
    val fullArtist by lazy { api.artists.getArtist(id) }

    /**
     * Converts this [SimpleArtist] into a full [Artist] object
     */
    @Deprecated("Replaced with a lazy fullArtist property", ReplaceWith("fullArtist"))
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
    override val href: String,
    override val id: String,
    override val uri: ArtistUri,

    val followers: Followers,
    val genres: List<String>,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String
) : CoreObject()

@Serializable
internal data class ArtistList(val artists: List<Artist?>)
