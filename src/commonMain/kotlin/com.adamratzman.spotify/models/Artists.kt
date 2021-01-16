/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simplified Artist object that can be used to retrieve a full [Artist]
 *
 * @param href A link to the Web API endpoint providing full details of the artist.
 * @param id The Spotify ID for the artist.
 * @param name The name of the artist
 * @param type The object type: "artist"
 */
@Serializable
public data class SimpleArtist(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: SpotifyUri,

    val name: String,
    val type: String
) : CoreObject() {
    /**
     * Converts this [SimpleArtist] into a full [Artist] object
     */
    public suspend fun toFullArtist(): Artist? = api.artists.getArtist(id)

    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> = listOf(this)
}

/**
 * Represents an Artist (distinct from a regular user) on Spotify
 *
 * @param followers Information about the followers of the artist.
 * @param genres A list of the genres the artist is associated with. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @param href A link to the Web API endpoint providing full details of the artist.
 * @param id The Spotify ID for the artist.
 * @param images Images of the artist in various sizes, widest first.
 * @param name The name of the artist
 * @param popularity The popularity of the artist. The value will be between 0 and 100, with 100 being the most
 * popular. The artist’s popularity is calculated from the popularity of all the artist’s tracks.
 * @param type The object type: "artist"
 */
@Serializable
public data class Artist(
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
) : CoreObject() {
    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> = listOf(this)
}

@Serializable
internal data class ArtistList(val artists: List<Artist?>)
