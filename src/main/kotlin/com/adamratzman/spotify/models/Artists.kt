/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.beust.klaxon.Json

/**
 * @property externalUrls Known external URLs for this artist.
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property name The name of the artist
 * @property type The object type: "artist"
 * @property uri The Spotify URI for the artist.
 */
data class SimpleArtist(
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: ArtistURI = ArtistURI(_uri)
) : Linkable() {
    fun toFullArtist() = api.artists.getArtist(id)
}

/**
 * @property externalUrls Known external URLs for this artist.
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
 * @property uri The Spotify URI for the artist.
 */
data class Artist(
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: ArtistURI = ArtistURI(_uri)
)

internal data class ArtistList(val artists: List<Artist?>)