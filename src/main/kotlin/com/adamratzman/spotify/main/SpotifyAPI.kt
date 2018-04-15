package com.adamratzman.spotify.main

import com.adamratzman.spotify.endpoints.priv.follow.UserFollowAPI
import com.adamratzman.spotify.endpoints.priv.library.UserLibraryAPI
import com.adamratzman.spotify.endpoints.priv.personalization.PersonalizationAPI
import com.adamratzman.spotify.endpoints.priv.player.PlayerAPI
import com.adamratzman.spotify.endpoints.priv.playlists.UserPlaylistAPI
import com.adamratzman.spotify.endpoints.priv.users.UserAPI
import com.adamratzman.spotify.endpoints.pub.album.AlbumAPI
import com.adamratzman.spotify.endpoints.pub.artists.ArtistsAPI
import com.adamratzman.spotify.endpoints.pub.browse.BrowseAPI
import com.adamratzman.spotify.endpoints.pub.follow.PublicFollowingAPI
import com.adamratzman.spotify.endpoints.pub.playlists.PlaylistsAPI
import com.adamratzman.spotify.endpoints.pub.search.SearchAPI
import com.adamratzman.spotify.endpoints.pub.tracks.TracksAPI
import com.adamratzman.spotify.endpoints.pub.users.PublicUserAPI
import com.adamratzman.spotify.utils.Token
import com.adamratzman.spotify.utils.byteEncode
import com.adamratzman.spotify.utils.toObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jsoup.Jsoup
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

open class SpotifyAPI internal constructor(val clientId: String?, val clientSecret: String?, var token: Token) {
    internal var expireTime = System.currentTimeMillis() + token.expires_in * 1000
    internal val executor = Executors.newScheduledThreadPool(1)
    val gson = GsonBuilder().setLenient().create()!!
    val search = SearchAPI(this)
    val albums = AlbumAPI(this)
    val browse = BrowseAPI(this)
    val artists = ArtistsAPI(this)
    val playlists = PlaylistsAPI(this)
    val users = PublicUserAPI(this)
    val tracks = TracksAPI(this)
    val publicFollowing = PublicFollowingAPI(this)
    val logger = SpotifyLogger(true)

    class Builder(private var clientId: String?, private var clientSecret: String?) {
        fun build(): SpotifyAPI {
            return try {
                val token = Gson().fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + ("$clientId:$clientSecret".byteEncode()))
                        .ignoreContentType(true).post().body().text(), Token::class.java)
                        ?: throw IllegalArgumentException("Invalid credentials provided")
                if (clientId != null && clientSecret != null) {
                    SpotifyAPI(clientId, clientSecret, token)
                } else throw IllegalArgumentException("ID and Secret must not be null!")
            } catch (e: Exception) {
                println("Invalid credentials provided")
                throw e
            }
        }
    }

    internal fun refreshClient() {
        token = gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                .data("grant_type", "client_credentials")
                .header("Authorization", "Basic " + ("$clientId:$clientSecret".byteEncode()))
                .ignoreContentType(true).post().body().text(), Token::class.java)
    }

    fun useLogger(enable: Boolean) {
        logger.enabled = enable
    }
}

class SpotifyClientAPI private constructor(clientId: String, clientSecret: String, token: Token, automaticRefresh: Boolean = false) : SpotifyAPI(clientId, clientSecret, token) {
    val personalization = PersonalizationAPI(this)
    val userProfile = UserAPI(this)
    val userLibrary = UserLibraryAPI(this)
    val userFollowing = UserFollowAPI(this)
    val player = PlayerAPI(this)
    val userPlaylists = UserPlaylistAPI(this)

    init {
        if (automaticRefresh) {
            executor.scheduleAtFixedRate({ refreshToken() }, ((token.expires_in - 30).toLong()), (token.expires_in - 30).toLong(), TimeUnit.SECONDS)
        }
    }

    fun cancelRefresh() = executor.shutdown()

    fun refreshToken() {
        val tempToken = gson.fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                .data("grant_type", "client_credentials")
                .data("refresh_token", token.refresh_token ?: "")
                .header("Authorization", "Basic " + ("$clientId:$clientSecret").byteEncode())
                .ignoreContentType(true).post().body().text(), Token::class.java)
        if (tempToken == null) {
            logger.logWarning("Spotify token refresh failed")
        } else {
            this.token = tempToken
            logger.logInfo("Successfully refreshed the Spotify token")
        }
    }

    class Builder(val clientId: String, val clientSecret: String, val redirectUri: String) {
        fun buildAuthCode(authorizationCode: String, automaticRefresh: Boolean = true): SpotifyClientAPI {
            return try {
                SpotifyClientAPI(clientId, clientSecret, Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "authorization_code")
                        .data("code", authorizationCode)
                        .data("redirect_uri", redirectUri)
                        .header("Authorization", "Basic " + ("$clientId:$clientSecret").byteEncode())
                        .ignoreContentType(true).post().body().text().toObject(Gson()), automaticRefresh)
            } catch (e: Exception) {
                println("Invalid credentials provided")
                throw e
            }
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
