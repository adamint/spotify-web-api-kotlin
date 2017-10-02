package com.adamratzman.main

import com.adamratzman.endpoints.client.library.ClientLibraryAPI
import com.adamratzman.endpoints.public.album.AlbumAPI
import com.adamratzman.endpoints.public.artists.ArtistsAPI
import com.adamratzman.endpoints.public.browse.BrowseAPI
import com.adamratzman.endpoints.public.playlists.PlaylistsAPI
import com.adamratzman.endpoints.public.profiles.ProfilesAPI
import com.adamratzman.endpoints.public.search.SearchAPI
import com.adamratzman.endpoints.public.tracks.TracksAPI
import com.google.gson.Gson
import com.adamratzman.obj.Token
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

val gson = Gson()

class SpotifyClientAPI private constructor(clientId: String, clientSecret: String, token: Token, automaticRefresh: Boolean = true) : SpotifyAPI(clientId, clientSecret, token, false) {
    val clientLibrary = ClientLibraryAPI(this)
    init {
        if (automaticRefresh) {
            // TODO("add automatic refreshing of client tokens")
        }
    }

    class Builder(val clientId: String, val clientSecret: String, val redirectUri: String) {
        fun build(authorizationCode: String, automaticRefresh: Boolean): SpotifyClientAPI {
            return SpotifyClientAPI(clientId, clientSecret, Jsoup.connect("https://accounts.spotify.com/api/token")
                    .data("grant_type", "authorization_code")
                    .data("code", authorizationCode)
                    .data("redirect_uri", redirectUri)
                    .header("Authorization", "Basic " + (clientId + ":" + clientSecret).encode())
                    .ignoreContentType(true).post().body().text().toObject(), automaticRefresh)
        }
        fun getAuthUrl(vararg scopes: Scope): String {
            return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
                    "&response_type=code" +
                    "&redirect_uri=$redirectUri" +
                    if (scopes.isEmpty()) "" else "&scope=${scopes.map { it.uri }.stream().collect(Collectors.joining("%20"))}"
        }
    }
    enum class Scope(val uri: String) {
        PLAYLIST_READ_PRIVATE("playlist-read-private"),
        PLAYLIST_READ_COLLABORATIVE("playlist-read-collaborative"),
        PLAYLIST_MODIFY_PUBLIC("playlist-modify-public"),
        PLAYLIST_MODIFY_PRIVATE("playlist-modify-private"),
        UGC_IMAGE_UPLOAD("ugc-image-upload"),
        USER_FOLLOW_MODIFY("user-follow-modify"),
        USER_FOLLOW_READ("user-follow-read"),
        USER_LIBRARY_READ("user-library-read"),
        USER_LIBRARY_MODIFY("user-library-modify"),
        USER_READ_PRIVATE("user-read-private"),
        USER_READ_BIRTHDATE("user-read-birthdate"),
        USER_READ_EMAIL("user-read-email"),
        USER_TOP_READ("user-top-read"),
        USER_READ_PLAYBACK_STATE("user-read-playback-state"),
        USER_READ_CURRENTLY_PLAYING("user-read-currently-playing"),
        USER_READ_RECENTLY_PLAYED("user-read-recently-played");
    }
}

open class SpotifyAPI internal constructor(val clientId: String?, val clientSecret: String?, var token: Token?, automaticRefresh: Boolean = true) {
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
            }, (token!!.expires_in - 30).toLong(), (token!!.expires_in - 30).toLong(), TimeUnit.SECONDS)
        }
    }

    class Builder(private var clientId: String?, private var clientSecret: String?, var automaticRefresh: Boolean = true) {
        constructor() : this(null, null)

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