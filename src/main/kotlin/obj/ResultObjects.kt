package obj

data class RecommendationSeed(val initialPoolSize: Int, val afterFilteringSize: Int, val afterRelinkingSize: Int?, val href: String, val id: String,
                              val type: String)

data class SpotifyCategory(val href: String, val icons: List<SpotifyImage>, val id: String, val name: String)

data class SpotifyCopyright(val text: String, val type: String)

data class PlaylistTrackInfo(val href: String, val total: Int)

/**
 * @param href Will always be null, per the Spotify documentation, until the Web API is updated to support this.
 */
data class Followers(val href: String?, val total: Int)

data class SpotifyPublicUser(val display_name: String, val external_urls: HashMap<String, String>, val followers: Followers, val href: String,
                             val id: String, val images: List<SpotifyImage>, val type: String, val uri: String)

data class SpotifyImage(val height: Int, val url: String, val width: Int)

data class LinkedTrack(val external_urls: HashMap<String, String>, val href: String, val id: String, val type: String, val uri: String)

data class SimpleArtist(val external_urls: HashMap<String, String>, val href: String, val id: String, val name: String,
                        val type: String, val uri: String)

data class SimpleTrack(val artists: List<SimpleArtist>, val available_markets: List<String>, val disc_number: Int, val duration_ms: Int,
                       val explicit: Boolean, val external_urls: HashMap<String, String>, val href: String, val id: String,
                       val is_playable: Boolean?, val linked_from: LinkedTrack?, val name: String, val preview_url: String,
                       val track_number: Int, val type: String, val uri: String)

data class Track(val album: SimpleAlbum, val artists: List<SimpleArtist>, val available_markets: List<String>, val disc_number: Int,
                 val duration_ms: Int, val explicit: Boolean, val external_ids: HashMap<String, String>, val external_urls: HashMap<String, String>,
                 val href: String, val id: String, val is_playable: Boolean?, val linked_from: LinkedTrack?, val name: String, val popularity: Int,
                 val preview_url: String, val track_number: Int, val type: String, val uri: String)

data class SimpleAlbum(val album_type: String, val artists: List<SimpleArtist>, val available_markets: List<String>, val external_urls: HashMap<String, String>,
                       val href: String, val id: String, val images: List<SpotifyImage>, val name: String, val type: String, val uri: String)

data class Album(val album_type: String, val artists: List<SimpleArtist>, val available_markets: List<String>, val copyrights: List<SpotifyCopyright>,
                 val external_ids: HashMap<String, String>, val external_urls: HashMap<String, String>, val genres: List<String>, val href: String,
                 val id: String, val images: List<SpotifyImage>, val label: String, val name: String, val popularity: Int, val release_date: String,
                 val release_date_precision: String, val tracks: PagingObject<SimpleTrack>, val type: String, val uri: String)

data class SimplePlaylist(val collaborative: Boolean, val external_urls: HashMap<String, String>, val href: String, val id: String,
                          val images: List<SpotifyImage>, val name: String, val owner: SpotifyPublicUser, val public: Boolean?,
                          val snapshot_id: String, val tracks: PlaylistTrackInfo, val type: String, val uri: String)


/**
 * Some parameters are timestamps and will be updated soon to reflect Spotify's use of a Timestamp string
 */
data class PlaylistTrack(val added_at: String, val added_by: SpotifyPublicUser, val is_local: Boolean, val track: Track)

data class Playlist(val collaborative: Boolean, val description: String, val external_urls: HashMap<String, String>, val followers: Followers,
                    val href: String, val id: String, val images: List<SpotifyImage>, val name: String, val owner: SpotifyPublicUser, val public: Boolean?,
                    val snapshot_id: String, val tracks: PagingObject<PlaylistTrack>, val type: String, val uri: String)

data class RecommendationResponse(val seeds: List<RecommendationSeed>, val tracks: List<SimpleTrack>)