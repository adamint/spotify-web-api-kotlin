/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
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
    override fun selectSerializer(element: JsonElement): KSerializer<out Playable> {
        val uri = (element as? JsonObject)?.get("uri")?.contentOrNull?.let { PlayableUri(it) }
        return when (uri) {
            is LocalTrackUri -> LocalTrack.serializer()
            else -> Track.serializer()
        }
    }
}

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
