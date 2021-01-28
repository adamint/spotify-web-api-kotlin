/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
    val typeRegex = "^spotify:(?:.*:)?$type:($uriContent*)(?::.*)*$|^([^:]+)\$".toRegex()
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
    override fun deserialize(decoder: Decoder): T {
        val str = decoder.decodeString()
        return ctor(str)
    }

    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.uri)
}

/**
 * @property uri retrieve this URI as a string
 * @property id representation of this uri as an id
 */
public interface ISpotifyUri {
    public val uri: String
    public val id: String
}

/**
 * Represents any Spotify **URI** (one of [ArtistUri], [PlayableUri], [ImmutableCollectionUri], [UserUri], [PlaylistUri]),
 * parsed from either a Spotify ID or taken from an endpoint.
 *
 * @param type The type (per Spotify) corresponding to the Uri.
 *
 */
@Serializable(with = SpotifyUriSerializer::class)
public sealed class SpotifyUri(input: String, public val type: String, allowColon: Boolean = false) : ISpotifyUri {
    public override val uri: String
    public override val id: String

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

    public companion object {
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
            val inputUriModified = input.removeSuffix(":recommended")

            val constructors = listOf(
                ::ArtistUri,
                PlayableUri.Companion::invoke,
                ImmutableCollectionUri.Companion::invoke,
                ::UserUri,
                ::PlaylistUri
            )
            for (ctor in constructors) {
                safeInitiate(inputUriModified, ctor)?.takeIf { it.uri == inputUriModified }?.also { return it }
            }

            throw SpotifyUriException("Illegal Spotify ID/URI: '$inputUriModified' isn't convertible to any arbitrary id")
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

/**
 * Convert any (artist, [PlayableUri], [ImmutableCollectionUri], user, playlist) uri string to a [SpotifyUri] object.
 * Ambiguity is not allowed.
 *
 * *Note*: it is preferable to use a more specific function ([toArtistUri], [toPlayableUri], [toImmutableCollectionUri], [toUserUri], [toPlaylistUri]) if possible.
 */
public fun String.toSpotifyUri(): SpotifyUri = SpotifyUri(this)

// TODO replace serialization with JSON specific code
public object SpotifyUriSerializer : KSerializer<SpotifyUri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SpotifyUri", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): SpotifyUri = SpotifyUri(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: SpotifyUri): Unit = encoder.encodeString(value.uri)
}

/**
 * Represents a Spotify **Collection** URI (one of [PlaylistUri] or [ImmutableCollectionUri]),
 * parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = CollectionUriSerializer::class)
public sealed class CollectionUri(input: String, type: String, allowColon: Boolean = false) :
    SpotifyUri(input, type, allowColon) {
    public companion object {
        /**
         * Creates an abstract [CollectionUri] of given input. Prefers [PlaylistUri] if the input is ambiguous.
         */
        public operator fun invoke(input: String): CollectionUri {
            val constructors = listOf(::PlaylistUri, ImmutableCollectionUri.Companion::invoke)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'playlist' or 'predefinedCollection' id")
        }
    }
}

/**
 * Convert a collection (playlist/[ImmutableCollectionUri]) id or uri string to an [ImmutableCollectionUri] object.
 * If an id is provided or the input is ambiguous, [PlaylistUri] is preferred.
 *
 * *Note*: it is preferable to use a more specific function ([toPlaylistUri], [toImmutableCollectionUri]) if possible.
 */
public fun String.toCollectionUri(): CollectionUri = CollectionUri(this)

public object CollectionUriSerializer : KSerializer<CollectionUri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CollectionUri", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): CollectionUri {
        return CollectionUri(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: CollectionUri): Unit = encoder.encodeString(value.uri)
}

/**
 * Represents a Spotify **Immutable Collection** URI (one of [AlbumUri] or [ShowUri]),
 * parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = ImmutableCollectionUriSerializer::class)
public sealed class ImmutableCollectionUri(input: String, type: String, allowColon: Boolean = false) :
    CollectionUri(input, type, allowColon) {
    public companion object {
        /**
         * Creates an abstract [ImmutableCollectionUri] of given input. Prefers [AlbumUri] if the input is ambiguous.
         */
        public operator fun invoke(input: String): ImmutableCollectionUri {
            val constructors = listOf(::AlbumUri, ::ShowUri)
            for (ctor in constructors) {
                safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'album' or 'show' id")
        }
    }
}

/**
 * Convert an immutable collection (album/show) id or uri string to an [ImmutableCollectionUri] object.
 * If an id is provided or the input is ambiguous, [AlbumUri] is preferred.
 *
 * *Note*: it is preferable to use a more specific function ([toAlbumUri], [toShowUri]) if possible.
 */
public fun String.toImmutableCollectionUri(): ImmutableCollectionUri = ImmutableCollectionUri(this)

public object ImmutableCollectionUriSerializer : KSerializer<ImmutableCollectionUri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ImmutableCollection", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): ImmutableCollectionUri =
        ImmutableCollectionUri(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ImmutableCollectionUri): Unit = encoder.encodeString(value.uri)
}

/**
 * Represents a Spotify **Playable** URI (one of [SpotifyTrackUri], [LocalTrackUri], or [EpisodeUri]),
 * parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = PlayableUriSerializer::class)
public sealed class PlayableUri(input: String, type: String, allowColon: Boolean = false) :
    SpotifyUri(input, type, allowColon) {
    public companion object {
        /**
         * Creates an abstract [PlayableUri] of given input. Prefers [SpotifyTrackUri] if the input is ambiguous.
         */
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
 * Convert a playable (track/local track/episode) id or uri string to a [PlayableUri] object.
 * If an id is provided or the input is ambiguous, [SpotifyTrackUri] is preferred.
 *
 * *Note*: it is preferable to use a more specific function ([toTrackUri], [toLocalTrackUri], [toEpisodeUri]) if possible.
 */
public fun String.toPlayableUri(): PlayableUri = PlayableUri(this)

public object PlayableUriSerializer : KSerializer<PlayableUri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlayableUri", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): PlayableUri = PlayableUri(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: PlayableUri): Unit = encoder.encodeString(value.uri)
}

/**
 * Represents a Spotify **Album** URI (spotify:album:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = AlbumUriSerializer::class)
public class AlbumUri(input: String) : ImmutableCollectionUri(input, "album"), ContextUri
public object AlbumUriSerializer : KSerializer<AlbumUri> by SimpleUriSerializer(::AlbumUri)

/**
 * Convert an album id or uri string to an [AlbumUri] object
 */
public fun String.toAlbumUri(): AlbumUri = AlbumUri(this)

/**
 * Represents a Spotify **Artist** URI (spotify:artist:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = ArtistUriSerializer::class)
public class ArtistUri(input: String) : SpotifyUri(input, "artist"), ContextUri
public object ArtistUriSerializer : KSerializer<ArtistUri> by SimpleUriSerializer(::ArtistUri)

/**
 * Convert an artist id or uri string to an [ArtistUri] object
 */
public fun String.toArtistUri(): ArtistUri = ArtistUri(this)

/**
 * Represents a Spotify **User** URI (spotify:user:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = UserUriSerializer::class)
public class UserUri(input: String) : SpotifyUri(input, "user")
public object UserUriSerializer : KSerializer<UserUri> by SimpleUriSerializer(::UserUri)

/**
 * Convert a user id or uri string to a [UserUri] object
 */
public fun String.toUserUri(): UserUri = UserUri(this)

/**
 * Represents a Spotify **Playlist** URI (spotify:playlist:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = PlaylistUriSerializer::class)
public class PlaylistUri(input: String) : CollectionUri(input, "playlist"), ContextUri
public object PlaylistUriSerializer : KSerializer<PlaylistUri> by SimpleUriSerializer(::PlaylistUri)

/**
 * Convert a playlist id or uri string to a [PlaylistUri] object
 */
public fun String.toPlaylistUri(): PlaylistUri = PlaylistUri(this)

/**
 * Represents a Spotify **Track** URI (spotify:track:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = SpotifyTrackUriSerializer::class)
public class SpotifyTrackUri(input: String) : PlayableUri(input, "track")
public object SpotifyTrackUriSerializer : KSerializer<SpotifyTrackUri> by SimpleUriSerializer(::SpotifyTrackUri)

/**
 * Convert a track (non-local) id or uri string to a [SpotifyTrackUri] object
 */
public fun String.toTrackUri(): SpotifyTrackUri = SpotifyTrackUri(this)

/**
 * Represents a Spotify **Local Track** URI (spotify:local:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = LocalTrackUriSerializer::class)
public class LocalTrackUri(input: String) : PlayableUri(input, "local", allowColon = true)
public object LocalTrackUriSerializer : KSerializer<LocalTrackUri> by SimpleUriSerializer(::LocalTrackUri)

/**
 * Convert a local track id or uri string to a [LocalTrackUri] object
 */
public fun String.toLocalTrackUri(): LocalTrackUri = LocalTrackUri(this)

/**
 * Represents a Spotify **Episode** URI (spotify:episode:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = EpisodeUriSerializer::class)
public class EpisodeUri(input: String) : PlayableUri(input, "episode")
public object EpisodeUriSerializer : KSerializer<EpisodeUri> by SimpleUriSerializer(::EpisodeUri)

/**
 * Convert an episode id or uri string to an [EpisodeUri] object
 */
public fun String.toEpisodeUri(): EpisodeUri = EpisodeUri(this)

/**
 * Represents a Spotify **Show** URI (spotify:show:XXXXXXXXXX), parsed from either a Spotify ID or taken from an endpoint.
 */
@Serializable(with = ShowUriSerializer::class)
public class ShowUri(input: String) : ImmutableCollectionUri(input, "show"), ContextUri
public object ShowUriSerializer : KSerializer<ShowUri> by SimpleUriSerializer(::ShowUri)

/**
 * Convert a show id or uri string to a [ShowUri] object
 */
public fun String.toShowUri(): ShowUri = ShowUri(this)

/**
 * Represents a Spotify **Context** URI (one of [AlbumUri], [ArtistUri], [PlaylistUri], or [ShowUri]),
*/
@Serializable(with = ContextUriSerializer::class)
public interface ContextUri : ISpotifyUri {
    public companion object {
        /**
         * Creates an abstract [ContextUri] of given input. Prefers [PlaylistUri] if the input is ambiguous.
         */
        public operator fun invoke(input: String): ContextUri {
            val constructors = listOf(::PlaylistUri, ::AlbumUri, ::ArtistUri, ::ShowUri)
            for (ctor in constructors) {
                SpotifyUri.safeInitiate(input, ctor)?.also { return it }
            }
            throw SpotifyUriException("Illegal Spotify ID/URI: '$input' isn't convertible to 'playlist' or 'album' or 'artist' or 'show' id")
        }
    }
}

/**
 * Convert any (artist, album, playlist, or show) uri string to a [ContextUri] object.
 *
 * *Note*: it is preferable to use a more specific function ([toPlaylistUri], [toAlbumUri], [toArtistUri], [toShowUri]) if possible.
 */
public fun String.toContextUri(): ContextUri = ContextUri(this)

public object ContextUriSerializer : KSerializer<ContextUri> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlayableUri", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): ContextUri = ContextUri(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: ContextUri): Unit = encoder.encodeString(value.uri)
}
