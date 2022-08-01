package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.models.AlbumUri
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.SpotifyAppRemoteApi
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.Subscription
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Capabilities
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.Image.Dimension
import com.spotify.protocol.types.LibraryState
import com.spotify.protocol.types.UserStatus
import kotlinx.serialization.Serializable

/**
 * Get user-related data and perform actions related to current user.
 *
 * Get an instance of a UserApi with [SpotifyAppRemoteApi.userApi].
 */
public class UserApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) : SpotifyAppRemoteWrappedApi {
    /**
     * Adds item to users' library.
     *
     * @param playableUri Uri to add.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun addPlayableToLibrary(
        playableUri: PlayableUri,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.addToLibrary(playableUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Removes item from users library.
     *
     * @param playableUri Uri to remove.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun removePlayableFromLibrary(
        playableUri: PlayableUri,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.removeFromLibrary(playableUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Adds item to users' library.
     *
     * @param albumUri Uri to add.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun addAlbumToLibrary(albumUri: AlbumUri, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.addToLibrary(albumUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Removes item from users library.
     *
     * @param albumUri Uri to remove.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun removeAlbumFromLibrary(albumUri: AlbumUri, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.userApi.removeFromLibrary(albumUri.uri)
            .toLibraryCallResult({ }, callback)
    }

    /**
     * Get whether the user can play a track via the remote api.
     *
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun getUserCanPlayOnDemand(callback: ((canUserPlayOnDemand: Boolean) -> Unit)? = null): CallResult<Capabilities, Boolean> {
        return spotifyAppRemoteWrapper.userApi.capabilities
            .toLibraryCallResult({ it.canPlayOnDemand }, callback)
    }

    /**
     * Gets library state for the given Spotify uri.
     *
     * @param playableUri The uri to get the library state of.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun getPlayableLibraryState(
        playableUri: PlayableUri,
        callback: ((libraryState: LibraryStateWrapper) -> Unit)? = null
    ): CallResult<LibraryState, LibraryStateWrapper> {
        return spotifyAppRemoteWrapper.userApi.getLibraryState(playableUri.uri)
            .toLibraryCallResult({ LibraryStateWrapper(it.uri, it.isAdded, it.canAdd) }, callback)
    }

    /**
     * Same as [getUserCanPlayOnDemand], but as a subscription.
     */
    public fun subscribeToCapabilitiesCanUserPlayOnDemand(): Subscription<Capabilities, Boolean> {
        return Subscription(spotifyAppRemoteWrapper.userApi.subscribeToCapabilities()) { capabilities ->
            capabilities.canPlayOnDemand
        }
    }

    /**
     * Subscribe to user status changes.
     */
    public fun subscribeToUserStatus(): Subscription<UserStatus, UserStatusWrapper> {
        return Subscription(spotifyAppRemoteWrapper.userApi.subscribeToUserStatus()) { userStatus ->
            UserStatusWrapper(userStatus.isLoggedIn, userStatus.shortMessage, userStatus.longMessage)
        }
    }
}

/**
 * Information about the current user's login status.
 *
 * @param isLoggedIn Whether the user is currently logged in.
 * @param shortMessage Unknown
 * @param longMessage Unknown
 */
@Serializable
public data class UserStatusWrapper(
    public val isLoggedIn: Boolean,
    public val shortMessage: String?,
    public val longMessage: String?
)

/**
 * Represents the current library state of a playable.
 *
 * @param uri The Spotify uri associated with this playable.
 * @param isSaved Whether this playable has been saved to the user's library.
 * @param canSave Whether this playable *can* be saved to the user's library.
 */
@Serializable
public data class LibraryStateWrapper(
    val uri: String?,
    val isSaved: Boolean,
    val canSave: Boolean
)