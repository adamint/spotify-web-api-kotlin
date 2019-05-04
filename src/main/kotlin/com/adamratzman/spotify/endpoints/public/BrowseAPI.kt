/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.models.ArtistURI
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.ErrorObject
import com.adamratzman.spotify.models.FeaturedPlaylists
import com.adamratzman.spotify.models.Market
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.RecommendationResponse
import com.adamratzman.spotify.models.RecommendationSeed
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.SpotifyCategory
import com.adamratzman.spotify.models.TrackURI
import com.adamratzman.spotify.models.serialization.toInnerArray
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.HashMap
import java.util.function.Supplier

/**
 * Endpoints for getting playlists and new album releases featured on Spotify’s Browse tab.
 */
class BrowseAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get available genre seeds for recommendations
     *
     * @return List of genre ids
     */
    fun getAvailableGenreSeeds(): SpotifyRestAction<List<String>> {
        return toAction(Supplier {
            get(EndpointBuilder("/recommendations/available-genre-seeds").toString()).toInnerArray<String>(
                    "genres",
                    api
            )
        })
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
     */
    fun getNewReleases(
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimpleAlbum, PagingObject<SimpleAlbum>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/browse/new-releases").with("limit", limit).with("offset", offset).with(
                            "country",
                            market?.code
                    ).toString()
            ).toPagingObject<SimpleAlbum>(
                    "albums", endpoint = this
            )
        })
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
     */
    fun getFeaturedPlaylists(
        limit: Int? = null,
        offset: Int? = null,
        locale: String? = null,
        market: Market? = null,
        timestamp: Long? = null
    ): SpotifyRestAction<FeaturedPlaylists> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/browse/featured-playlists").with("limit", limit).with("offset", offset).with(
                            "market",
                            market?.code
                    )
                            .with("locale", locale).with("timestamp", timestamp?.let {
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Date.from(Instant.ofEpochMilli(timestamp)))
                            }).toString()
            ).toObject<FeaturedPlaylists>(api)
        })
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
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/browse/categories").with("limit", limit).with("offset", offset).with(
                            "market",
                            market?.code
                    ).with("locale", locale).toString()
            ).toPagingObject<SpotifyCategory>(
                    "categories", endpoint = this
            )
        })
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
        return toAction(Supplier {
            get(
                    EndpointBuilder("/browse/categories/${categoryId.encode()}").with("market", market?.code)
                            .with("locale", locale).toString()
            ).toObject<SpotifyCategory>(api)
        })
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
     */
    fun getPlaylistsForCategory(
        categoryId: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/browse/categories/${categoryId.encode()}/playlists").with(
                            "limit",
                            limit
                    ).with("offset", offset)
                            .with("market", market?.code).toString()
            ).toPagingObject<SimplePlaylist>("playlists", endpoint = this)
        })
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
    fun getRecommendations(
        seedArtists: List<String>? = null,
        seedGenres: List<String>? = null,
        seedTracks: List<String>? = null,
        limit: Int? = null,
        market: Market? = null,
        targetAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(),
        minAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(),
        maxAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf()
    ): SpotifyRestAction<RecommendationResponse> {
        if (seedArtists?.isEmpty() != false && seedGenres?.isEmpty() != false && seedTracks?.isEmpty() != false) {
            throw BadRequestException(ErrorObject(400, "At least one seed (genre, artist, track) must be provided."))
        }
        return toAction(Supplier {
            val builder = EndpointBuilder("/recommendations").with("limit", limit).with("market", market?.code)
                    .with("seed_artists", seedArtists?.joinToString(",") { ArtistURI(it).id.encode() })
                    .with("seed_genres", seedGenres?.joinToString(",") { it.encode() })
                    .with("seed_tracks", seedTracks?.joinToString(",") { TrackURI(it).id.encode() })
            targetAttributes.forEach { attribute, value -> builder.with("target_$attribute", value) }
            minAttributes.forEach { attribute, value -> builder.with("min_$attribute", value) }
            maxAttributes.forEach { attribute, value -> builder.with("max_$attribute", value) }
            get(builder.toString()).toObject<RecommendationResponse>(api)
        })
    }
}

/**
 * Describes a track attribute
 *
 * @param attribute the spotify id for the track attribute
 */
enum class TuneableTrackAttribute(private val attribute: String) {
    /**
     * A confidence measure from 0.0 to 1.0 of whether the track is acoustic.
     * 1.0 represents high confidence the track is acoustic.
     */
    ACOUSTICNESS("acousticness"),
    /**
     * Danceability describes how suitable a track is for dancing based on a combination of musical
     * elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is
     * least danceable and 1.0 is most danceable.
     */
    DANCEABILITY("danceability"),
    /**
     * The duration of the track in milliseconds.
     */
    DURATION_IN_MILLISECONDS("duration_ms"),
    /**
     * Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy,
     * while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute
     * include dynamic range, perceived loudness, timbre, onset rate, and general entropy.
     */
    ENERGY("energy"),
    /**
     * Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as
     * instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The
     * closer the instrumentalness value is to 1.0, the greater likelihood the track contains
     * no vocal content. Values above 0.5 are intended to represent instrumental tracks, but
     * confidence is higher as the value approaches 1.0.
     */
    INSTRUMENTALNESS("instrumentalness"),
    /**
     * The key the track is in. Integers map to pitches using standard Pitch Class notation.
     * E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.
     */
    KEY("key"),
    /**
     * Detects the presence of an audience in the recording. Higher liveness values represent an increased
     * probability that the track was performed live. A value above 0.8 provides strong likelihood
     * that the track is live.
     */
    LIVENESS("liveness"),
    /**
     * The overall loudness of a track in decibels (dB). Loudness values are averaged across the
     * entire track and are useful for comparing relative loudness of tracks. Loudness is the
     * quality of a sound that is the primary psychological correlate of physical strength (amplitude).
     * Values typical range between -60 and 0 db.
     */
    LOUDNESS("loudness"),
    /**
     * Mode indicates the modality (major or minor) of a track, the type of scale from which its
     * melodic content is derived. Major is represented by 1 and minor is 0.
     */
    MODE("mode"),
    /**
     * The popularity of the track. The value will be between 0 and 100, with 100 being the most popular.
     * The popularity is calculated by algorithm and is based, in the most part, on the total number of
     * plays the track has had and how recent those plays are. Note: When applying track relinking via
     * the market parameter, it is expected to find relinked tracks with popularities that do not match
     * min_*, max_*and target_* popularities. These relinked tracks are accurate replacements for unplayable tracks with the expected popularity scores. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response.
     */
    POPULARITY("popularity"),
    /**
     * Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the
     * recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above
     * 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66
     * describe tracks that may contain both music and speech, either in sections or layered, including
     * such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like
     * tracks.
     */
    SPEECHINESS("speechiness"),
    /**
     * The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the
     * speed or pace of a given piece and derives directly from the average beat duration.
     */
    TEMPO("tempo"),
    /**
     * An estimated overall time signature of a track. The time signature (meter)
     * is a notational convention to specify how many beats are in each bar (or measure).
     */
    TIME_SIGNATURE("time_signature"),
    /**
     * A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high
     * valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence
     * sound more negative (e.g. sad, depressed, angry).
     */
    VALENCE("valence");

    override fun toString() = attribute
}