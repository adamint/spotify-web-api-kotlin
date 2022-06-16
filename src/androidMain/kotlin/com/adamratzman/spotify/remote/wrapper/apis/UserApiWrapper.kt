package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.models.AlbumUri
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.Subscription
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Capabilities
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.LibraryState
import com.spotify.protocol.types.UserStatus
import kotlinx.serialization.Serializable

public class UserApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) : SpotifyAppRemoteWrappedApi {
    public fun addPlayableToLibrary(
        playableUri: PlayableUri,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.addToLibrary(playableUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    public fun removePlayableFromLibrary(
        playableUri: PlayableUri,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.removeFromLibrary(playableUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    public fun addAlbumToLibrary(albumUri: AlbumUri, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.addToLibrary(albumUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    public fun removeAlbumFromLibrary(albumUri: AlbumUri, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.removeFromLibrary(albumUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    public fun getUserCanPlayOnDemand(callback: ((Boolean) -> Unit)? = null): CallResult<Capabilities, Boolean> {
        return spotifyAppRemoteWrapper.userApi.capabilities
            .toLibraryCallResult({ it.canPlayOnDemand }, callback)
    }

    public fun getPlayableLibraryState(
        playableUri: PlayableUri,
        callback: ((LibraryStateWrapper) -> Unit)? = null
    ): CallResult<LibraryState, LibraryStateWrapper> {
        return spotifyAppRemoteWrapper.userApi.getLibraryState(playableUri.uri)
            .toLibraryCallResult({ LibraryStateWrapper(it.uri, it.isAdded, it.canAdd) }, callback)
    }

    public fun getPlayableAlbumState(
        albumUri: AlbumUri,
        callback: ((LibraryStateWrapper) -> Unit)? = null
    ): CallResult<LibraryState, LibraryStateWrapper> {
        return spotifyAppRemoteWrapper.userApi.getLibraryState(albumUri.uri)
            .toLibraryCallResult({ LibraryStateWrapper(it.uri, it.isAdded, it.canAdd) }, callback)
    }

    public fun subscribeToCapabilitiesCanUserPlayOnDemand(): Subscription<Capabilities, Boolean> {
        return Subscription(spotifyAppRemoteWrapper.userApi.subscribeToCapabilities()) { capabilities ->
            capabilities.canPlayOnDemand
        }
    }

    public fun subscribeToUserStatus(): Subscription<UserStatus, UserStatusWrapper> {
        return Subscription(spotifyAppRemoteWrapper.userApi.subscribeToUserStatus()) { userStatus ->
            UserStatusWrapper(userStatus.isLoggedIn, userStatus.shortMessage, userStatus.longMessage)
        }
    }
}

@Serializable
public data class UserStatusWrapper(
    public val isLoggedIn: Boolean,
    public val shortMessage: String,
    public val longMessage: String
)

@Serializable
public data class LibraryStateWrapper(
    val uri: String?,
    val isSaved: Boolean,
    val canSave: Boolean
)