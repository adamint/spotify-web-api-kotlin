/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * A Playable object represents a [Track], [Episode], or [LocalTrack] that has been returned by the Spotify api.
 *
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property uri The URI associated with the object
 * @property type The object type
 */
interface Playable {
    val href: String?
    val id: String?
    val uri: PlayableUri
    val type: String

    @Suppress("EXPERIMENTAL_API_USAGE")
    @Serializer(forClass = Playable::class)
    companion object : KSerializer<Playable> by object : JsonContentPolymorphicSerializer<Playable>(Playable::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Playable> {
            val uri: PlayableUri? = (element as? JsonObject)?.get("uri")
                    ?.jsonPrimitive
                    ?.contentOrNull
                    ?.let { PlayableUri(it) }

            return when (uri) {
                is LocalTrackUri -> LocalTrack.serializer()
                is EpisodeUri -> Episode.serializer()
                is SpotifyTrackUri -> Track.serializer()
                null -> throw IllegalStateException("Couldn't find a serializer for uri $uri")
            }
        }
    }

    /**
     * This Playable object as a [LocalTrack]
     */
    val asLocalTrack get() = this as LocalTrack

    /**
     * This Playable object as a [Track]
     */
    val asTrack get() = this as Track

    /**
     * This Playable object as an [Episode]
     */
    val asEpisode get() = this as Episode
}
