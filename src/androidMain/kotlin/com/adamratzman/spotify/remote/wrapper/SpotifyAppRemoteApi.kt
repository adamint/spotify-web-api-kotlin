package com.adamratzman.spotify.remote.wrapper

import android.content.Context
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteGeneralException
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.SpotifyScope.APP_REMOTE_CONTROL
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.adamratzman.spotify.remote.impl.RemoteLocalConnectorWrapper
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ConnectApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ContentApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.ImagesApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.PlayerApiWrapper
import com.adamratzman.spotify.remote.wrapper.apis.UserApiWrapper
import com.spotify.android.appremote.internal.SdkRemoteClientConnectorFactory
import com.spotify.android.appremote.internal.SpotifyLocator
import com.spotify.protocol.types.ImageUri

private val spotifyLocator = SpotifyLocator()

/**
 * The interface through which you can connect to the Spotify app to remotely control playback.
 * Obtain an instance through [SpotifyAppRemoteApi.create].
 *
 * @param api The api to use to connect.
 * @param onFailure The callback to invoke whenever a connection exception is thrown during the lifetime of the remote connector.
 */
public class SpotifyAppRemoteApi(
    private val api: SpotifyClientApi,
    public val onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
) {
    private val connector = RemoteLocalConnectorWrapper(spotifyLocator, SdkRemoteClientConnectorFactory())
    private lateinit var spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper

    /**
     * Whether the remote connector has been successfully connected. If this is false, please use [connect] to connect.
     */
    public var isConnected: Boolean = ::spotifyAppRemoteWrapper.isInitialized && spotifyAppRemoteWrapper.isConnected

    /**
     * Obtain an instance of the [ImagesApiWrapper] through which you can obtain a Bitmap from an [ImageUri]
     */
    public val imagesApi: ImagesApiWrapper by lazy {
        assertConnectorIsConnected()
        ImagesApiWrapper(spotifyAppRemoteWrapper)
    }

    /**
     * Obtain an instance of the [ContentApiWrapper] through which you can get recommended content and content info.
     */
    public val contentApi: ContentApiWrapper by lazy {
        assertConnectorIsConnected()
        ContentApiWrapper(spotifyAppRemoteWrapper)
    }

    /**
     * Obtain an instance of the [ConnectApiWrapper] through which you can control which device Spotify should be
     * playing on, and at what volume.
     */
    public val connectApi: ConnectApiWrapper by lazy {
        assertConnectorIsConnected()
        ConnectApiWrapper(spotifyAppRemoteWrapper)
    }

    /**
     * Obtain an instance of the [UserApiWrapper] through which you can get user-related data and perform actions related to current user.
     */
    public val userApi: UserApiWrapper by lazy {
        assertConnectorIsConnected()
        UserApiWrapper(spotifyAppRemoteWrapper)
    }

    /**
     * Obtain an instance of the [PlayerApiWrapper] through which you can interact remotely with the Spotify Player.
     */
    public val playerApi: PlayerApiWrapper by lazy {
        assertConnectorIsConnected()
        PlayerApiWrapper(spotifyAppRemoteWrapper)
    }

    /**
     * Connect to the remote client. This must be performed before accessing any apis.
     *
     * @param context The current context of your application
     */
    public suspend fun connect(context: Context) {
        if (api.clientId == null || api.redirectUri == null) {
            throw IllegalArgumentException("Neither the clientId nor the redirectUri can be null")
        }

        val scopes: List<SpotifyScope>? = SpotifyDefaultCredentialStore(api.clientId, api.redirectUri!!, context).spotifyToken?.scopes
        if (scopes == null || !scopes.contains(APP_REMOTE_CONTROL)) {
            throw SpotifyException.SpotifyScopesNeededException(
                SpotifyException.ReAuthenticationNeededException(message = "You need to re-authenticate the client and store the credential in the SpotifyDefaultCredentialStore before connecting to the app remote api."),
                listOf(APP_REMOTE_CONTROL)
            )
        }

        spotifyAppRemoteWrapper = connector.connect(context, api, onFailure)
    }

    /**
     * Disconnect from the remote client. Connection must be re-established before using any other apis.
     */
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
        /**
         * Check if Spotify is installed on this device.
         *
         * @param context The current context in your application.
         */
        public fun isSpotifyInstalled(context: Context): Boolean {
            return spotifyLocator.isSpotifyInstalled(context)
        }

        /**
         * Obtain an instance of [SpotifyAppRemoteApi] through which you can connect to the Spotify app to remotely control playback.
         *
         * @param api The api to use to connect.
         * @param onFailure The callback to invoke whenever a connection exception is thrown during the lifetime of the remote connector.
         */
        public fun create(
            api: SpotifyClientApi,
            onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
        ): SpotifyAppRemoteApi {
            return SpotifyAppRemoteApi(api, onFailure)
        }
    }

}