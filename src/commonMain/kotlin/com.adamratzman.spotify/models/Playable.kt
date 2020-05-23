/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonParametricSerializer
import kotlinx.serialization.json.contentOrNull

interface Playable {
    val href: String?
    val id: String?
    val uri: PlayableUri
    val type: String

    @Serializer(forClass = Playable::class)
    companion object : KSerializer<Playable> by object : JsonParametricSerializer<Playable>(Playable::class) {
        override fun selectSerializer(element: JsonElement): KSerializer<out Playable> {
            val uri: PlayableUri? = (element as? JsonObject)?.get("uri")?.contentOrNull?.let { PlayableUri(it) }

            return when (uri) {
                is LocalTrackUri -> LocalTrack.serializer()
                is EpisodeUri -> Episode.serializer()
                is SpotifyTrackUri -> Track.serializer()
                null -> throw IllegalStateException("Couldn't find a serializer for uri $uri")
            }
        }
    }
}
