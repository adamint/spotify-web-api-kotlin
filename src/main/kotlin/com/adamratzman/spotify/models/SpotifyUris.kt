/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

private fun String.matchType(type: String): String? {
    val typeRegex = "^spotify:(?:.*:)*$type:([^:]+)(?::.*)*$|^([^:]+)$".toRegex()
    val match = typeRegex.matchEntire(this)?.groupValues ?: return null
    return match[1].takeIf { it.isNotEmpty() } ?: match[2].takeIf { it.isNotEmpty() }
}

private fun String.add(type: String): String {
    this.matchType(type)?.let {
        return "spotify:$type:${it.trim()}"
    }
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' uri")
}

private fun String.remove(type: String): String {
    this.matchType(type)?.let {
        return it.trim()
    }
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' id")
}

/**
 * Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
sealed class SpotifyUri(input: String, type: String) {
    val uri: String
    val id: String

    init {
        input.replace(" ", "").let {
            this.uri = it.add(type)
            this.id = it.remove(type)
        }
    }

    override fun equals(other: Any?): Boolean {
        val spotifyUri = other as? SpotifyUri ?: return false
        return spotifyUri.uri == this.uri
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
class AlbumURI(input: String) : SpotifyUri(input, "album")

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
class ArtistURI(input: String) : SpotifyUri(input, "artist")

/**
 * Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
class TrackURI(input: String) : SpotifyUri(input, "track")

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
class UserURI(input: String) : SpotifyUri(input, "user")

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
class PlaylistURI(input: String) : SpotifyUri(input, "playlist")
