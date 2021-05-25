/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * A local track, episode, or track.
 *
 * @property href A link to the Web API endpoint providing full details of the playable.
 * @property id The Spotify ID for the playable.
 * @property uri The URI associated with the object.
 * @property type The type of the playable.
 */
@Serializable(with = PlayableSerializer::class)
public interface Playable {
    public val href: String?
    public val id: String?
    public val uri: PlayableUri
    public val type: String

    /**
     * This Playable as a local track, or else null if it is an episode or track.
     *
     */
    public val asLocalTrack: LocalTrack? get() = this as? LocalTrack

    /**
     * This Playable as an episode (podcast), or else null if it is a local track or track.
     *
     */
    public val asPodcastEpisodeTrack: PodcastEpisodeTrack? get() = this as? PodcastEpisodeTrack

    /**
     * This Playable as a track, or else null if it is a local track or episode (podcast).
     *
     */
    public val asTrack: Track? get() = this as? Track
}

public object PlayableSerializer :
    KSerializer<Playable> by object : JsonContentPolymorphicSerializer<Playable>(Playable::class) {
        override fun selectDeserializer(element: JsonElement): KSerializer<out Playable> {
            return when (val uri: PlayableUri? =
                (element as? JsonObject)?.get("uri")?.jsonPrimitive?.contentOrNull?.let { PlayableUri(it) }) {
                is LocalTrackUri -> LocalTrack.serializer()
                is EpisodeUri -> PodcastEpisodeTrack.serializer()
                is SpotifyTrackUri -> Track.serializer()
                null -> throw IllegalStateException("Couldn't find a serializer for uri $uri")
            }
        }
    }
