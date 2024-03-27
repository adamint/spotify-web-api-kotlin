/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.utils.getExternalUrls
import com.adamratzman.spotify.utils.match
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Context in which a track was played
 *
 * @param uri The Spotify URI for the context.
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param typeString The object type, e.g. “artist”, “playlist”, “album”, “show”.
 *
 * @property type The object type, e.g. “artist”, “playlist”, “album”, “show”.
 * @property externalUrls Known external URLs for this object
 */
@Serializable
public data class SpotifyContext(
    @SerialName("external_urls") private val externalUrlsString: Map<String, String>,
    val href: String,
    val uri: ContextUri,
    @SerialName("type") val typeString: String
) {
    val type: SpotifyContextType get() = SpotifyContextType.entries.toTypedArray().match(typeString)!!
    val externalUrls: List<ExternalUrl> get() = getExternalUrls(externalUrlsString)
}

/**
 * Information about a previously-played track
 *
 * @param track The track the user listened to.
 * @param playedAt The date and time the track was played.
 * @param context The context the track was played from.
 */
@Serializable
public data class PlayHistory(
    val track: Track,
    @SerialName("played_at") val playedAt: String,
    val context: SpotifyContext? = null
)

/**
 * A device which is connected to the Spotify user
 *
 * @param id The device ID. This may be null.
 * @param isActive If this device is the currently active device.
 * @param isPrivateSession If this device is currently in a private session.
 * @param isRestricted Whether controlling this device is restricted. At present
 * if this is “true” then no Web API commands will be accepted by this device.
 * @param name The name of the device.
 *
 * @property type Device type, such as “Computer”, “Smartphone” or “Speaker”.
 */
@Serializable
public data class Device(
    override val id: String? = null,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("is_private_session") val isPrivateSession: Boolean,
    @SerialName("is_restricted") val isRestricted: Boolean,
    val name: String,
    @SerialName("type") val typeString: String,
    @SerialName("volume_percent") val volumePercent: Int
) : IdentifiableNullable() {
    val type: DeviceType get() = DeviceType.entries.first { it.identifier.equals(typeString, true) }

    override val href: String? = null
}

/**
 * Electronic type of registered Spotify device
 *
 * @param identifier readable name
 */
public enum class DeviceType(public val identifier: String) {
    Computer("Computer"),
    Tablet("Tablet"),
    Smartphone("Smartphone"),
    Speaker("Speaker"),
    Tv("TV"),
    Avr("AVR"),
    Stb("STB"),
    AudioDongle("AudioDongle"),
    GameConsole("GameConsole"),
    CastVideo("CastVideo"),
    CastAudio("CastAudio"),
    Automobile("Automobile"),
    Unknown("Unknown");
}

/**
 * Information about the current playback
 *
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param device The device that is currently active
 * @param progressMs Progress into the currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @param isPlaying If something is currently playing.
 * @param item The currently playing item (track or episode). Can be null (e.g. If private session is enabled this will be null).
 * @param context A Context Object. Can be null (e.g. If private session is enabled this will be null).
 * *Note*: this will likely be null when playing the first track in a playlist or show context.
 * @param shuffleState If shuffle is on or off
 *
 * @property repeatState If and how the playback is repeating
 */
@Serializable
public data class CurrentlyPlayingContext(
    val timestamp: Long,
    val device: Device,
    @SerialName("progress_ms") val progressMs: Int? = null,
    @SerialName("is_playing") val isPlaying: Boolean,
    @Serializable(with = PlayableSerializer::class)
    @SerialName("item")
    val item: Playable? = null,
    @SerialName("shuffle_state") val shuffleState: Boolean,
    @SerialName("repeat_state") val repeatStateString: String,
    val context: SpotifyContext? = null
) {
    val repeatState: ClientPlayerApi.PlayerRepeatState
        get() = ClientPlayerApi.PlayerRepeatState.entries.toTypedArray().match(repeatStateString)!!
}

/**
 * Information about the currently playing track and context
 *
 * @param context A Context Object. Can be null.
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param progressMs Progress into the currently playing track. Can be null.
 * @param isPlaying If something is currently playing.
 * @param item The currently playing track or episode. Can be null.
 * @param actions Allows to update the user interface based on which playback actions are available within the current context
 *
 * @property currentlyPlayingType The object type of the currently playing item. Can be one of track, episode, ad or unknown.
 */
@Serializable
public data class CurrentlyPlayingObject(
    val context: SpotifyContext? = null,
    val timestamp: Long,
    @SerialName("progress_ms") val progressMs: Int? = null,
    @SerialName("is_playing") val isPlaying: Boolean,
    @Serializable(with = PlayableSerializer::class)
    @SerialName("item")
    val item: Playable? = null,
    @SerialName("currently_playing_type") private val currentlyPlayingTypeString: String,
    val actions: PlaybackActions
) {
    val currentlyPlayingType: CurrentlyPlayingType
        get() = CurrentlyPlayingType.entries.toTypedArray().match(currentlyPlayingTypeString)!!
}

/**
 * List of playback actions (pause, resume, etc) which a user is disallowed or allowed to do. Playback actions
 * NOT in [disallows] are allowed.
 *
 * @property disallows A list of [DisallowablePlaybackAction] that have an explicit setting
 */
@Serializable
public data class PlaybackActions(
    @SerialName("disallows") val disallowsString: Map<String, Boolean?>
) {
    val disallows: List<DisallowablePlaybackAction>
        get() = disallowsString.map {
            DisallowablePlaybackAction(
                PlaybackAction.entries.toTypedArray().match(it.key)!!,
                it.value ?: false
            )
        }
}

@Serializable
public data class CurrentUserQueue(
    @SerialName("currently_playing") val currentlyPlaying: Playable? = null,
    @SerialName("queue") val queue: List<Playable>
)

/**
 * Maps a playback action to whether the user is disallowed from doing it
 *
 * @param action The [PlaybackAction] for which the explicit setting is provided
 * @param disallowed Whether the action is not allowed.
 */
@Serializable
public data class DisallowablePlaybackAction(val action: PlaybackAction, val disallowed: Boolean)

/**
 * Action a user takes that will affect current playback
 */
public enum class PlaybackAction(private val identifier: String) : ResultEnum {
    InterruptingPlayback("interrupting_playback"),
    Pausing("pausing"),
    Playing("playing"),
    Resuming("resuming"),
    Seeking("seeking"),
    SkippingNext("skipping_next"),
    SkippingPrev("skipping_prev"),
    Stopping("stopping"),
    TogglingRepeatContext("toggling_repeat_context"),
    TogglingShuffle("toggling_shuffle"),
    TogglingRepeatTrack("toggling_repeat_track"),
    TransferringPlayback("transferring_playback");

    override fun retrieveIdentifier(): String = identifier
}

/**
 * The object type of the currently playing item
 */
public enum class CurrentlyPlayingType(public val identifier: String) : ResultEnum {
    Track("track"),
    Episode("episode"),
    Ad("ad"),
    Unknown("unknown");

    override fun retrieveIdentifier(): String = identifier
}

public enum class SpotifyContextType(public val identifier: String) : ResultEnum {
    Artist("artist"),
    Playlist("playlist"),
    Album("album"),
    Show("show");

    override fun retrieveIdentifier(): String = identifier
}
