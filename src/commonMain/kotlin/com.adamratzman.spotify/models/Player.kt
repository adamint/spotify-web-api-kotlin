/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.utils.getExternalUrls
import com.adamratzman.spotify.utils.match
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Context in which a track was played
 *
 * @param type The object type, e.g. “artist”, “playlist”, “album”.
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param uri The URI associated with the object
 *
 * @property externalUrls Known external URLs for this object
 */
@Serializable
public data class PlayHistoryContext(
    @SerialName("external_urls") private val externalUrlsString: Map<String, String>,
    val href: String,
    val uri: SpotifyUri,
    @SerialName("type") val typeString: String
) {
    val type: ContextType? get() = ContextType.values().find { it.spotifyType == typeString }
    val externalUrls: List<ExternalUrl> get() = getExternalUrls(externalUrlsString)

    public enum class ContextType(public val spotifyType: String) {
        ARTIST("artist"),
        PLAYLIST("playlist"),
        ALBUM("album"),
        SHOW("show")
    }
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
    val track: SimpleTrack,
    @SerialName("played_at") val playedAt: String,
    val context: PlayHistoryContext? = null
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
    val type: DeviceType get() = DeviceType.values().first { it.identifier.equals(typeString, true) }

    override val href: String? = null
}

/**
 * Electronic type of registered Spotify device
 *
 * @param identifier readable name
 */
public enum class DeviceType(public val identifier: String) {
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
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param device The device that is currently active
 * @param progressMs Progress into the currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @param isPlaying If something is currently playing.
 * @param track The currently playing track. Can be null (e.g. If private session is enabled this will be null).
 * @param context A Context Object. Can be null (e.g. If private session is enabled this will be null).
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
    @SerialName("item") val track: Track? = null,
    @SerialName("shuffle_state") val shuffleState: Boolean,
    @SerialName("repeat_state") val repeatStateString: String,
    val context: Context? = null
) {
    val repeatState: ClientPlayerApi.PlayerRepeatState get() = ClientPlayerApi.PlayerRepeatState.values().match(repeatStateString)!!
}

/**
 * Information about the currently playing track and context
 *
 * @param context A Context Object. Can be null.
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 * @param progressMs Progress into the currently playing track. Can be null.
 * @param isPlaying If something is currently playing.
 * @param track The currently playing track. Can be null.
 * @param actions Allows to update the user interface based on which playback actions are available within the current context
 *
 * @property currentlyPlayingType The object type of the currently playing item. Can be one of track, episode, ad or unknown.
 */
@Serializable
public data class CurrentlyPlayingObject(
    val context: PlayHistoryContext? = null,
    val timestamp: Long,
    @SerialName("progress_ms") val progressMs: Int? = null,
    @SerialName("is_playing") val isPlaying: Boolean,
    @SerialName("item") val track: Track,
    @SerialName("currently_playing_type") private val currentlyPlayingTypeString: String,
    val actions: PlaybackActions
) {
    val currentlyPlayingType: CurrentlyPlayingType
        get() = CurrentlyPlayingType.values().match(currentlyPlayingTypeString)!!
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
                PlaybackAction.values().match(it.key)!!,
                it.value ?: false
            )
        }
}

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

    override fun retrieveIdentifier(): String = identifier
}

/**
 * The object type of the currently playing item
 */
public enum class CurrentlyPlayingType(public val identifier: String) : ResultEnum {
    TRACK("track"),
    EPISODE("episode"),
    AD("ad"),
    UNKNOWN("unknown");

    override fun retrieveIdentifier(): String = identifier
}

/**
 * Puts an object in-context by linking to other related endpoints
 */
@Serializable
public data class Context(
    @SerialName("external_urls") private val externalUrlsString: Map<String, String>
) {
    val externalUrls: List<ExternalUrl> get() = getExternalUrls(externalUrlsString)
}
