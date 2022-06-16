package com.adamratzman.spotify.remote.impl

import com.spotify.android.appremote.api.AppRemote
import com.spotify.android.appremote.api.ConnectApi
import com.spotify.android.appremote.api.ContentApi
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.UserApi
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.RemoteClient
import com.spotify.protocol.client.RemoteClientConnector
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.Item

public class SpotifyAppRemoteWrapper constructor(
    internal val mRemoteClient: RemoteClient,
    internal val mPlayerApi: PlayerApi,
    internal val mImagesApi: ImagesApi,
    internal val mUserApi: UserApi,
    internal val mContentApi: ContentApi,
    internal val mConnectApi: ConnectApi,
    private val mRemoteClientConnector: RemoteClientConnector
) : AppRemote {
    @Volatile
    private var mIsConnected = false

    override fun getImagesApi(): ImagesApi {
        return mImagesApi
    }

    override fun getPlayerApi(): PlayerApi {
        return mPlayerApi
    }

    override fun getUserApi(): UserApi {
        return mUserApi
    }

    override fun getContentApi(): ContentApi {
        return mContentApi
    }

    override fun getConnectApi(): ConnectApi {
        return mConnectApi
    }

    override fun isConnected(): Boolean {
        return mIsConnected
    }

    internal fun setConnected(connected: Boolean) {
        mIsConnected = connected
    }

    internal fun disconnectRemoteClient() {
        mIsConnected = false
        mRemoteClient.goodbye()
        mRemoteClientConnector.disconnect()
    }

    override fun <T : Item?, S : Item?> call(
        uri: String?,
        argument: S?,
        resultType: Class<T>?
    ): CallResult<T> {
        return mRemoteClient.call(uri, argument, resultType)
    }

    override fun <T : Item?> subscribe(uri: String?, eventType: Class<T>?): Subscription<T> {
        return mRemoteClient.subscribe(uri, eventType)
    }
}