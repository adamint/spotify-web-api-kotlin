/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.endpoints.client.PlaylistSnapshot
import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simplified Playlist object that can be used to retrieve a full [Playlist]
 *
 * @param collaborative Returns true if context is not search and the owner allows other users to
 * modify the playlist. Otherwise returns false.
 * @param href A link to the Web API endpoint providing full details of the playlist.
 * @param id The Spotify ID for the playlist.
 * @param images Images for the playlist. The array may be empty or contain up to three images.
 * The images are returned by size in descending order. See Working with Playlists.
 * Note: If returned, the source URL for the image ( url ) is temporary and will expire in less than a day.
 * @param name The name of the playlist.
 * @param owner The user who owns the playlist
 * @param primaryColor Unknown.
 * @param public The playlist’s public/private status: true the playlist is public, false the
 * playlist is private, null the playlist status is not relevant.
 * @param tracks A collection containing a link ( href ) to the Web API endpoint where full details of the
 * playlist’s tracks can be retrieved, along with the total number of tracks in the playlist.
 * @param type The object type: “playlist”
 * @param description The playlist description. Only returned for modified, verified playlists, otherwise null.
 *
 * @property snapshot The version identifier for the current playlist. Can be supplied in other
 * requests to target a specific playlist version
 */
@Serializable
public data class SimplePlaylist(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: SpotifyUri,

    val collaborative: Boolean,
    val images: List<SpotifyImage>,
    val name: String,
    val description: String? = null,
    val owner: SpotifyPublicUser,
    @SerialName("primary_color") val primaryColor: String? = null,
    val public: Boolean? = null,
    @SerialName("snapshot_id") private val snapshotIdString: String,
    val tracks: PlaylistTrackInfo,
    val type: String
) : CoreObject() {
    val snapshot: PlaylistSnapshot get() = PlaylistSnapshot(snapshotIdString)

    /**
     * Converts this [SimplePlaylist] into a full [Playlist] object with the given
     * market
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    public suspend fun toFullPlaylist(market: Market? = null): Playlist? =
            api.playlists.getPlaylist(id, market)
}

/**
 * Represents a Spotify track inside a [Playlist]
 *
 * @param primaryColor Unknown. Undocumented field
 * @param addedAt The date and time the track was added. Note that some very old playlists may return null in this field.
 * @param addedBy The Spotify user who added the track. Note that some very old playlists may return null in this field.
 * @param isLocal Whether this track is a local file or not.
 * @param track Information about the track. In rare occasions, this field may be null if this track's API entry is broken.
 * **Warning:** if this is a podcast, the track will be null if you are using [SpotifyAppApi].
 */
@Serializable
public data class PlaylistTrack(
    @SerialName("primary_color") val primaryColor: String? = null,
    @SerialName("added_at") val addedAt: String? = null,
    @SerialName("added_by") val addedBy: SpotifyPublicUser? = null,
    @SerialName("is_local") val isLocal: Boolean? = null,
    @Serializable(with = Playable.Companion::class) val track: Playable? = null,
    @SerialName("video_thumbnail") val videoThumbnail: VideoThumbnail? = null
)

/**
 * Represents a Playlist on Spotify
 *
 * @param collaborative Returns true if context is not search and the owner allows other users to modify the playlist.
 * Otherwise returns false.
 * @param description The playlist description. Only returned for modified, verified playlists, otherwise null.
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
 * a specific playlist version
 * @param tracks Information about the tracks of the playlist.
 * @param type The object type: “playlist”
 *
 * @property snapshot The version identifier for the current playlist. Can be supplied in other requests to target
 */
@Serializable
public data class Playlist(
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: PlaylistUri,

    val collaborative: Boolean,
    val description: String? = null,
    val followers: Followers,
    @SerialName("primary_color") val primaryColor: String? = null,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser,
    val public: Boolean? = null,
    @SerialName("snapshot_id") private val snapshotIdString: String,
    val tracks: PagingObject<PlaylistTrack>,
    val type: String
) : CoreObject() {
    val snapshot: PlaylistSnapshot get() = PlaylistSnapshot(snapshotIdString)
}

/**
 * A collection containing a link ( href ) to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved, along with the total number of tracks in the playlist.
 *
 * @param href link to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved
 * @param total the total number of tracks in the playlist.
 */
@Serializable
public data class PlaylistTrackInfo(
    val href: String,
    val total: Int
)

@Serializable
public data class VideoThumbnail(val url: String?)
