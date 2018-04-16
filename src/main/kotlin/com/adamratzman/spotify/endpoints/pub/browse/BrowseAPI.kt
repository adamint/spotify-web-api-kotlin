package com.adamratzman.spotify.endpoints.pub.browse

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import org.json.JSONObject
import java.sql.Timestamp
import java.util.function.Supplier
import java.util.stream.Collectors

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
            JSONObject(get("https://api.spotify.com/v1/recommendations/available-genre-seeds")).getJSONArray("genres").map { it.toString() }
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
    fun getNewReleases(limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<PagingObject<Album>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/new-releases?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<Album>("albums", api)
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
     * @param timestamp Use this parameter to specify the user’s local time to get results tailored for that specific
     * date and time in the day. If not provided, the response defaults to the current UTC time.
     *
     * @throws BadRequestException if filter parameters are illegal or [locale] does not exist
     */
    fun getFeaturedPlaylists(limit: Int = 20, offset: Int = 0, locale: String? = null, market: Market? = null, timestamp: Timestamp? = null): SpotifyRestAction<FeaturedPlaylists> {
        return toAction(Supplier {
            timestamp?.let { timestamp.time = 1000 * (Math.floor(timestamp.time / 1000.0).toLong()) }
            get("https://api.spotify.com/v1/browse/featured-playlists?limit=$limit&offset=$offset" +
                    "${if (locale != null) "&locale=$locale" else ""}${if (market != null) "&market=${market.code}" else ""}${if (timestamp != null) "&timestamp=$timestamp" else ""}")
                    .toObject<FeaturedPlaylists>(api)
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
     */
    fun getCategoryList(limit: Int = 20, offset: Int = 0, locale: String? = null, market: Market? = null): SpotifyRestAction<PagingObject<SpotifyCategory>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/categories?limit=$limit&offset=$offset${if (locale != null) "&locale=$locale" else ""}${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<SpotifyCategory>("categories", api)
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
            val params = when {
                market != null && locale == null -> "?market=${market.code}"
                market == null && locale != null -> "?locale=$locale"
                market != null && locale != null -> "?market=${market.code}&locale=$locale"
                else -> null
            }
            get("https://api.spotify.com/v1/browse/categories/${categoryId.encode()}" + params).toObject<SpotifyCategory>(api)
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
    fun getPlaylistsForCategory(categoryId: String, market: Market? = null, limit: Int = 20, offset: Int = 0): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/categories/${categoryId.encode()}/playlists?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<SimplePlaylist>("playlists", api)
        })
    }

    /**
     * Create a playlist-style listening experience based on seed artists, tracks and genres.
     * Recommendations are generated based on the available information for a given seed entity and matched against similar
     * artists and tracks. If there is sufficient information about the provided seeds, a list of tracks will be returned
     * together with pool size details. For artists and tracks that are very new or obscure there might not be enough data
     * to generate a list of tracks.
     *
     * Tuneable track attribute descriptions and ranges are described [here](https://hastebin.com/olojoxonul.vbs)
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations
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
    fun getRecommendations(seedArtists: List<String>? = null, seedGenres: List<String>? = null, seedTracks: List<String>? = null, limit: Int = 20,
                           market: Market? = null, targetAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(),
                           minAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf(), maxAttributes: HashMap<TuneableTrackAttribute, Number> = hashMapOf())
            : SpotifyRestAction<RecommendationResponse> {
        return toAction(Supplier {
            val url = StringBuilder("https://api.spotify.com/v1/recommendations?limit=$limit")
            if (market != null) url.append("&market=${market.code}")
            if (seedArtists != null) url.append("&seed_artists=${seedArtists.map { it.encode() }.stream().collect(Collectors.joining(","))}")
            if (seedGenres != null) url.append("&seed_genres=${seedGenres.map { it.encode() }.stream().collect(Collectors.joining(","))}")
            if (seedTracks != null) url.append("&seed_tracks=${seedTracks.map { it.encode() }.stream().collect(Collectors.joining(","))}")
            if (targetAttributes.isNotEmpty()) targetAttributes.forEach { url.append("&target_${it.key.attribute}=${it.value}") }
            if (minAttributes.isNotEmpty()) minAttributes.forEach { url.append("&min_${it.key.attribute}=${it.value}") }
            if (maxAttributes.isNotEmpty()) maxAttributes.forEach { url.append("&max_${it.key.attribute}=${it.value}") }
            get(url.toString()).toObject<RecommendationResponse>(api)
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
    VALENCE("valence")
}