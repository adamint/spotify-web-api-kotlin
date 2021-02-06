/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package co.scdn.sdk

import kotlinx.browser.window

public fun setOnSpotifyWebPlaybackSDKReady(callback: suspend () -> Unit) {
    val dynamicWindow: dynamic = window
    dynamicWindow["onSpotifyWebPlaybackSDKReady"] = callback
}
