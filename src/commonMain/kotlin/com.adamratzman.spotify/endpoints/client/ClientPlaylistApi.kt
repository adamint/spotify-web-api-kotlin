/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.public.PlaylistApi
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toJson
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.BufferedImage
import com.adamratzman.spotify.utils.File
import com.adamratzman.spotify.utils.convertFileToBufferedImage
import com.adamratzman.spotify.utils.convertLocalImagePathToBufferedImage
import com.adamratzman.spotify.utils.convertUrlPathToBufferedImage
import com.adamratzman.spotify.utils.encodeBufferedImageToBase64String
import com.adamratzman.spotify.utils.jsonMap
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
     * Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)
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
     * @return The created [Playlist] object with no tracks
     */
    public suspend fun createClientPlaylist(
        name: String,
        description: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        user: String? = null
    ): Playlist {
        if (name.isEmpty()) throw BadRequestException(ErrorObject(400, "Name cannot be empty"))
        val body = jsonMap()
        body += buildJsonObject { put("name", name) }
        if (description != null) body += buildJsonObject { put("description", description) }
        if (public != null) body += buildJsonObject { put("public", public) }
        if (collaborative != null) body += buildJsonObject { put("collaborative", collaborative) }

        return post(
            endpointBuilder("/users/${UserUri(user ?: (api as SpotifyClientApi).getUserId()).id.encodeUrl()}/playlists").toString(),
            body.toJson()
        ).toObject(Playlist.serializer(), api, json)
    }

    /**
     * Add a track to a user’s playlist.
     *
     * Adding tracks to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding tracks to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param track Track id or uri
     * @param position The position to insert the tracks, a zero-based index. For example, to insert the tracks in the
     * first position: position=0; to insert the tracks in the third position: position=2. If omitted, the tracks will
     * be appended to the playlist. Tracks are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid track ids is provided or the playlist is not found
     */

    public suspend fun addTrackToClientPlaylist(playlist: String, track: String, position: Int? = null): Unit =
        addTracksToClientPlaylist(playlist, track, position = position)

    /**
     * Add one or more tracks to a user’s playlist.
     *
     * Adding tracks to the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * adding tracks to the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param tracks Spotify track ids. Maximum 100
     * @param position The position to insert the tracks, a zero-based index. For example, to insert the tracks in the
     * first position: position=0; to insert the tracks in the third position: position=2. If omitted, the tracks will
     * be appended to the playlist. Tracks are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid track ids is provided or the playlist is not found
     */
    public suspend fun addTracksToClientPlaylist(playlist: String, vararg tracks: String, position: Int? = null) {
        checkBulkRequesting(100, tracks.size)

        bulkRequest(100, tracks.toList()) { chunk ->
            val body = jsonMap()
            body += buildJsonObject {
                put(
                    "uris",
                    JsonArray(chunk.map { PlayableUri(PlayableUri(it).id.encodeUrl()).uri }.map(::JsonPrimitive))
                )
            }
            if (position != null) body += buildJsonObject { put("position", position) }
            post(
                endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
                body.toJson()
            )
        }
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
    public suspend fun changeClientPlaylistDetails(
        playlist: String,
        name: String? = null,
        public: Boolean? = null,
        collaborative: Boolean? = null,
        description: String? = null
    ) {
        val body = jsonMap()
        if (name != null) body += buildJsonObject { put("name", name) }
        if (public != null) body += buildJsonObject { put("public", public) }
        if (collaborative != null) body += buildJsonObject { put("collaborative", collaborative) }
        if (description != null) body += buildJsonObject { put("description", description) }
        require(body.isNotEmpty()) { "At least one option must not be null" }
        put(endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}").toString(), body.toJson())
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
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
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
            .toPagingObject(SimplePlaylist.serializer(), endpoint = this, json = json)
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
    public suspend fun getClientPlaylist(id: String): SimplePlaylist? {
        val playlists = getClientPlaylists()
        return playlists.items.find { it.id == id } ?: playlists.getAllItems().find { it?.id == id }
    }

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
     * Reorder a track or a group of tracks in a playlist.
     *
     * When reordering tracks, the timestamp indicating when they were added and the user who added them will be kept
     * untouched. In addition, the users following the playlists won’t be notified about changes in the playlists
     * when the tracks are reordered.
     *
     * Reordering tracks in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * reordering tracks in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/reorder-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param reorderRangeStart The position of the first track to be reordered.
     * @param reorderRangeLength The amount of tracks to be reordered. Defaults to 1 if not set.
     * The range of tracks to be reordered begins from the range_start position, and includes the range_length subsequent tracks.
     * Example: To move the tracks at index 9-10 to the start of the playlist, range_start is set to 9, and range_length is set to 2.
     * @param insertionPoint The position where the tracks should be inserted. To reorder the tracks to the end of the playlist, simply set insert_before to the position after the last track.
     * @param snapshotId the playlist snapshot against which to apply this action. **recommended to have**
     *
     * @throws BadRequestException if the playlist is not found or illegal filters are applied
     */
    public suspend fun reorderClientPlaylistTracks(
        playlist: String,
        reorderRangeStart: Int,
        reorderRangeLength: Int? = null,
        insertionPoint: Int,
        snapshotId: String? = null
    ): PlaylistSnapshot {
        val body = jsonMap()
        body += buildJsonObject { put("range_start", reorderRangeStart) }
        body += buildJsonObject { put("insert_before", insertionPoint) }
        if (reorderRangeLength != null) body += buildJsonObject { put("range_length", reorderRangeLength) }
        if (snapshotId != null) body += buildJsonObject { put("snapshot_id", snapshotId) }

        return put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
            body.toJson()
        ).toObject(PlaylistSnapshot.serializer(), api, json)
    }

    /**
     * Replace all the tracks in a playlist, overwriting its existing tracks. This powerful request can be useful
     * for replacing tracks, re-ordering existing tracks, or clearing the playlist.
     *
     * Setting tracks in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting tracks in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param tracks The Spotify track ids. Maximum **100**.
     *
     * @throws BadRequestException if playlist is not found or illegal tracks are provided
     */
    public suspend fun setClientPlaylistTracks(playlist: String, vararg tracks: String) {
        val body = jsonMap()
        body += buildJsonObject {
            put(
                "uris",
                JsonArray(tracks.map { PlayableUri(PlayableUri(it).id.encodeUrl()).uri }.map(::JsonPrimitive))
            )
        }
        put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").toString(),
            body.toJson()
        )
    }

    /**
     * Replace all the tracks in a playlist, overwriting its existing tracks. This powerful request can be useful
     * for replacing tracks, re-ordering existing tracks, or clearing the playlist.
     *
     * Setting tracks in the current user’s public playlists requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * setting tracks in the current user’s private playlist (including collaborative playlists) requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param tracks The Spotify track ids. Maximum **100**.
     *
     * @throws BadRequestException if playlist is not found or illegal tracks are provided
     */
    public suspend fun replaceClientPlaylistTracks(playlist: String, vararg tracks: String): Unit =
        setClientPlaylistTracks(playlist, *tracks)

    /**
     * Remove all the tracks in a playlist
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/replace-playlists-tracks/)**
     *
     * @param playlist the id or uri for the playlist.
     */
    public suspend fun removeAllClientPlaylistTracks(playlist: String) {
        setClientPlaylistTracks(playlist)
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
     * @param imageFile Optionally specify the image [File]
     * @param image Optionally specify the image's [BufferedImage] object
     * @param imageData Optionally specify the Base64-encoded image data yourself
     *
     * @throws BadRequestException if invalid data is provided
     */
    public suspend fun uploadClientPlaylistCover(
        playlist: String,
        imagePath: String? = null,
        imageFile: File? = null,
        image: BufferedImage? = null,
        imageData: String? = null,
        imageUrl: String? = null
    ) {
        val data = imageData ?: when {
            image != null -> encodeBufferedImageToBase64String(image)
            imageFile != null -> encodeBufferedImageToBase64String(convertFileToBufferedImage(imageFile))
            imageUrl != null -> encodeBufferedImageToBase64String(convertUrlPathToBufferedImage(imageUrl))
            imagePath != null -> encodeBufferedImageToBase64String(convertLocalImagePathToBufferedImage(imagePath))
            else -> throw IllegalArgumentException("No cover image was specified")
        }
        put(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/images").toString(),
            data, contentType = "image/jpeg"
        )
    }

    /**
     * Remove a track in the specified positions (zero-based) from the specified playlist.
     *
     * Removing tracks from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing tracks from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param track The track id
     * @param positions The positions at which the track is located in the playlist
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removeTrackFromClientPlaylist(
        playlist: String,
        track: String,
        positions: SpotifyTrackPositions,
        snapshotId: String? = null
    ): PlaylistSnapshot = removeTracksFromClientPlaylist(playlist, track to positions, snapshotId = snapshotId)

    /**
     * Remove all occurrences of a track from the specified playlist.
     *
     * Removing tracks from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing tracks from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param track The track id
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removeTrackFromClientPlaylist(
        playlist: String,
        track: String,
        snapshotId: String? = null
    ): PlaylistSnapshot = removeTracksFromClientPlaylist(playlist, track, snapshotId = snapshotId)

    /**
     * Remove all occurrences of the specified tracks from the given playlist.
     *
     * Removing tracks from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing tracks from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param tracks An array of track ids. Maximum **100**.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removeTracksFromClientPlaylist(
        playlist: String,
        vararg tracks: String,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlaylistTracksImpl(playlist, tracks.map { it to null }.toTypedArray(), snapshotId)

    /**
     * Remove tracks (each with their own positions) from the given playlist. **Bulk requesting is only available when [snapshotId] is null.**
     *
     * Removing tracks from a user’s public playlist requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * removing tracks from a private playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/remove-tracks-playlist/)**
     *
     * @param playlist The playlist id
     * @param tracks An array of [Pair]s of track ids *and* track positions (zero-based). Maximum **100**.
     * @param snapshotId The playlist snapshot against which to apply this action. **recommended to have**
     */
    public suspend fun removeTracksFromClientPlaylist(
        playlist: String,
        vararg tracks: Pair<String, SpotifyTrackPositions>,
        snapshotId: String? = null
    ): PlaylistSnapshot = removePlaylistTracksImpl(playlist, tracks.toList().toTypedArray(), snapshotId)

    private suspend fun removePlaylistTracksImpl(
        playlist: String,
        tracks: Array<Pair<String, SpotifyTrackPositions?>>,
        snapshotId: String?
    ): PlaylistSnapshot {
        checkBulkRequesting(100, tracks.size)
        if (snapshotId != null && tracks.size > 100) throw BadRequestException("You cannot provide both the snapshot id and attempt bulk requesting")

        return bulkRequest(100, tracks.toList()) { chunk ->
            val body = jsonMap()
            if (snapshotId != null) body += buildJsonObject { put("snapshot_id", snapshotId) }
            body += buildJsonObject {
                put(
                    "tracks", JsonArray(
                        chunk.map { (track, positions) ->
                            val json = jsonMap()
                            json += buildJsonObject { put("uri", PlayableUri(track).uri) }
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
                endpointBuilder("/playlists/${PlaylistUri(playlist).id}/tracks").toString(), body = body.toJson()
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
 * Represents the positions inside a playlist's items list of where to locate the track
 *
 * @param positions Track positions (zero-based)
 */
public class SpotifyTrackPositions(public vararg val positions: Int)
