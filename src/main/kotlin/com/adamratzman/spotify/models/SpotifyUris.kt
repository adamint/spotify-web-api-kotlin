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
sealed class SpotifyUri(input: String, type: UriType) {
    val uri: String
    val id: String

    init {
        input.replace(" ", "").let {
            if (input == "spotify:user:") {
                this.uri = input
                this.id = input
            } else {
                this.uri = it.add(type.toString())
                this.id = it.remove(type.toString())
            }
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

    enum class UriType(private val typeStr: String) {
        ALBUM("album"),
        ARTIST("artist"),
        TRACK("track"),
        USER("user"),
        PLAYLIST("playlist"),
        LOCAL_TRACK("local");

        override fun toString() = typeStr
    }

    companion object {
        fun isUriType(uri: String, type: UriType) = uri.matchType(type.toString()) != null
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
class AlbumURI(input: String) : SpotifyUri(input, UriType.ALBUM)

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
class ArtistURI(input: String) : SpotifyUri(input, UriType.ARTIST)

/**
 * Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
class TrackURI(input: String) : SpotifyUri(input, UriType.TRACK)

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
class UserURI(input: String) : SpotifyUri(input, UriType.USER)

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
class PlaylistURI(input: String) : SpotifyUri(input, UriType.PLAYLIST)

/**
 * Represents a Spotify **local track** URI
 */
class LocalTrackURI(input: String) : SpotifyUri(input, UriType.LOCAL_TRACK)