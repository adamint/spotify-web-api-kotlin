/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Exception instantiating or deserializing a uri perceived as invalid
 */
public class SpotifyUriException(message: String) : SpotifyException.BadRequestException(message)

private fun String.matchType(type: String, allowColon: Boolean): String? {
    val uriContent = "[^:]".takeUnless { allowColon } ?: "."
    val typeRegex = "^spotify:(?:.*:)*$type:($uriContent*)(?::.*)*$|^([^:]+)$".toRegex()
    val match = typeRegex.matchEntire(this)?.groupValues ?: return null
    return match[1].takeIf { it.isNotBlank() || match[2].isEmpty() } ?: match[2].takeIf { it.isNotEmpty() }
}

private fun String.add(type: String, allowColon: Boolean): String {
    this.matchType(type, allowColon)?.let {
        return "spotify:$type:${it.trim()}"
    }
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' uri")
}

private fun String.remove(type: String, allowColon: Boolean): String {
    this.matchType(type, allowColon)?.let {
        return it.trim()
    }
    throw SpotifyUriException("Illegal Spotify ID/URI: '$this' isn't convertible to '$type' id")
}

private class SimpleUriSerializer<T : SpotifyUri>(val ctor: (String) -> T) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SimpleUri", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): T = ctor(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.uri)
}

/**
 * Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint.
 *
 * @param type The type (per Spotify) corresponding to the Uri.
 *
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
@Serializable
public sealed class SpotifyUri(input: String, public val type: String, allowColon: Boolean = false) {
    public val uri: String
    public val id: String

    init {
        input.replace(" ", "").also {
            this.uri = it.add(type, allowColon)
            this.id = it.remove(type, allowColon)
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
        return "SpotifyUri(type=$type, uri=$uri)"
    }

    // TODO replace serialization with JSON specific code
    @Serializer(forClass = SpotifyUri::class)
    public companion object : KSerializer<SpotifyUri> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SpotifyUri", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): SpotifyUri = SpotifyUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: SpotifyUri): Unit = encoder.encodeString(value.uri)

        /**
         * This function safely instantiates a SpotifyUri from given constructor.
         * */
        public inline fun <T : SpotifyUri> safeInitiate(uri: String, ctor: (String) -> T): T? {
            return try {
                ctor(uri)
            } catch (e: SpotifyUriException) {
                null
            }
        }

        /**
         * Creates a abstract SpotifyUri of given input. Doesn't allow ambiguity by disallowing creation by id.
         * */
        public operator fun invoke(input: String): SpotifyUri {
            val constructors = listOf(
                ::ArtistUri,
                PlayableUri.Companion::invoke,
                ImmutableCollectionUri.Companion::invoke,
                ::UserUri,
                ::PlaylistUri
            )
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
        public inline fun <reified T : SpotifyUri> isType(input: String): Boolean {
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
        public inline fun <reified T : SpotifyUri> canBeType(input: String): Boolean {
            return isType<T>(input) || !input.contains(':')
        }
    }
}

@Serializable
public sealed class CollectionUri(input: String, type: String, allowColon: Boolean = false) :
    SpotifyUri(input, type, allowColon) {
    @Serializer(forClass = CollectionUri::class)
    public companion object : KSerializer<CollectionUri> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CollectionUri", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): CollectionUri = CollectionUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: CollectionUri): Unit = encoder.encodeString(value.uri)

        public operator fun invoke(input: String): CollectionUri {
            val constructors = listOf(::PlaylistUri, ImmutableCollectionUri.Companion::invoke)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'playlist' or 'predefinedCollection' id")
        }
    }
}

@Serializable
public sealed class ImmutableCollectionUri(input: String, type: String, allowColon: Boolean = false) :
    CollectionUri(input, type, allowColon) {
    @Serializer(forClass = ImmutableCollectionUri::class)
    public companion object : KSerializer<ImmutableCollectionUri> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ImmutableCollection", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): ImmutableCollectionUri =
            ImmutableCollectionUri(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: ImmutableCollectionUri): Unit = encoder.encodeString(value.uri)

        public operator fun invoke(input: String): ImmutableCollectionUri {
            val constructors = listOf(::AlbumUri, ::ShowUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'album' or 'show' id")
        }
    }
}

@Serializable
public sealed class PlayableUri(input: String, type: String, allowColon: Boolean = false) :
    SpotifyUri(input, type, allowColon) {
    @Serializer(forClass = PlayableUri::class)
    public companion object : KSerializer<PlayableUri> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlayableUri", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): PlayableUri = PlayableUri(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: PlayableUri): Unit = encoder.encodeString(value.uri)

        /**
         * Creates a abstract TrackURI of given input. Prefers SpotifyTrackUri if the input is ambiguous.
         * */
        public operator fun invoke(input: String): PlayableUri {
            val constructors = listOf(::SpotifyTrackUri, ::LocalTrackUri, ::EpisodeUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'track' or 'localTrack' or 'episode' id")
        }
    }
}

/**
 * Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
public class AlbumUri(input: String) : ImmutableCollectionUri(input, "album") {
    @Serializer(forClass = AlbumUri::class)
    public companion object : KSerializer<AlbumUri> by SimpleUriSerializer(::AlbumUri)
}

@Serializable
public class ShowUri(input: String) : ImmutableCollectionUri(input, "show") {
    @Serializer(forClass = ShowUri::class)
    public companion object : KSerializer<ShowUri> by SimpleUriSerializer(::ShowUri)
}

/**
 * Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
public class ArtistUri(input: String) : SpotifyUri(input, "artist") {
    @Serializer(forClass = ArtistUri::class)
    public companion object : KSerializer<ArtistUri> by SimpleUriSerializer(::ArtistUri)
}

/**
 * Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
public class UserUri(input: String) : SpotifyUri(input, "user") {
    @Serializer(forClass = UserUri::class)
    public companion object : KSerializer<UserUri> by SimpleUriSerializer(::UserUri)
}

/**
 * Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
public class PlaylistUri(input: String) : CollectionUri(input, "playlist") {
    @Serializer(forClass = PlaylistUri::class)
    public companion object : KSerializer<PlaylistUri> by SimpleUriSerializer(::PlaylistUri)
}

/**
 * Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable
public class SpotifyTrackUri(input: String) : PlayableUri(input, "track") {
    @Serializer(forClass = SpotifyTrackUri::class)
    public companion object : KSerializer<SpotifyTrackUri> by SimpleUriSerializer(::SpotifyTrackUri)
}

/**
 * Represents a Spotify **local track** URI
 */
@Serializable
public class LocalTrackUri(input: String) : PlayableUri(input, "local", allowColon = true) {
    @Serializer(forClass = LocalTrackUri::class)
    public companion object : KSerializer<LocalTrackUri> by SimpleUriSerializer(::LocalTrackUri)
}

@Serializable
public class EpisodeUri(input: String) : PlayableUri(input, "episode") {
    @Serializer(forClass = EpisodeUri::class)
    public companion object : KSerializer<EpisodeUri> by SimpleUriSerializer(::EpisodeUri)
}
