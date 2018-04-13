package com.adamratzman.spotify.endpoints.pub.search

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.net.URLEncoder
import java.util.function.Supplier

class SearchAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    enum class SearchType(val id: String) {
        ALBUM("album"), TRACK("track"), ARTIST("artist"), PLAYLIST("playlist")
    }

    fun searchPlaylist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<Playlist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.PLAYLIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<Playlist>("playlists", api)
        })

    }

    fun searchArtist(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.ARTIST.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<Artist>("artists", api)
        })
    }

    fun searchAlbum(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<SimpleAlbum>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.ALBUM.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<SimpleAlbum>("albums", api)
        })
    }

    fun searchTrack(query: String, limit: Int = 20, offset: Int = 0, market: Market = Market.US): SpotifyRestAction<PagingObject<SimpleTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/search?q=${query.encode()}&type=${SearchType.TRACK.id}&market=${market.code}&limit=$limit&offset=$offset")
                    .toPagingObject<SimpleTrack>("tracks", api)
        })
    }
}