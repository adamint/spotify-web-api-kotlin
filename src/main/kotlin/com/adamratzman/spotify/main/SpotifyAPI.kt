package com.adamratzman.spotify.main

import com.adamratzman.spotify.endpoints.client.*
import com.adamratzman.spotify.endpoints.public.*
import com.adamratzman.spotify.utils.Token
import com.adamratzman.spotify.utils.byteEncode
import com.adamratzman.spotify.utils.toObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jsoup.Jsoup
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal val base = "https://api.spotify.com/v1"

open class SpotifyAPI internal constructor(val clientId: String, val clientSecret: String, var token: Token) {
    internal var expireTime = System.currentTimeMillis() + token.expires_in * 1000
    internal val executor = Executors.newScheduledThreadPool(1)
    val gson = GsonBuilder().setLenient().create()!!
    val search = SearchAPI(this)
    val albums = PublicAlbumAPI(this)
    val browse = BrowseAPI(this)
    val artists = PublicArtistsAPI(this)
    val playlists = PublicPlaylistsAPI(this)
    val users = PublicUserAPI(this)
    val tracks = PublicTracksAPI(this)
    val publicFollowing = PublicFollowingAPI(this)
    internal val logger = SpotifyLogger(true)

    class Builder(private var clientId: String, private var clientSecret: String) {
        fun build(): SpotifyAPI {
            return try {
                val token = Gson().fromJson(Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + ("$clientId:$clientSecret".byteEncode()))
                        .ignoreContentType(true).post().body().text(), Token::class.java)
                        ?: throw IllegalArgumentException("Invalid credentials provided")
                SpotifyAPI(clientId, clientSecret, token)
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
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

    fun authorizeUser(authorizationCode: String, redirectUri: String, automaticRefresh: Boolean = true): SpotifyClientAPI {
        return SpotifyClientAPI.Builder(clientId, clientSecret, redirectUri).buildAuthCode(authorizationCode, automaticRefresh)
    }

    fun authorizeUserToken(oauthToken: String, redirectUri: String): SpotifyClientAPI {
        return SpotifyClientAPI.Builder(clientId, clientSecret, redirectUri).buildToken(oauthToken)
    }

    fun getAuthUrl(vararg scopes: SpotifyScope, redirectUri: String): String = getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)
}

class SpotifyClientAPI private constructor(clientId: String, clientSecret: String, token: Token, automaticRefresh: Boolean = false, var redirectUri: String) : SpotifyAPI(clientId, clientSecret, token) {
    val personalization = ClientPersonalizationAPI(this)
    val clientProfile = ClientProfileAPI(this)
    val clientLibrary = ClientLibraryAPI(this)
    val clientFollowing = ClientFollowAPI(this)
    val player = PlayerAPI(this)
    val clientPlaylists = ClientPlaylistAPI(this)

    init {
        if (automaticRefresh) {
            executor.scheduleAtFixedRate({ refreshToken() }, ((token.expires_in - 30).toLong()), (token.expires_in - 30).toLong(), TimeUnit.SECONDS)
        }
    }

    fun cancelRefresh() = executor.shutdown()

    private fun refreshToken() {
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

    fun getAuthUrl(vararg spotifyScopes: SpotifyScope): String = getAuthUrlFull(*spotifyScopes, clientId = clientId, redirectUri = redirectUri)

    class Builder(val clientId: String, val clientSecret: String, val redirectUri: String) {
        fun buildAuthCode(authorizationCode: String, automaticRefresh: Boolean = true): SpotifyClientAPI {
            return try {
                SpotifyClientAPI(clientId, clientSecret, Jsoup.connect("https://accounts.spotify.com/api/token")
                        .data("grant_type", "authorization_code")
                        .data("code", authorizationCode)
                        .data("redirect_uri", redirectUri)
                        .header("Authorization", "Basic " + ("$clientId:$clientSecret").byteEncode())
                        .ignoreContentType(true).post().body().text().toObject(Gson()), automaticRefresh, redirectUri)
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
        }

        fun buildToken(oauthToken: String): SpotifyClientAPI {
            return SpotifyClientAPI(clientId, clientSecret, Token(oauthToken, "client_credentials", 1000,
                    null, null), false, redirectUri)
        }

        fun getAuthUrl(vararg spotifyScopes: SpotifyScope): String = getAuthUrlFull(*spotifyScopes, clientId = clientId, redirectUri = redirectUri)
    }
}

private fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String {
    return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
            "&response_type=code" +
            "&redirect_uri=$redirectUri" +
            if (scopes.isEmpty()) "" else "&scope=${scopes.map { it.uri }.joinToString("%20")}"
}