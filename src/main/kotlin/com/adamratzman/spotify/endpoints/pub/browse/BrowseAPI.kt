package com.adamratzman.spotify.endpoints.pub.browse

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * Endpoints for getting playlists and new album releases featured on Spotify’s Browse tab.
 */
class BrowseAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getNewReleases(limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<PagingObject<Album>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/new-releases?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<Album>("albums", api)
        })
    }

    fun getFeaturedPlaylists(limit: Int = 20, offset: Int = 0, locale: String? = null, market: Market? = null): SpotifyRestAction<FeaturedPlaylists> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/featured-playlists?limit=$limit&offset=$offset${if (locale != null) "&locale=$locale" else ""}${if (market != null) "&market=${market.code}" else ""}").toObject<FeaturedPlaylists>(api)
        })
    }

    fun getCategoryList(limit: Int = 20, offset: Int = 0, locale: String? = null, market: Market? = null): SpotifyRestAction<PagingObject<SpotifyCategory>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/categories?limit=$limit&offset=$offset${if (locale != null) "&locale=$locale" else ""}${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<SpotifyCategory>("categories", api)
        })
    }

    fun getCategory(categoryId: String, market: Market? = null): SpotifyRestAction<SpotifyCategory> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/categories/${categoryId.encode()}${if (market != null) "?market=${market.code}" else ""}").toObject<SpotifyCategory>(api)
        })
    }

    fun getPlaylistsForCategory(categoryId: String, market: Market? = null, limit: Int = 20, offset: Int = 0): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/browse/categories/${categoryId.encode()}/playlists?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toPagingObject<SimplePlaylist>("playlists", api)
        })
    }

    /**
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param targets A provided HashMap of attributes you'd like to weight. <b>See https://developer.spotify.com/web-api/complete-recommendations/ and scroll down to "<b>Tuneable Track attributes</b>" for a full list of optional attributes like "speechiness"
     */
    fun getRecommendations(seedArtists: List<String>? = null, seedGenres: List<String>? = null, seedTracks: List<String>? = null, targets: HashMap<String, Number> = hashMapOf(), limit: Int = 20): SpotifyRestAction<RecommendationResponse> {
        val url = StringBuilder("https://api.spotify.com/v1/recommendations?limit=$limit")
        if (seedArtists != null) url.append("&seed_artists=${seedArtists.map { it.encode() }.stream().collect(Collectors.joining(","))}")
        if (seedGenres != null) url.append("&seed_genres=${seedGenres.map { it.encode() }.stream().collect(Collectors.joining(","))}")
        if (seedTracks != null) url.append("&seed_tracks=${seedTracks.map { it.encode() }.stream().collect(Collectors.joining(","))}")
        if (targets.size > 0) targets.forEach { url.append("&target_${it.key.encode()}=${it.value}") }
        return toAction(Supplier {
            get(url.toString()).toObject<RecommendationResponse>(api)
        })
    }
}