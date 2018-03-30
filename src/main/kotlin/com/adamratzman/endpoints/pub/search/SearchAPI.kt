package com.adamratzman.endpoints.pub.search

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toPagingObject
import com.adamratzman.obj.*
import java.net.URLEncoder

class SearchAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    enum class SearchType(val id: String) {
        ALBUM("album"), TRACK("track"), ARTIST("artist"), PLAYLIST("playlist")
    }

    fun searchPlaylist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): PagingObject<Playlist> {
        return get("https://api.spotify.com/v1/search?q=${URLEncoder.encode(query, "UTF-8")}&type=${SearchType.PLAYLIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                .toPagingObject("playlists")

    }

    fun searchArtist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): PagingObject<Artist> {
        return get("https://api.spotify.com/v1/search?q=${URLEncoder.encode(query, "UTF-8")}&type=${SearchType.ARTIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                .toPagingObject("artists")
    }

    fun searchAlbum(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): PagingObject<SimpleAlbum> {
        return get("https://api.spotify.com/v1/search?q=${URLEncoder.encode(query, "UTF-8")}&type=${SearchType.ALBUM.id}&market=${market.code}&limit=$limit&offset=$offset")
                .toPagingObject("albums")
    }

    fun searchTrack(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): PagingObject<SimpleTrack> {
        return get("https://api.spotify.com/v1/search?q=${URLEncoder.encode(query, "UTF-8")}&type=${SearchType.TRACK.id}&market=${market.code}&limit=$limit&offset=$offset")
                .toPagingObject("tracks")
    }
}