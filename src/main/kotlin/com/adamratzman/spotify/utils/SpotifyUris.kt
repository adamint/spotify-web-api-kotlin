package com.adamratzman.spotify.utils

private fun String.matchType(type: String): String? {
    val typeRegex = "^spotify:(?:.*:)*$type:([^:]+)(?::.*)*$|^([^:]+)$".toRegex()
    val match = typeRegex.matchEntire(this)?.groupValues ?: return null
    return match[1].takeIf { it.isNotEmpty() } ?: match[2].takeIf { it.isNotEmpty() }
}

private fun String.add(type: String): String {
    this.matchType(type)?.also {
        return "spotify:$type:${it.trim()}"
    }
    throw IllegalArgumentException("'$this' isn't convertible to '$type' uri")
}

private fun String.remove(type: String): String {
    this.matchType(type)?.also {
        return it.trim()
    }
    throw IllegalArgumentException("'$this' isn't convertible to '$type' id")
}


inline class AlbumURI(private val input: String) {
    val uri: String
        get() = input.add("album")
    val id: String
        get() = input.remove("album")
}

inline class ArtistURI(private val input: String) {
    val uri: String
        get() = input.add("artist")
    val id: String
        get() = input.remove("artist")
}

inline class TrackURI(private val input: String) {
    val uri: String
        get() = input.add("track")
    val id: String
        get() = input.remove("track")
}

inline class UserURI(private val input: String) {
    val uri: String
        get() = input.add("user")
    val id: String
        get() = input.remove("user")
}

inline class PlaylistURI(private val input: String) {
    val uri: String
        get() = input.add("playlist")
    val id: String
        get() = input.remove("playlist")
}

