/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.match
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Context in which a track was played
 *
 * @property type The object type, e.g. “artist”, “playlist”, “album”.
 * @property href A link to the Web API endpoint providing full details of the track.
 */
@Serializable // TODO remove id. It's wrong to extend CoreObject here, as it doesn't has any id
data class PlayHistoryContext(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val uri: SpotifyUri,

    val type: String,
    override val id: String = TRANSIENT_EMPTY_STRING
) : CoreObject()

/**
 * Information about a previously-played track
 *
 * @property track The track the user listened to.
 * @property playedAt The date and time the track was played.
 * @property context The context the track was played from.
 */
@Serializable
data class PlayHistory(
    val track: SimpleTrack,
    @SerialName("played_at") val playedAt: String,
    val context: PlayHistoryContext? = null
)

/**
 * A device which is connected to the Spotify user
 *
 * @property id The device ID. This may be null.
 * @property isActive If this device is the currently active device.
 * @property isPrivateSession If this device is currently in a private session.
 * @property isRestricted Whether controlling this device is restricted. At present
 * if this is “true” then no Web API commands will be accepted by this device.
 * @property name The name of the device.
 * @property type Device type, such as “Computer”, “Smartphone” or “Speaker”.
 */
@Serializable
data class Device(
    override val id: String? = null,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("is_private_session") val isPrivateSession: Boolean,
    @SerialName("is_restricted") val isRestricted: Boolean,
    val name: String,
    val typeString: String,
    @SerialName("volume_percent") val volumePercent: Int
) : IdentifiableNullable() {
    @Transient
    val type: DeviceType = DeviceType.values().first { it.identifier.equals(typeString, true) }

    override val href: String? = null
}

/**
 * Electronic type of registered Spotify device
 *
 * @property identifier readable name
 */
enum class DeviceType(val identifier: String) {
    COMPUTER("Computer"),
    TABLET("Tablet"),
    SMARTPHONE("Smartphone"),
    SPEAKER("Speaker"),
    TV("TV"),
    AVR("AVR"),
    STB("STB"),
    AUDIO_DONGLE("AudioDongle"),
    GAME_CONSOLE("GameConsole"),
    CAST_VIDEO("CastVideo"),
    CAST_AUDIO("CastAudio"),
    AUTOMOBILE("Automobile"),
    UNKNOWN("Unknown");
}

/**
 * Information about the current playback
 *
 * @property timestamp Unix Millisecond Timestamp when data was fetched
 * @property device The device that is currently active
 * @property progressMs Progress into the currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @property isPlaying If something is currently playing.
 * @property track The currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @property context A Context Object. Can be null (e.g. If private session is enabled this will be null).
 * @property shuffleState If shuffle is on or off
 * @property repeatState If and how the playback is repeating
 *
 */
@Serializable
data class CurrentlyPlayingContext(
    val timestamp: Long,
    val device: Device,
    @SerialName("progress_ms") val progressMs: Int? = null,
    @SerialName("is_playing") val isPlaying: Boolean,
    @SerialName("item") val track: Track? = null,
    @SerialName("shuffle_state") val shuffleState: Boolean,
    @SerialName("repeat_state") val repeatStateString: String,
    val context: Context
) {
    @Transient
    val repeatState: RepeatState = RepeatState.values().match(repeatStateString)!!
}

/**
 * How and if playback is repeating
 */
enum class RepeatState(val identifier: String) : ResultEnum {
    OFF("off"),
    TRACK("track"),
    CONTEXT("context");

    override fun retrieveIdentifier() = identifier
}

/**
 * Information about the currently playing track and context
 *
 * @property context A Context Object. Can be null.
 * @property timestamp Unix Millisecond Timestamp when data was fetched
 * @property progressMs Progress into the currently playing track. Can be null.
 * @property isPlaying If something is currently playing.
 * @property track The currently playing track. Can be null.
 * @property currentlyPlayingType The object type of the currently playing item. Can be one of track, episode, ad or unknown.
 * @property actions Allows to update the user interface based on which playback actions are available within the current context
 *
 */
@Serializable
data class CurrentlyPlayingObject(
    val context: PlayHistoryContext? = null,
    val timestamp: Long,
    @SerialName("progress_ms") val progressMs: Int? = null,
    @SerialName("is_playing") val isPlaying: Boolean,
    @SerialName("item") val track: Track,
    @SerialName("currently_playing_type") private val currentlyPlayingTypeString: String,
    val actions: PlaybackActions
) {
    @Transient
    val currentlyPlayingType: CurrentlyPlayingType = CurrentlyPlayingType.values().match(currentlyPlayingTypeString)!!
}

/**
 * List of playback actions (pause, resume, etc) which a user is disallowed or allowed to do. Playback actions
 * NOT in [disallows] are allowed.
 *
 * @property disallows A list of [DisallowablePlaybackAction] that have an explicit setting
 */
@Serializable
data class PlaybackActions(
    @SerialName("disallows") val disallowsString: Map<String, Boolean?>
) {
    @Transient
    val disallows: List<DisallowablePlaybackAction> = disallowsString.map {
        DisallowablePlaybackAction(
            PlaybackAction.values().match(it.key)!!,
            it.value ?: false
        )
    }
}

/**
 * Maps a playback action to whether the user is disallowed from doing it
 *
 * @property action The [PlaybackAction] for which the explicit setting is provided
 * @property disallowed Whether the action is not allowed.
 */
@Serializable
data class DisallowablePlaybackAction(val action: PlaybackAction, val disallowed: Boolean)

/**
 * Action a user takes that will affect current playback
 */
enum class PlaybackAction(private val identifier: String) : ResultEnum {
    INTERRUPTING_PLAYBACK("interrupting_playback"),
    PAUSING("pausing"),
    PLAYING("playing"),
    RESUMING("resuming"),
    SEEKING("seeking"),
    SKIPPING_NEXT("skipping_next"),
    SKIPPING_PREV("skipping_prev"),
    STOPPING("stopping"),
    TOGGLING_REPEAT_CONTEXT("toggling_repeat_context"),
    TOGGLING_SHUFFLE("toggling_shuffle"),
    TOGGLING_REPEAT_TRACK("toggling_repeat_track"),
    TRANSFERRING_PLAYBACK("transferring_playback");

    override fun retrieveIdentifier() = identifier
}

/**
 * The object type of the currently playing item
 */
enum class CurrentlyPlayingType(val identifier: String) : ResultEnum {
    TRACK("track"),
    EPISODE("episode"),
    AD("ad"),
    UNKNOWN("unknown");

    override fun retrieveIdentifier() = identifier
}

/**
 * Puts an object in-context by linking to other related endpoints
 */
@Serializable
data class Context(
    @SerialName("external_urls") private val externalUrlsString: Map<String, String>
) {
    @Transient
    val externalUrls = externalUrlsString.map { ExternalUrl(it.key, it.value) }
}
