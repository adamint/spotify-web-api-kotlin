/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Simplified Playlist object that can be used to retrieve a full [Playlist]
 *
 * @property artists The artists who performed the track. Each artist object includes a link in href to
 * more detailed information about the artist.
 * @property availableMarkets A list of the countries in which the track can be played,
 * identified by their ISO 3166-1 alpha-2 code.
 * @property discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @property durationMs The track length in milliseconds.
 * @property explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
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
 * @property isLocal Whether or not the track is from a local file.
 * @property popularity the popularity of this track. possibly null
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available in
 * the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
@Serializable
data class SimpleTrack(
    @SerialName("external_urls") override val _externalUrls: Map<String, String>,
    @SerialName("available_markets") private val _availableMarkets: List<String> = listOf(),
    @SerialName("external_ids") private val _externalIds: Map<String, String> = hashMapOf(),
    @SerialName("href") override val href: String,
    @SerialName("id") override val id: String,
    @SerialName("uri") val _uri: String,

    val artists: List<SimpleArtist>,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("is_playable") val isPlayable: Boolean = true,
    @SerialName("linked_from") private val linkedFrom: LinkedTrack? = null,
    val name: String,
    @SerialName("preview_url") val previewUrl: String?,
    @SerialName("track_number") val trackNumber: Int,
    val type: String,
    @SerialName("is_local") val isLocal: Boolean? = null,
    val popularity: Int? = null,
    val restrictions: Restrictions? = null
) : RelinkingAvailableResponse(linkedFrom, href, id, TrackURI(_uri), _externalUrls) {
    @Transient
    val availableMarkets = _availableMarkets.map { CountryCode.valueOf(it) }

    @Transient
    val externalIds = _externalIds.map { ExternalId(it.key, it.value) }

    /**
     * Converts this [SimpleTrack] into a full [Track] object with the given
     * market
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    fun toFullTrack(market: CountryCode? = null) = api.tracks.getTrack(id, market)
}

/**
 * Represents a music track on Spotify
 *
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
 * @property isLocal Whether or not the track is from a local file.
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available in
 * the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
@Serializable
data class Track(
    @SerialName("external_urls") override val _externalUrls: Map<String, String>,
    @SerialName("external_ids") private val _externalIds: Map<String, String> = hashMapOf(),
    @SerialName("available_markets") private val _availableMarkets: List<String> = listOf(),
    @SerialName("href") private val _href: String,
    @SerialName("id") private val _id: String,
    @SerialName("uri") private val _uri: String,

    val album: SimpleAlbum,
    val artists: List<SimpleArtist>,
    @SerialName("is_playable") val isPlayable: Boolean = true,
    @SerialName("disc_number") val discNumber: Int,
    @SerialName("duration_ms") val durationMs: Int,
    val explicit: Boolean,
    @SerialName("linked_from") private val linked_from: LinkedTrack? = null,
    val name: String,
    val popularity: Int,
    @SerialName("preview_url") val previewUrl: String?,
    @SerialName("track_number") val trackNumber: Int,
    val type: String,
    @SerialName("is_local") val isLocal: Boolean?,
    val restrictions: Restrictions? = null
) : RelinkingAvailableResponse(
    linked_from,
    _href,
    _id,
    if (_uri.contains("local:")) LocalTrackURI(_uri) else TrackURI(_uri),
    _externalUrls
) {
    @Transient
    val availableMarkets = _availableMarkets.map { CountryCode.valueOf(it) }

    @Transient
    val externalIds = _externalIds.map { ExternalId(it.key, it.value) }
}

/**
 * Represents a [relinked track](https:github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the
 * searched market. If null, the API result is playable in the market.
 *
 * @property href A link to the Web API endpoint providing full details of the track.
 * @property id The Spotify ID for the track.
 * @property type The object type: “track”.
 */
@Serializable
data class LinkedTrack(
    @SerialName("external_urls") override val _externalUrls: Map<String, String>,
    @SerialName("href") private val _href: String,
    @SerialName("id") private val _id: String,
    @SerialName("uri") private val _uri: String,

    val type: String
) : CoreObject(_href, _id, TrackURI(_uri), _externalUrls) {

    /**
     * Retrieves the full [Track] object associated with this [LinkedTrack] with the given market
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */

    fun toFullTrack(market: CountryCode? = null) = api.tracks.getTrack(id, market)
}

@Serializable
internal data class AudioFeaturesResponse(
    @SerialName("audio_features") val audioFeatures: List<AudioFeatures?>
)

/**
 * The Audio Analysis endpoint provides low-level audio analysis for all of the tracks
 * in the Spotify catalog. The Audio Analysis describes the track’s structure
 * and musical content, including rhythm, pitch, and timbre. All information is
 * precise to the audio sample. Many elements of analysis include confidence values,
 * a floating-point number ranging from 0.0 to 1.0. Confidence indicates the reliability
 * of its corresponding attribute. Elements carrying a small confidence value should
 * be considered speculative. There may not be sufficient data in the audio to
 * compute the attribute with high certainty.
 *
 *
 * @property bars The time intervals of the bars throughout the track. A bar (or measure) is a segment of time defined as
 * a given number of beats. Bar offsets also indicate downbeats, the first beat of the measure.
 * @property beats The time intervals of beats throughout the track. A beat is the basic time unit of a piece of music;
 * for example, each tick of a metronome. Beats are typically multiples of tatums.
 * @property meta Analysis meta information (limited use)
 * @property sections Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar
 * solo, etc. Each section contains its own descriptions of tempo, key, mode, time_signature, and loudness.
 * @property segments Audio segments attempts to subdivide a song into many segments, with each segment containing
 * a roughly consitent sound throughout its duration.
 * @property tatums A tatum represents the lowest regular pulse train that a listener intuitively infers from the timing
 * of perceived musical events (segments).
 * @property track An analysis of the track as a whole. Undocumented on Spotify's side.
 */
@Serializable
data class AudioAnalysis(
    val bars: List<TimeInterval>,
    val beats: List<TimeInterval>,
    val meta: AudioAnalysisMeta,
    val sections: List<AudioSection>,
    val segments: List<AudioSegment>,
    val tatums: List<TimeInterval>,
    val track: TrackAnalysis
)

/**
 * Information about the analysis run
 *
 * @property analyzerVersion Which version of the Spotify analyzer the analysis was run on
 * @property platform The OS the analysis was run on
 * @property detailedStatus Whether there was an error in the analysis or "OK"
 * @property statusCode 0 on success, any other integer on error
 * @property timestamp When this analysis was completed
 * @property analysisTime How long, in milliseconds, this analysis took to run
 * @property inputProcess The process used in the analysis
 */
@Serializable
data class AudioAnalysisMeta(
    @SerialName("analyzer_version") val analyzerVersion: String,
    val platform: String,
    @SerialName("detailed_status") val detailedStatus: String,
    @SerialName("status_code") val statusCode: Int,
    val timestamp: Long,
    @SerialName("analysis_time") val analysisTime: Float,
    @SerialName("input_process") val inputProcess: String
)

/**
 * Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar solo, etc.
 * Each section contains its own descriptions of tempo, key, mode, time_signature, and loudness.*
 *
 * @property start The starting point (in seconds) of the section.
 * @property duration The duration (in seconds) of the section.
 * @property confidence The confidence, from 0.0 to 1.0, of the reliability of the section’s “designation”.
 * @property loudness The overall loudness of the section in decibels (dB). Loudness values are useful
 * for comparing relative loudness of sections within tracks.
 * @property tempo The overall estimated tempo of the section in beats per minute (BPM). In musical terminology, tempo
 * is the speed or pace of a given piece and derives directly from the average beat duration.
 * @property tempoConfidence The confidence, from 0.0 to 1.0, of the reliability of the tempo. Some tracks contain tempo
 * changes or sounds which don’t contain tempo (like pure speech) which would correspond to a low value in this field.
 * @property key The estimated overall key of the section. The values in this field ranging from 0 to 11 mapping to
 * pitches using standard Pitch Class notation (E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on). If no key was detected,
 * the value is -1.
 * @property keyConfidence The confidence, from 0.0 to 1.0, of the reliability of the key.
 * Songs with many key changes may correspond to low values in this field.
 * @property mode Indicates the modality (major or minor) of a track, the type of scale from which its melodic content is
 * derived. This field will contain a 0 for “minor”, a 1 for “major”, or a -1 for no result. Note that the major key
 * (e.g. C major) could more likely be confused with the minor key at 3 semitones lower (e.g. A minor) as both
 * keys carry the same pitches.
 * @property modeConfidence The confidence, from 0.0 to 1.0, of the reliability of the mode.
 * @property timeSignature An estimated overall time signature of a track. The time signature (meter) is a notational
 * convention to specify how many beats are in each bar (or measure). The time signature ranges from 3 to 7
 * indicating time signatures of “3/4”, to “7/4”.
 * @property timeSignatureConfidence The confidence, from 0.0 to 1.0, of the reliability of the time_signature.
 * Sections with time signature changes may correspond to low values in this field.
 */
@Serializable
data class AudioSection(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    val loudness: Float,
    val tempo: Float,
    @SerialName("tempo_confidence") val tempoConfidence: Float,
    val key: Int,
    @SerialName("key_confidence") val keyConfidence: Float,
    val mode: Int,
    @SerialName("mode_confidence") val modeConfidence: Float,
    @SerialName("time_signature") val timeSignature: Int,
    @SerialName("time_signature_confidence") val timeSignatureConfidence: Float
)

/**
 * Audio segments attempts to subdivide a song into many segments, with each segment containing
 * a roughly consistent sound throughout its duration.
 *
 * @property start The starting point (in seconds) of the segment.
 * @property duration The duration (in seconds) of the segment.
 * @property confidence The confidence, from 0.0 to 1.0, of the reliability of the segmentation. Segments of the song which
 * are difficult to logically segment (e.g: noise) may correspond to low values in this field.
 * @property loudnessStart The onset loudness of the segment in decibels (dB). Combined with loudness_max and
 * loudness_max_time, these components can be used to desctibe the “attack” of the segment.
 * @property loudnessMaxTime The segment-relative offset of the segment peak loudness in seconds. Combined with
 * loudness_start and loudness_max, these components can be used to desctibe the “attack” of the segment.
 * @property loudnessMax The peak loudness of the segment in decibels (dB). Combined with loudness_start and
 * loudness_max_time, these components can be used to desctibe the “attack” of the segment.
 * @property loudnessEnd The offset loudness of the segment in decibels (dB). This value should be equivalent to the
 * loudness_start of the following segment.
 * @property pitches A “chroma” vector representing the pitch content of the segment, corresponding to the 12 pitch classes
 * C, C#, D to B, with values ranging from 0 to 1 that describe the relative dominance of every pitch in the chromatic scale
 * @property timbre Timbre is the quality of a musical note or sound that distinguishes different types of musical
 * instruments, or voices. Timbre vectors are best used in comparison with each other.
 */
@Serializable
data class AudioSegment(
    val start: Float,
    val duration: Float,
    val confidence: Float,
    @SerialName("loudness_start") val loudnessStart: Float,
    @SerialName("loudness_max_time") val loudnessMaxTime: Float,
    @SerialName("loudness_max") val loudnessMax: Float,
    @SerialName("loudness_end") val loudnessEnd: Float? = null,
    val pitches: List<Float>,
    val timbre: List<Float>
)

/**
 * General information about the track as a whole
 */
@Serializable
data class TrackAnalysis(
    @SerialName("num_samples") val numSamples: Int,
    val duration: Float,
    @SerialName("sample_md5") val sampleMd5: String,
    @SerialName("offset_seconds") val offsetSeconds: Int,
    @SerialName("window_seconds") val windowSeconds: Int,
    @SerialName("analysis_sample_rate") val analysisSampleRate: Int,
    @SerialName("analysis_channels") val analysisChannels: Int,
    @SerialName("end_of_fade_in") val endOfFadeIn: Float,
    @SerialName("start_of_fade_out") val startOfFadeOut: Float,
    val loudness: Float,
    val tempo: Float,
    @SerialName("tempo_confidence") val tempoConfidence: Float,
    @SerialName("time_signature") val timeSignature: Int,
    @SerialName("time_signature_confidence") val timeSignatureConfidence: Float,
    val key: Int,
    @SerialName("key_confidence") val keyConfidence: Float,
    val mode: Int,
    @SerialName("mode_confidence") val modeConfidence: Float,
    val codestring: String,
    @SerialName("code_version") val codeVersion: Float,
    val echoprintstring: String,
    @SerialName("echoprint_version") val echoprintVersion: Float,
    val synchstring: String,
    @SerialName("synch_version") val synchVersion: Float,
    val rhythmstring: String,
    @SerialName("rhythm_version") val rhythmVersion: Float
)

/**
 * General attributes of a [Track]
 *
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
@Serializable
data class AudioFeatures(
    val acousticness: Float,
    @SerialName("analysis_url") val analysisUrl: String,
    val danceability: Float,
    @SerialName("duration_ms") val durationMs: Int,
    val energy: Float,
    val id: String,
    val instrumentalness: Float,
    val key: Int,
    val liveness: Float,
    val loudness: Float,
    val mode: Int,
    val speechiness: Float,
    val tempo: Float,
    @SerialName("time_signature") val timeSignature: Int,
    @SerialName("track_href") val trackHref: String,
    val type: String,
    @SerialName("uri") private val _uri: String,
    val valence: Float
) {
    @Transient
    val uri: TrackURI = TrackURI(_uri)
}

/**
 * This is a generic object used to represent various time intervals within Audio Analysis.
 *
 * @property start The starting point (in seconds) of the time interval.
 * @property duration The duration (in seconds) of the time interval.
 * @property confidence The confidence, from 0.0 to 1.0, of the reliability of the interval
 */
@Serializable
data class TimeInterval(val start: Float, val duration: Float, val confidence: Float)

@Serializable
internal data class TrackList(val tracks: List<Track?>)