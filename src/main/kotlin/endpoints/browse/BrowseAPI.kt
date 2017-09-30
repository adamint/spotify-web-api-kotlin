package endpoints.browse

import main.SpotifyAPI
import main.toObject
import obj.*
import java.net.URLEncoder
import java.util.stream.Collectors

class BrowseAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getNewReleases(limit: Int = 20, offset: Int = 0, country: String? = null): AlbumPagingObject {
        return get("https://api.spotify.com/v1/browse/new-releases?limit=$limit&offset=$offset${if (country != null) "&country=$country" else ""}")
                .toObject<AlbumPagingResponse>().albums
    }

    fun getFeaturedPlaylists(limit: Int = 20, offset: Int = 0, locale: String? = null, country: String? = null): MessageResult<SimplePlaylist> {
        return get("https://api.spotify.com/v1/browse/featured-playlists?limit=$limit&offset=$offset${if (locale != null) "&locale=$locale" else ""}${if (country != null) "&country=$country" else ""}").toObject()
    }

    fun getCategoryList(limit: Int = 20, offset: Int = 0, locale: String? = null, country: String? = null): SpotifyCategoryPagingObject {
        return get("https://api.spotify.com/v1/browse/categories?limit=$limit&offset=$offset${if (locale != null) "&locale=$locale" else ""}${if (country != null) "&country=$country" else ""}")
                .toObject<SpotifyCategoryPagingResponse>().categories
    }

    fun getCategory(categoryId: String, country: String? = null): SpotifyCategory {
        return get("https://api.spotify.com/v1/browse/categories/${URLEncoder.encode(categoryId, "UTF-8")}${if (country != null) "?country=$country" else ""}").toObject()
    }

    fun getPlaylistsForCategory(categoryId: String, country: String? = null, limit: Int = 20, offset: Int = 0): SimplePlaylistPagingObject {
        return get("https://api.spotify.com/v1/browse/categories/$categoryId/playlists?limit=$limit&offset=$offset${if (country != null) "&country=$country" else ""}")
                .toObject<SimplePlaylistPagingResponse>().playlists
    }

    /**
     * @param seedArtists A possibly null provided list of <b>Artist IDs</b> to be used to generate recommendations
     * @param seedGenres A possibly null provided list of <b>Genre IDs</b> to be used to generate recommendations
     * @param seedTracks A possibly null provided list of <b>Track IDs</b> to be used to generate recommendations
     * @param targets A provided HashMap of attributes you'd like to weight. <b>See https://developer.spotify.com/web-api/get-recommendations/ and scroll down to "<b>Tuneable Track attributes</b>" for a full list of optional attributes like "speechiness"
     */
    fun getRecommendations(seedArtists: List<String>? = null, seedGenres: List<String>? = null, seedTracks: List<String>? = null, targets: HashMap<String, Number> = hashMapOf(), limit: Int = 20): RecommendationResponse {
        val url = StringBuilder("https://api.spotify.com/v1/recommendations?limit=$limit")
        if (seedArtists != null) url.append("&seed_artists=${seedArtists.stream().collect(Collectors.joining(","))}")
        if (seedGenres != null) url.append("&seed_genres=${seedGenres.stream().collect(Collectors.joining(","))}")
        if (seedTracks != null) url.append("&seed_tracks=${seedTracks.stream().collect(Collectors.joining(","))}")
        if (targets.size > 0) targets.forEach { url.append("&target_${it.key}=${it.value}") }
        return get(url.toString()).toObject()
    }
}