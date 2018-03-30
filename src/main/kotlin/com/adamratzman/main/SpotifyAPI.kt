package com.adamratzman.main

import com.adamratzman.endpoints.priv.follow.FollowingAPI
import com.adamratzman.endpoints.priv.library.UserLibraryAPI
import com.adamratzman.endpoints.priv.personalization.PersonalizationAPI
import com.adamratzman.endpoints.priv.player.PlayerAPI
import com.adamratzman.endpoints.priv.users.PrivateUserAPI
import com.adamratzman.endpoints.pub.album.AlbumAPI
import com.adamratzman.endpoints.pub.artists.ArtistsAPI
import com.adamratzman.endpoints.pub.browse.BrowseAPI
import com.adamratzman.endpoints.pub.follow.PublicFollowingAPI
import com.adamratzman.endpoints.pub.playlists.PlaylistsAPI
import com.adamratzman.endpoints.pub.search.SearchAPI
import com.adamratzman.endpoints.pub.tracks.TracksAPI
import com.adamratzman.endpoints.pub.users.PublicUserAPI
import com.adamratzman.obj.*
import com.google.gson.Gson
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

val gson = Gson()

class SpotifyClientAPI private constructor(clientId: String, clientSecret: String, token: Token?, automaticRefresh: Boolean = false) : SpotifyAPI(clientId, clientSecret, token) {
    val personalization = PersonalizationAPI(this)
    val userProfile = PrivateUserAPI(this)
    val userLibrary = UserLibraryAPI(this)
    val userFollowing = FollowingAPI(this)
    val player = PlayerAPI(this)
    init {
        if (automaticRefresh) {
            println("Automatic token refresh is enabled")
            val executor = Executors.newSingleThreadScheduledExecutor()
            executor.scheduleAtFixedRate({ refreshToken() }, (token!!.expires_in - 30).toLong(), (token.expires_in - 30).toLong(), TimeUnit.SECONDS)
        }
    }

    private fun refreshToken() {
        val tempToken = gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                .data("grant_type", "client_credentials")
                .data("refresh_token", token?.refresh_token ?: "")
                .header("Authorization", "Basic " + ("$clientId:$clientSecret").encode())
                .ignoreContentType(true).post().body().text(), Token::class.java)
        if (tempToken == null) {
            println("WARNING: Spotify Token refresh failed")
        } else {
            this.token = tempToken
            println("INFO: Successfully refreshed Spotify token")
        }
    }

    class Builder(val clientId: String, val clientSecret: String, val redirectUri: String) {
        fun build(authorizationCode: String): SpotifyClientAPI {
            return SpotifyClientAPI(clientId, clientSecret, Jsoup.connect("https://accounts.spotify.com/api/token")
                    .data("grant_type", "authorization_code")
                    .data("code", authorizationCode)
                    .data("redirect_uri", redirectUri)
                    .header("Authorization", "Basic " + ("$clientId:$clientSecret").encode())
                    .ignoreContentType(true).post().body().text().toObject(), false)
        }

        fun buildToken(oauthToken: String): SpotifyClientAPI {
            return SpotifyClientAPI(clientId, clientSecret, Token(oauthToken, "client_credentials", 1000,
                    null, null), false)
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
        PLAYLIST_MODIFY_PUBLIC("playlist-modify-pub"),
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

open class SpotifyAPI internal constructor(val clientId: String?, val clientSecret: String?, var token: Token?) {
    val search = SearchAPI(this)
    val albums = AlbumAPI(this)
    val browse = BrowseAPI(this)
    val artists = ArtistsAPI(this)
    val playlists = PlaylistsAPI(this)
    val users = PublicUserAPI(this)
    val tracks = TracksAPI(this)
    val publicFollowing = PublicFollowingAPI(this)

    init {
        if (token == null) println("No token provided, this library will not work!")
    }

    class Builder(private var clientId: String?, private var clientSecret: String?) {
        constructor() : this(null, null)

        fun build(): SpotifyAPI {
            return if (clientId != null && clientSecret != null) {
                SpotifyAPI(clientId, clientSecret, gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + (clientId + ":" + clientSecret).encode())
                        .ignoreContentType(true).post().body().text(), Token::class.java))
            } else SpotifyAPI(null, null, null)
        }
    }
}

fun String.encode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

inline fun <reified T> Any.toObject(): T {
    return gson.fromJson(this as String, T::class.java)
}

inline fun <reified T> String.toPagingObject(innerObjectName: String? = null): PagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return PagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>() },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            jsonObject.get("offset") as Int,
            jsonObject.get("previous") as? String,
            jsonObject.getInt("total"))
}

inline fun <reified T> String.toCursorBasedPagingObject(innerObjectName: String? = null): CursorBasedPagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return CursorBasedPagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>() },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            gson.fromJson(jsonObject.getJSONObject("cursors").toString(), Cursor::class.java),
            if (jsonObject.keySet().contains("total")) jsonObject.getInt("total") else -1)
}

inline fun <reified T> String.toLinkedResult(): LinkedResult<T> {
    val jsonObject = JSONObject(this)
    return LinkedResult(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>() })
}

inline fun <reified T> String.toInnerObject(innerName: String): List<T> {
    return JSONObject(this).getJSONArray(innerName).map { it.toString().toObject<T>() }
}