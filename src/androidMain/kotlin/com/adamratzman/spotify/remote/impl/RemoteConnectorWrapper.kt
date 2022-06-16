package com.adamratzman.spotify.remote.impl

import android.content.Context
import com.adamratzman.spotify.SpotifyClientApi

internal interface RemoteConnectorWrapper {
    suspend fun connect(
        context: Context,
        api: SpotifyClientApi,
        onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
    ): SpotifyAppRemoteWrapper

    suspend fun disconnect(spotifyAppRemote: SpotifyAppRemoteWrapper?)
}
