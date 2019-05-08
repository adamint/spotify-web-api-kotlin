/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.match
import com.beust.klaxon.Json

/**
 * Context in which a track was played
 *
 * @property type The object type, e.g. “artist”, “playlist”, “album”.
 * @property href A link to the Web API endpoint providing full details of the track.
 */
data class PlayHistoryContext(
    @Json(name = "href") private val _href: String,
    @Json(name = "external_urls") private val _externalUrls: Map<String, String>,
    @Json(name = "uri") private val _uri: String,

    val type: String
) : CoreObject(_href, _href, TrackURI(_uri), _externalUrls)

/**
 * Information about a previously-played track
 *
 * @property track The track the user listened to.
 * @property playedAt The date and time the track was played.
 * @property context The context the track was played from.
 */
data class PlayHistory(
    val track: SimpleTrack,
    @Json(name = "played_at") val playedAt: String,
    val context: PlayHistoryContext
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
data class Device(
    @Json(name = "id") private val _id: String?,

    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "is_private_session") val isPrivateSession: Boolean,
    @Json(name = "is_restricted") val isRestricted: Boolean,
    val name: String,
    val _type: String,
    @Json(name = "volume_percent") val volumePercent: Int,
    val type: DeviceType = DeviceType.values().first { it.identifier.equals(_type, true) }
) : IdentifiableNullable(null, _id)

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
    UNKNOWN("Unknown")
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
data class CurrentlyPlayingContext(
    val timestamp: Long,
    val device: Device,
    @Json(name = "progress_ms") val progressMs: Int?,
    @Json(name = "is_playing") val isPlaying: Boolean,
    @Json(name = "item") val track: Track?,
    @Json(name = "shuffle_state") val shuffleState: Boolean,
    @Json(name = "repeat_state") val _repeatState: String,
    val context: Context
) {
    @Json(ignored = true)
    val repeatState: RepeatState = RepeatState.values().match(_repeatState)!!
}

/**
 * How and if playback is repeating
 */
enum class RepeatState(val identifier: String) : ResultEnum {
    OFF("off"),
    TRACK("track"),
    CONTEXT("context")
    ;

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
data class CurrentlyPlayingObject(
    val context: PlayHistoryContext?,
    val timestamp: Long,
    @Json(name = "progress_ms") val progressMs: Int?,
    @Json(name = "is_playing") val isPlaying: Boolean,
    @Json(name = "item") val track: Track,
    @Json(name = "currently_playing_type") private val _currentlyPlayingType: String,
    val actions: PlaybackActions
) {
    @Json(ignored = true)
    val currentlyPlayingType: CurrentlyPlayingType = CurrentlyPlayingType.values().match(_currentlyPlayingType)!!
}

/**
 * List of playback actions (pause, resume, etc) which a user is disallowed or allowed to do. Playback actions
 * NOT in [disallows] are allowed.
 *
 * @property disallows A list of [DisallowablePlaybackAction] that have an explicit setting
 */
data class PlaybackActions(
    @Json(name = "disallows") val _disallows: Map<String, Boolean?>
) {
    @Json(ignored = true)
    val disallows: List<DisallowablePlaybackAction> = _disallows.map {
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
    TRANSFERRING_PLAYBACK("transferring_playback")
    ;

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
data class Context(
    @Json(name = "external_urls") private val _externalUrls: Map<String, String>
) {
    @Json(ignored = true)
    val externalUrls = _externalUrls.map { ExternalUrl(it.key, it.value) }
}