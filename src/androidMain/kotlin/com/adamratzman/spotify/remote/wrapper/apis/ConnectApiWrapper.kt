package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.Subscription
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.VolumeState
import kotlinx.serialization.Serializable

public class ConnectApiWrapper(
    private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper
) : SpotifyAppRemoteWrappedApi {
    public fun increaseVolume(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectIncreaseVolume()
            .toLibraryCallResult({ }, callback)
    }

    public fun decreaseVolume(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectDecreaseVolume()
            .toLibraryCallResult({ }, callback)
    }

    public fun setVolume(newVolume: Float, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        if (newVolume !in 0.0f..1.0f) throw IllegalArgumentException("Volume must be between 0.0 and 1.0")

        return spotifyAppRemoteWrapper.connectApi.connectSetVolume(newVolume)
            .toLibraryCallResult({ }, callback)
    }

    public fun switchPlaybackToThisDevice(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectSwitchToLocalDevice()
            .toLibraryCallResult({ }, callback)
    }

    public fun subscribeToVolumeState(): Subscription<VolumeState, VolumeStateWrapper> {
        return Subscription(spotifyAppRemoteWrapper.connectApi.subscribeToVolumeState()) { volumeState ->
            VolumeStateWrapper(
                volumeState.mVolume,
                volumeState.mControllable
            )
        }
    }
}

@Serializable
public data class VolumeStateWrapper(val volume: Float, val isControllable: Boolean)