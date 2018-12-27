package com.adamratzman.spotify.utils

private fun String.add(type: String, length: Int = 22): String {
    val match = "^(?:$type:)?(.{$length})(?::.*)?$".toRegex().matchEntire(this)
    match?.groupValues?.get(1)?.also {
        return "$type:$it"
    }
    throw IllegalArgumentException("$this isn't convertible to $type uri")
}

private fun String.remove(type: String, length: Int = 22): String {
    val match = "^(?:$type:)?(.{$length})(?::.*)?$".toRegex().matchEntire(this)
    match?.groupValues?.get(1)?.also {
        return it
    }
    throw IllegalArgumentException("'$this' isn't convertible to '$type' id")
}


inline class AlbumURI(private val input: String) {
    val uri: String
        get() = input.add("spotify:album")
    val id: String
        get() = input.remove("spotify:album")
}

inline class ArtistURI(private val input: String) {
    val uri: String
        get() = input.add("spotify:artist")
    val id: String
        get() = input.remove("spotify:artist")
}

inline class TrackURI(private val input: String) {
    val uri: String
        get() = input.add("spotify:track")
    val id: String
        get() = input.remove("spotify:track")
}

inline class UserURI(private val input: String) {
    val uri: String
        get() = input.add("spotify:user", 25)
    val id: String
        get() = input.remove("spotify:user", 25)

    // inline is not supported as non-top-level-class
    inner /*inline*/ class PlaylistURI(private val input: String) {
        val uri: String
            get() = input.add("${this@UserURI.uri}:playlist")
        val id: String
            get() = input.remove("${this@UserURI.uri}:playlist")
    }

    companion object {
        @Suppress("FunctionName") // "static" constructor
        fun PlaylistURI(input: String): PlaylistURI {
            return UserURI(input).PlaylistURI(input).also { it.uri /*check format*/ }
        }
    }
}
