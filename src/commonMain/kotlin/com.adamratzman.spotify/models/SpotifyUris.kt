/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyException
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor

/**
 * Exception instantiating or deserializing a uri perceived as invalid
 */
class SpotifyUriException(message: String) : SpotifyException.BadRequestException(message)

private fun String.matchType(type: String): String? {
    val typeRegex = "^spotify:(?:.*:)*$type:([^:]*)(?::.*)*$|^([^:]+)$".toRegex()
    val match = typeRegex.matchEntire(this)?.groupValues ?: return null
    return match[1].takeIf { it.isNotBlank() || match[2].isEmpty() } ?: match[2].takeIf { it.isNotEmpty() }
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

@PublishedApi
internal fun fixSpotifyUri(uri: String): String {
    val matchesOldPlaylistUriFormat = "^spotify:user:(?:.*:)*playlist:(.+)\$".toRegex().matchEntire(uri)
    return (matchesOldPlaylistUriFormat?.let { result -> "spotify:playlist:${result.groupValues[1]}" } ?: uri)
            .replace(" ", "")
}

/**
 * Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
@Serializable
sealed class SpotifyUri(input: String, type: String) {
    val uri: String
    val id: String

    init {
        fixSpotifyUri(input).let {
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

    override fun toString(): String {
        return "SpotifyUri(${fixSpotifyUri(uri)})"
    }

    @Serializer(forClass = SpotifyUri::class)
    companion object : KSerializer<SpotifyUri> {
        override val descriptor: SerialDescriptor = StringDescriptor
        override fun deserialize(decoder: Decoder): SpotifyUri = SpotifyUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, obj: SpotifyUri) = encoder.encodeString(obj.uri)

        /**
         * This function safely instantiates a SpotifyUri from given constructor.
         * */
        inline fun <T : SpotifyUri> safeInitiate(uri: String, ctor: (String) -> T): T? {
            return try {
                ctor(fixSpotifyUri(uri))
            } catch (e: SpotifyUriException) {
                null
            }
        }

        /**
         * Creates a abstract SpotifyUri of given input. Doesn't allow ambiguity by disallowing creation by id.
         * */
        operator fun invoke(inputTemp: String): SpotifyUri {
            val input = fixSpotifyUri(inputTemp)
            val constructors = listOf(::AlbumUri, ::ArtistUri, TrackUri.Companion::invoke, ::UserUri, ::PlaylistUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.takeIf { it.uri == input }?.also { return it }
            }

            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to any arbitrary id")
        }

        /**
         * This function returns whether or not the given input IS a given type.
         *
         * @example ```Kotlin
         *     SpotifyUri.isType<UserUri>("abc") // returns: false
         *     SpotifyUri.isType<UserUri>("spotify:user:abc") // returns: true
         *     SpotifyUri.isType<UserUri>("spotify:track:abc") // returns: false
         * ```
         * */
        inline fun <reified T : SpotifyUri> isType(inputTemp: String): Boolean {
            val input = fixSpotifyUri(inputTemp)
            return safeInitiate(input, ::invoke)?.let { it is T } ?: false
        }

        /**
         * This function returns whether ot not the given input CAN be a given type.
         *
         * @example ```Kotlin
         *     SpotifyUri.canBeType<UserUri>("abc") // returns: true
         *     SpotifyUri.canBeType<UserUri>("spotify:user:abc") // returns: true
         *     SpotifyUri.canBeType<UserUri>("spotify:track:abc") // returns: false
         * ```
         * */
        inline fun <reified T : SpotifyUri> canBeType(inputTemp: String): Boolean {
            val input = fixSpotifyUri(inputTemp)
            return isType<T>(input) || !input.contains(':')
        }
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class AlbumUri(input: String) : SpotifyUri(input, "album") {
    @Serializer(forClass = AlbumUri::class)
    companion object : KSerializer<AlbumUri> by SimpleUriSerializer(::AlbumUri)
}

@Deprecated("renamed", ReplaceWith("AlbumUri", "com.adamratzman.spotify.models.AlbumUri"))
typealias AlbumURI = AlbumUri

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class ArtistUri(input: String) : SpotifyUri(input, "artist") {
    @Serializer(forClass = ArtistUri::class)
    companion object : KSerializer<ArtistUri> by SimpleUriSerializer(::ArtistUri)
}

@Deprecated("renamed", ReplaceWith("ArtistUri", "com.adamratzman.spotify.models.ArtistUri"))
typealias ArtistURI = ArtistUri

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class UserUri(input: String) : SpotifyUri(input, "user") {
    @Serializer(forClass = UserUri::class)
    companion object : KSerializer<UserUri> by SimpleUriSerializer(::UserUri)
}

@Deprecated("renamed", ReplaceWith("UserUri", "com.adamratzman.spotify.models.UserUri"))
typealias UserURI = UserUri

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class PlaylistUri(input: String) : SpotifyUri(input, "playlist") {
    @Serializer(forClass = PlaylistUri::class)
    companion object : KSerializer<PlaylistUri> by SimpleUriSerializer(::PlaylistUri)
}

@Deprecated("renamed", ReplaceWith("PlaylistUri", "com.adamratzman.spotify.models.PlaylistUri"))
typealias PlaylistURI = PlaylistUri

/**
 * Represents a Spotify **Track** URI, ether LocalTrack or SpotifyTrack, parsed from either a Spotify ID or taken
 * from an endpoint
 * */
@Serializable
sealed class TrackUri(input: String, type: String) : SpotifyUri(input, type) {
    @Serializer(forClass = TrackUri::class)
    companion object : KSerializer<TrackUri> {
        override val descriptor: SerialDescriptor = StringDescriptor
        override fun deserialize(decoder: Decoder): TrackUri = TrackUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, obj: TrackUri) = encoder.encodeString(obj.uri)

        /**
         * Creates a abstract TrackURI of given input. Prefers SpotifyTrackUri if the input is ambiguous.
         * */
        operator fun invoke(input: String): TrackUri {
            val constructors = listOf(::SpotifyTrackUri, ::LocalTrackUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'track' or 'local' id")
        }
    }
}

@Deprecated("renamed", ReplaceWith("TrackUri", "com.adamratzman.spotify.models.TrackUri"))
typealias TrackURI = TrackUri

/**
 * Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class SpotifyTrackUri(input: String) : TrackUri(input, "track") {
    @Serializer(forClass = SpotifyTrackUri::class)
    companion object : KSerializer<SpotifyTrackUri> by SimpleUriSerializer(::SpotifyTrackUri)
}

/**
 * Represents a Spotify **local track** URI
 */
@Serializable
class LocalTrackUri(input: String) : TrackUri(input, "local") {
    @Serializer(forClass = LocalTrackUri::class)
    companion object : KSerializer<LocalTrackUri> by SimpleUriSerializer(::LocalTrackUri)
}

@Deprecated("renamed", ReplaceWith("LocalTrackUri", "com.adamratzman.spotify.models.LocalTrackUri"))
typealias LocalTrackURI = LocalTrackUri
