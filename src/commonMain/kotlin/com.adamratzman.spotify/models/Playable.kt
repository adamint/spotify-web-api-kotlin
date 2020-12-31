/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

public interface Playable {
    public val href: String?
    public val id: String?
    public val uri: PlayableUri
    public val type: String

    @Suppress("EXPERIMENTAL_API_USAGE")
    @Serializer(forClass = Playable::class)
    public companion object : KSerializer<Playable> by object : JsonContentPolymorphicSerializer<Playable>(Playable::class) {
        override fun selectDeserializer(element: JsonElement): KSerializer<out Playable> {

            return when (val uri: PlayableUri? = (element as? JsonObject)?.get("uri")?.jsonPrimitive?.contentOrNull?.let { PlayableUri(it) }) {
                is LocalTrackUri -> LocalTrack.serializer()
                is EpisodeUri -> Episode.serializer()
                is SpotifyTrackUri -> Track.serializer()
                null -> throw IllegalStateException("Couldn't find a serializer for uri $uri")
            }
        }
    }
}
