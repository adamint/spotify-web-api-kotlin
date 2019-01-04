/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.endpoints.client.ClientPlaylistAPI
import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.beust.klaxon.Json

/**
 * @param message the featured message in "Overview"
 */

data class FeaturedPlaylists(val message: String, val playlists: PagingObject<SimplePlaylist>)

data class AudioFeaturesResponse(val audio_features: List<AudioFeatures?>)

data class AlbumsResponse(val albums: List<Album?>)

data class ArtistList(val artists: List<Artist?>)

data class TrackList(val tracks: List<Track?>)

data class RecommendationSeed(
    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int?,
    val href: String?,
    val id: String,
    val type: String
)

data class SpotifyCategory(
    val href: String,
    val icons: List<SpotifyImage>,
    val id: String,
    val name: String
)

data class SpotifyCopyright(
    val text: String,
    val type: String
)

data class PlaylistTrackInfo(
    val href: String,
    val total: Int
)

/**
 * @param href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @param total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */

data class Followers(
    val href: String?,
    val total: Int
)

data class SpotifyUserInformation(
    val birthdate: String? = null,
    val country: String? = null,
    @Json(name = "display_name") val displayName: String? = null,
    val email: String? = null,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val product: String?,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:UserURI=UserURI(_uri)
)

data class SpotifyPublicUser(
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers = Followers(null, -1),
    val href: String,
    val id: String,
    val images: List<SpotifyImage> = listOf(),
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:UserURI=UserURI(_uri)
)

data class SpotifyImage(
    val height: Int?,
    val url: String,
    val width: Int?
)

abstract class Linkable {
    @Json(ignored = true)
    lateinit var api: SpotifyAPI
}

/**
 * Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the
 * searched market. If null, the API result is playable in the market.
 */

data class LinkedTrack(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:TrackURI= TrackURI(_uri)
)

data class SimpleArtist(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:ArtistURI=ArtistURI(_uri)
) : Linkable() {
    fun toFullArtist() = api.artists.getArtist(id)
}

data class Artist(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val popularity: Int,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:ArtistURI=ArtistURI(_uri)
)

data class SimpleTrack(
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String> = listOf(),
    @Json(name = "disc_number") val discNumber: Int,
    @Json(name = "duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,//
    @Json(name = "external_ids") val externalIds: HashMap<String, String> = hashMapOf(),
    val href: String,
    val id: String,
    @Json(name = "is_playable") val isPlayable: Boolean = true,
    @Json(name = "linked_from", ignored = false) private val linkedFrom: LinkedTrack? = null,
    val name: String,
    @Json(name = "preview_url") val previewUrl: String?,
    @Json(name = "track_number") val trackNumber: Int,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:TrackURI=TrackURI(_uri),
    @Json(name = "is_local") val isLocal: Boolean? = null,
    val popularity: Int? = null
) : RelinkingAvailableResponse(linkedFrom) {
    fun toFullTrack(market: Market? = null) = api.tracks.getTrack(id, market)
}

data class Track(
    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String>? = null,
    @Json(name = "is_playable") val isPlayable: Boolean = true,
    @Json(name = "disc_number") val discNumber: Int,
    @Json(name = "duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @Json(name = "external_ids") val externalIds: Map<String, String>,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    @Json(name = "linked_from", ignored = false) private val linked_from: LinkedTrack? = null,
    val name: String,
    val popularity: Int,
    @Json(name = "preview_url") val previewUrl: String?,
    @Json(name = "track_number") val trackNumber: Int,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:TrackURI=TrackURI(_uri),
    @Json(name = "is_local") val isLocal: Boolean?
) : RelinkingAvailableResponse(linked_from)

data class SimpleAlbum(
    @Json(name = "album_type") val albumType: String,
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String>? = null,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:AlbumURI=AlbumURI(_uri),
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "release_date_precision") val releaseDatePrecision: String,
    @Json(name = "total_tracks") val totalTracks: Int? = null,
    @Json(name = "album_group", ignored = false) private val albumGroupString: String? = null,
    @Json(ignored = true) val albumGroup: AlbumGroup? = albumGroupString?.let { _ ->
        AlbumGroup.values().find { it.id == albumGroupString }
    }
) : Linkable() {
    fun toFullAlbum(market: Market? = null) = api.albums.getAlbum(id, market)
}

enum class AlbumGroup(internal val id: String) {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation"),
    APPEARS_ON("appears_on")
}

data class Album(
    @Json(name = "album_type") val albumType: String,
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String>,
    val copyrights: List<SpotifyCopyright>,
    @Json(name = "external_ids") val externalIds: Map<String, String>,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val label: String,
    val name: String,
    val popularity: Int,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "release_date_precision") val releaseDatePrecision: String,
    val tracks: PagingObject<SimpleTrack>,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:AlbumURI= AlbumURI(_uri),
    @Json(name = "total_tracks") val totalTracks: Int
)

data class SimplePlaylist(
    val collaborative: Boolean,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
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
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:PlaylistURI=PlaylistURI(_uri),
    @Json(ignored = true) val snapshotId: ClientPlaylistAPI.Snapshot = ClientPlaylistAPI.Snapshot(_snapshotId)
) : Linkable() {
    fun toFullPlaylist(market: Market? = null): SpotifyRestAction<Playlist?> = api.playlists.getPlaylist(id, market)
}

/**
 * Some parameters are timestamps and will be updated soon to reflect Spotify's use of a Timestamp string
 */

data class PlaylistTrack(
    @Json(name = "primary_color") val primary_color: String? = null,
    @Json(name = "added_at") val addedAt: String,
    @Json(name = "added_by") val addedBy: SpotifyPublicUser,
    @Json(name = "is_local") val isLocal: Boolean?,
    val track: Track,
    @Json(name = "video_thumbnail") val videoThumbnail: VideoThumbnail? = null
)

data class VideoThumbnail(val url: String?)

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
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:PlaylistURI=PlaylistURI(_uri),
    @Json(ignored = true) val snapshotId: ClientPlaylistAPI.Snapshot = ClientPlaylistAPI.Snapshot(_snapshotId)
)

data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<SimpleTrack>)

data class AudioAnalysis(
    val bars: List<AudioBar>,
    val beats: List<AudioBeat>,
    val meta: AudioAnalysisMeta,
    val sections: List<AudioSection>,
    val segments: List<AudioSegment>,
    val tatums: List<AudioTatum>,
    val track: TrackAnalysis
)

data class AudioBar(val start: Float, val duration: Float, val confidence: Float)

data class AudioBeat(val start: Float, val duration: Float, val confidence: Float)

data class AudioTatum(val start: Float, val duration: Float, val confidence: Float)

data class AudioAnalysisMeta(
    @Json(name = "analyzer_version") val analyzerVersion: String,
    val platform: String,
    @Json(name = "detailed_status") val detailedStatus: String,
    @Json(name = "status_code") val statusCode: Int,
    val timestamp: Long,
    @Json(name = "analysis_time") val analysisTime: Float,
    @Json(name = "input_process") val inputProcess: String
)

data class AudioSection(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    val loudness: Float,
    val tempo: Float,
    @Json(name = "tempo_confidence") val tempoConfidence: Float,
    val key: Int,
    @Json(name = "key_confidence") val keyConfidence: Float,
    val mode: Int,
    @Json(name = "mode_confidence") val modeConfidence: Float,
    val time_signature: Int,
    @Json(name = "time_signature_confidence") val timeSignatureConfidence: Float
)

data class AudioSegment(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    @Json(name = "loudness_start") val loudnessStart: Float,
    @Json(name = "loudness_max_time") val loudnessMaxTime: Float,
    @Json(name = "loudness_max") val loudnessMax: Float,
    @Json(name = "loudness_end") val loudnessEnd: Float? = null,
    val pitches: List<Float>,
    val timbre: List<Float>
)

data class TrackAnalysis(
    @Json(name = "num_samples") val numSamples: Int,
    val duration: Float,
    @Json(name = "sample_md5") val sampleMd5: String,
    @Json(name = "offset_seconds") val offsetSeconds: Int,
    @Json(name = "window_seconds") val windowSeconds: Int,
    @Json(name = "analysis_sample_rate") val analysisSampleRate: Int,
    @Json(name = "analysis_channels") val analysisChannels: Int,
    @Json(name = "end_of_fade_in") val endOfFadeIn: Float,
    @Json(name = "start_of_fade_out") val startOfFadeOut: Float,
    val loudness: Float,
    val tempo: Float,
    @Json(name = "tempo_confidence") val tempoConfidence: Float,
    @Json(name = "time_signature") val timeSignature: Int,
    @Json(name = "time_signature_confidence") val timeSignatureConfidence: Float,
    val key: Int,
    @Json(name = "key_confidence") val keyConfidence: Float,
    val mode: Int,
    @Json(name = "mode_confidence") val modeConfidence: Float,
    val codestring: String,
    @Json(name = "code_version") val codeVersion: Float,
    val echoprintstring: String,
    @Json(name = "echoprint_version") val echoprintVersion: Float,
    val synchstring: String,
    @Json(name = "synch_version") val synchVersion: Float,
    val rhythmstring: String,
    @Json(name = "rhythm_version") val rhythmVersion: Float
)

data class AudioFeatures(
    val acousticness: Float,
    @Json(name = "analysis_url") val analysisUrl: String,
    val danceability: Float,
    @Json(name = "duration_ms") val durationMs: Int,
    val energy: Float,
    val id: String,
    val instrumentalness: Float,
    val key: Int,
    val liveness: Float,
    val loudness: Float,
    val mode: Int,
    val speechiness: Float,
    val tempo: Float,
    @Json(name = "time_signature") val timeSignature: Int,
    @Json(name = "track_href") val trackHref: String,
    val type: String,
    @Json(name="uri",ignored=false)private val _uri: String,
    @Json(ignored = true) val uri:TrackURI=TrackURI(_uri),
    val valence: Float
)

data class SavedAlbum(
    @Json(name = "added_at") val addedAt: String,
    val album: Album
)

data class SavedTrack(
    @Json(name = "added_at") val addedAt: String,
    val track: Track
)

data class Device(
    val id: String,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "is_restricted") val isRestricted: Boolean,
    val name: String,
    val type: String,
    @Json(name = "volume_percent") val volumePercent: Int
)

data class CurrentlyPlayingContext(
    val timestamp: Long?,
    val device: Device,
    @Json(name = "progress_ms") val progressMs: String,
    @Json(name = "is_playing") val isPlaying: Boolean,
    val item: Track?,
    @Json(name = "shuffle_state") val shuffleState: Boolean,
    @Json(name = "repeat_state") val repeatState: String,
    val context: Context
)

data class Context(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>
)

data class CurrentlyPlayingObject(
    val context: PlayHistoryContext?,
    val timestamp: Long,
    @Json(name = "progress_ms") val progressMs: Int,
    @Json(name = "is_playing") val isPlaying: Boolean,
    val item: Track
)

data class PlayHistoryContext(
    val type: String,
    val href: String,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val uri: String
)

data class PlayHistory(
    val track: SimpleTrack,
    @Json(name = "played_at") val playedAt: String,
    val context: PlayHistoryContext
)