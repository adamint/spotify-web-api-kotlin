/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.webplayer

typealias ErrorListener = (err: Error) -> Unit
typealias PlaybackInstanceListener = (inst: WebPlaybackInstance) -> Unit
typealias PlaybackStateListener = (s: PlaybackState) -> Unit
