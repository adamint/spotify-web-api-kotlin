package com.adamratzman.spotify.endpoints.pub.album

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*
import java.util.stream.Collectors

class AlbumAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getAlbum(albumId: String, market: String? = null): Album? {
        return get("https://api.spotify.com/v1/albums/$albumId${if (market != null) "?market=$market" else ""}").toObject(api)
    }

    fun getAlbums(market: String? = null, vararg albumIds: String): List<Album?> {
        if (albumIds.isEmpty()) throw BadRequestException(ErrorObject(404, "You cannot send a request with no album ids!"))
        return get("https://api.spotify.com/v1/albums?ids=${albumIds.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=$market" else ""}")
                .toObject<AlbumsResponse>(api).albums
    }

    fun getAlbumTracks(albumId: String, limit: Int = 20, offset: Int = 0, market: String? = null): LinkedResult<SimpleTrack>? {
        return get("https://api.spotify.com/v1/albums/$albumId/tracks?limit=$limit&offset=$offset${if (market != null) "&market=$market" else ""}").toObject(api)
    }
}