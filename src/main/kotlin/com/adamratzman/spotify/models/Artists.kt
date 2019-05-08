/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.beust.klaxon.Json

/**
 * Simplified Artist object that can be used to retrieve a full [Artist]
 *
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property name The name of the artist
 * @property type The object type: "artist"
 */
data class SimpleArtist(
    @Json(name = "external_urls") private val _externalUrls: Map<String, String>,
    @Json(name = "href") private val _href: String,
    @Json(name = "id") private val _id: String,
    @Json(name = "uri", ignored = false) private val _uri: String,

    val name: String,
    val type: String
) : CoreObject(_href, _id, ArtistURI(_uri), _externalUrls) {
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
data class Artist(
    @Json(name = "external_urls") private val _externalUrls: Map<String, String>,
    @Json(name = "href") private val _href: String,
    @Json(name = "id") private val _id: String,
    @Json(name = "uri", ignored = false) private val _uri: String,

    val followers: Followers,
    val genres: List<String>,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String
) : CoreObject(_href, _id, ArtistURI(_uri), _externalUrls)

internal data class ArtistList(val artists: List<Artist?>)