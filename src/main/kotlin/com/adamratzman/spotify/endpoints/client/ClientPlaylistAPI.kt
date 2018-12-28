package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.endpoints.public.PlaylistsAPI
import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.SpotifyRestPagingAction
import com.adamratzman.spotify.utils.*
import org.json.JSONObject
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import java.util.function.Supplier
import javax.imageio.IIOException
import javax.imageio.ImageIO
import javax.xml.bind.DatatypeConverter


/**
 * Endpoints for retrieving information about a user’s playlists and for managing a user’s playlists.
 */
class ClientPlaylistAPI(api: SpotifyAPI) : PlaylistsAPI(api) {
    /**
     * Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)
     *
     * @param userId The user’s Spotify user ID.
     * @param name The name for the new playlist, for example "Your Coolest Playlist" . This name does not need to be
     * unique; a user may have several playlists with the same name.
     * @param description
     * @param public Defaults to true . If true the playlist will be public, if false it will be private.
     * To be able to create private playlists, the user must have granted the playlist-modify-private scope.
     * @param collaborative Defaults to false . If true the playlist will be collaborative. Note that to create a
     * collaborative playlist you must also set public to false . To create collaborative playlists you must have
     * granted playlist-modify-private and playlist-modify-public scopes.
     *
     * @return The created [Playlist] object with no tracks
     */
    fun createPlaylist(name: String, description: String? = null, public: Boolean? = null, collaborative: Boolean? = null, userId: String = (api as SpotifyClientAPI).userId): SpotifyRestAction<Playlist> {
        if (name.isEmpty()) throw BadRequestException(ErrorObject(400, "Name cannot be empty"))
        return toAction(Supplier {
            val json = JSONObject()
            json.put("name", name)
            if (description != null) json.put("description", description)
            if (public != null) json.put("public", public)
            if (collaborative != null) json.put("collaborative", collaborative)
            post(EndpointBuilder("/users/${userId.encode()}/playlists").toString(), json.toString()).toObject(api, Playlist::class.java)
        })
    }

    /**
     * Add one or more tracks to a user’s playlist.
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param ids Spotify track ids. A maximum of 100 tracks can be added in one request.
     * @param position The position to insert the tracks, a zero-based index. For example, to insert the tracks in the
     * first position: position=0; to insert the tracks in the third position: position=2 . If omitted, the tracks will
     * be appended to the playlist. Tracks are added in the order they are listed in the query string or request body.
     *
     * @throws BadRequestException if any invalid track ids is provided or the playlist is not found
     */
    fun addTracksToPlaylist(playlistId: String, vararg ids: String, position: Int? = null): SpotifyRestAction<Unit> {
        val json = JSONObject().put("uris", ids.map { "spotify:track:${it.encode()}" })
        if (position != null) json.put("position", position)
        return toAction(Supplier {
            post(EndpointBuilder("/playlists/${playlistId.encode()}/tracks").toString(), json.toString())
            Unit
        })
    }

    /**
     * Change a playlist’s name and public/private state. (The user must, of course, own the playlist.)
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param name Optional. The name to change the playlist to.
     * @param public Optional. Whether to make the playlist public or not.
     * @param collaborative Optional. Whether to make the playlist collaborative or not.
     * @param description Optional. Whether to change the description or not.
     *
     * @throws BadRequestException if the playlist is not found or parameters exceed the max length
     */
    fun changePlaylistDescription(playlistId: String, name: String? = null, public: Boolean? = null, collaborative: Boolean? = null,
                                  description: String? = null): SpotifyRestAction<Unit> {
        val json = JSONObject()
        if (name != null) json.put("name", name)
        if (public != null) json.put("public", public)
        if (collaborative != null) json.put("collaborative", collaborative)
        if (description != null) json.put("description", description)
        if (json.length() == 0) throw IllegalArgumentException("At least one option must not be null")
        return toAction(Supplier {
            put(EndpointBuilder("/playlists/${playlistId.encode()}").toString(), json.toString())
            Unit
        })
    }

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * @param limit The maximum number of tracks to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first track to return. Default: 0 (the first object). Use with limit to get the next set of tracks.
     *
     * @throws BadRequestException if the filters provided are illegal
     */
    fun getClientPlaylists(limit: Int? = null, offset: Int? = null): SpotifyRestPagingAction<SimplePlaylist, PagingObject<SimplePlaylist>> {
        if (limit != null && limit !in 1..50) throw IllegalArgumentException("Limit must be between 1 and 50. Provided $limit")
        if (offset != null && offset !in 0..100000) throw IllegalArgumentException("Offset must be between 0 and 100,000. Provided $limit")
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/me/playlists").with("limit", limit).with("offset", offset).toString())
                    .toPagingObject(endpoint = this, tClazz = SimplePlaylist::class.java)
        })
    }

    /**
     * Find a client playlist by its id. Convenience method
     *
     * @param id the Spotify identifier of the ID
     *
     * @return possibly-null SimplePlaylist
     */
    fun getClientPlaylist(id: String): SpotifyRestAction<SimplePlaylist?> {
        return toAction(Supplier {
            val playlists = getClientPlaylists().completeWithPaging()
            playlists.items.find { it.id == id } ?: playlists.getAllItems().complete().find { it.id == id }
        })
    }

    /**
     * This method is equivalent to unfollowing a playlist with the given [id] and [ownerId].
     *
     * Unfortunately, Spotify does not allow **deletion** of playlists themselves
     *
     * @param id playlist id
     * @param ownerId the owner of this playlist. Ignore if it's the authenticated user
     */
    fun deletePlaylist(id: String): SpotifyRestAction<Unit> {
        api as SpotifyClientAPI
        return api.following.unfollowPlaylist(id)
    }

    /**
     * Reorder a track or a group of tracks in a playlist.
     *
     * When reordering tracks, the timestamp indicating when they were added and the user who added them will be kept
     * untouched. In addition, the users following the playlists won’t be notified about changes in the playlists
     * when the tracks are reordered.
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param reorderRangeStart The position of the first track to be reordered.
     * @param reorderRangeLength The amount of tracks to be reordered. Defaults to 1 if not set.
     * The range of tracks to be reordered begins from the range_start position, and includes the range_length subsequent tracks.
     * Example: To move the tracks at index 9-10 to the start of the playlist, range_start is set to 9, and range_length is set to 2.
     * @param insertionPoint
     * @param snapshotId
     *
     * @throws BadRequestException if the playlist is not found or illegal filters are applied
     */
    fun reorderTracks(playlistId: String, reorderRangeStart: Int, reorderRangeLength: Int? = null, insertionPoint: Int,
                      snapshotId: String? = null): SpotifyRestAction<Snapshot> {
        return toAction(Supplier {
            val json = JSONObject()
            json.put("range_start", reorderRangeStart)
            json.put("insert_before", insertionPoint)
            if (reorderRangeLength != null) json.put("range_length", reorderRangeLength)
            if (snapshotId != null) json.put("snapshot_id", snapshotId)
            put(EndpointBuilder("/playlists/${playlistId.encode()}/tracks").toString(), json.toString())
                    .toObject(api, Snapshot::class.java)
        })
    }

    /**
     * Replace all the tracks in a playlist, overwriting its existing tracks. This powerful request can be useful
     * for replacing tracks, re-ordering existing tracks, or clearing the playlist.
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param trackIds The Spotify track ids.
     *
     * @throws BadRequestException if playlist is not found or illegal tracks are provided
     */
    fun setPlaylistTracks(playlistId: String, vararg trackIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            val json = JSONObject()
            json.put("uris", trackIds.map { "spotify:track:${it.encode()}" })
            put(EndpointBuilder("/playlists/${playlistId.encode()}/tracks").toString(),
                    json.toString())
            Unit
        })
    }

    /**
     * Remove all the tracks in a playlist
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     */
    fun removeAllPlaylistTracks(playlistId: String): SpotifyRestAction<Unit> {
        return setPlaylistTracks(playlistId)
    }

    /**
     * Replace the image used to represent a specific playlist. Image type **must** be jpeg.
     *
     * Must specify a JPEG image path or image data, maximum payload size is 256 KB
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param imagePath Optionally specify the full local path to the image
     * @param imageUrl Optionally specify a URL to the image
     * @param imageFile Optionally specify the image [File]
     * @param image Optionally specify the image's [BufferedImage] object
     * @param imageData Optionally specify the Base64-encoded image data yourself
     *
     * @throws IIOException if the image is not found
     * @throws BadRequestException if invalid data is provided
     */
    fun uploadPlaylistCover(playlistId: String, imagePath: String? = null,
                            imageFile: File? = null, image: BufferedImage? = null, imageData: String? = null,
                            imageUrl: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            val data = imageData ?: when {
                image != null -> encode(image)
                imageFile != null -> encode(ImageIO.read(imageFile))
                imageUrl != null -> encode(ImageIO.read(URL(imageUrl)))
                imagePath != null -> encode(ImageIO.read(URL("file:///$imagePath")))
                else -> throw IllegalArgumentException("No cover image was specified")
            }
            put(EndpointBuilder("/playlists/${playlistId.encode()}/images").toString(),
                    data, contentType = "image/jpeg")
            Unit
        })
    }

    /*
    fun removeAllOccurances(userId: String, playlistId: String, vararg trackIds: String): SpotifyRestAction<Unit> {
        if (trackIds.isEmpty()) throw IllegalArgumentException("Tracks to remove must not be empty")
        return toAction(Supplier {
            val json = JSONObject()
            json.put("tracks", trackIds.map { JSONObject().put("uri", "spotify:track:$it") })
            println(json.toString())
            delete("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks",
                    body =  json.toString(), contentType = "application/json")
            Unit
        })
    }
*/
    private fun encode(image: BufferedImage): String {
        val bos = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", bos)
        bos.close()
        return DatatypeConverter.printBase64Binary(bos.toByteArray())
    }

    data class Snapshot(val snapshot_id: String)
}