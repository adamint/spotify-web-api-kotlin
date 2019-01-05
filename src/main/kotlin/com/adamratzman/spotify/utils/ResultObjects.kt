/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.endpoints.client.ClientPlaylistAPI
import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.beust.klaxon.Json

/**
 * Spotify featured playlists (on the Browse tab)
 *
 * @property message the featured message in "Overview"
 * @property playlists [PagingObject] of returned items
 */
data class FeaturedPlaylists(val message: String, val playlists: PagingObject<SimplePlaylist>)

internal data class AudioFeaturesResponse(
    @Json(name = "audio_features") val audioFeatures: List<AudioFeatures?>
)

internal data class AlbumsResponse(val albums: List<Album?>)

internal data class ArtistList(val artists: List<Artist?>)

internal data class TrackList(val tracks: List<Track?>)

data class VideoThumbnail(val url: String?)

/**
 * Contains an explanation of why a track is not available
 *
 * @property reason why the track is not available
 */
data class Restrictions(val reason: String)

/**
 * @property start The starting point (in seconds) of the time interval.
 * @property duration The duration (in seconds) of the time interval.
 * @property confidence The confidence, from 0.0 to 1.0, of the reliability of the interval
 */
data class TimeInterval(val start: Float, val duration: Float, val confidence: Float)

data class Context(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>
)

/**
 * Seed from which the recommendation was constructed
 *
 * @property initialPoolSize The number of recommended tracks available for this seed.
 * @property afterFilteringSize The number of tracks available after min_* and max_* filters have been applied.
 * @property afterRelinkingSize The number of tracks available after relinking for regional availability.
 * @property href A link to the full track or artist data for this seed. For tracks this will be a link to a Track
 * Object. For artists a link to an Artist Object. For genre seeds, this value will be null.
 * @property id The id used to select this seed. This will be the same as the string used in the
 * seed_artists , seed_tracks or seed_genres parameter.
 * @property type The entity type of this seed. One of artist , track or genre.
 */
data class RecommendationSeed(
    val initialPoolSize: Int,
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int?,
    val href: String?,
    val id: String,
    val type: String
)

/**
 * Spotify music category
 *
 * @property href A link to the Web API endpoint returning full details of the category.
 * @property icons The category icon, in various sizes.
 * @property id The Spotify category ID of the category.
 * @property name The name of the category.
 */
data class SpotifyCategory(
    val href: String,
    val icons: List<SpotifyImage>,
    val id: String,
    val name: String
)

/**
 * Describes an album's copyright
 *
 * @property text The copyright text for this album.
 * @property type The type of copyright: C = the copyright,
 * P = the sound recording (performance) copyright.
 */
data class SpotifyCopyright(
    val text: String,
    val type: String
)

/**
 * A collection containing a link ( href ) to the Web API endpoint where full details of the playlist’s tracks can be retrieved, along with the total number of tracks in the playlist.
 *
 * @property href link to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved
 * @property total the total number of tracks in the playlist.
 */
data class PlaylistTrackInfo(
    val href: String,
    val total: Int
)

/**
 * Spotify user's followers
 *
 * @property href Will always be null, per the Spotify documentation,
 * until the Web API is updated to support this.
 *
 * @property total -1 if the user object does not contain followers, otherwise the amount of followers the user has
 */
data class Followers(
    val href: String?,
    val total: Int
)

/**
 * @property birthdate The user’s date-of-birth. This field is only available when the current user
 * has granted access to the user-read-birthdate scope.
 * @property country The country of the user, as set in the user’s account profile. An ISO 3166-1 alpha-2
 * country code. This field is only available when the current user has granted access to the user-read-private scope.
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property email The user’s email address, as entered by the user when creating their account. Important! This email
 * address is unverified; there is no proof that it actually belongs to the user. This field is only
 * available when the current user has granted access to the user-read-email scope.
 * @property externalUrls Known external URLs for this user.
 * @property followers Information about the followers of the user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for the user
 * @property images The user’s profile image.
 * @property product The user’s Spotify subscription level: “premium”, “free”, etc.
 * (The subscription level “open” can be considered the same as “free”.) This field is only available when the
 * current user has granted access to the user-read-private scope.
 * @property type The object type: “user”
 * @property uri The Spotify URI for the user.
 */
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
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: UserURI = UserURI(_uri)
)

/**
 * @property displayName The name displayed on the user’s profile. null if not available.
 * @property externalUrls Known public external URLs for this user.
 * @property followers Information about the followers of this user.
 * @property href A link to the Web API endpoint for this user.
 * @property id The Spotify user ID for this user.
 * @property images The user’s profile image.
 * @property type The object type: “user”
 * @property uri The Spotify URI for this user.
 */
data class SpotifyPublicUser(
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "external_urls") val externalUrls: Map<String, String>,
    val followers: Followers = Followers(null, -1),
    val href: String,
    val id: String,
    val images: List<SpotifyImage> = listOf(),
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: UserURI = UserURI(_uri)
)

/**
 * A Spotify image
 *
 * @property height The image height in pixels. If unknown: null or not returned.
 * @property url The source URL of the image.
 * @property width The image width in pixels. If unknown: null or not returned.
 */
data class SpotifyImage(
    val height: Int? = null,
    val url: String,
    val width: Int? = null
)

/**
 * Allow for track relinking
 */
abstract class Linkable {
    @Json(ignored = true)
    lateinit var api: SpotifyAPI
}

/**
 * Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the
 * searched market. If null, the API result is playable in the market.
 *
 * @property externalUrls
 * @property href
 * @property id
 * @property type
 * @property uri
 */
data class LinkedTrack(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: TrackURI = TrackURI(_uri)
)

/**
 * @property externalUrls Known external URLs for this artist.
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property name The name of the artist
 * @property type The object type: "artist"
 * @property uri The Spotify URI for the artist.
 */
data class SimpleArtist(
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: ArtistURI = ArtistURI(_uri)
) : Linkable() {
    fun toFullArtist() = api.artists.getArtist(id)
}

/**
 * @property externalUrls Known external URLs for this artist.
 * @property followers Information about the followers of the artist.
 * @property genres A list of the genres the artist is associated with. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property images Images of the artist in various sizes, widest first.
 * @property name The name of the artist
 * @property popularity The popularity of the artist. The value will be between 0 and 100, with 100 being the most
 * popular. The artist’s popularity is calculated from the popularity of all the artist’s tracks.
 * @property type The object type: "artist"
 * @property uri The Spotify URI for the artist.
 */
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
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: ArtistURI = ArtistURI(_uri)
)

/**
 * @property artists The artists who performed the track. Each artist object includes a link in href to
 * more detailed information about the artist.
 * @property availableMarkets A list of the countries in which the track can be played,
 * identified by their ISO 3166-1 alpha-2 code.
 * @property discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @property durationMs The track length in milliseconds.
 * @property explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @property externalUrls External URLs for this track.
 * @property externalIds External IDs for this track.
 * @property href A link to the Web API endpoint providing full details of the track.
 * @property id The Spotify ID for the track.
 * @property isPlayable Part of the response when Track Relinking is applied. If true ,
 * the track is playable in the given market. Otherwise false.
 * @property linkedTrack Part of the response when Track Relinking is applied and is only part of the response
 * if the track linking, in fact, exists. The requested track has been replaced with a different track. The track in
 * the [linkedFrom] object contains information about the originally requested track.
 * @property name The name of the track.
 * @property previewUrl A URL to a 30 second preview (MP3 format) of the track.
 * @property trackNumber The number of the track. If an album has several discs, the track number
 * is the number on the specified disc.
 * @property type The object type: “track”.
 * @property uri The Spotify URI for the track.
 * @property isLocal Whether or not the track is from a local file.
 * @property popularity the popularity of this track. possibly null
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available in
 * the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
data class SimpleTrack(
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String> = listOf(),
    @Json(name = "disc_number") val discNumber: Int,
    @Json(name = "duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>, //
    @Json(name = "external_ids") val externalIds: HashMap<String, String> = hashMapOf(),
    val href: String,
    val id: String,
    @Json(name = "is_playable") val isPlayable: Boolean = true,
    @Json(name = "linked_from", ignored = false) private val linkedFrom: LinkedTrack? = null,
    val name: String,
    @Json(name = "preview_url") val previewUrl: String?,
    @Json(name = "track_number") val trackNumber: Int,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: TrackURI = TrackURI(_uri),
    @Json(name = "is_local") val isLocal: Boolean? = null,
    val popularity: Int? = null,
    val restrictions: Restrictions? = null
) : RelinkingAvailableResponse(linkedFrom) {
    fun toFullTrack(market: Market? = null) = api.tracks.getTrack(id, market)
}

/**
 * @property album The album on which the track appears. The album object includes a link in
 * href to full information about the album.
 * @property artists The artists who performed the track. Each artist object includes a link in href
 * to more detailed information about the artist.
 * @property availableMarkets A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code.
 * @property isPlayable Part of the response when Track Relinking is applied. If true , the track is playable in the
 * given market. Otherwise false.
 * @property discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @property durationMs The track length in milliseconds.
 * @property explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @property externalUrls External URLs for this track.
 * @property externalIds External IDs for this track.
 * @property href A link to the Web API endpoint providing full details of the track.
 * @property id The Spotify ID for the track.
 * @property linkedTrack Part of the response when Track Relinking is applied and is only part of the response
 * if the track linking, in fact, exists. The requested track has been replaced with a different track. The track in
 * the [linkedTrack] object contains information about the originally requested track.
 * @property name The name of the track.
 * @property popularity The popularity of the track. The value will be between 0 and 100, with 100 being the most
 * popular. The popularity of a track is a value between 0 and 100, with 100 being the most popular. The popularity
 * is calculated by algorithm and is based, in the most part, on the total number of plays the track has had and how
 * recent those plays are. Generally speaking, songs that are being played a lot now will have a higher popularity
 * than songs that were played a lot in the past. Duplicate tracks (e.g. the same track from a single and an album)
 * are rated independently. Artist and album popularity is derived mathematically from track popularity. Note that
 * the popularity value may lag actual popularity by a few days: the value is not updated in real time.
 * @property previewUrl A link to a 30 second preview (MP3 format) of the track. Can be null
 * @property trackNumber The number of the track. If an album has several discs, the track number is the number on the specified disc.
 * @property type The object type: “track”.
 * @property uri The Spotify URI for the track.
 * @property isLocal Whether or not the track is from a local file.
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available in
 * the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
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
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: TrackURI = TrackURI(_uri),
    @Json(name = "is_local") val isLocal: Boolean?,
    val restrictions: Restrictions? = null
) : RelinkingAvailableResponse(linked_from)

/**
 * @property albumGroup Optional. The field is present when getting an artist’s albums. Possible values
 * are “album”, “single”, “compilation”, “appears_on”. Compare to album_type this field represents relationship
 * between the artist and the album.
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed information about the artist.
 * @property availableMarkets The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note
 * that an album is considered available in a market when at least 1 of its tracks is available in that market.
 * @property externalUrls Known external URLs for this album.
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify id for the album
 * @property images The cover art for the album in various sizes, widest first.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property type The object type: “album”
 * @property uri The Spotify URI for the album.
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property albumType The type of the album: one of “album”, “single”, or “compilation”.
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
data class SimpleAlbum(
    @Json(name = "album_type", ignored = false) private val _albumType: String,
    val artists: List<SimpleArtist>,
    @Json(name = "available_markets") val availableMarkets: List<String>? = null,
    @Json(name = "external_urls") val externalUrls: HashMap<String, String>,
    val href: String,
    val id: String,
    val images: List<SpotifyImage>,
    val name: String,
    val type: String,
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: AlbumURI = AlbumURI(_uri),
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "release_date_precision") val releaseDatePrecision: String,
    @Json(name = "total_tracks") val totalTracks: Int? = null,
    @Json(name = "album_group", ignored = false) private val albumGroupString: String? = null,
    val restrictions: Restrictions? = null,
    @Json(ignored = true) val albumGroup: AlbumResultType? = albumGroupString?.let { _ ->
        AlbumResultType.values().find { it.id == albumGroupString }
    },
    @Json(ignored = true) val albumType: AlbumResultType = _albumType.let { _ ->
        AlbumResultType.values().first { it.id == albumGroupString }
    }
) : Linkable() {
    fun toFullAlbum(market: Market? = null) = api.albums.getAlbum(id, market)
}

/**
 * Album type
 */
enum class AlbumResultType(internal val id: String) {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation"),
    APPEARS_ON("appears_on")
}

/**
 * @property albumType The type of the album: one of "album" , "single" , or "compilation".
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed
 * information about the artist.
 * @property availableMarkets The markets in which the album is available:
 * ISO 3166-1 alpha-2 country codes. Note that an album is considered
 * available in a market when at least 1 of its tracks is available in that market.
 * @property copyrights The copyright statements of the album.
 * @property externalIds Known external IDs for the album.
 * @property externalUrls Known external URLs for this album.
 * @property genres A list of the genres used to classify the album. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property images The cover art for the album in various sizes, widest first.
 * @property label The label for the album.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property popularity The popularity of the album. The value will be between 0 and 100, with 100 being the most
 * popular. The popularity is calculated from the popularity of the album’s individual tracks.
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property tracks The tracks of the album.
 * @property type The object type: “album”
 * @property uri The Spotify URI for the album.
 * @property totalTracks the total amount of tracks in this album
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with.
 * The track response will still contain metadata for the original track, and a
 * restrictions object containing the reason why the track is not available: "restrictions" : {"reason" : "market"}
 */
data class Album(
    @Json(name = "album_type", ignored = false) private val _albumType: String,
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
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: AlbumURI = AlbumURI(_uri),
    @Json(name = "total_tracks") val totalTracks: Int,
    val restrictions: Restrictions? = null,
    @Json(ignored = true) val albumType: AlbumResultType = AlbumResultType.values().first { it.id == _albumType }
)

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
 * @property seeds An array of recommendation seed objects.
 * @property tracks An array of track object (simplified) ordered according to the parameters supplied.
 */
data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<SimpleTrack>)

/**
 * @param bars The time intervals of the bars throughout the track. A bar (or measure) is a segment of time defined as
 * a given number of beats. Bar offsets also indicate downbeats, the first beat of the measure.
 * @param beats The time intervals of beats throughout the track. A beat is the basic time unit of a piece of music;
 * for example, each tick of a metronome. Beats are typically multiples of tatums.
 * @param meta Analysis meta information (limited use)
 * @param sections Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar
 * solo, etc. Each section contains its own descriptions of tempo, key, mode, time_signature, and loudness.
 * @param segments Audio segments attempts to subdivide a song into many segments, with each segment containing
 * a roughly consitent sound throughout its duration.
 * @param tatums A tatum represents the lowest regular pulse train that a listener intuitively infers from the timing
 * of perceived musical events (segments).
 * @param track An analysis of the track as a whole. Undocumented on Spotify's side.
 */
data class AudioAnalysis(
    val bars: List<TimeInterval>,
    val beats: List<TimeInterval>,
    val meta: AudioAnalysisMeta,
    val sections: List<AudioSection>,
    val segments: List<AudioSegment>,
    val tatums: List<TimeInterval>,
    val track: TrackAnalysis
)

data class AudioAnalysisMeta(
    @Json(name = "analyzer_version") val analyzerVersion: String,
    val platform: String,
    @Json(name = "detailed_status") val detailedStatus: String,
    @Json(name = "status_code") val statusCode: Int,
    val timestamp: Long,
    @Json(name = "analysis_time") val analysisTime: Float,
    @Json(name = "input_process") val inputProcess: String
)

/**
 * @param start The starting point (in seconds) of the section.
 * @param duration The duration (in seconds) of the section.
 * @param confidence The confidence, from 0.0 to 1.0, of the reliability of the section’s “designation”.
 * @param loudness The overall loudness of the section in decibels (dB). Loudness values are useful
 * for comparing relative loudness of sections within tracks.
 * @param tempo The overall estimated tempo of the section in beats per minute (BPM). In musical terminology, tempo
 * is the speed or pace of a given piece and derives directly from the average beat duration.
 * @param tempoConfidence The confidence, from 0.0 to 1.0, of the reliability of the tempo. Some tracks contain tempo
 * changes or sounds which don’t contain tempo (like pure speech) which would correspond to a low value in this field.
 * @param key The estimated overall key of the section. The values in this field ranging from 0 to 11 mapping to
 * pitches using standard Pitch Class notation (E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on). If no key was detected,
 * the value is -1.
 * @param keyConfidence The confidence, from 0.0 to 1.0, of the reliability of the key.
 * Songs with many key changes may correspond to low values in this field.
 * @param mode Indicates the modality (major or minor) of a track, the type of scale from which its melodic content is
 * derived. This field will contain a 0 for “minor”, a 1 for “major”, or a -1 for no result. Note that the major key
 * (e.g. C major) could more likely be confused with the minor key at 3 semitones lower (e.g. A minor) as both
 * keys carry the same pitches.
 * @param modeConfidence The confidence, from 0.0 to 1.0, of the reliability of the mode.
 * @param timeSignature An estimated overall time signature of a track. The time signature (meter) is a notational
 * convention to specify how many beats are in each bar (or measure). The time signature ranges from 3 to 7
 * indicating time signatures of “3/4”, to “7/4”.
 * @param timeSignatureConfidence The confidence, from 0.0 to 1.0, of the reliability of the time_signature.
 * Sections with time signature changes may correspond to low values in this field.
 */
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
    @Json(name="time_signature") val timeSignature: Int,
    @Json(name = "time_signature_confidence") val timeSignatureConfidence: Float
)

/**
 * @param start The starting point (in seconds) of the segment.
 * @param duration The duration (in seconds) of the segment.
 * @param confidence The confidence, from 0.0 to 1.0, of the reliability of the segmentation. Segments of the song which
 * are difficult to logically segment (e.g: noise) may correspond to low values in this field.
 * @param loudnessStart The onset loudness of the segment in decibels (dB). Combined with loudness_max and
 * loudness_max_time, these components can be used to desctibe the “attack” of the segment.
 * @param loudnessMaxTime The segment-relative offset of the segment peak loudness in seconds. Combined with
 * loudness_start and loudness_max, these components can be used to desctibe the “attack” of the segment.
 * @param loudnessMax The peak loudness of the segment in decibels (dB). Combined with loudness_start and
 * loudness_max_time, these components can be used to desctibe the “attack” of the segment.
 * @param loudnessEnd The offset loudness of the segment in decibels (dB). This value should be equivalent to the
 * loudness_start of the following segment.
 * @param pitches A “chroma” vector representing the pitch content of the segment, corresponding to the 12 pitch classes
 * C, C#, D to B, with values ranging from 0 to 1 that describe the relative dominance of every pitch in the chromatic scale
 * @param timbre Timbre is the quality of a musical note or sound that distinguishes different types of musical
 * instruments, or voices. Timbre vectors are best used in comparison with each other.
 */
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

/**
 * @property acousticness A confidence measure from 0.0 to 1.0 of whether the track is acoustic.
 * 1.0 represents high confidence the track is acoustic.
 * @property analysisUrl An HTTP URL to access the full audio analysis of this track.
 * An access token is required to access this data.
 * @property danceability Danceability describes how suitable a track is for dancing based on a combination
 * of musical elements including tempo, rhythm stability, beat strength, and overall regularity. A value of
 * 0.0 is least danceable and 1.0 is most danceable.
 * @property durationMs The duration of the track in milliseconds.
 * @property energy Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and
 * activity. Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy,
 * while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute include
 * dynamic range, perceived loudness, timbre, onset rate, and general entropy.
 * @property id The Spotify ID for the track.
 * @property instrumentalness Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are
 * treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The closer
 * the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content.
 * Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as
 * the value approaches 1.0.
 * @property key The key the track is in. Integers map to pitches using standard Pitch Class notation.
 * E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.
 * @property liveness Detects the presence of an audience in the recording. Higher liveness values represent
 * an increased probability that the track was performed live. A value above 0.8 provides strong likelihood
 * that the track is live.
 * @property loudness The overall loudness of a track in decibels (dB). Loudness values are averaged across
 * the entire track and are useful for comparing relative loudness of tracks. Loudness is the quality of a
 * sound that is the primary psychological correlate of physical strength (amplitude). Values typical range
 * between -60 and 0 db.
 * @property mode Mode indicates the modality (major or minor) of a track, the type of scale from which
 * its melodic content is derived. Major is represented by 1 and minor is 0.
 * @property speechiness Speechiness detects the presence of spoken words in a track. The more exclusively
 * speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value.
 * Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33
 * and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including
 * such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks.
 * @property tempo The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo
 * is the speed or pace of a given piece and derives directly from the average beat duration.
 * @property timeSignature An estimated overall time signature of a track. The time signature (meter) is a
 * notational convention to specify how many beats are in each bar (or measure).
 * @property trackHref A link to the Web API endpoint providing full details of the track.
 * @property type The object type: “audio_features”
 * @property uri The Spotify URI for the track.
 * @property valence A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track.
 * Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low
 * valence sound more negative (e.g. sad, depressed, angry).
 */
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
    @Json(name = "uri", ignored = false) private val _uri: String,
    @Json(ignored = true) val uri: TrackURI = TrackURI(_uri),
    val valence: Float
)

/**
 * @property addedAt The date and time the album was saved.
 * @property track Information about the album.
 */
data class SavedAlbum(
    @Json(name = "added_at") val addedAt: String,
    val album: Album
)

/**
 * @property addedAt The date and time the track was saved.
 * @property track Information about the track.
 */
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