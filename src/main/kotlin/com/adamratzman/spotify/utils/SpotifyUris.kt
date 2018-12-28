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
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' uri")
}

private fun String.remove(type: String): String {
    this.matchType(type)?.also {
        return it.trim()
    }
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' id")
}

abstract class SpotifyUri(tmp: String, val input: String = tmp.replace(" ", "")) {
    abstract val uri: String
    abstract val id: String
}

class AlbumURI(input: String) : SpotifyUri(input) {
    override val uri: String = super.input.add("album")
    override val id: String = super.input.remove("album")
}

class ArtistURI(input: String) : SpotifyUri(input) {
    override val uri: String = super.input.add("artist")
    override val id: String = super.input.remove("artist")
}

class TrackURI(input: String) : SpotifyUri(input) {
    override val uri: String = super.input.add("track")
    override val id: String = super.input.remove("track")
}

class UserURI(input: String) : SpotifyUri(input) {
    override val uri: String = super.input.add("user")
    override val id: String = super.input.remove("user")
}

class PlaylistURI(input: String) : SpotifyUri(input) {
    override val uri: String = super.input.add("playlist")
    override val id: String = super.input.remove("playlist")
}

