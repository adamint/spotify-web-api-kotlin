/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:JsQualifier("window.Spotify")
@file:Suppress("PropertyName", "unused")

package com.adamratzman.spotify.webplayer

import co.scdn.sdk.ErrorListener
import co.scdn.sdk.PlaybackPlayerListener
import co.scdn.sdk.PlaybackStateListener
import co.scdn.sdk.SpotifyWebPlaybackEvent
import kotlin.js.Promise

public external interface Album {
    public var uri: String
    public var name: String
    public var images: Array<Image>
}

public external interface Artist {
    public var name: String
    public var uri: String
}

public external interface Error {
    public var message: String
}

public external interface Image {
    public val height: Number?
    public val url: String
    public val width: Number?
}

/**
 * Playback context for the player
 *
 * @property metadata Additional metadata for the context (can be null)
 * @property uri The URI of the context (can be null)
 */
public external interface PlaybackContext {
    public var metadata: Any
    public var uri: String?
}

/**
 * A simplified set of restriction controls for the track. By default, these fields will either be set to false or undefined, which indicates that the particular operation is allowed. When the field is set to `true`, this means that the operation is not permitted. For example, `skipping_next`, `skipping_prev` and `seeking` will be set to `true` when playing an ad track.
 */
public external interface PlaybackDisallows {
    public var pausing: Boolean
    public var peeking_next: Boolean
    public var peeking_prev: Boolean
    public var resuming: Boolean
    public var seeking: Boolean
    public var skipping_next: Boolean
    public var skipping_prev: Boolean
}

public external interface PlaybackRestrictions {
    public var disallow_pausing_reasons: Array<String>
    public var disallow_peeking_next_reasons: Array<String>
    public var disallow_peeking_prev_reasons: Array<String>
    public var disallow_resuming_reasons: Array<String>
    public var disallow_seeking_reasons: Array<String>
    public var disallow_skipping_next_reasons: Array<String>
    public var disallow_skipping_prev_reasons: Array<String>
}

/**
 * This is an object that is provided every time Spotify.Player#getCurrentState is called. It contains information on context, permissions, playback state, the user’s session, and more.
 * @property context Playback context
 * @property disallows A simplified set of restriction controls for the track. By default, these fields will either be set to false or undefined, which indicates that the particular operation is allowed. When the field is set to `true`, this means that the operation is not permitted. For example, `skipping_next`, `skipping_prev` and `seeking` will be set to `true` when playing an ad track.
 * @property paused Whether the current track is paused
 * @property position The position_ms of the current track.
 * @property repeat_mode The repeat mode. No repeat mode is 0, once-repeat is 1 and full repeat is 2.
 * @property shuffle True if shuffled, false otherwise.
 * @property track_window Playback information
 */
public external interface PlaybackState {
    public var context: PlaybackContext
    public var disallows: PlaybackDisallows
    public var paused: Boolean
    public var duration: Number
    public var position: Number
    public var repeat_mode: Number
    public var shuffle: Boolean
    public var restrictions: PlaybackRestrictions
    public var track_window: PlaybackTrackWindow
}

public external interface PlaybackTrackWindow {
    public var current_track: Track
    public var previous_tracks: Array<Track>
    public var next_tracks: Array<Track>
}

/**
 * Options for instantiating the [Player]
 *
 * @property name The name of the Spotify Connect player. It will be visible in other Spotify apps.
 * @property getOAuthToken This will be called every time you run Spotify.Player#connect or when a user's access token has expired (maximum of 60 minutes).
 * You need to invoke cb (the callback) with the access token within this method. Ex: cb("STATIC_ACCESS"TOKEN")
 * @property volume The default volume of the player. Represented as a decimal between 0 and 1. Default value is 1.
 */
public external interface PlayerInit {
    public var name: String
    public var getOAuthToken: (cb: (token: String) -> Unit) -> Unit
    public var volume: Number?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * The main constructor for initializing the Web Playback SDK. It should contain an object with the player name, volume and access token.
 *
 * @param options The options to instantiate this Player
 */
public open external class Player(options: PlayerInit) {
    /**
     * Connect our Web Playback SDK instance to Spotify with the credentials provided during initialization.
     * @return Returns a Promise containing a Boolean (either true or false) with the success of the connection.
     */
    public open fun connect(): Promise<Boolean>

    /**
     * Closes the current session our Web Playback SDK has with Spotify.
     */
    public open fun disconnect()

    /**
     * Collect metadata on local playback.
     * @return Returns a Promise. It will return either a WebPlaybackState object or null depending on if the user is successfully connected.
     */
    public open fun getCurrentState(): Promise<PlaybackState?>

    /**
     * Some browsers prevent autoplay of media by ensuring that all playback is triggered by
     * synchronous event-paths originating from user interaction such as a click.
     * This event allows you to manually activate the element if action is deferred or done
     * in a separate execution context than the user action.
     */
    public fun activateElement(): Promise<Any>

    /**
     * Get the local volume currently set in the Web Playback SDK.
     * @return Returns a Promise containing the local volume (as a Float between 0 and 1).
     */
    public open fun getVolume(): Promise<Number>

    /**
     * Skip to the next track in local playback.
     */
    public open fun nextTrack(): Promise<Unit>

    /**
     * Pause the local playback.
     */
    public open fun pause(): Promise<Unit>

    /**
     * Switch to the previous track in local playback.
     */
    public open fun previousTrack(): Promise<Unit>

    /**
     * Resume the local playback.
     */
    public open fun resume(): Promise<Unit>

    /**
     * Seek to a position in the current track in local playback.
     * @param pos_ms The position in milliseconds to seek to.
     */
    public open fun seek(pos_ms: Number): Promise<Unit>

    /**
     * Rename the Spotify Player device. This is visible across all Spotify Connect devices.
     * @param name The new desired player name.
     */
    public open fun setName(name: String): Promise<Unit>

    /**
     * Set the local volume for the Web Playback SDK.
     *
     * @param volume The new desired volume for local playback. Between 0 and 1.
     */
    public open fun setVolume(volume: Float): Promise<Unit>

    /**
     * Resume/pause the local playback.
     */
    public open fun togglePlay(): Promise<Unit>

    /**
     * Create a new event listener under event [event] of type [PlaybackPlayerListener] in the Web Playback SDK. Alias for Spotify.Player#on.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun addListener(event: String, cb: PlaybackPlayerListener)

    /**
     * Create a new event listener under event [event] of type [PlaybackStateListener] in the Web Playback SDK. Alias for Spotify.Player#on.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun addListener(event: String, cb: PlaybackStateListener)

    /**
     * Create a new event listener under event [event] of type [ErrorListener] in the Web Playback SDK. Alias for Spotify.Player#on.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun addListener(event: String, cb: ErrorListener)

    /**
     * Create a new event listener under event [event] of type [PlaybackPlayerListener] in the Web Playback SDK.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun on(event: String, cb: PlaybackPlayerListener)

    /**
     * Create a new event listener under event [event] of type [PlaybackStateListener] in the Web Playback SDK.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun on(event: String, cb: PlaybackStateListener)

    /**
     * Create a new event listener under event [event] of type [ErrorListener] in the Web Playback SDK.
     *
     * @param event A valid event name. See Web Playback SDK Events.
     * @param cb A callback function to be fired when the event has been executed.
     */
    public open fun on(event: String, cb: ErrorListener)

    /**
     * Remove all event listeners registered under a specific [event] type in the Web Playback SDK.
     * **Please use [SpotifyWebPlaybackEvent.spotifyId] to get web playback events' spotify ids**
     *
     * @return Returns a Boolean. Returns true if the event name is valid with registered callbacks from #addListener.
     */
    public open fun removeListener(event: String)

    /**
     * Remove a specific [ErrorListener] registered under event type [event] in the Web Playback SDK.
     */
    public open fun removeListener(
        event: String,
        cb: ErrorListener = definedExternally
    )

    /**
     * Remove a specific [PlaybackPlayerListener] registered under event type [event] in the Web Playback SDK.
     * **Please use [SpotifyWebPlaybackEvent.spotifyId] to get web playback events' spotify ids**
     */
    public open fun removeListener(
        event: String,
        cb: PlaybackPlayerListener = definedExternally
    )

    /**
     * Remove a specific [PlaybackStateListener] registered under event type [event] in the Web Playback SDK.
     * **Please use [SpotifyWebPlaybackEvent.spotifyId] to get web playback events' spotify ids**
     */
    public open fun removeListener(
        event: String,
        cb: PlaybackStateListener = definedExternally
    )
}

/**
 * @property type track, episode, ad
 * @property media_type audio, video
 */
public external interface Track {
    public var uri: String
    public var id: String?
    public var type: String
    public var media_type: String
    public var name: String
    public var is_playable: Boolean
    public var album: Album
    public var artists: Array<Artist>
}

/**
 * This is an object that is provided in the ready event as an argument. WebPlaybackPlayer objects contain information related to the current player instance of the Web Playback SDK.
 */
public external interface WebPlaybackInstance {
    public var device_id: String
}
