/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

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

sealed class SpotifyUri(input: String, type: String) {
    val uri: String
    val id: String

    init {
        input.replace(" ", "").let {
            this.uri = it.add(type)
            this.id = it.remove(type)
        }
    }
}

class AlbumURI(input: String) : SpotifyUri(input, "album")

class ArtistURI(input: String) : SpotifyUri(input, "artist")

class TrackURI(input: String) : SpotifyUri(input, "track")

class UserURI(input: String) : SpotifyUri(input, "user")

class PlaylistURI(input: String) : SpotifyUri(input, "playlist")
