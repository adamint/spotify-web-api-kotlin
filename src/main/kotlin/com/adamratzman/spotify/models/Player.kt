package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.match
import com.beust.klaxon.Json


/**
 * Context in which a track was played
 *
 * @param type The object type, e.g. “artist”, “playlist”, “album”.
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param externalUrls External URLs for this context.
 * @param uri The Spotify URI for the context.
 */
data class PlayHistoryContext(
        val type: String,
        val href: String,
        @Json(name = "external_urls") val externalUrls: Map<String, String>,
        @Json(name = "uri") private val _uri: String,
        @Json(ignored = true) val uri: TrackURI = TrackURI(_uri)
)

/**
 * Information about a previously-played track
 *
 * @param track The track the user listened to.
 * @param playedAt The date and time the track was played.
 * @param context The context the track was played from.
 */
data class PlayHistory(
        val track: SimpleTrack,
        @Json(name = "played_at") val playedAt: String,
        val context: PlayHistoryContext
)


/**
 * @param id The device ID. This may be null.
 * @param isActive If this device is the currently active device.
 * @param isPrivateSession If this device is currently in a private session.
 * @param isRestricted Whether controlling this device is restricted. At present
 * if this is “true” then no Web API commands will be accepted by this device.
 * @param name The name of the device.
 * @param type Device type, such as “Computer”, “Smartphone” or “Speaker”.
 */
data class Device(
        val id: String?,
        @Json(name = "is_active") val isActive: Boolean,
        @Json(name = "is_private_session") val isPrivateSession: Boolean,
        @Json(name = "is_restricted") val isRestricted: Boolean,
        val name: String,
        val _type: String,
        @Json(name = "volume_percent") val volumePercent: Int,
        val type: DeviceType = DeviceType.values().first { it.identifier.equals(_type, true) }

)

/**
 * Electronic type of registered Spotify device
 *
 * @param identifier readable name
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
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param device The device that is currently active
 * @param progressMs Progress into the currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @param isPlaying If something is currently playing.
 * @param item The currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @param context A Context Object. Can be null (e.g. If private session is enabled this will be null).
 * @param shuffleState If shuffle is on or off
 * @param repeatState If and how the playback is repeating
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
        @Json(ignored = true) val repeatState: RepeatState = RepeatState.values().match(_repeatState)!!,
        val context: Context
)

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
 * @param context A Context Object. Can be null.
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param progressMs Progress into the currently playing track. Can be null.
 * @param isPlaying If something is currently playing.
 * @param track The currently playing track. Can be null.
 * @param currentlyPlayingType The object type of the currently playing item. Can be one of track, episode, ad or unknown.
 * @param actions Allows to update the user interface based on which playback actions are available within the current context
 *
 */
data class CurrentlyPlayingObject(
        val context: PlayHistoryContext?,
        val timestamp: Long,
        @Json(name = "progress_ms") val progressMs: Int?,
        @Json(name = "is_playing") val isPlaying: Boolean,
        @Json(name = "item") val track: Track,
        @Json(name = "currently_playing_type") private val _currentlyPlayingType: String,
        @Json(ignored = true) val currentlyPlayingType: CurrentlyPlayingType = CurrentlyPlayingType.values().match(_currentlyPlayingType)!!,
        val actions: PlaybackActions
)

data class PlaybackActions(
        @Json(name = "disallows") val _disallows: Map<String, Boolean?>,
        @Json(ignored = true) val disallows: List<DisallowablePlaybackAction> = _disallows.map {
            DisallowablePlaybackAction(
                    PlaybackAction.values().match(it.key)!!,
                    it.value ?: false
            )
        }
)

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
        @Json(name = "external_urls") val externalUrls: Map<String, String>
)