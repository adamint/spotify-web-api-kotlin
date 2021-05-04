/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.utils.Locale
import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An episode (podcast) on Spotify
 *
 * @param album The album on which the track appears. The album object includes a link in
 * href to full information about the album.
 * @param artists The artists who performed the track. Each artist object includes a link in href
 * to more detailed information about the artist.
 * @property availableMarkets A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code.
 * @param discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @param durationMs The track length in milliseconds.
 *
 * @param explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @param isLocal Whether or not the track is from a local file.
 * @param isPlayable Part of the response when Track Relinking is applied. If true , the track is playable in the
 * given market. Otherwise false.
 * @param name The name of the track.
 * @param popularity The popularity of the track. The value will be between 0 and 100, with 100 being the most
 * popular. The popularity of a track is a value between 0 and 100, with 100 being the most popular. The popularity
 * is calculated by algorithm and is based, in the most part, on the total number of plays the track has had and how
 * recent those plays are. Generally speaking, songs that are being played a lot now will have a higher popularity
 * than songs that were played a lot in the past. Duplicate tracks (e.g. the same track from a single and an album)
 * are rated independently. Artist and album popularity is derived mathematically from track popularity. Note that
 * the popularity value may lag actual popularity by a few days: the value is not updated in real time.
 * @param previewUrl A link to a 30 second preview (MP3 format) of the track. Can be null.
 * @param track Whether this episode is also a track.
 * @param trackNumber The number of the track. If an album has several discs, the track number is the number on the specified disc.
 * @param type The object type: “episode”.
 *
 */
@Serializable
public data class PodcastEpisodeTrack(
    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    @SerialName("available_markets") private val availableMarketsString: List<String> = listOf(),
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val episode: Boolean? = null,
    val explicit: Boolean,
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    @SerialName("external_ids") private val externalIdsString: Map<String, String> = hashMapOf(),
    override val href: String,
    override val id: String,
    @SerialName("is_local") val isLocal: Boolean? = null,
    @SerialName("is_playable") val isPlayable: Boolean = true,
    val name: String,
    val popularity: Int,
    @SerialName("preview_url") val previewUrl: String? = null,
    val track: Boolean? = null,
    @SerialName("track_number") val trackNumber: Int,
    override val type: String,
    override val uri: PlayableUri,
    override val linkedTrack: LinkedTrack? = null
) : RelinkingAvailableResponse(), Playable {
    val availableMarkets: List<Market> get() = availableMarketsString.map { Market.valueOf(it) }

    val externalIds: List<ExternalId> get() = externalIdsString.map { ExternalId(it.key, it.value) }

    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> = listOf(album) + artists + linkedTrack + this
}

/**
 * An episode (podcast) on Spotify
 *
 * @param audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @param description A description of the episode.
 * @param durationMs The episode length in milliseconds.
 * @param explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param images The cover art for the episode in various sizes, widest first.
 * @param isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @param isPlayable True if the episode is playable in the given market. Otherwise false.
 * @param name The name of the episode.
 * @param releaseDatePrecisionString The precision with which release_date value is known: "year", "month", or "day".
 * @param resumePoint The user’s most recent position in the episode. Set if the supplied access token is a user token and has the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 * @param type The object type: "episode".
 * @param show The show on which the episode belongs.
 *
 * @property languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @property releaseDate The date the episode was first released, for example "1981-12-15". Depending on the precision, it might be shown as "1981" or "1981-12".
 */
@Serializable
public data class Episode(
    @SerialName("audio_preview_url") val audioPreviewUrl: String? = null,
    val description: String? = null,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    val images: List<SpotifyImage>,
    @SerialName("is_externally_hosted") val isExternallyHosted: Boolean,
    @SerialName("is_playable") val isPlayable: Boolean,
    @Deprecated("This field is deprecated and might be removed in the future. Please use the languages field instead")
    private val language: String? = null,
    @SerialName("languages") private val showLanguagesPrivate: List<String>,
    val name: String,
    @SerialName("release_date") private val releaseDateString: String,
    @SerialName("release_date_precision") val releaseDatePrecisionString: String,
    @SerialName("resume_point") val resumePoint: ResumePoint? = null,
    val show: SimpleShow,
    val type: String,
    override val uri: EpisodeUri
) : CoreObject() {
    val releaseDate: ReleaseDate get() = getReleaseDate(releaseDateString)

    @Suppress("DEPRECATION")
    val languages: List<Locale>
        get() = (language?.let { showLanguagesPrivate + it } ?: showLanguagesPrivate).map { languageString ->
            Locale.valueOf(languageString.replace("-", "_"))
        }

    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> = listOf(show, this)
}

/**
 * A simplified episode (podcast) on Spotify
 *
 * @param audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @param description A description of the episode.
 * @param durationMs The episode length in milliseconds.
 * @param explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param images The cover art for the episode in various sizes, widest first.
 * @param isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @param isPlayable True if the episode is playable in the given market. Otherwise false.
 * @param name The name of the episode.
 * @param releaseDatePrecisionString The precision with which release_date value is known: "year", "month", or "day".
 * @param resumePoint The user’s most recent position in the episode. Set if the supplied access token is a user token and has the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 * @param type The object type: "episode".
 *
 * @property languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @property releaseDate The date the episode was first released, for example "1981-12-15". Depending on the precision, it might be shown as "1981" or "1981-12".
 */
@Serializable
public data class SimpleEpisode(
    @SerialName("audio_preview_url") val audioPreviewUrl: String? = null,
    val description: String? = null,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    val images: List<SpotifyImage>,
    @SerialName("is_externally_hosted") val isExternallyHosted: Boolean,
    @SerialName("is_playable") val isPlayable: Boolean,
    @Deprecated("This field is deprecated and might be removed in the future. Please use the languages field instead")
    private val language: String? = null,
    @SerialName("languages") private val showLanguagesPrivate: List<String>,
    val name: String,
    @SerialName("release_date") private val releaseDateString: String,
    @SerialName("release_date_precision") val releaseDatePrecisionString: String,
    @SerialName("resume_point") val resumePoint: ResumePoint? = null,
    val type: String,
    override val uri: SpotifyUri
) : CoreObject() {
    val releaseDate: ReleaseDate get() = getReleaseDate(releaseDateString)

    @Suppress("DEPRECATION")
    val languages: List<Locale>
        get() = (language?.let { showLanguagesPrivate + it } ?: showLanguagesPrivate)
            .map { Locale.valueOf(it.replace("-", "_")) }

    /**
     * Converts this [SimpleEpisode] into a full [Episode] object
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    public suspend fun toFullEpisode(market: Market): Episode? = api.episodes.getEpisode(id, market)

    /**
     * Converts this [SimpleEpisode] into a full [Episode] object
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    public fun toFullEpisodeRestAction(market: Market): SpotifyRestAction<Episode?> = SpotifyRestAction { toFullEpisode(market) }

    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> = listOf(this)
}

/**
 * Represents the user’s most recent position in the episode. Set if the supplied access token is a user token and has
 * the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 *
 * @param fullyPlayed Whether or not the episode has been fully played by the user.
 * @param resumePositionMs The user’s most recent position in the episode in milliseconds.
 */
@Serializable
public data class ResumePoint(
    @SerialName("fully_played") val fullyPlayed: Boolean,
    @SerialName("resume_position_ms") val resumePositionMs: Int
)

@Serializable
internal data class EpisodeList(val episodes: List<Episode?>)
