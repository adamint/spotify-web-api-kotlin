/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:JsQualifier("window.Spotify")
package com.adamratzman.spotify.webplayer

import kotlin.js.Promise

external interface Album {
    var uri: String
    var name: String
    var images: Array<Image>
}

external interface Artist {
    var name: String
    var uri: String
}

external interface Error {
    var message: String
}

external interface Image {
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
    var url: String
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlaybackContext {
    var metadata: Any
    var uri: String?
}

external interface PlaybackDisallows {
    var pausing: Boolean
    var peeking_next: Boolean
    var peeking_prev: Boolean
    var resuming: Boolean
    var seeking: Boolean
    var skipping_next: Boolean
    var skipping_prev: Boolean
}

external interface PlaybackRestrictions {
    var disallow_pausing_reasons: Array<String>
    var disallow_peeking_next_reasons: Array<String>
    var disallow_peeking_prev_reasons: Array<String>
    var disallow_resuming_reasons: Array<String>
    var disallow_seeking_reasons: Array<String>
    var disallow_skipping_next_reasons: Array<String>
    var disallow_skipping_prev_reasons: Array<String>
}

external interface PlaybackState {
    var context: PlaybackContext
    var disallows: PlaybackDisallows
    var duration: Number
    var paused: Boolean
    var position: Number
    var repeat_mode: Number /* 0 | 1 | 2 */
    var shuffle: Boolean
    var restrictions: PlaybackRestrictions
    var track_window: PlaybackTrackWindow
}

external interface PlaybackTrackWindow {
    var current_track: Track
    var previous_tracks: Array<Track>
    var next_tracks: Array<Track>
}

external interface PlayerInit {
    var name: String
    fun getOAuthToken(cb: (token: String) -> Unit)
    var volume: Number?
        get() = definedExternally
        set(value) = definedExternally
}

open external class Player(options: PlayerInit) {
    open fun connect(): Promise<Boolean>
    open fun disconnect()
    open fun getCurrentState(): Promise<PlaybackState?>
    open fun getVolume(): Promise<Number>
    open fun nextTrack(): Promise<Unit>
    open var addListener: (event: String /* "ready" | "not_ready" */, cb: PlaybackInstanceListener) -> Unit /* (event: String /* "ready" | "not_ready" */, cb: PlaybackInstanceListener) -> Unit & (event: String /* "player_state_changed" */, cb: PlaybackStateListener) -> Unit & (event: String /* "account_error" | "authentication_error" | "initialization_error" | "playback_error" */, cb: ErrorListener) -> Unit */
    open var on: (event: String /* "ready" | "not_ready" */, cb: PlaybackInstanceListener) -> Unit /* (event: String /* "ready" | "not_ready" */, cb: PlaybackInstanceListener) -> Unit & (event: String /* "player_state_changed" */, cb: PlaybackStateListener) -> Unit & (event: String /* "account_error" | "authentication_error" | "initialization_error" | "playback_error" */, cb: ErrorListener) -> Unit */
    open fun removeListener(event: String /* "ready" | "not_ready" | "player_state_changed" | "account_error" | "authentication_error" | "initialization_error" | "playback_error" */, cb: ErrorListener = definedExternally)
    open fun removeListener(event: String /* "ready" | "not_ready" | "player_state_changed" | "account_error" | "authentication_error" | "initialization_error" | "playback_error" */)
    open fun removeListener(event: String /* "ready" | "not_ready" | "player_state_changed" | "account_error" | "authentication_error" | "initialization_error" | "playback_error" */, cb: PlaybackInstanceListener = definedExternally)
    open fun removeListener(event: String /* "ready" | "not_ready" | "player_state_changed" | "account_error" | "authentication_error" | "initialization_error" | "playback_error" */, cb: PlaybackStateListener = definedExternally)
    open fun pause(): Promise<Unit>
    open fun previousTrack(): Promise<Unit>
    open fun resume(): Promise<Unit>
    open fun seek(pos_ms: Number): Promise<Unit>
    open fun setName(name: String): Promise<Unit>
    open fun setVolume(volume: Number): Promise<Unit>
    open fun togglePlay(): Promise<Unit>
}

external interface Track {
    var uri: String
    var id: String?
    var type: String /* "track" | "episode" | "ad" */
    var media_type: String /* "audio" | "video" */
    var name: String
    var is_playable: Boolean
    var album: Album
    var artists: Array<Artist>
}

external interface WebPlaybackInstance {
    var device_id: String
}
