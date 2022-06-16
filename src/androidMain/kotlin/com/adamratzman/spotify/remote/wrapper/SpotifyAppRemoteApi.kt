package com.adamratzman.spotify.remote.wrapper

import android.content.Context
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteGeneralException
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.remote.impl.RemoteLocalConnectorWrapper
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ConnectApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ContentApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ImagesApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.PlayerApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.SpotifyAppRemoteWrappedApi
import com.adamratzman.spotify.remote.wrapper.apis.UserApiWrapper
import com.spotify.android.appremote.internal.SdkRemoteClientConnectorFactory
import com.spotify.android.appremote.internal.SpotifyLocator

private val spotifyLocator = SpotifyLocator()

public class SpotifyAppRemoteApi(
    private val api: SpotifyClientApi,
    public val onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
) {
    private val connector = RemoteLocalConnectorWrapper(spotifyLocator, SdkRemoteClientConnectorFactory())
    private lateinit var spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper

    public var isConnected: Boolean = ::spotifyAppRemoteWrapper.isInitialized && spotifyAppRemoteWrapper.isConnected

    public val imagesApi: ImagesApiWrapper by lazy {
        assertConnectorIsConnected()
        ImagesApiWrapper(spotifyAppRemoteWrapper)
    }

    public val contentApi: ContentApiWrapper by lazy {
        assertConnectorIsConnected()
        ContentApiWrapper(spotifyAppRemoteWrapper)
    }

    public val connectApi: ConnectApiWrapper by lazy {
        assertConnectorIsConnected()
        ConnectApiWrapper(spotifyAppRemoteWrapper)
    }

    public val userApi: UserApiWrapper by lazy {
        assertConnectorIsConnected()
        UserApiWrapper(spotifyAppRemoteWrapper)
    }

    public val playerApi: PlayerApiWrapper by lazy {
        assertConnectorIsConnected()
        PlayerApiWrapper(spotifyAppRemoteWrapper)
    }

    public suspend fun connect(context: Context) {
        spotifyAppRemoteWrapper = connector.connect(context, api, onFailure)
    }

    public fun disconnect() {
        assertConnectorIsConnected()
        if (isConnected) {
            isConnected = false
            spotifyAppRemoteWrapper.disconnectRemoteClient()
        }
    }

    private fun assertConnectorIsConnected() {
        if (!isConnected) throw SpotifyAppRemoteGeneralException("The connector is not yet connected. Please make sure to call SpotifyAppRemoteApi.connect before accessing any inner API")
    }

    public companion object {
        public fun isSpotifyInstalled(context: Context): Boolean {
            return spotifyLocator.isSpotifyInstalled(context)
        }

        public fun create(
            api: SpotifyClientApi,
            onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
        ): SpotifyAppRemoteApi {
            return SpotifyAppRemoteApi(api, onFailure)
        }
    }

}