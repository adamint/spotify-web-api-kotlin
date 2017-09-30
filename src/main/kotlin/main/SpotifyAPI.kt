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
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val gson = Gson()

class SpotifyAPI private constructor(val clientId: String?, val clientSecret: String?, var token: Token?, automaticRefresh: Boolean = true) {
    val search = SearchAPI(this)
    val albums = AlbumAPI(this)
    val browse = BrowseAPI(this)
    val artists = ArtistsAPI(this)
    val playlists = PlaylistsAPI(this)
    val profiles = ProfilesAPI(this)
    val tracks = TracksAPI(this)

    init {
        if (token == null) println("No token provided, the vast majority of available methods will not work without OAuth!")
        else if (automaticRefresh) {
            println("Automatic token refresh is enabled")
            val executor = Executors.newSingleThreadScheduledExecutor()
            executor.scheduleAtFixedRate({
                val tempToken = gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + (clientId + ":" + clientSecret).encode())
                        .ignoreContentType(true).post().body().text(), Token::class.java)
                if (tempToken == null) {
                    println("WARNING: Spotify Token refresh failed")
                    executor.shutdown()
                } else {
                    token = tempToken
                    println("INFO: Successfully refreshed token")
                }
            }, 5, 5, TimeUnit.SECONDS)
        }
    }

    class Builder(private var clientId: String?, private var clientSecret: String?, var automaticRefresh: Boolean = true) {
        constructor() : this(null, null) {}

        @Throws(IOException::class)
        fun build(): SpotifyAPI {
            return if (clientId != null && clientSecret != null) {
                SpotifyAPI(clientId, clientSecret, gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + (clientId + ":" + clientSecret).encode())
                        .ignoreContentType(true).post().body().text(), Token::class.java), automaticRefresh)
            } else
                SpotifyAPI(null, null, null, false)
        }
    }
}

fun String.encode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

inline fun <reified T> Any.toObject(): T {
    return gson.fromJson(this as String, T::class.java)
}