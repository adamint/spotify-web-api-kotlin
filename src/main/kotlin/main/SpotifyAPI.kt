package main

import com.google.gson.Gson
import endpoints.album.AlbumAPI
import endpoints.artists.ArtistsAPI
import endpoints.browse.BrowseAPI
import endpoints.playlists.PlaylistsAPI
import endpoints.profiles.ProfilesAPI
import endpoints.search.SearchAPI
import endpoints.tracks.TracksAPI
import obj.Token
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

val gson = Gson()

class SpotifyAPI private constructor(val clientId: String?, val clientSecret: String?, val token: Token?) {
    val search = SearchAPI(this)
    val albums = AlbumAPI(this)
    val browse = BrowseAPI(this)
    val artists = ArtistsAPI(this)
    val playlists = PlaylistsAPI(this)
    val profiles = ProfilesAPI(this)
    val tracks = TracksAPI(this)

    init {
        if (token == null) println("No token provided, the vast majority of available methods will not work without OAuth!")
    }

    class Builder {
        private var clientId: String? = null
        private var clientSecret: String? = null
        private var automaticRefresh = false

        constructor(clientId: String, clientSecret: String) {
            this.clientId = clientId
            this.clientSecret = clientSecret
            this.automaticRefresh = automaticRefresh
        }

        constructor() {}

        @Throws(IOException::class)
        fun build(): SpotifyAPI {
            return if (clientId != null && clientSecret != null) {
                SpotifyAPI(clientId, clientSecret, gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + encode(clientId + ":" + clientSecret))
                        .ignoreContentType(true).post().body().text(), Token::class.java))
            } else
                SpotifyAPI(null, null, null)
        }
    }
}

fun encode(str: String): String {
    return String(Base64.getEncoder().encode(str.toByteArray()))
}

inline fun <reified T> retrieveSomething(): Class<T> {
    return T::class.java
}

inline fun <reified T> Any.toObject(): T {
    return gson.fromJson(this as String, retrieveSomething<T>()) as T
}