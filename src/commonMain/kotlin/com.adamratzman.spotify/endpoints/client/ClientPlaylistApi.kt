/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.pub.PlaylistApi
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.mapToJsonString
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.BufferedImage
import com.adamratzman.spotify.utils.convertBufferedImageToBase64JpegString
import com.adamratzman.spotify.utils.convertFileToBufferedImage
import com.adamratzman.spotify.utils.convertLocalImagePathToBufferedImage
import com.adamratzman.spotify.utils.convertUrlPathToBufferedImage
import com.adamratzman.spotify.utils.encodeUrl
import com.adamratzman.spotify.utils.jsonMap
import com.soywiz.korio.file.VfsFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Endpoints for retrieving information about a user’s playlists and for managing a user’s playlists.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/)**
 */
public class ClientPlaylistApi(api: GenericSpotifyApi) : PlaylistApi(api) {
    /**
     * Create a playlist for a Spotify user. (The playlist will be empty until you add playables.)
     *
     * Creating a public playlist for a user requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * creating a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/create-playlist/)**
     *
     * @param user The user’s Spotify user ID.
     * @param name The name for the new playlist, for example "Your Coolest Playlist" . This name does not need to be
     * unique; a user may have several playlists with the same name.
     * @param description
     * @param public Defaults to true . If true the playlist will be public, if false it will be private.
     * To be able to create private playlists, the user must have granted the playlist-modify-private scope.
     * @param collaborative Defaults to false . If true the playlist will be collaborative. Note that to create a
     * collaborative playlist you must also set public to false . To create collaborative playlists you must have
     * granted [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] and [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scopes.
     *
     * @return The created [Playlist] object with no playables
     */
    public suspend fun createClientPlaylist(
        name: String,
        description: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        user: String? = null
    ): Playlist {
        if (public == null || public) requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC)
        else if (collaborative == null || !collaborative) requireScopes(SpotifyScope.PLAYLIST_MODIFY_PRIVATE)
        else if (collaborative) requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE)

        if (name.isEmpty()) throw BadRequestException(ErrorObject(400, "Name cannot be empty"))
        val body = jsonMap()
        body += buildJsonObject { put("name", name) }
        if (description != null) body += buildJsonObject { put("description", description) }
        if (public != null) body += buildJsonObject { put("public", public) }
        if (collaborative != null) body += buildJsonObject { put("collaborative", collaborative) }

        return post(
            endpointBuilder("/users/${UserUri(user ?: (api as SpotifyClientApi).getUserId()).id.encodeUrl()}/playlists").toString(),
            body.mapToJsonString()
        ).toObject(Playlist.serializer(), api, json)
    }

    /**
     * Create a playlist for a Spotify user. (The playlist will be empty until you add playables.)
     *
     * Creating a public playlist for a user requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * creating a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/create-playlist/)**
     *
     * @param user The user’s Spotify user ID.
     * @param name The name for the new playlist, for example "Your Coolest Playlist" . This name does not need to be
     * unique; a user may have several playlists with the same name.
     * @param description
     * @param public Defaults to true . If true the playlist will be public, if false it will be private.
     * To be able to create private playlists, the user must have granted the playlist-modify-private scope.
     * @param collaborative Defaults to false . If true the playlist will be collaborative. Note that to create a
     * collaborative playlist you must also set public to false . To create collaborative playlists you must have
     * granted [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] and [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scopes.
     *
     * @return The created [Playlist] object with no playables
     */
    public fun createClientPlaylistRestAction(
        name: String,
        description: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        user: String? = null
    ): SpotifyRestAction<Playlist> =
        SpotifyRestAction { createClientPlaylist(name, description, public, collaborative, user) }

    /**
     * Add a [Playable] to a user’s playlist.
     *
     * Adding playables to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding playables to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playable Playable uri
     * @param position The position to insert the playables, a zero-based index. For example, to insert the playables in the
     * first position: position=0; to insert the playables in the third position: position=2. If omitted, the playables will
     * be appended to the playlist. Playables are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid playable ids is provided or the playlist is not found
     */
    public suspend fun addPlayableToClientPlaylist(playlist: String, playable: PlayableUri, position: Int? = null): Unit =
        addPlayablesToClientPlaylist(playlist, playable, position = position)

    /**
     * Add a [Playable] to a user’s playlist.
     *
     * Adding playables to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding playables to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playable Playable uri
     * @param position The position to insert the playables, a zero-based index. For example, to insert the playables in the
     * first position: position=0; to insert the playables in the third position: position=2. If omitted, the playables will
     * be appended to the playlist. Playables are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid playable ids is provided or the playlist is not found
     */
    public fun addPlayableToClientPlaylistRestAction(
        playlist: String,
        playable: PlayableUri,
        position: Int? = null
    ): SpotifyRestAction<Unit> = SpotifyRestAction { addPlayableToClientPlaylist(playlist, playable, position) }

    /**
     * Add a [Playable] to a user’s playlist.
     *
     * Adding playables to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding playables to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables Playable uris. Max 100 without bulk requesting enabled
     * @param position The position to insert the playables, a zero-based index. For example, to insert the playables in the
     * first position: position=0; to insert the playables in the third position: position=2. If omitted, the playables will
     * be appended to the playlist. Playables are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid playable ids is provided or the playlist is not found
     */
    public suspend fun addPlayablesToClientPlaylist(playlist: String, vararg playables: PlayableUri, position: Int? = null) {
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)
        checkBulkRequesting(100, playables.size)

        bulkStatefulRequest(100, playables.toList()) { chunk ->
            val body = jsonMap()
            body += buildJsonObject {
                put(
                    "uris",
                    JsonArray(chunk.map { it.uri }.map(::JsonPrimitive))
                )
            }
            if (position != null) body += buildJsonObject { put("position", position) }
            post(
                endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
                body.mapToJsonString()
            )
        }
    }

    /**
     * Add a [Playable] to a user’s playlist.
     *
     * Adding playables to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding playables to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables Playable uris. Max 100 without bulk requesting enabled
     * @param position The position to insert the playables, a zero-based index. For example, to insert the playables in the
     * first position: position=0; to insert the playables in the third position: position=2. If omitted, the playables will
     * be appended to the playlist. Playables are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid playable ids is provided or the playlist is not found
     */
    public fun addPlayablesToClientPlaylistRestAction(
        playlist: String,
        vararg playables: PlayableUri,
        position: Int? = null
    ): SpotifyRestAction<Unit> = SpotifyRestAction { addPlayablesToClientPlaylist(playlist, *playables, position = position) }

    /**
     * Change a playlist’s name and public/private state. (The user must, of course, own the playlist.)
     *
     * Modifying a public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * modifying a private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/change-playlist-details/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param name Optional. The name to change the playlist to.
     * @param public Optional. Whether to make the playlist public or not.
     * @param collaborative Optional. Whether to make the playlist collaborative or not.
     * @param description Optional. Whether to change the description or not.
     *
     * @throws BadRequestException if the playlist is not found or parameters exceed the max length
     */
    public suspend fun changeClientPlaylistDetails(
        playlist: String,
        name: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        description: String? = null
    ) {
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)

        val body = jsonMap()
        if (name != null) body += buildJsonObject { put("name", name) }
        if (public != null) body += buildJsonObject { put("public", public) }
        if (collaborative != null) body += buildJsonObject { put("collaborative", collaborative) }
        if (description != null) body += buildJsonObject { put("description", description) }
        require(body.isNotEmpty()) { "At least one option must not be null" }
        put(endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}").toString(), body.mapToJsonString())
    }

    /**
     * Change a playlist’s name and public/private state. (The user must, of course, own the playlist.)
     *
     * Modifying a public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * modifying a private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/change-playlist-details/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param name Optional. The name to change the playlist to.
     * @param public Optional. Whether to make the playlist public or not.
     * @param collaborative Optional. Whether to make the playlist collaborative or not.
     * @param description Optional. Whether to change the description or not.
     *
     * @throws BadRequestException if the playlist is not found or parameters exceed the max length
     */
    public fun changeClientPlaylistDetailsRestAction(
        playlist: String,
        name: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        description: String? = null
    ): SpotifyRestAction<Unit> =
        SpotifyRestAction { changeClientPlaylistDetails(playlist, name, public, collaborative, description) }

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * Private playlists are only retrievable for the current user and requires the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return collaborative playlists, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and requires the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-a-list-of-current-users-playlists/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first playable to return. Default: 0. Use with limit to get the next set of playables
     *
     * @throws BadRequestException if the filters provided are illegal
     */
    public suspend fun getClientPlaylists(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null
    ): PagingObject<SimplePlaylist> {
        require(!(limit != null && limit !in 1..50)) { "Limit must be between 1 and 50. Provided $limit" }
        require(!(offset != null && offset !in 0..100000)) { "Offset must be between 0 and 100,000. Provided $limit" }
        return get(endpointBuilder("/me/playlists").with("limit", limit).with("offset", offset).toString())
            .toNonNullablePagingObject(SimplePlaylist.serializer(), api = api, json = json)
    }

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * Private playlists are only retrievable for the current user and requires the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return collaborative playlists, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and requires the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-a-list-of-current-users-playlists/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first playable to return. Default: 0. Use with limit to get the next set of playables
     *
     * @throws BadRequestException if the filters provided are illegal
     */
    public fun getClientPlaylistsRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null
    ): SpotifyRestAction<PagingObject<SimplePlaylist>> = SpotifyRestAction { getClientPlaylists(limit, offset) }

    /**
     * Find a client playlist by its id. If you want to find multiple playlists, consider using [getClientPlaylists]
     *
     * **Note that** private playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return a collaborative playlist, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-a-list-of-current-users-playlists/)**
     *
     * @param id Playlist id or uri
     *
     * @return A possibly-null [SimplePlaylist] if the playlist doesn't exist
     */
    public suspend fun getClientPlaylist(id: String): SimplePlaylist? {
        val playlists = getClientPlaylists()
        return playlists.items.find { it.id == id } ?: playlists.getAllItems().find { it?.id == id }
    }

    /**
     * Find a client playlist by its id. If you want to find multiple playlists, consider using [getClientPlaylists]
     *
     * **Note that** private playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return a collaborative playlist, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-a-list-of-current-users-playlists/)**
     *
     * @param id Playlist id or uri
     *
     * @return A possibly-null [SimplePlaylist] if the playlist doesn't exist
     */
    public fun getClientPlaylistRestAction(id: String): SpotifyRestAction<SimplePlaylist?> =
        SpotifyRestAction { getClientPlaylist(id) }

    /**
     * This method is equivalent to unfollowing a playlist with the given [playlist].
     *
     * Unfortunately, Spotify does not allow **deletion** of playlists themselves
     *
     * @param playlist playlist id
     */
    public suspend fun deleteClientPlaylist(playlist: String): String =
        (api as SpotifyClientApi).following.unfollowPlaylist(PlaylistUri(playlist).id)

    /**
     * This method is equivalent to unfollowing a playlist with the given [playlist].
     *
     * Unfortunately, Spotify does not allow **deletion** of playlists themselves
     *
     * @param playlist playlist id
     */
    public fun deleteClientPlaylistRestAction(playlist: String): SpotifyRestAction<String> =
        SpotifyRestAction { deleteClientPlaylist(playlist) }

    /**
     * Reorder a playable or a group of playables in a playlist.
     *
     * When reordering playables, the timestamp indicating when they were added and the user who added them will be kept
     * untouched. In addition, the users following the playlists won’t be notified about changes in the playlists
     * when the playables are reordered.
     *
     * Reordering playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * reordering playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/reorder-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param reorderRangeStart The position of the first playable to be reordered.
     * @param reorderRangeLength The amount of playables to be reordered. Defaults to 1 if not set.
     * The range of playables to be reordered begins from the range_start position, and includes the range_length subsequent playables.
     * Example: To move the playables at index 9-10 to the start of the playlist, range_start is set to 9, and range_length is set to 2.
     * @param insertionPoint The position where the playables should be inserted. To reorder the playables to the end of the playlist, simply set insert_before to the position after the last track.
     * @param snapshotId the playlist snapshot against which to apply this action. **recommended to have**
     *
     * @throws BadRequestException if the playlist is not found or illegal filters are applied
     */
    public suspend fun reorderClientPlaylistPlayables(
        playlist: String,
        reorderRangeStart: Int,
        reorderRangeLength: Int? = null,
        insertionPoint: Int,
        snapshotId: String? = null
    ): PlaylistSnapshot {
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)

        val body = jsonMap()
        body += buildJsonObject { put("range_start", reorderRangeStart) }
        body += buildJsonObject { put("insert_before", insertionPoint) }
        if (reorderRangeLength != null) body += buildJsonObject { put("range_length", reorderRangeLength) }
        if (snapshotId != null) body += buildJsonObject { put("snapshot_id", snapshotId) }

        return put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
            body.mapToJsonString()
        ).toObject(PlaylistSnapshot.serializer(), api, json)
    }

    /**
     * Reorder a playable or a group of playables in a playlist.
     *
     * When reordering playables, the timestamp indicating when they were added and the user who added them will be kept
     * untouched. In addition, the users following the playlists won’t be notified about changes in the playlists
     * when the playables are reordered.
     *
     * Reordering playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * reordering playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/reorder-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param reorderRangeStart The position of the first playable to be reordered.
     * @param reorderRangeLength The amount of playables to be reordered. Defaults to 1 if not set.
     * The range of playables to be reordered begins from the range_start position, and includes the range_length subsequent playables.
     * Example: To move the playables at index 9-10 to the start of the playlist, range_start is set to 9, and range_length is set to 2.
     * @param insertionPoint The position where the playables should be inserted. To reorder the playables to the end of the playlist, simply set insert_before to the position after the last track.
     * @param snapshotId the playlist snapshot against which to apply this action. **recommended to have**
     *
     * @throws BadRequestException if the playlist is not found or illegal filters are applied
     */
    public fun reorderClientPlaylistPlayablesRestAction(
        playlist: String,
        reorderRangeStart: Int,
        reorderRangeLength: Int? = null,
        insertionPoint: Int,
        snapshotId: String? = null
    ): SpotifyRestAction<PlaylistSnapshot> = SpotifyRestAction {
        reorderClientPlaylistPlayables(
            playlist,
            reorderRangeStart,
            reorderRangeLength,
            insertionPoint,
            snapshotId
        )
    }

    /**
     * Replace all the playables in a playlist, overwriting its existing playables. This powerful request can be useful
     * for replacing playables, re-ordering existing playables, or clearing the playlist.
     *
     * Setting playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables The Spotify playable uris. Maximum **100** without bulk requesting enabled.
     *
     * @throws BadRequestException if playlist is not found or illegal playables are provided
     */
    public suspend fun setClientPlaylistPlayables(playlist: String, vararg playables: PlayableUri) {
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)

        val body = jsonMap()
        body += buildJsonObject {
            put(
                "uris",
                JsonArray(playables.map { it.uri }.map(::JsonPrimitive))
            )
        }
        put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
            body.mapToJsonString()
        )
    }

    /**
     * Replace all the playables in a playlist, overwriting its existing playables. This powerful request can be useful
     * for replacing playables, re-ordering existing playables, or clearing the playlist.
     *
     * Setting playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables The Spotify playable uris. Maximum **100** without bulk requesting enabled.
     *
     * @throws BadRequestException if playlist is not found or illegal playables are provided
     */
    public fun setClientPlaylistPlayablesRestAction(playlist: String, vararg playables: PlayableUri): SpotifyRestAction<Unit> =
        SpotifyRestAction {
            setClientPlaylistPlayables(playlist, *playables)
        }

    /**
     * Replace all the playables in a playlist, overwriting its existing playables. This powerful request can be useful
     * for replacing playables, re-ordering existing playables, or clearing the playlist.
     *
     * Setting playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables The Spotify playable uris. Maximum **100** without bulk requesting enabled.
     *
     * @throws BadRequestException if playlist is not found or illegal playables are provided
     */
    public suspend fun replaceClientPlaylistPlayables(playlist: String, vararg playables: PlayableUri): Unit =
        setClientPlaylistPlayables(playlist, *playables)

    /**
     * Replace all the playables in a playlist, overwriting its existing playables. This powerful request can be useful
     * for replacing playables, re-ordering existing playables, or clearing the playlist.
     *
     * Setting playables in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting playables in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param playables The Spotify playable uris. Maximum **100** without bulk requesting enabled.
     *
     * @throws BadRequestException if playlist is not found or illegal playables are provided
     */
    public fun replaceClientPlaylistPlayablesRestAction(playlist: String, vararg playables: PlayableUri): SpotifyRestAction<Unit> =
        SpotifyRestAction {
            replaceClientPlaylistPlayables(playlist, *playables)
        }

    /**
     * Remove all the playables in a playlist
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist the id or uri for the playlist.
     */
    public suspend fun removeAllClientPlaylistPlayables(playlist: String) {
        setClientPlaylistPlayables(playlist)
    }

    /**
     * Remove all the playables in a playlist
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist the id or uri for the playlist.
     */
    public fun removeAllClientPlaylistPlayablesRestAction(playlist: String): SpotifyRestAction<Unit> = SpotifyRestAction {
        removeAllClientPlaylistPlayables(playlist)
    }

    /**
     * Replace the image used to represent a specific playlist. Image type **must** be jpeg.
     *
     * You must specify a JPEG image path or image data, maximum payload size is 256 KB
     *
     * **Required conditions**: This access token must be tied to the user who owns the playlist, and must have the
     * scope [ugc-image-upload][SpotifyScope.UGC_IMAGE_UPLOAD] granted. In addition, the token must also
     * contain [playlist-modify-public][SpotifyScope.PLAYLIST_MODIFY_PUBLIC] and/or
     * [playlist-modify-private][SpotifyScope.PLAYLIST_MODIFY_PRIVATE], depending on the
     * public status of the playlist you want to update.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/upload-custom-playlist-cover/)**
     *
     * @param playlist the id or uri for the playlist.
     * @param imagePath Optionally specify the full local path to the image
     * @param imageUrl Optionally specify a URL to the image
     * @param imageFile Optionally specify the image [VfsFile]. Note that in each platform, there is a [toVfs] method to convert
     * the platform's file type to a [VfsFile]. For example, `java.util.File.toVfs()` will return a [VfsFile].
     * @param image Optionally specify the image's [BufferedImage] object
     * @param imageData Optionally specify the Base64-encoded image data yourself
     *
     * @throws BadRequestException if invalid data is provided
     */
    public suspend fun uploadClientPlaylistCover(
        playlist: String,
        imagePath: String? = null,
        imageFile: VfsFile? = null,
        image: BufferedImage? = null,
        imageData: String? = null,
        imageUrl: String? = null
    ) {
        requireScopes(SpotifyScope.UGC_IMAGE_UPLOAD)
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)

        val data = imageData ?: when {
            image != null -> convertBufferedImageToBase64JpegString(image)
            imageFile != null -> convertBufferedImageToBase64JpegString(convertFileToBufferedImage(imageFile))
            imageUrl != null -> convertBufferedImageToBase64JpegString(convertUrlPathToBufferedImage(imageUrl))
            imagePath != null -> convertBufferedImageToBase64JpegString(convertLocalImagePathToBufferedImage(imagePath))
            else -> throw IllegalArgumentException("No cover image was specified")
        }
        put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/images").toString(),
            data, contentType = "image/jpeg"
        )
    }

    /**
     * Replace the image used to represent a specific playlist. Image type **must** be jpeg.
     *
     * You must specify a JPEG image path or image data, maximum payload size is 256 KB
     *
     * **Required conditions**: This access token must be tied to the user who owns the playlist, and must have the
     * scope [ugc-image-upload][SpotifyScope.UGC_IMAGE_UPLOAD] granted. In addition, the token must also
     * contain [playlist-modify-public][SpotifyScope.PLAYLIST_MODIFY_PUBLIC] and/or
     * [playlist-modify-private][SpotifyScope.PLAYLIST_MODIFY_PRIVATE], depending on the
     * public status of the playlist you want to update.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/upload-custom-playlist-cover/)**
     *
     * @param playlist the id or uri for the playlist.
     * @param imagePath Optionally specify the full local path to the image
     * @param imageUrl Optionally specify a URL to the image
     * @param imageFile Optionally specify the image [VfsFile]. Note that in each platform, there is a [toVfs] method to convert
     * the platform's file type to a [VfsFile]. For example, `java.util.File.toVfs()` will return a [VfsFile].
     * @param image Optionally specify the image's [BufferedImage] object
     * @param imageData Optionally specify the Base64-encoded image data yourself
     *
     * @throws BadRequestException if invalid data is provided
     */
    public fun uploadClientPlaylistCoverRestAction(
        playlist: String,
        imagePath: String? = null,
        imageFile: VfsFile? = null,
        image: BufferedImage? = null,
        imageData: String? = null,
        imageUrl: String? = null
    ): SpotifyRestAction<Unit> =
        SpotifyRestAction { uploadClientPlaylistCover(playlist, imagePath, imageFile, image, imageData, imageUrl) }

    /**
     * Remove a playable in the specified positions (zero-based) from the specified playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playable The playable uri
     * @param positions The positions at which the playable is located in the playlist
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removePlayableFromClientPlaylist(
        playlist: String,
        playable: PlayableUri,
        positions: SpotifyPlayablePositions,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlayablesFromClientPlaylist(playlist, playable to positions, snapshotId = snapshotId)

    /**
     * Remove a playable in the specified positions (zero-based) from the specified playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playable The playable uri
     * @param positions The positions at which the playable is located in the playlist
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public fun removePlayableFromClientPlaylistRestAction(
        playlist: String,
        playable: PlayableUri,
        positions: SpotifyPlayablePositions,
        snapshotId: String? = null
    ): SpotifyRestAction<PlaylistSnapshot> =
        SpotifyRestAction { removePlayableFromClientPlaylist(playlist, playable, positions, snapshotId) }

    /**
     * Remove all occurrences of a playable from the specified playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playable The playable uri
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removePlayableFromClientPlaylist(
        playlist: String,
        playable: PlayableUri,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlayablesFromClientPlaylist(playlist, playable, snapshotId = snapshotId)

    /**
     * Remove all occurrences of a playable from the specified playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playable The playable uri
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public fun removePlayableFromClientPlaylistRestAction(
        playlist: String,
        playable: PlayableUri,
        snapshotId: String? = null
    ): SpotifyRestAction<PlaylistSnapshot> =
        SpotifyRestAction { removePlayableFromClientPlaylist(playlist, playable, snapshotId) }

    /**
     * Remove all occurrences of the specified playables from the given playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playables An array of playable uris. Maximum **100** without bulk requesting.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removePlayablesFromClientPlaylist(
        playlist: String,
        vararg playables: PlayableUri,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlaylistPlayablesImpl(playlist, playables.map { it to null }.toTypedArray(), snapshotId)

    /**
     * Remove all occurrences of the specified playables from the given playlist.
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playables An array of playable uris. Maximum **100** without bulk requesting.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public fun removePlayablesFromClientPlaylistRestAction(
        playlist: String,
        vararg playables: PlayableUri,
        snapshotId: String? = null
    ): SpotifyRestAction<PlaylistSnapshot> =
        SpotifyRestAction { removePlayablesFromClientPlaylist(playlist, *playables, snapshotId = snapshotId) }

    /**
     * Remove playables (each with their own positions) from the given playlist. **Bulk requesting is only available when [snapshotId] is null.**
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playables An array of [Pair]s of playable uris *and* playable positions (zero-based). Maximum **100**.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removePlayablesFromClientPlaylist(
        playlist: String,
        vararg playables: Pair<PlayableUri, SpotifyPlayablePositions>,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlaylistPlayablesImpl(playlist, playables.toList().toTypedArray(), snapshotId)

    /**
     * Remove playables (each with their own positions) from the given playlist. **Bulk requesting is only available when [snapshotId] is null.**
     *
     * Removing playables from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing playables from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param playables An array of [Pair]s of playable uris *and* playable positions (zero-based). Maximum **100**.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public fun removePlayablesFromClientPlaylistRestAction(
        playlist: String,
        vararg playables: Pair<PlayableUri, SpotifyPlayablePositions>,
        snapshotId: String? = null
    ): SpotifyRestAction<PlaylistSnapshot> =
        SpotifyRestAction { removePlayablesFromClientPlaylist(playlist, *playables, snapshotId = snapshotId) }

    private suspend fun removePlaylistPlayablesImpl(
        playlist: String,
        playables: Array<Pair<PlayableUri, SpotifyPlayablePositions?>>,
        snapshotId: String?
    ): PlaylistSnapshot {
        requireScopes(SpotifyScope.PLAYLIST_MODIFY_PUBLIC, SpotifyScope.PLAYLIST_MODIFY_PRIVATE, anyOf = true)
        checkBulkRequesting(100, playables.size)
        if (snapshotId != null && playables.size > 100) throw BadRequestException("You cannot provide both the snapshot id and attempt bulk requesting")

        return bulkStatefulRequest(100, playables.toList()) { chunk ->
            val body = jsonMap()
            if (snapshotId != null) body += buildJsonObject { put("snapshot_id", snapshotId) }
            body += buildJsonObject {
                put(
                    "tracks", JsonArray(
                        chunk.map { (playable, positions) ->
                            val json = jsonMap()
                            json += buildJsonObject { put("uri", playable.uri) }
                            if (positions?.positions?.isNotEmpty() == true) json += buildJsonObject {
                                put(
                                    "positions", JsonArray(
                                        positions.positions.map(::JsonPrimitive)
                                    )
                                )
                            }
                            JsonObject(json)
                        })
                )
            }
            delete(
                endpointBuilder("/playlists/${PlaylistUri(playlist).id}/tracks").toString(),
                body = body.mapToJsonString()
            ).toObject(PlaylistSnapshot.serializer(), api, json)
        }.last()
    }
}

/**
 * Contains the snapshot id, returned from API responses
 *
 * @param snapshotId The playlist state identifier
 */
@Serializable
public data class PlaylistSnapshot(@SerialName("snapshot_id") val snapshotId: String)

/**
 * Represents the positions inside a playlist's playables list of where to locate the playable
 *
 * @param positions Playable positions (zero-based)
 */
public class SpotifyPlayablePositions(public vararg val positions: Int)
