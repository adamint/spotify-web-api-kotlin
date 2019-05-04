/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.endpoints.client.ClientPlaylistAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.beust.klaxon.Json

/**
 * @property collaborative Returns true if context is not search and the owner allows other users to
 * modify the playlist. Otherwise returns false.
 * @property externalUrls Known external URLs for this playlist.
 * @property href A link to the Web API endpoint providing full details of the playlist.
 * @property id The Spotify ID for the playlist.
 * @property images Images for the playlist. The array may be empty or contain up to three images.
 * The images are returned by size in descending order. See Working with Playlists.
 * Note: If returned, the source URL for the image ( url ) is temporary and will expire in less than a day.
 * @property name The name of the playlist.
 * @property owner The user who owns the playlist
 * @property primaryColor Unknown.
 * @property public The playlist’s public/private status: true the playlist is public, false the
 * playlist is private, null the playlist status is not relevant.
 * @property tracks A collection containing a link ( href ) to the Web API endpoint where full details of the
 * playlist’s tracks can be retrieved, along with the total number of tracks in the playlist.
 * @property type The object type: “playlist”
 * @property uri The Spotify URI for the playlist.
 * @property snapshot The version identifier for the current playlist. Can be supplied in other
 * requests to target a specific playlist version
 */
data class SimplePlaylist(
    val collaborative: Boolean,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser,
    @Json(name = "primary_color") val primaryColor: String? = null,
    val public: Boolean? = null,
    @Json(name = "snapshot_id", ignored = false) private val _snapshotId: String,
    val tracks: PlaylistTrackInfo,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: PlaylistURI = PlaylistURI(_uri),
    @Json(ignored = true) val snapshot: ClientPlaylistAPI.Snapshot = ClientPlaylistAPI.Snapshot(_snapshotId)
) : Linkable() {
    fun toFullPlaylist(market: Market? = null): SpotifyRestAction<Playlist?> = api.playlists.getPlaylist(id, market)
}

/**
 * @property primaryColor Unknown. Spotify has released no information about this
 * @property addedAt The date and time the track was added. Note that some very old playlists may return null in this field.
 * @property addedBy The Spotify user who added the track. Note that some very old playlists may return null in this field.
 * @property isLocal Whether this track is a local file or not.
 * @property track Information about the track.
 */
data class PlaylistTrack(
    @Json(name = "primary_color") val primaryColor: String? = null,
    @Json(name = "added_at") val addedAt: String?,
    @Json(name = "added_by") val addedBy: SpotifyPublicUser?,
    @Json(name = "is_local") val isLocal: Boolean?,
    val track: Track,
    @Json(name = "video_thumbnail") val videoThumbnail: VideoThumbnail? = null
)

/**
 * @param collaborative Returns true if context is not search and the owner allows other users to modify the playlist.
 * Otherwise returns false.
 * @param description The playlist description. Only returned for modified, verified playlists, otherwise null.
 * @param externalUrls Known external URLs for this playlist.
 * @param followers
 * @param href A link to the Web API endpoint providing full details of the playlist.
 * @param id The Spotify ID for the playlist.
 * @param primaryColor Unknown.
 * @param images Images for the playlist. The array may be empty or contain up to three images.
 * The images are returned by size in descending order.Note: If returned, the source URL for the
 * image ( url ) is temporary and will expire in less than a day.
 * @param name The name of the playlist.
 * @param owner The user who owns the playlist
 * @param public The playlist’s public/private status: true the playlist is public, false the playlist is private,
 * null the playlist status is not relevant
 * @param snapshot The version identifier for the current playlist. Can be supplied in other requests to target
 * a specific playlist version
 * @param tracks Information about the tracks of the playlist.
 * @param type The object type: “playlist”
 * @param uri The Spotify URI for the playlist.
 */
data class Playlist(
    val collaborative: Boolean,
    val description: String,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers,
    val href: String,
    val id: String,
    @Json(name = "primary_color") val primaryColor: String? = null,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser,
    val public: Boolean? = null,
    @Json(name = "snapshot_id", ignored = false) private val _snapshotId: String,
    val tracks: PagingObject<PlaylistTrack>,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: PlaylistURI = PlaylistURI(_uri),
    @Json(ignored = true) val snapshot: ClientPlaylistAPI.Snapshot = ClientPlaylistAPI.Snapshot(_snapshotId)
)

/**
 * A collection containing a link ( href ) to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved, along with the total number of tracks in the playlist.
 *
 * @property href link to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved
 * @property total the total number of tracks in the playlist.
 */
data class PlaylistTrackInfo(
    val href: String,
    val total: Int
)

data class VideoThumbnail(val url: String?)