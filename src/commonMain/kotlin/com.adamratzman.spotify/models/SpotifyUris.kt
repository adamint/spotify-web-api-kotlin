/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient

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
@Serializable
sealed class SpotifyUri(val input: String, val type: String) {
    val uri: String
    val id: String

    init {
        input.replace(" ", "").let {
            this.uri = it.add(type)
            this.id = it.remove(type)
        }
    }

    override fun equals(other: Any?): Boolean {
        val spotifyUri = other as? SpotifyUri ?: return (other as? String)?.let { this.uri == it } ?: false
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

    companion object {
        /**
         * This function safely instantiates a SpotifyUri from given constructor.
         * */
        inline fun <T : SpotifyUri> safeInitiate(uri: String, ctor: (String) -> T): T? {
            return try {
                ctor(uri)
            } catch (e: SpotifyUriException) {
                null
            }
        }

        /**
         * Creates a abstract SpotifyUri of given input. Doesn't allow ambiguity by disallowing creation by id.
         * */
        operator fun invoke(input: String): SpotifyUri {
            val constructors = listOf(::AlbumUri, ::ArtistUri, ::LocalTrackUri, ::PlaylistUri, ::SpotifyTrackUri, ::UserUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.takeIf {
                    @Suppress("ReplaceCallWithBinaryOperator")
                    it.equals(input)
                }?.also { return it }
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
        inline fun <reified T : SpotifyUri> isType(input: String): Boolean {
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
        inline fun <reified T : SpotifyUri> canBeType(input: String): Boolean {
            return isType<T>(input) || !input.contains(':')
        }
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class AlbumUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : SpotifyUri(_input, "album")

@Deprecated("renamed", ReplaceWith("AlbumUri", "com.adamratzman.spotify.models.AlbumUri"))
typealias AlbumURI = AlbumUri

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class ArtistUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : SpotifyUri(_input, "artist")
@Deprecated("renamed", ReplaceWith("ArtistUri", "com.adamratzman.spotify.models.ArtistUri"))
typealias ArtistURI = ArtistUri

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class UserUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : SpotifyUri(_input, "user")

@Deprecated("renamed", ReplaceWith("UserUri", "com.adamratzman.spotify.models.UserUri"))
typealias UserURI = UserUri

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
class PlaylistUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : SpotifyUri(_input, "playlist") {
    override fun equals(other: Any?): Boolean {
        val spotifyUri = other as? SpotifyUri ?: return (other as? String)?.endsWith(this.uri.removePrefix("spotify")) ?: false
        return spotifyUri.uri == this.uri
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + _input.hashCode()
        return result
    }
}

@Deprecated("renamed", ReplaceWith("PlaylistUri", "com.adamratzman.spotify.models.PlaylistUri"))
typealias PlaylistURI = PlaylistUri

/**
 * Represents a Spotify **Track** URI, ether LocalTrack or SpotifyTrack, parsed from either a Spotify ID or taken
 * from an endpoint
 * */
@Serializable
sealed class TrackUri(
    @Transient private val _input: String = TRANSIENT_EMPTY_STRING,
    @Transient private val _type: String = TRANSIENT_EMPTY_STRING
) :
    SpotifyUri(_input, _type) {

    @Serializer(forClass = TrackUri::class)
    companion object {
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
class SpotifyTrackUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : TrackUri(_input, "track")

/**
 * Represents a Spotify **local track** URI
 */
@Serializable
class LocalTrackUri(@Transient private val _input: String = TRANSIENT_EMPTY_STRING) : TrackUri(_input, "local")

@Deprecated("renamed", ReplaceWith("LocalTrackUri", "com.adamratzman.spotify.models.LocalTrackUri"))
typealias LocalTrackURI = LocalTrackUri
