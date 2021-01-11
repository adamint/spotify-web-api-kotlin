/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.FeaturedPlaylists
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.RecommendationResponse
import com.adamratzman.spotify.models.RecommendationSeed
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.SpotifyCategory
import com.adamratzman.spotify.models.serialization.toInnerArray
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.Locale
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.formatDate
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Endpoints for getting playlists and new album releases featured on Spotify’s Browse tab.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/)**
 */
public class BrowseApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Retrieve a list of available genres seed parameter values for recommendations.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/)**
     *
     * @return List of genre ids
     */
    public suspend fun getAvailableGenreSeeds(): List<String> =
        get(endpointBuilder("/recommendations/available-genre-seeds").toString()).toInnerArray(
            ListSerializer(String.serializer()),
            "genres",
            json
        )

    /**
     * Get a list of new album releases featured in Spotify (shown, for example, on a Spotify player’s “Browse” tab).
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-list-new-releases/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @throws BadRequestException if filter parameters are illegal
     * @return [PagingObject] of new album released, ordered by release date (descending)
     */
    public suspend fun getNewReleases(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SimpleAlbum> = get(
        endpointBuilder("/browse/new-releases").with("limit", limit).with("offset", offset).with(
            "country",
            market?.name
        ).toString()
    ).toPagingObject(SimpleAlbum.serializer(), "albums", endpoint = this, json = json)

    /**
     * Get a list of Spotify featured playlists (shown, for example, on a Spotify player’s ‘Browse’ tab).
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-list-featured-playlists/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
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
    public suspend fun getFeaturedPlaylists(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        locale: Locale? = null,
        market: Market? = null,
        timestamp: Long? = null
    ): FeaturedPlaylists = get(
        endpointBuilder("/browse/featured-playlists").with("limit", limit).with("offset", offset).with(
            "market",
            market?.name
        ).with("locale", locale).with("timestamp", timestamp?.let { formatDate(it) }).toString()
    ).toObject(FeaturedPlaylists.serializer(), api, json)

    /**
     * Get a list of categories used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-list-categories/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
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
    public suspend fun getCategoryList(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        locale: Locale? = null,
        market: Market? = null
    ): PagingObject<SpotifyCategory> = get(
        endpointBuilder("/browse/categories").with("limit", limit).with("offset", offset).with(
            "market",
            market?.name
        ).with("locale", locale).toString()
    ).toPagingObject(SpotifyCategory.serializer(), "categories", endpoint = this, json = json)

    /**
     * Get a single category used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-category/)**
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
    public suspend fun getCategory(
        categoryId: String,
        market: Market? = null,
        locale: Locale? = null
    ): SpotifyCategory = get(
        endpointBuilder("/browse/categories/${categoryId.encodeUrl()}").with("market", market?.name)
            .with("locale", locale).toString()
    ).toObject(SpotifyCategory.serializer(), api, json)

    /**
     * Get a list of Spotify playlists tagged with a particular category.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-categorys-playlists/)**
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if [categoryId] is not found or filters are illegal
     * @return [PagingObject] of top playlists tagged with [categoryId]
     */
    public suspend fun getPlaylistsForCategory(
        categoryId: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SimplePlaylist> = get(
        endpointBuilder("/browse/categories/${categoryId.encodeUrl()}/playlists").with(
            "limit",
            limit
        ).with("offset", offset)
            .with("market", market?.name).toString()
    ).toPagingObject((SimplePlaylist.serializer()), "playlists", endpoint = this, json = json)

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
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/)**
     *
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations. Invalid genres are ignored
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
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
    public suspend fun getTrackRecommendations(
        seedArtists: List<String>? = null,
        seedGenres: List<String>? = null,
        seedTracks: List<String>? = null,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        market: Market? = null,
        targetAttributes: List<TrackAttribute<*>> = listOf(),
        minAttributes: List<TrackAttribute<*>> = listOf(),
        maxAttributes: List<TrackAttribute<*>> = listOf()
    ): RecommendationResponse =
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
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/)**
     *
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations. Invalid genres are ignored
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
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
    public suspend fun getRecommendations(
        seedArtists: List<String>? = null,
        seedGenres: List<String>? = null,
        seedTracks: List<String>? = null,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        market: Market? = null,
        targetAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf(),
        minAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf(),
        maxAttributes: Map<TuneableTrackAttribute<*>, Number> = mapOf()
    ): RecommendationResponse {
        if (seedArtists?.isEmpty() != false && seedGenres?.isEmpty() != false && seedTracks?.isEmpty() != false) {
            throw BadRequestException(
                ErrorObject(
                    400,
                    "At least one seed (genre, artist, track) must be provided."
                )
            )
        }

        val builder = endpointBuilder("/recommendations").with("limit", limit).with("market", market?.name)
            .with("seed_artists", seedArtists?.joinToString(",") { ArtistUri(it).id.encodeUrl() })
            .with("seed_genres", seedGenres?.joinToString(",") { it.encodeUrl() })
            .with("seed_tracks", seedTracks?.joinToString(",") { PlayableUri(it).id.encodeUrl() })
        targetAttributes.forEach { (attribute, value) -> builder.with("target_$attribute", value) }
        minAttributes.forEach { (attribute, value) -> builder.with("min_$attribute", value) }
        maxAttributes.forEach { (attribute, value) -> builder.with("max_$attribute", value) }
        return get(builder.toString()).toObject(RecommendationResponse.serializer(), api, json)
    }
}

/**
 * Describes a track attribute
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/)**
 *
 * @param attribute The spotify id for the track attribute.
 * @param integerOnly Whether this attribute can only take integers.
 * @param min The minimum value allowed for this attribute.
 * @param max The maximum value allowed for this attribute.
 * @param typeClass The type, a subclass of [Number], one of [Float] or [Int] that corresponds to this attribute.
 */
@Serializable
public sealed class TuneableTrackAttribute<T : Number>(
    public val attribute: String,
    public val integerOnly: Boolean,
    public val min: T?,
    public val max: T?,
    public val typeClass: KClass<T>
) {
    /**
     * A confidence measure from 0.0 to 1.0 of whether the track is acoustic.
     * 1.0 represents high confidence the track is acoustic.
     */
    public object Acousticness : TuneableTrackAttribute<Float>("acousticness", false, 0f, 1f, Float::class)

    /**
     * Danceability describes how suitable a track is for dancing based on a combination of musical
     * elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is
     * least danceable and 1.0 is most danceable.
     */
    public object Danceability : TuneableTrackAttribute<Float>("danceability", false, 0f, 1f, Float::class)

    /**
     * The duration of the track in milliseconds.
     */
    public object DurationInMilliseconds : TuneableTrackAttribute<Int>("duration_ms", true, 0, null, Int::class)

    /**
     * Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy,
     * while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute
     * include dynamic range, perceived loudness, timbre, onset rate, and general entropy.
     */
    public object Energy : TuneableTrackAttribute<Float>("energy", false, 0f, 1f, Float::class)

    /**
     * Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as
     * instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The
     * closer the instrumentalness value is to 1.0, the greater likelihood the track contains
     * no vocal content. Values above 0.5 are intended to represent instrumental tracks, but
     * confidence is higher as the value approaches 1.0.
     */
    public object Instrumentalness : TuneableTrackAttribute<Float>("instrumentalness", false, 0f, 1f, Float::class)

    /**
     * The key the track is in. Integers map to pitches using standard Pitch Class notation.
     * E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.
     */
    public object Key : TuneableTrackAttribute<Int>("key", true, 0, 11, Int::class)

    /**
     * Detects the presence of an audience in the recording. Higher liveness values represent an increased
     * probability that the track was performed live. A value above 0.8 provides strong likelihood
     * that the track is live.
     */
    public object Liveness : TuneableTrackAttribute<Float>("liveness", false, 0f, 1f, Float::class)

    /**
     * The overall loudness of a track in decibels (dB). Loudness values are averaged across the
     * entire track and are useful for comparing relative loudness of tracks. Loudness is the
     * quality of a sound that is the primary psychological correlate of physical strength (amplitude).
     * Values typically range between -60 and 0 db.
     */
    public object Loudness : TuneableTrackAttribute<Float>("loudness", false, null, null, Float::class)

    /**
     * Mode indicates the modality (major or minor) of a track, the type of scale from which its
     * melodic content is derived. Major is represented by 1 and minor is 0.
     */
    public object Mode : TuneableTrackAttribute<Int>("mode", true, 0, 1, Int::class)

    /**
     * The popularity of the track. The value will be between 0 and 100, with 100 being the most popular.
     * The popularity is calculated by algorithm and is based, in the most part, on the total number of
     * plays the track has had and how recent those plays are. Note: When applying track relinking via
     * the market parameter, it is expected to find relinked tracks with popularities that do not match
     * min_*, max_*and target_* popularities. These relinked tracks are accurate replacements for unplayable tracks with the expected popularity scores. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response.
     */
    public object Popularity : TuneableTrackAttribute<Int>("popularity", true, 0, 100, Int::class)

    /**
     * Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the
     * recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above
     * 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66
     * describe tracks that may contain both music and speech, either in sections or layered, including
     * such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like
     * tracks.
     */
    public object Speechiness : TuneableTrackAttribute<Float>("speechiness", false, 0f, 1f, Float::class)

    /**
     * The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the
     * speed or pace of a given piece and derives directly from the average beat duration.
     */
    public object Tempo : TuneableTrackAttribute<Float>("tempo", false, 0f, null, Float::class)

    /**
     * An estimated overall time signature of a track. The time signature (meter)
     * is a notational convention to specify how many beats are in each bar (or measure).
     * The time signature ranges from 3 to 7 indicating time signatures of 3/4, to 7/4.
     * A value of -1 may indicate no time signature, while a value of 1 indicates a rather complex or changing time signature.
     */
    public object TimeSignature : TuneableTrackAttribute<Int>("time_signature", true, -1, 7, Int::class)

    /**
     * A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high
     * valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence
     * sound more negative (e.g. sad, depressed, angry).
     */
    public object Valence : TuneableTrackAttribute<Float>("valence", false, 0f, 1f, Float::class)

    override fun toString(): String = attribute

    public fun <V : Number> asTrackAttribute(value: V): TrackAttribute<T> {
        require(!(min != null && min.toDouble() > value.toDouble())) { "Attribute value for $this must be greater than $min!" }
        require(!(max != null && max.toDouble() < value.toDouble())) { "Attribute value for $this must be less than $max!" }

        @Suppress("UNCHECKED_CAST")
        return TrackAttribute(
            this, when (typeClass) {
                Int::class -> value.toInt() as T
                Float::class -> value.toFloat() as T
                Double::class -> value.toDouble() as T
                else -> value.toDouble() as T
            }
        )
    }

    public companion object {
        public fun values(): List<TuneableTrackAttribute<*>> = listOf(
            Acousticness,
            Danceability,
            DurationInMilliseconds,
            Energy,
            Instrumentalness,
            Key,
            Liveness,
            Loudness,
            Mode,
            Popularity,
            Speechiness,
            Tempo,
            TimeSignature,
            Valence
        )
    }
}

/**
 * The track attribute wrapper contains a set value for a specific [TuneableTrackAttribute]
 *
 * @param tuneableTrackAttribute The [TuneableTrackAttribute] that this [TrackAttribute] will correspond to.
 * @param value The value of the [tuneableTrackAttribute].
 */
@Serializable
public data class TrackAttribute<T : Number>(val tuneableTrackAttribute: TuneableTrackAttribute<T>, val value: T) {
    public companion object {
        public fun <T : Number> create(tuneableTrackAttribute: TuneableTrackAttribute<T>, value: T): TrackAttribute<T> =
            tuneableTrackAttribute.asTrackAttribute(value)
    }
}
