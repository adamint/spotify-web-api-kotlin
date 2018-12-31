/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @param message the featured message in "Overview"
 */
@Serializable
data class FeaturedPlaylists(val message: String, val playlists: PagingObject<Playlist>)

@Serializable
data class AudioFeaturesResponse(val audio_features: List<AudioFeatures?>)

@Serializable
data class AlbumsResponse(val albums: List<Album?>)

@Serializable
data class ArtistList(val artists: List<Artist?>)

@Serializable
data class TrackList(val tracks: List<Track?>)

@Serializable
data class RecommendationSeed(
    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int?,
    val href: String,
    val id: String,
    val type: String
)

@Serializable
data class SpotifyCategory(val href: String, val icons: List<SpotifyImage>, val id: String, val name: String)

@Serializable
data class SpotifyCopyright(val text: String, val type: String)

@Serializable
data class PlaylistTrackInfo(val href: String, val total: Int)

/**
 * @param href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @param total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */
@Serializable
data class Followers(val href: String?, val total: Int)

@Serializable
data class SpotifyUserInformation(
    @Optional val birthdate: String? = null,
    @Optional val country: String? = null,
    @Optional val display_name: String? = null,
    @Optional val email: String? = null,
    val external_urls: HashMap<String, String>,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val product: String?,
    val type: String,
    val uri: String
)

@Serializable
data class SpotifyPublicUser(
    @Optional val display_name: String? = null,
    val external_urls: HashMap<String, String>,
    @Optional val followers: Followers = Followers(null, -1),
    val href: String,
    val id: String,
    @Optional val images: List<SpotifyImage> = listOf(),
    val type: String,
    val uri: String
)

@Serializable
data class SpotifyImage(val height: Int?, val url: String, val width: Int?)

abstract class Linkable {
    @Transient
    lateinit var api: SpotifyAPI
}

/**
 * Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the
 * searched market. If null, the API result is playable in the market.
 */
@Serializable
data class LinkedTrack(
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)

@Serializable
data class SimpleArtist(
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
) : Linkable() {
    fun toFullArtist() = api.artists.getArtist(id)
}

@Serializable
data class Artist(
    val external_urls: HashMap<String, String>,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

@Serializable
data class SimpleTrack(
    val artists: List<SimpleArtist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    @Optional val is_playable: Boolean = true,
    private val linked_from: LinkedTrack?,
    val name: String,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String,
    val is_local: Boolean
) : RelinkingAvailableResponse(linked_from) {
    fun toFullTrack(market: Market? = null) = api.tracks.getTrack(id, market)
}

@Serializable
data class Track(
    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    @Optional val available_markets: List<String>? = null,
    @Optional val is_playable: Boolean = true,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: HashMap<String, String>,
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    @Optional private val linked_from: LinkedTrack? = null,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String,
    val is_local: Boolean
) : RelinkingAvailableResponse(linked_from)

@Serializable
data class SimpleAlbum(
    val album_type: String,
    val artists: List<SimpleArtist>,
    @Optional val available_markets: List<String>? = null,
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val type: String,
    val uri: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    @Optional private val album_group: String? = null
) : Linkable() {
    @Transient val albumGroup: AlbumGroup? = album_group?.let { _ -> AlbumGroup.values().find { it.id == album_group } }
    fun toFullAlbum(market: Market? = null) = api.albums.getAlbum(id, market)
}

enum class AlbumGroup(internal val id: String) {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation"),
    APPEARS_ON("appears_on")
}

@Serializable
data class Album(
    val album_type: String,
    val artists: List<SimpleArtist>,
    val available_markets: List<String>,
    val copyrights: List<SpotifyCopyright>,
    val external_ids: HashMap<String, String>,
    val external_urls: HashMap<String, String>,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val label: String,
    val name: String,
    val popularity: Int,
    val release_date: String,
    val release_date_precision: String,
    val tracks: PagingObject<SimpleTrack>,
    val type: String,
    val uri: String,
    val total_tracks: Int
)

@Serializable
data class SimplePlaylist(
    val collaborative: Boolean,
    val external_urls: HashMap<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser,
    @Optional val primary_color: String? = null,
    @Optional val public: Boolean? = null,
    val snapshot_id: String,
    val tracks: PlaylistTrackInfo,
    val type: String,
    val uri: String
) : Linkable() {
    fun toFullPlaylist(market: Market? = null): SpotifyRestAction<Playlist?> = api.playlists.getPlaylist(id, market)
}

/**
 * Some parameters are timestamps and will be updated soon to reflect Spotify's use of a Timestamp string
 */
@Serializable
data class PlaylistTrack(
    @Optional val primary_color: String? = null,
    val added_at: String,
    val added_by: SpotifyPublicUser,
    val is_local: Boolean,
    val track: Track,
    @Optional val video_thumbnail: VideoThumbnail? = null
)

@Serializable
data class VideoThumbnail(val url: String?)

@Serializable
data class Playlist(
    val collaborative: Boolean,
    val description: String,
    val external_urls: HashMap<String, String>,
    val followers: Followers,
    val href: String,
    val id: String,
    @Optional val primary_color: String? = null,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser,
    @Optional val public: Boolean? = null,
    val snapshot_id: String,
    val tracks: PagingObject<PlaylistTrack>,
    val type: String,
    val uri: String
)

@Serializable
data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<SimpleTrack>)

@Serializable
data class AudioAnalysis(
    val bars: List<AudioBar>,
    val beats: List<AudioBeat>,
    val meta: AudioAnalysisMeta,
    val sections: List<AudioSection>,
    val segments: List<AudioSegment>,
    val tatums: List<AudioTatum>,
    val track: TrackAnalysis
)

@Serializable
data class AudioBar(val start: Float, val duration: Float, val confidence: Float)

@Serializable
data class AudioBeat(val start: Float, val duration: Float, val confidence: Float)

@Serializable
data class AudioTatum(val start: Float, val duration: Float, val confidence: Float)

@Serializable
data class AudioAnalysisMeta(
    val analyzer_version: String,
    val platform: String,
    val detailed_status: String,
    val status_code: String,
    val timestamp: Long,
    val analysis_time: Float,
    val input_process: String
)

@Serializable
data class AudioSection(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    val loudness: Float,
    val tempo: Float,
    val tempo_confidence: Float,
    val key: Int,
    val key_confidence: Float,
    val mode: Int,
    val mode_confidence: Float,
    val time_signature: Int,
    val time_signature_confidence: Float
)

@Serializable
data class AudioSegment(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    val loudness_start: Float,
    val loudness_max_time: Float,
    val loudness_max: Float,
    @Optional val loudness_end: Float? = null,
    val pitches: List<Float>,
    val timbre: List<Float>
)

@Serializable
data class TrackAnalysis(
    val num_samples: Int,
    val duration: Float,
    val sample_md5: String,
    val offset_seconds: Int,
    val window_seconds: Int,
    val analysis_sample_rate: Int,
    val analysis_channels: Int,
    val end_of_fade_in: Float,
    val start_of_fade_out: Float,
    val loudness: Float,
    val tempo: Float,
    val tempo_confidence: Float,
    val time_signature: Int,
    val time_signature_confidence: Float,
    val key: Int,
    val key_confidence: Float,
    val mode: Int,
    val mode_confidence: Float,
    val codestring: String,
    val code_version: Float,
    val echoprintstring: String,
    val echoprint_version: Float,
    val synchstring: String,
    val synch_version: Float,
    val rhythmstring: String,
    val rhythm_version: Float
)

@Serializable
data class AudioFeatures(
    val acousticness: Float,
    val analysis_url: String,
    val danceability: Float,
    val duration_ms: Int,
    val energy: Float,
    val id: String,
    val instrumentalness: Float,
    val key: Int,
    val liveness: Float,
    val loudness: Float,
    val mode: Int,
    val speechiness: Float,
    val tempo: Float,
    val time_signature: Int,
    val track_href: String,
    val type: String,
    val uri: String,
    val valence: Float
)

@Serializable
data class SavedAlbum(val added_at: String, val album: Album)

@Serializable
data class SavedTrack(val added_at: String, val track: Track)

@Serializable
data class Device(
    val id: String,
    val is_active: Boolean,
    val is_restricted: Boolean,
    val name: String,
    val type: String,
    val volume_percent: Int
)

@Serializable
data class CurrentlyPlayingContext(
    val timestamp: Long?,
    val device: Device,
    val progress_ms: String,
    val is_playing: Boolean,
    val item: Track?,
    val shuffle_state: Boolean,
    val repeat_state: String,
    val context: Context
)

@Serializable
data class Context(val external_urls: HashMap<String, String>)

@Serializable
data class CurrentlyPlayingObject(
    val context: PlayHistoryContext?,
    val timestamp: Long,
    val progress_ms: Int,
    val is_playing: Boolean,
    val item: Track
)

@Serializable
data class PlayHistoryContext(
    val type: String,
    val href: String,
    val external_urls: HashMap<String, String>,
    val uri: String
)

@Serializable
data class PlayHistory(val track: SimpleTrack, val played_at: String, val context: PlayHistoryContext)