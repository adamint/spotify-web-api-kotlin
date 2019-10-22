/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.FeaturedPlaylists
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.RecommendationResponse
import com.adamratzman.spotify.models.RecommendationSeed
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.SpotifyCategory
import com.adamratzman.spotify.models.TrackUri
import com.adamratzman.spotify.models.serialization.toInnerArray
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.formatDate
import kotlinx.serialization.list
import kotlinx.serialization.serializer

typealias BrowseAPI = BrowseApi

/**
 * Endpoints for getting playlists and new album releases featured on Spotify’s Browse tab.
 */
class BrowseApi(api: SpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Retrieve a list of available genres seed parameter values for recommendations.
     *
     * @return List of genre ids
     */
    fun getAvailableGenreSeeds(): SpotifyRestAction<List<String>> {
        return toAction {
            get(EndpointBuilder("/recommendations/available-genre-seeds").toString()).toInnerArray(
                String.serializer().list,
                "genres"
            )
        }
    }

    /**
     * Get a list of new album releases featured in Spotify (shown, for example, on a Spotify player’s “Browse” tab).
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @throws BadRequestException if filter parameters are illegal
     * @return [PagingObject] of new album released, ordered by release date (descending)
     */
    fun getNewReleases(
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimpleAlbum, PagingObject<SimpleAlbum>> {
        return toActionPaging {
            get(
                EndpointBuilder("/browse/new-releases").with("limit", limit).with("offset", offset).with(
                    "country",
                    market?.name
                ).toString()
            ).toPagingObject(SimpleAlbum.serializer(), "albums", endpoint = this)
        }
    }

    /**
     * Get a list of Spotify featured playlists (shown, for example, on a Spotify player’s ‘Browse’ tab).
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param locale The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”.
     * Provide this parameter if you want the results returned in a particular language (where available).
     * Note that, if locale is not supplied, or if the specified language is not available,
     * all strings will be returned in the Spotify default language (American English. The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
     * For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param timestamp Use this parameter (time in milliseconds) to specify the user’s local time to get results tailored for that specific
     * date and time in the day. If not provided, the response defaults to the current UTC time.
     *
     * @throws BadRequestException if filter parameters are illegal or [locale] does not exist
     * @return [FeaturedPlaylists] object with the current featured message and featured playlists
     */
    fun getFeaturedPlaylists(
        limit: Int? = null,
        offset: Int? = null,
        locale: String? = null,
        market: Market? = null,
        timestamp: Long? = null
    ): SpotifyRestAction<FeaturedPlaylists> {
        return toAction {
            get(
                EndpointBuilder("/browse/featured-playlists").with("limit", limit).with("offset", offset).with(
                    "market",
                    market?.name
                ).with("locale", locale).with("timestamp", timestamp?.let {
                    formatDate("yyyy-MM-dd'T'HH:mm:ss", it)
                }).toString()
            ).toObject(FeaturedPlaylists.serializer(), api)
        }
    }

    /**
     * Get a list of categories used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param locale The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”.
     * Provide this parameter if you want the results returned in a particular language (where available).
     * Note that, if locale is not supplied, or if the specified language is not available,
     * all strings will be returned in the Spotify default language (American English. The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
     * For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Default category list if [locale] is invalid, otherwise the localized PagingObject
     */
    fun getCategoryList(
        limit: Int? = null,
        offset: Int? = null,
        locale: String? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SpotifyCategory, PagingObject<SpotifyCategory>> {
        return toActionPaging {
            get(
                EndpointBuilder("/browse/categories").with("limit", limit).with("offset", offset).with(
                    "market",
                    market?.name
                ).with("locale", locale).toString()
            ).toPagingObject(SpotifyCategory.serializer(), "categories", endpoint = this)
        }
    }

    /**
     * Get a single category used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     *
     * @param locale The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”.
     * Provide this parameter if you want the results returned in a particular language (where available).
     * Note that, if locale is not supplied, or if the specified language is not available,
     * all strings will be returned in the Spotify default language (American English. The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
     * For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @throws BadRequestException if [categoryId] is not found or [locale] does not exist on Spotify
     */
    fun getCategory(
        categoryId: String,
        market: Market? = null,
        locale: String? = null
    ): SpotifyRestAction<SpotifyCategory> {
        return toAction {
            get(
                EndpointBuilder("/browse/categories/${categoryId.encodeUrl()}").with("market", market?.name)
                    .with("locale", locale).toString()
            ).toObject(SpotifyCategory.serializer(), api)
        }
    }

    /**
     * Get a list of Spotify playlists tagged with a particular category.
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if [categoryId] is not found or filters are illegal
     * @return [PagingObject] of top playlists tagged with [categoryId]
     */
    fun getPlaylistsForCategory(
        categoryId: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging {
            get(
                EndpointBuilder("/browse/categories/${categoryId.encodeUrl()}/playlists").with(
                    "limit",
                    limit
                ).with("offset", offset)
                    .with("market", market?.name).toString()
            ).toPagingObject(SimplePlaylist.serializer(), "playlists", endpoint = this)
        }
    }

    /**
     * Create a playlist-style listening experience based on seed artists, tracks and genres.
     * Recommendations are generated based on the available information for a given seed entity and matched against similar
     * artists and tracks. If there is sufficient information about the provided seeds, a list of tracks will be returned
     * together with pool size details. For artists and tracks that are very new or obscure there might not be enough data
     * to generate a list of tracks.
     *
     * **5** seeds of any combination of [seedArtists], [seedGenres], and [seedTracks] can be provided. AT LEAST 1 seed must be provided.
     *
     * **All attributes** are weighted equally.
     *
     * See [here](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/#tuneable-track-attributes) for a list
     * and descriptions of tuneable track attributes and their ranges.
     *
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations. Invalid genres are ignored
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param targetAttributes For each of the tunable track attributes a target value may be provided.
     * Tracks with the attribute values nearest to the target values will be preferred.
     * @param minAttributes For each tunable track attribute, a hard floor on the selected track attribute’s value can be provided.
     * @param maxAttributes For each tunable track attribute, a hard ceiling on the selected track attribute’s value can be provided.
     * For example, setting max instrumentalness equal to 0.35 would filter out most tracks that are likely to be instrumental.
     *
     * @return [RecommendationResponse] with [RecommendationSeed]s used and [SimpleTrack]s found
     *
     * @throws BadRequestException if any filter is applied illegally
     */
    @Suppress("DEPRECATION")
    fun getTrackRecommendations(
        seedArtists: List<String>? = null,
        seedGenres: List<String>? = null,
        seedTracks: List<String>? = null,
        limit: Int? = null,
        market: Market? = null,
        targetAttributes: List<TrackAttribute<*>> = listOf(),
        minAttributes: List<TrackAttribute<*>> = listOf(),
        maxAttributes: List<TrackAttribute<*>> = listOf()
    ): SpotifyRestAction<RecommendationResponse> =
        getRecommendations(
            seedArtists,
            seedGenres,
            seedTracks,
            limit,
            market,
            targetAttributes.map { it.tuneableTrackAttribute to it.value }.toMap(),
            minAttributes.map { it.tuneableTrackAttribute to it.value }.toMap(),
            maxAttributes.map { it.tuneableTrackAttribute to it.value }.toMap()
        )

    /**
     * Create a playlist-style listening experience based on seed artists, tracks and genres.
     * Recommendations are generated based on the available information for a given seed entity and matched against similar
     * artists and tracks. If there is sufficient information about the provided seeds, a list of tracks will be returned
     * together with pool size details. For artists and tracks that are very new or obscure there might not be enough data
     * to generate a list of tracks.
     *
     * **5** seeds of any combination of [seedArtists], [seedGenres], and [seedTracks] can be provided. AT LEAST 1 seed must be provided.
     *
     * **All attributes** are weighted equally.
     *
     * See [here](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/#tuneable-track-attributes) for a list
     * and descriptions of tuneable track attributes and their ranges.
     *
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations. Invalid genres are ignored
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param targetAttributes For each of the tunable track attributes a target value may be provided.
     * Tracks with the attribute values nearest to the target values will be preferred.
     * @param minAttributes For each tunable track attribute, a hard floor on the selected track attribute’s value can be provided.
     * @param maxAttributes For each tunable track attribute, a hard ceiling on the selected track attribute’s value can be provided.
     * For example, setting max instrumentalness equal to 0.35 would filter out most tracks that are likely to be instrumental.
     *
     * @return [RecommendationResponse] with [RecommendationSeed]s used and [SimpleTrack]s found
     *
     * @throws BadRequestException if any filter is applied illegally
     *
     */
    @Deprecated("Ambiguous track attribute setting. Please use BrowseAPI#getTrackRecommendations instead")
    fun getRecommendations(
        seedArtists: List<String>? = null,
        seedGenres: List<String>? = null,
        seedTracks: List<String>? = null,
        limit: Int? = null,
        market: Market? = null,
        targetAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf(),
        minAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf(),
        maxAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf()
    ): SpotifyRestAction<RecommendationResponse> {
        if (seedArtists?.isEmpty() != false && seedGenres?.isEmpty() != false && seedTracks?.isEmpty() != false) {
            throw SpotifyException.BadRequestException(
                ErrorObject(
                    400,
                    "At least one seed (genre, artist, track) must be provided."
                )
            )
        }

        return toAction {
            val builder = EndpointBuilder("/recommendations").with("limit", limit).with("market", market?.name)
                .with("seed_artists", seedArtists?.joinToString(",") { ArtistUri(it).id.encodeUrl() })
                .with("seed_genres", seedGenres?.joinToString(",") { it.encodeUrl() })
                .with("seed_tracks", seedTracks?.joinToString(",") { TrackUri(it).id.encodeUrl() })
            targetAttributes.forEach { (attribute, value) -> builder.with("target_$attribute", value) }
            minAttributes.forEach { (attribute, value) -> builder.with("min_$attribute", value) }
            maxAttributes.forEach { (attribute, value) -> builder.with("max_$attribute", value) }
            get(builder.toString()).toObject(RecommendationResponse.serializer(), api)
        }
    }
}

/**
 * Describes a track attribute
 *
 * @param attribute The spotify id for the track attribute
 */
sealed class TuneableTrackAttribute<T : Number>(
    val attribute: String,
    val integerOnly: Boolean,
    val min: T?,
    val max: T?
) {
    /**
     * A confidence measure from 0.0 to 1.0 of whether the track is acoustic.
     * 1.0 represents high confidence the track is acoustic.
     */
    object ACOUSTICNESS : TuneableTrackAttribute<Float>("acousticness", false, 0f, 1f)

    /**
     * Danceability describes how suitable a track is for dancing based on a combination of musical
     * elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is
     * least danceable and 1.0 is most danceable.
     */
    object DANCEABILITY : TuneableTrackAttribute<Float>("danceability", false, 0f, 1f)

    /**
     * The duration of the track in milliseconds.
     */
    object DURATION_IN_MILLISECONDS : TuneableTrackAttribute<Int>("duration_ms", true, 0, null)

    /**
     * Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy,
     * while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute
     * include dynamic range, perceived loudness, timbre, onset rate, and general entropy.
     */
    object ENERGY : TuneableTrackAttribute<Float>("energy", false, 0f, 1f)

    /**
     * Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as
     * instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The
     * closer the instrumentalness value is to 1.0, the greater likelihood the track contains
     * no vocal content. Values above 0.5 are intended to represent instrumental tracks, but
     * confidence is higher as the value approaches 1.0.
     */
    object INSTRUMENTALNESS : TuneableTrackAttribute<Float>("instrumentalness", false, 0f, 1f)

    /**
     * The key the track is in. Integers map to pitches using standard Pitch Class notation.
     * E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.
     */
    object KEY : TuneableTrackAttribute<Int>("key", true, 0, 11)

    /**
     * Detects the presence of an audience in the recording. Higher liveness values represent an increased
     * probability that the track was performed live. A value above 0.8 provides strong likelihood
     * that the track is live.
     */
    object LIVENESS : TuneableTrackAttribute<Float>("liveness", false, 0f, 1f)

    /**
     * The overall loudness of a track in decibels (dB). Loudness values are averaged across the
     * entire track and are useful for comparing relative loudness of tracks. Loudness is the
     * quality of a sound that is the primary psychological correlate of physical strength (amplitude).
     * Values typically range between -60 and 0 db.
     */
    object LOUDNESS : TuneableTrackAttribute<Float>("loudness", false, null, null)

    /**
     * Mode indicates the modality (major or minor) of a track, the type of scale from which its
     * melodic content is derived. Major is represented by 1 and minor is 0.
     */
    object MODE : TuneableTrackAttribute<Int>("mode", true, 0, 1)

    /**
     * The popularity of the track. The value will be between 0 and 100, with 100 being the most popular.
     * The popularity is calculated by algorithm and is based, in the most part, on the total number of
     * plays the track has had and how recent those plays are. Note: When applying track relinking via
     * the market parameter, it is expected to find relinked tracks with popularities that do not match
     * min_*, max_*and target_* popularities. These relinked tracks are accurate replacements for unplayable tracks with the expected popularity scores. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response.
     */
    object POPULARITY : TuneableTrackAttribute<Int>("popularity", true, 0, 100)

    /**
     * Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the
     * recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above
     * 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66
     * describe tracks that may contain both music and speech, either in sections or layered, including
     * such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like
     * tracks.
     */
    object SPEECHINESS : TuneableTrackAttribute<Float>("speechiness", false, 0f, 1f)

    /**
     * The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the
     * speed or pace of a given piece and derives directly from the average beat duration.
     */
    object TEMPO : TuneableTrackAttribute<Float>("tempo", false, 0f, null)

    /**
     * An estimated overall time signature of a track. The time signature (meter)
     * is a notational convention to specify how many beats are in each bar (or measure).
     * The time signature ranges from 3 to 7 indicating time signatures of 3/4, to 7/4.
     * A value of -1 may indicate no time signature, while a value of 1 indicates a rather complex or changing time signature.
     */
    object TIME_SIGNATURE : TuneableTrackAttribute<Int>("time_signature", true, -1, 7)

    /**
     * A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high
     * valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence
     * sound more negative (e.g. sad, depressed, angry).
     */
    object VALENCE : TuneableTrackAttribute<Float>("valence", false, 0f, 1f)

    override fun toString() = attribute

    fun asTrackAttribute(value: T): TrackAttribute<T> {
        require(!(min != null && min.toDouble() > value.toDouble())) { "Attribute value for $this must be greater than $min!" }
        require(!(max != null && max.toDouble() < value.toDouble())) { "Attribute value for $this must be less than $max!" }

        return TrackAttribute(this, value)
    }

    companion object {
        fun values() = listOf(
            ACOUSTICNESS,
            DANCEABILITY,
            DURATION_IN_MILLISECONDS,
            ENERGY,
            INSTRUMENTALNESS,
            KEY,
            LIVENESS,
            LOUDNESS,
            MODE,
            POPULARITY,
            SPEECHINESS,
            TEMPO,
            TIME_SIGNATURE,
            VALENCE
        )
    }
}

data class TrackAttribute<T : Number>(val tuneableTrackAttribute: TuneableTrackAttribute<T>, val value: T) {
    companion object {
        fun <T : Number> create(tuneableTrackAttribute: TuneableTrackAttribute<T>, value: T) =
            tuneableTrackAttribute.asTrackAttribute(value)
    }
}
