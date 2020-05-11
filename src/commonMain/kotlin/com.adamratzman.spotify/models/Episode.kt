/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.utils.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * An episode (podcast) on Spotify
 *
 * @property audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @property description A description of the episode.
 * @property durationMs The episode length in milliseconds.
 * @property explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @property images The cover art for the episode in various sizes, widest first.
 * @property isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @property isPlayable True if the episode is playable in the given market. Otherwise false.
 * @property languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @property releaseDate The date the episode was first released, for example "1981-12-15". Depending on the precision, it might be shown as "1981" or "1981-12".
 * @property name The name of the episode.
 * @property releaseDatePrecisionString The precision with which release_date value is known: "year", "month", or "day".
 * @property resumePoint The user’s most recent position in the episode. Set if the supplied access token is a user token and has the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 * @property type The object type: "episode".
 * @property show The show on which the episode belongs.
 */
@Serializable
data class Episode(
    @SerialName("audio_preview_url") val audioPreviewUrl: String? = null,
    val description: String,
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
    override val type: String,
    override val uri: EpisodeUri
) : CoreObject(), Playable {
    @Transient
    val releaseDate = getReleaseDate(releaseDateString)

    @Suppress("DEPRECATION")
    val languages
        get() = (language?.let { showLanguagesPrivate + it } ?: showLanguagesPrivate).map { languageString ->
                Locale.valueOf(languageString.replace("-", "_"))
        }
}

/**
 * A simplified episode (podcast) on Spotify
 *
 * @property audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @property description A description of the episode.
 * @property durationMs The episode length in milliseconds.
 * @property explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @property images The cover art for the episode in various sizes, widest first.
 * @property isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @property isPlayable True if the episode is playable in the given market. Otherwise false.
 * @property languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @property releaseDate The date the episode was first released, for example "1981-12-15". Depending on the precision, it might be shown as "1981" or "1981-12".
 * @property name The name of the episode.
 * @property releaseDatePrecisionString The precision with which release_date value is known: "year", "month", or "day".
 * @property resumePoint The user’s most recent position in the episode. Set if the supplied access token is a user token and has the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 * @property type The object type: "episode".
 */
@Serializable
data class SimpleEpisode(
    @SerialName("audio_preview_url") val audioPreviewUrl: String? = null,
    val description: String,
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
    private val name: String,
    @SerialName("release_date") private val releaseDateString: String,
    @SerialName("release_date_precision") val releaseDatePrecisionString: String,
    @SerialName("resume_point") val resumePoint: ResumePoint? = null,
    override val type: String,
    override val uri: EpisodeUri
) : CoreObject(), Playable {
    @Transient
    val releaseDate = getReleaseDate(releaseDateString)

    @Suppress("DEPRECATION")
    val languages
        get() = (language?.let { showLanguagesPrivate + it } ?: showLanguagesPrivate)
                .map { Locale.valueOf(it.replace("-", "_")) }
}

/**
 * Represents the user’s most recent position in the episode. Set if the supplied access token is a user token and has
 * the scope [SpotifyScope.USER_READ_PLAYBACK_POSITION].
 *
 * @property fullyPlayed Whether or not the episode has been fully played by the user.
 * @property resumePositionMs The user’s most recent position in the episode in milliseconds.
 */
@Serializable
data class ResumePoint(
    @SerialName("fully_played") val fullyPlayed: Boolean,
    @SerialName("resume_position_ms") val resumePositionMs: Int
)

@Serializable
internal data class EpisodeList(val episodes: List<Episode?>)
