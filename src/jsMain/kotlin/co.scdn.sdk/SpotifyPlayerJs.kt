/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package co.scdn.sdk

import com.adamratzman.spotify.webplayer.Error
import com.adamratzman.spotify.webplayer.PlaybackState
import com.adamratzman.spotify.webplayer.WebPlaybackInstance
import kotlinx.browser.window

public fun setOnSpotifyWebPlaybackSDKReady(callback: suspend () -> Unit) {
    val dynamicWindow: dynamic = window
    dynamicWindow["onSpotifyWebPlaybackSDKReady"] = callback
}

public enum class SpotifyWebPlaybackEvent(public val spotifyId: String) {
    /**
     * Emitted when the Web Playback SDK has successfully connected and is ready to stream content in the browser from Spotify. Type [PlaybackPlayerListener]
     */
    Ready("ready"),

    /**
     * Emitted when the Web Playback SDK is not ready to play content, typically due to no internet connection. Type [PlaybackPlayerListener]
     */
    NotReady("not_ready"),

    /**
     * Emitted when the state of the local playback has changed. It may be also executed in random intervals. Type [PlaybackStateListener]
     */
    PlayerStateChanged("player_state_changed"),

    /**
     * Emitted when the Spotify.Player fails to instantiate a player capable of playing content in the current environment.
     * Most likely due to the browser not supporting EME protection. Type [ErrorListener]
     */
    InitializationError("initialization_error"),

    /**
     * Emitted when the Spotify.Player fails to instantiate a valid Spotify connection from the access token provided to getOAuthToken. Type [ErrorListener]
     */
    AuthenticationError("authentication_error"),

    /**
     * Emitted when the user authenticated does not have a valid Spotify Premium subscription. Type [ErrorListener]
     */
    AccountError("account_error"),

    /**
     * Emitted when loading and/or playing back a track failed. Type [ErrorListener]
     */
    PlaybackError("playback_error")
}

public typealias ErrorListener = (err: Error) -> Unit
public typealias PlaybackPlayerListener = (inst: WebPlaybackInstance) -> Unit
public typealias PlaybackStateListener = (s: PlaybackState) -> Unit
