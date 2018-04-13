package com.adamratzman.spotify.endpoints.pub.album

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier
import java.util.stream.Collectors

class AlbumAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getAlbum(albumId: String, market: Market? = null): SpotifyRestAction<Album> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums/$albumId${if (market != null) "?market=${market.code}" else ""}").toObject<Album>(api)
        })
    }

    fun getAlbums(market: Market? = null, vararg albumIds: String): SpotifyRestAction<List<Album>> {
        if (albumIds.isEmpty()) throw BadRequestException(ErrorObject(404, "You cannot send a request with no album ids!"))
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums?ids=${albumIds.map { it.encode() }.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=${market.code}" else ""}")
                    .toObject<AlbumsResponse>(api).albums
        })
    }

    fun getAlbumTracks(albumId: String, limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<LinkedResult<SimpleTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/albums/${albumId.encode()}/tracks?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}").toLinkedResult<SimpleTrack>(api)
        })
    }
}