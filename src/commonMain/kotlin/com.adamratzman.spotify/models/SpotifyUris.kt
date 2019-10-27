/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor

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

private class SimpleUriSerializer<T : SpotifyUri>(val ctor: (String) -> T) : KSerializer<T> {
    override val descriptor: SerialDescriptor = StringDescriptor
    override fun deserialize(decoder: Decoder): T = ctor(decoder.decodeString())
    override fun serialize(encoder: Encoder, obj: T) = encoder.encodeString(obj.uri)
}

/**
 * Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
@Serializable
sealed class SpotifyUri(input: String, type: UriType) {
    val uri: String
    val id: String

    init {
        input.replace(" ", "").let {
            this.uri = it.add(type.toString())
            this.id = it.remove(type.toString())
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

    override fun toString(): String {
        return "SpotifyUri($uri)"
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

    @Serializer(forClass = SpotifyUri::class)
    companion object : KSerializer<SpotifyUri> {
        fun isUriType(uri: String, type: UriType) = uri.matchType(type.toString()) != null

        override val descriptor: SerialDescriptor = StringDescriptor
        override fun deserialize(decoder: Decoder): SpotifyUri = SpotifyUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, obj: SpotifyUri) = encoder.encodeString(obj.uri)

        private inline fun <T : SpotifyUri> safeInitiate(uri: String, ctor: (String) -> T): T? {
            return try {
                ctor(uri).takeIf { it.uri == uri }
            } catch (e: SpotifyUriException) {
                null
            }
        }

        operator fun invoke(input: String): SpotifyUri {
            val constructors = listOf(::AlbumUri, ::ArtistUri, ::TrackUri, ::UserUri, ::PlaylistUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }

            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to any arbitrary id")
        }
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class AlbumUri(val input: String) : SpotifyUri(input, UriType.ALBUM) {
    @Serializer(forClass = AlbumUri::class)
    companion object : KSerializer<AlbumUri> by SimpleUriSerializer(::AlbumUri)
}
typealias AlbumURI = AlbumUri

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class ArtistUri(val input: String) : SpotifyUri(input, UriType.ARTIST) {
    @Serializer(forClass = ArtistUri::class)
    companion object : KSerializer<ArtistUri> by SimpleUriSerializer(::ArtistUri)
}
typealias ArtistURI = ArtistUri

/**
 * Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class TrackUri(val input: String) : SpotifyUri(input, UriType.TRACK) {
    @Serializer(forClass = TrackUri::class)
    companion object : KSerializer<TrackUri> by SimpleUriSerializer(::TrackUri)
}
typealias TrackURI = TrackUri

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class UserUri(val input: String) : SpotifyUri(input, UriType.USER) {
    @Serializer(forClass = UserUri::class)
    companion object : KSerializer<UserUri> by SimpleUriSerializer(::UserUri)
}
typealias UserURI = UserUri

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class PlaylistUri(val input: String) : SpotifyUri(input, UriType.PLAYLIST) {
    @Serializer(forClass = PlaylistUri::class)
    companion object : KSerializer<PlaylistUri> by SimpleUriSerializer(::PlaylistUri)
}
typealias PlaylistURI = PlaylistUri

/**
 * Represents a Spotify **local track** URI
 */
@Serializable
class LocalTrackUri(val input: String) : SpotifyUri(input, UriType.LOCAL_TRACK) {
    @Serializer(forClass = LocalTrackUri::class)
    companion object : KSerializer<LocalTrackUri> by SimpleUriSerializer(::LocalTrackUri)
}
typealias LocalTrackURI = LocalTrackUri
