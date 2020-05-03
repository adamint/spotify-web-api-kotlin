/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonParametricSerializer
import kotlinx.serialization.json.contentOrNull

interface Playable {
    val href: String?
    val id: String?
    val uri: PlayableUri
    val type: String
}

object PlayableTransformingSerializer : JsonParametricSerializer<Playable>(Playable::class) {
    private val spotifyLocalUriRegex = "spotify:local:.+".toRegex()

    override fun selectSerializer(element: JsonElement): KSerializer<out Playable> {
        element as JsonObject
        return when {
            element["uri"]?.contentOrNull?.matches(spotifyLocalUriRegex) == true -> LocalTrack.serializer()
            else -> Track.serializer()
        }
    }
}

@Serializable
data class SimpleLocalAlbum(
        @SerialName("album_type") private val albumTypeString: String? = null,
        val artists: List<SimpleLocalArtist> = listOf(),
        val name: String,
        @SerialName("release_date") private val releaseDate: String? = null,
        @SerialName("release_date_precision") val releaseDatePrecisionString: String? = null,
        val type: String
)

@Serializable
data class SimpleLocalArtist(
        val name: String,
        val type: String
)

@Serializable
data class LocalTrack(
        val album: SimpleLocalAlbum,
        val artists: List<SimpleLocalArtist>,
        override val href: String? = null,
        override val id: String? = null,
        @SerialName("disc_number") val discNumber: String? = null,
        @SerialName("duration_ms") val durationMs: Int? = null,
        @SerialName("explicit") val explicit: Boolean? = null,
        @SerialName("is_local") val isLocal: Boolean = true,
        val name: String,
        val popularity: Int? = null,
        @SerialName("track_number") val trackNumber: Int? = null,
        override val type: String,
        override val uri: LocalTrackUri
) : IdentifiableNullable(), Playable

/*
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
        override val type: String,

        private val availableMarketsString: List<String>
) : Playable()

@Serializable
data class SimpleShow(
        val uri: ShowUri
)*/