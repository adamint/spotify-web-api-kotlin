/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
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
public interface Playable {
    public val href: String?
    public val id: String?
    public val uri: PlayableUri
    public val type: String

    /**
     * This Playable as a local track.
     *
     */
    public val asLocalTrack: LocalTrack? get() = this as? LocalTrack

    /**
     * This Playable as an episode.
     *
     */
    public val asPodcastEpisodeTrack: PodcastEpisodeTrack? get() = this as? PodcastEpisodeTrack

    /**
     * This Playable as a track.
     *
     */
    public val asTrack: Track? get() = this as? Track

    @Suppress("EXPERIMENTAL_API_USAGE")
    @Serializer(forClass = Playable::class)
    public companion object : KSerializer<Playable> by object : JsonContentPolymorphicSerializer<Playable>(Playable::class) {
        override fun selectDeserializer(element: JsonElement): KSerializer<out Playable> {
            return when (val uri: PlayableUri? = (element as? JsonObject)?.get("uri")?.jsonPrimitive?.contentOrNull?.let { PlayableUri(it) }) {
                is LocalTrackUri -> LocalTrack.serializer()
                is EpisodeUri -> PodcastEpisodeTrack.serializer()
                is SpotifyTrackUri -> Track.serializer()
                null -> throw IllegalStateException("Couldn't find a serializer for uri $uri")
            }
        }
    }
}
