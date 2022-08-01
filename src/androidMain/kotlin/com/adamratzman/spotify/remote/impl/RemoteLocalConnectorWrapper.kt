package com.adamratzman.spotify.remote.impl

import android.content.Context
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteAuthenticationException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteConnectionFailedException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteGeneralException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteNotLoggedInException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteOfflineModeException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteUnsupportedFeatureVersionException
import com.adamratzman.spotify.SpotifyAppRemoteException.SpotifyAppRemoteUserNotAuthorizedException
import com.adamratzman.spotify.SpotifyClientApi
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException
import com.spotify.android.appremote.api.error.SpotifyRemoteServiceException
import com.spotify.android.appremote.internal.ConnectApiImpl
import com.spotify.android.appremote.internal.ContentApiImpl
import com.spotify.android.appremote.internal.ImagesApiImpl
import com.spotify.android.appremote.internal.PlayerApiImpl
import com.spotify.android.appremote.internal.SdkRemoteClientConnector
import com.spotify.android.appremote.internal.SdkRemoteClientConnectorFactory
import com.spotify.android.appremote.internal.SpotifyAppRemoteIsConnectedRule
import com.spotify.android.appremote.internal.SpotifyLocator
import com.spotify.android.appremote.internal.StrictRemoteClient
import com.spotify.android.appremote.internal.UserApiImpl
import com.spotify.protocol.client.Debug
import com.spotify.protocol.client.RemoteClient
import com.spotify.protocol.client.RemoteClientConnector
import com.spotify.protocol.client.error.RemoteClientException
import com.spotify.protocol.types.UserStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class RemoteLocalConnectorWrapper(
    private val mSpotifyLocator: SpotifyLocator,
    private val mSdkRemoteClientConnectorFactory: SdkRemoteClientConnectorFactory
) : RemoteConnectorWrapper {
    override suspend fun connect(
        context: Context,
        api: SpotifyClientApi,
        onFailure: suspend (exception: Throwable, wasConnectionTerminated: Boolean) -> Unit
    ): SpotifyAppRemoteWrapper {
        return runBlocking {
            if (!mSpotifyLocator.isSpotifyInstalled(context)) {
                throw SpotifyAppRemoteConnectionFailedException(null, CouldNotFindSpotifyApp())
            }

            val connectionParams = ConnectionParams.Builder(api.clientId)
                .setRedirectUri(api.redirectUri)
                .build()

            try {
                val remoteClientConnector = mSdkRemoteClientConnectorFactory.newConnector(
                    context,
                    connectionParams,
                    mSpotifyLocator.getSpotifyBestPackageName(context)
                ) as SdkRemoteClientConnector

                var spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper? = null

                remoteClientConnector.connect(object : RemoteClientConnector.ConnectionCallback {
                    override fun onConnected(client: RemoteClient) {
                        val strictRemoteClient = StrictRemoteClient(client)
                        val spotifyAppRemote = SpotifyAppRemoteWrapper(
                            strictRemoteClient,
                            PlayerApiImpl(strictRemoteClient),
                            ImagesApiImpl(strictRemoteClient),
                            UserApiImpl(strictRemoteClient),
                            ContentApiImpl(strictRemoteClient),
                            ConnectApiImpl(strictRemoteClient),
                            remoteClientConnector
                        )

                        spotifyAppRemote.isConnected = true
                        strictRemoteClient.addRule(SpotifyAppRemoteIsConnectedRule(spotifyAppRemote))
                        remoteClientConnector.setConnectionTerminatedListener {
                            spotifyAppRemote.disconnectRemoteClient()
                            runBlocking {
                                onFailure(
                                    SpotifyAppRemoteConnectionFailedException(
                                        null,
                                        SpotifyConnectionTerminatedException()
                                    ),
                                    true
                                )
                            }
                        }

                        val userApi = spotifyAppRemote.userApi
                        val userStatusSubscription = userApi.subscribeToUserStatus()
                        userStatusSubscription.setEventCallback { data: UserStatus ->
                            Debug.d(
                                "LoggedIn:%s",
                                data.isLoggedIn
                            )
                            if (data.isLoggedIn) {
                                spotifyAppRemoteWrapper = spotifyAppRemote
                            } else {
                                runBlocking {
                                    onFailure(
                                        SpotifyAppRemoteConnectionFailedException(
                                            "The user must go to Spotify and log-in",
                                            Throwable("The user must go to Spotify and log-in")
                                        ),
                                        false
                                    )
                                }
                            }
                        }

                        userStatusSubscription.setErrorCallback { error: Throwable ->
                            runBlocking {
                                onFailure(error, false)
                            }
                        }
                    }

                    override fun onConnectionFailed(reason: Throwable) {
                        Debug.d(reason, "Connection failed.", *arrayOfNulls(0))
                        remoteClientConnector.disconnect()
                        val connectionError = asAppRemoteException(reason)

                        runBlocking {
                            onFailure(
                                SpotifyAppRemoteConnectionFailedException(
                                    null,
                                    connectionError
                                ),
                                true
                            )
                        }
                    }
                })

                while (spotifyAppRemoteWrapper == null) {
                    delay(1)
                }

                spotifyAppRemoteWrapper!!
            } catch (exception: CouldNotFindSpotifyApp) {
                throw SpotifyAppRemoteConnectionFailedException(null, exception)
            }
        }
    }

    override suspend fun disconnect(spotifyAppRemote: SpotifyAppRemoteWrapper?) {
        spotifyAppRemote?.disconnectRemoteClient()
    }

    companion object {
        private fun asAppRemoteException(reason: Throwable): Throwable {
            val reasonUri = if (reason is RemoteClientException) reason.reasonUri else null
            val message = reason.message
            return if (reason is SpotifyRemoteServiceException) {
                reason
            } else {
                val connectionError: Any
                connectionError =
                    when (reasonUri) {
                        "com.spotify.error.client_authentication_failed" -> {
                            SpotifyAppRemoteAuthenticationException(message ?: "Authentication failed", reason)
                        }
                        "com.spotify.error.unsupported_version" -> {
                            SpotifyAppRemoteUnsupportedFeatureVersionException(message, reason)
                        }
                        "com.spotify.error.offline_mode_active" -> {
                            SpotifyAppRemoteOfflineModeException(message, reason)
                        }
                        "com.spotify.error.user_not_authorized" -> {
                            SpotifyAppRemoteUserNotAuthorizedException(message, reason)
                        }
                        "com.spotify.error.not_logged_in" -> {
                            SpotifyAppRemoteNotLoggedInException(message, reason)
                        }
                        else -> {
                            SpotifyAppRemoteGeneralException(message, reason)
                        }
                    }
                connectionError
            }
        }
    }
}
