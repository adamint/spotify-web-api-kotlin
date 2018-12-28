package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.SpotifyRestPagingAction
import com.adamratzman.spotify.utils.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
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
            JSONObject(get(EndpointBuilder("/recommendations/available-genre-seeds").toString())).getJSONArray("genres").map { it.toString() }
        })
    }

    /**
     * Get a list of new album releases featured in Spotify (shown, for example, on a Spotify player’s “Browse” tab).
     *
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @throws BadRequestException if filter parameters are illegal
     */
    fun getNewReleases(limit: Int? = null, offset: Int? = null, market: Market? = null): SpotifyRestPagingAction<Album, PagingObject<Album>> {
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/browse/new-releases").with("limit", limit).with("offset", offset).with("country", market?.code).toString())
                    .toPagingObject("albums", endpoint = this, tClazz = Album::class.java)
        })
    }

    /**
     * Get a list of Spotify featured playlists (shown, for example, on a Spotify player’s ‘Browse’ tab).
     *
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
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
    fun getFeaturedPlaylists(limit: Int? = null, offset: Int? = null, locale: String? = null, market: Market? = null, timestamp: Long? = null): SpotifyRestAction<FeaturedPlaylists> {
        return toAction(Supplier {
            get(EndpointBuilder("/browse/featured-playlists").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .with("locale", locale).with("timestamp", timestamp?.let {
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Date.from(Instant.ofEpochMilli(timestamp)))
                    }).toString()).toObject(api, FeaturedPlaylists::class.java)
        })
    }

    /**
     * Get a list of categories used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).
     *
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
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
    fun getCategoryList(limit: Int? = null, offset: Int? = null, locale: String? = null, market: Market? = null): SpotifyRestPagingAction<SpotifyCategory, PagingObject<SpotifyCategory>> {
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/browse/categories").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .with("locale", locale).toString()).toPagingObject("categories", endpoint = this, tClazz = SpotifyCategory::class.java)
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
    fun getCategory(categoryId: String, market: Market? = null, locale: String? = null): SpotifyRestAction<SpotifyCategory> {
        return toAction(Supplier {
            get(EndpointBuilder("/browse/categories/${categoryId.encode()}").with("market", market?.code)
                    .with("locale", locale).toString()).toObject(api, SpotifyCategory::class.java)
        })
    }

    /**
     * Get a list of Spotify playlists tagged with a particular category.
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @throws BadRequestException if [categoryId] is not found or filters are illegal
     */
    fun getPlaylistsForCategory(categoryId: String, limit: Int? = null, offset: Int? = null, market: Market? = null): SpotifyRestPagingAction<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/browse/categories/${categoryId.encode()}/playlists").with("limit", limit).with("offset", offset)
                    .with("market", market?.code).toString()).toPagingObject("playlists", endpoint = this, tClazz = SimplePlaylist::class.java)
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
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
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
    fun getRecommendations(seedArtists: List<String>? = null, seedGenres: List<String>? = null, seedTracks: List<String>? = null, limit: Int? = null,
                           market: Market? = null, targetAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(),
                           minAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(), maxAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf())
            : SpotifyRestAction<RecommendationResponse> {
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
            get(builder.toString()).toObject(api, RecommendationResponse::class.java)
        })
    }
}

enum class TuneableTrackAttribute(val attribute: String) {
    ACOUSTICNESS("acousticness"),
    DANCEABILITY("danceability"),
    DURATION_IN_MILLISECONDS("duration_ms"),
    ENERGY("energy"),
    INSTRUMENTALNESS("instrumentalness"),
    KEY("key"),
    LIVENESS("liveness"),
    LOUDNESS("loudness"),
    MODE("mode"),
    POPULARITY("popularity"),
    SPEECHINESS("speechiness"),
    TEMPO("tempo"),
    TIME_SIGNATURE("time_signature"),
    VALENCE("valence");

    override fun toString() = attribute
}