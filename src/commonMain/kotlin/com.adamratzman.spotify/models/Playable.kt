/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.Language
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonOutput

@Serializable
internal data class InternalPlayable(
        // core properties that should be send everywhere
    val href: String?,
    val id: String?,
    val type: String,
    val uri: PlayableUri,

        // track stuff
    val album: SimpleAlbum? = null,
    val artists: List<SimpleArtist> = emptyList(),
    val explicit: Boolean? = null,
    val name: String? = null,
    val popularity: Int? = null,
    val restrictions: Restrictions? = null,
    val episode: Boolean? = null,
    val track: Boolean? = null,

    @SerialName("external_urls") val externalUrlsString: Map<String, String> = emptyMap(),
    @SerialName("external_ids") val externalIdsString: Map<String, String> = hashMapOf(),
    @SerialName("available_markets") val availableMarketsString: List<String> = emptyList(),
    @SerialName("is_local") val isLocal: Boolean? = null,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("track_number") val trackNumber: Int? = null,
    @SerialName("linked_from") val linkedTrack: LinkedTrack? = null,
    @SerialName("is_playable") val isPlayable: Boolean? = null,
    @SerialName("disc_number") val discNumber: Int? = null,
    @SerialName("duration_ms") val durationMs: Int? = null,

        // episode stuff
    @Deprecated("use languages instead") val language: Language? = null,

    val description: String? = null,
    val images: List<SpotifyImage> = emptyList(),
    val languages: List<Language> = mutableListOf(),
    val resumePoint: String? = null,
    val show: SimpleShow? = null,

    @SerialName("audio_preview_url") val audioPreviewUrl: String? = null,
    @SerialName("is_externally_hosted") val isExternallyHosted: Boolean? = null,
    @SerialName("release_date_precision") val releaseDatePrecision: String? = null,
    @SerialName("release_date") val releaseDateString: String? = null
) {
    init {
        @Suppress("DEPRECATION")
        if (language != null && languages.isEmpty()) (languages as MutableList<Language>).add(language)
    }

    fun toTrack(): Track? {
        return Track(
                uri = uri as? SpotifyTrackUri ?: return null,

                artists = artists,
                availableMarketsString = availableMarketsString,
                episode = episode,
                externalUrlsString = externalUrlsString,
                externalIdsString = externalIdsString,
                isLocal = isLocal,
                linkedTrack = linkedTrack,
                previewUrl = previewUrl,
                restrictions = restrictions,
                track = track,
                type = type,

                isPlayable = isPlayable ?: true,

                album = album ?: return null,
                discNumber = discNumber ?: return null,
                durationMs = durationMs ?: return null,
                explicit = explicit ?: return null,
                href = href ?: return null,
                id = id ?: return null,
                name = name ?: return null,
                popularity = popularity ?: return null,
                trackNumber = trackNumber ?: return null
        )
    }

    fun toLocalTrack(): LocalTrack? {
        return LocalTrack(
                uri = uri as? LocalTrackUri ?: return null,

                artists = artists,
                type = type,

                isLocal = isLocal ?: true,

                name = name ?: return null,
                album = album ?: return null,
                durationMs = durationMs ?: return null
        )
    }

    fun toEpisode(): Episode? {
        return Episode(
                uri = uri as? EpisodeUri ?: return null,

                audioPreviewUrl = audioPreviewUrl,
                availableMarketsString = availableMarketsString,
                externalUrlsString = externalUrlsString,
                images = images,
                languages = languages,
                resumePoint = resumePoint,
                type = type,

                description = description ?: return null,
                durationMs = durationMs ?: return null,
                explicit = explicit ?: return null,
                href = href ?: return null,
                id = id ?: return null,
                isExternallyHosted = isExternallyHosted ?: return null,
                isPlayable = isPlayable ?: return null,
                name = name ?: return null,
                releaseDatePrecision = releaseDatePrecision ?: return null,
                releaseDateString = releaseDateString ?: return null,
                show = show ?: return null
        )
    }
}

private class PlayableSerializer<T : Playable>(val type: String, val toT: InternalPlayable.() -> T?) : KSerializer<T> {
    private val serializer = InternalPlayable.serializer()

    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): T = serializer.deserialize(decoder).toT()
            ?: throw SerializationException("Could not convert internal playable representation to '$type'")

    override fun serialize(encoder: Encoder, value: T) {
        encoder as? JsonOutput ?: throw SerializationException("This class can only be serialized to json")
        encoder.encodeJson(encoder.json.toJson(serializer, value.toInternalPlayable()))
    }
}

@Serializable
sealed class Playable(
    open val href: String? = null,
    open val id: String? = null,
    open val uri: PlayableUri
) {
    internal abstract fun toInternalPlayable(): InternalPlayable

    companion object : KSerializer<Playable> by PlayableSerializer("Playable", {
        toTrack() ?: toLocalTrack() ?: toEpisode()
    })
}

@Serializable
data class Track(
    override val href: String,
    override val id: String,
    override val uri: SpotifyTrackUri,

    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    val isPlayable: Boolean = true,
    val discNumber: Int,
    val durationMs: Int,
    val explicit: Boolean,
    val linkedTrack: LinkedTrack? = null,
    val name: String,
    val popularity: Int,
    val previewUrl: String? = null,
    val trackNumber: Int,
    val type: String,
    val isLocal: Boolean? = null,
    val restrictions: Restrictions? = null,
    val episode: Boolean? = null,
    val track: Boolean? = null,

    private val externalUrlsString: Map<String, String>,
    private val externalIdsString: Map<String, String> = hashMapOf(),
    private val availableMarketsString: List<String> = listOf()
) : Playable(href, id, uri) {
    override fun toInternalPlayable(): InternalPlayable {
        return InternalPlayable(
                href = href,
                id = id,
                type = type,
                uri = uri,
                album = album,
                artists = artists,
                explicit = explicit,
                name = name,
                popularity = popularity,
                restrictions = restrictions,
                episode = episode,
                track = track,
                externalUrlsString = externalUrlsString,
                externalIdsString = externalIdsString,
                availableMarketsString = availableMarketsString,
                isLocal = isLocal,
                previewUrl = previewUrl,
                trackNumber = trackNumber,
                linkedTrack = linkedTrack,
                isPlayable = isPlayable,
                discNumber = discNumber,
                durationMs = durationMs
        )
    }

    @Serializer(forClass = Track::class)
    companion object : KSerializer<Track> by PlayableSerializer("Track", InternalPlayable::toTrack)
}

@Serializable
data class LocalTrack(
    override val uri: LocalTrackUri,

    val name: String,
    val type: String,
    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    val durationMs: Int,
    val isLocal: Boolean = true
) : Playable(uri = uri) {
    override fun toInternalPlayable(): InternalPlayable {
        return InternalPlayable(
                null,
                null,
                type,
                uri,
                album,
                artists,
                durationMs = durationMs,
                isLocal = isLocal,
                name = name

        )
    }

    @Serializer(forClass = LocalTrack::class)
    companion object : KSerializer<LocalTrack> by PlayableSerializer("LocalTrack", InternalPlayable::toLocalTrack)
}

@Serializable
data class Episode(
    override val href: String,
    override val id: String,
    override val uri: EpisodeUri,

    val show: SimpleShow,
    val audioPreviewUrl: String? = null,
    val description: String,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrlsString: Map<String, String>,
    val images: List<SpotifyImage>,
    val isExternallyHosted: Boolean,
    val isPlayable: Boolean,
    val languages: List<Language>,
    val name: String,
    private val releaseDateString: String,
    private val releaseDatePrecision: String,
    val resumePoint: String? = null,
    val type: String,

    private val availableMarketsString: List<String>
) : Playable(href, id, uri) {
    override fun toInternalPlayable(): InternalPlayable {
        return InternalPlayable(
                href = href,
                id = id,
                uri = uri,
                show = show,
                audioPreviewUrl = audioPreviewUrl,
                description = description,
                durationMs = durationMs,
                explicit = explicit,
                externalUrlsString = externalUrlsString,
                images = images,
                isExternallyHosted = isExternallyHosted,
                isPlayable = isPlayable,
                languages = languages,
                name = name,
                releaseDateString = releaseDateString,
                releaseDatePrecision = releaseDatePrecision,
                resumePoint = resumePoint,
                type = type
        )
    }

    @Serializer(forClass = Episode::class)
    companion object : KSerializer<Episode> by PlayableSerializer("Episode", InternalPlayable::toEpisode)
}

@Serializable
data class SimpleShow(
    val uri: ShowUri
)

@Serializable
sealed class SimplePlayable(
    val href: String?,
    val id: String?,
    val uri: PlayableUri
)
