package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.SpotifyAppRemoteApi
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.Subscription
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.VolumeState
import kotlinx.serialization.Serializable

/**
 * Control on what device the Spotify App should be playing music, as well as volume control on device.
 *
 * Get an instance of a ConnectApi with [SpotifyAppRemoteApi.connectApi].
 */
public class ConnectApiWrapper(
    private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper
) : SpotifyAppRemoteWrappedApi {
    /**
     * Increase volume by a step size determined by Spotify Connect. The step size varies across device types.
     *
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun increaseVolume(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectIncreaseVolume()
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Decrease volume by a step size determined by Spotify Connect. The step size varies across device types.
     *
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun decreaseVolume(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectDecreaseVolume()
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Set a volume on the currently active device via Spotify Connect.
     *
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun setVolume(newVolume: Float, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        if (newVolume !in 0.0f..1.0f) throw IllegalArgumentException("Volume must be between 0.0 and 1.0")

        return spotifyAppRemoteWrapper.connectApi.connectSetVolume(newVolume)
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Switch music to play on this (local) device, in case it is playing on another device (such as a computer) over Spotify Connect.
     *
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun switchPlaybackToThisDevice(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.connectApi.connectSwitchToLocalDevice()
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Subscribe to volume state changes on the device.
     */
    public fun subscribeToVolumeState(): Subscription<VolumeState, VolumeStateWrapper> {
        return Subscription(spotifyAppRemoteWrapper.connectApi.subscribeToVolumeState()) { volumeState ->
            VolumeStateWrapper(
                volumeState.mVolume,
                volumeState.mControllable
            )
        }
    }
}

/**
 * The wrapper for current value of volume of active device over Spotify Connect.
 *
 * @param volume Current active device volume.
 * @param isControllable Whether the device can be controlled via the remote api.
 */
@Serializable
public data class VolumeStateWrapper(val volume: Float, val isControllable: Boolean)