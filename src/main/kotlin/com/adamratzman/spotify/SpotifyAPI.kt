/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.endpoints.client.ClientFollowingAPI
import com.adamratzman.spotify.endpoints.client.ClientLibraryAPI
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationAPI
import com.adamratzman.spotify.endpoints.client.ClientPlayerAPI
import com.adamratzman.spotify.endpoints.client.ClientPlaylistAPI
import com.adamratzman.spotify.endpoints.client.ClientUserAPI
import com.adamratzman.spotify.endpoints.public.AlbumAPI
import com.adamratzman.spotify.endpoints.public.ArtistsAPI
import com.adamratzman.spotify.endpoints.public.BrowseAPI
import com.adamratzman.spotify.endpoints.public.FollowingAPI
import com.adamratzman.spotify.endpoints.public.PlaylistsAPI
import com.adamratzman.spotify.endpoints.public.SearchAPI
import com.adamratzman.spotify.endpoints.public.TracksAPI
import com.adamratzman.spotify.endpoints.public.UserAPI
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.byteEncode
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.serialization.getAlbumConverter
import com.adamratzman.spotify.models.serialization.getFeaturedPlaylistsConverter
import com.adamratzman.spotify.models.serialization.getPlaylistConverter
import com.adamratzman.spotify.models.serialization.getPublicUserConverter
import com.adamratzman.spotify.models.serialization.getSavedTrackConverter
import com.adamratzman.spotify.models.serialization.toObjectNullable
import com.beust.klaxon.Klaxon
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal val base = "https://api.spotify.com/v1"

abstract class SpotifyAPI internal constructor(
    val clientId: String,
    val clientSecret: String,
    var token: Token,
    var useCache: Boolean
) {
    internal var expireTime = System.currentTimeMillis() + token.expiresIn * 1000
    internal val executor = Executors.newScheduledThreadPool(2)

    abstract val search: SearchAPI
    abstract val albums: AlbumAPI
    abstract val browse: BrowseAPI
    abstract val artists: ArtistsAPI
    abstract val playlists: PlaylistsAPI
    abstract val users: UserAPI
    abstract val tracks: TracksAPI
    abstract val following: FollowingAPI

    internal val logger = SpotifyLogger(true)

    abstract val klaxon: Klaxon

    abstract fun refreshToken()
    abstract fun clearCache()

    init {
        executor.scheduleAtFixedRate(::clearCache, 10, 10, TimeUnit.MINUTES)
    }

    internal fun clearAllCaches(vararg endpoints: SpotifyEndpoint) {
        endpoints.forEach { it.cache.clear() }
    }

    fun useLogger(enable: Boolean) {
        logger.enabled = enable
    }

    fun getAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String): String {
        return getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)
    }
}

class SpotifyAppAPI internal constructor(clientId: String, clientSecret: String, token: Token, useCache: Boolean) :
        SpotifyAPI(clientId, clientSecret, token, useCache) {
    override val search: SearchAPI = SearchAPI(this)
    override val albums: AlbumAPI = AlbumAPI(this)
    override val browse: BrowseAPI = BrowseAPI(this)
    override val artists: ArtistsAPI = ArtistsAPI(this)
    override val playlists: PlaylistsAPI = PlaylistsAPI(this)
    override val users: UserAPI = UserAPI(this)
    override val tracks: TracksAPI = TracksAPI(this)
    override val following: FollowingAPI = FollowingAPI(this)

    override val klaxon: Klaxon = getKlaxon(this)

    init {
        if (clientId == "not-set" || clientSecret == "not-set") {
            logger.logWarning("Token refresh is disabled - application parameters not set")
        }
    }

    override fun refreshToken() {
        if (clientId != "not-set" && clientSecret != "not-set")
            getCredentialedToken(clientId, clientSecret)?.let { token = it }
        expireTime = System.currentTimeMillis() + token.expiresIn * 1000
    }

    override fun clearCache() = clearAllCaches(
            search,
            albums,
            browse,
            artists,
            playlists,
            users,
            tracks,
            following
    )

    override fun equals(other: Any?) = other is SpotifyAppAPI && other.token == this.token
}

class SpotifyClientAPI internal constructor(
    clientId: String,
    clientSecret: String,
    token: Token,
    automaticRefresh: Boolean = false,
    var redirectUri: String,
    useCache: Boolean
) : SpotifyAPI(clientId, clientSecret, token, useCache) {
    override val search: SearchAPI = SearchAPI(this)
    override val albums: AlbumAPI = AlbumAPI(this)
    override val browse: BrowseAPI = BrowseAPI(this)
    override val artists: ArtistsAPI = ArtistsAPI(this)
    override val playlists: ClientPlaylistAPI = ClientPlaylistAPI(this)
    override val users: ClientUserAPI = ClientUserAPI(this)
    override val tracks: TracksAPI = TracksAPI(this)
    override val following: ClientFollowingAPI = ClientFollowingAPI(this)
    val personalization: ClientPersonalizationAPI = ClientPersonalizationAPI(this)
    val library: ClientLibraryAPI = ClientLibraryAPI(this)
    val player: ClientPlayerAPI = ClientPlayerAPI(this)

    override val klaxon: Klaxon = getKlaxon(this)

    val userId: String

    init {
        init(automaticRefresh)
        userId = users.getUserProfile().complete().id
    }

    private fun init(automaticRefresh: Boolean) {
        if (automaticRefresh) {
            if (clientId != "not-set" && clientSecret != "not-set" && redirectUri != "not-set") {
                if (token.expiresIn > 60) {
                    executor.scheduleAtFixedRate(
                            { refreshToken() },
                            (token.expiresIn - 30).toLong(),
                            (token.expiresIn - 30).toLong(),
                            TimeUnit.SECONDS
                    )
                } else {
                    refreshToken()
                    init(automaticRefresh)
                }
            } else logger.logWarning("Automatic refresh unavailable - client parameters not set")
        }
    }

    /**
     * This function will stop all automatic functions like refreshToken or clearCache
     * */
    fun cancelAutomatics() = executor.shutdown()

    override fun refreshToken() {
        val tempToken =
                HttpConnection(
                        url = "https://accounts.spotify.com/api/token",
                        method = HttpRequestMethod.POST,
                        body = "grant_type=refresh_token&refresh_token=${token.refreshToken ?: ""}",
                        contentType = "application/x-www-form-urlencoded",
                        api = this
                ).execute(HttpHeader("Authorization", "Basic ${"$clientId:$clientSecret".byteEncode()}")).body
                        .toObjectNullable<Token>(null)
        if (tempToken?.accessToken == null) {
            logger.logWarning("Spotify token refresh failed")
        } else {
            this.token = tempToken.copy(
                    refreshToken = tempToken.refreshToken ?: this.token.refreshToken,
                    scopes = tempToken.scopes ?: this.token.scopes
            )
            logger.logInfo("Successfully refreshed the Spotify token")
        }
    }

    override fun clearCache() = clearAllCaches(
            search,
            albums,
            browse,
            artists,
            playlists,
            users,
            tracks,
            following,
            personalization,
            library,
            player
    )

    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        return getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)
    }

    override fun equals(other: Any?) = other is SpotifyClientAPI && other.token == this.token
}

internal fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String {
    return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
            "&response_type=code" +
            "&redirect_uri=$redirectUri" +
            if (scopes.isEmpty()) "" else "&scope=${scopes.joinToString("%20") { it.uri }}"
}

internal fun getCredentialedToken(clientId: String, clientSecret: String) =
        HttpConnection(
                url = "https://accounts.spotify.com/api/token",
                method = HttpRequestMethod.POST,
                body = "grant_type=client_credentials",
                contentType = "application/x-www-form-urlencoded",
                api = null
        ).execute(HttpHeader("Authorization", "Basic ${"$clientId:$clientSecret".byteEncode()}")).body
                .toObjectNullable<Token>(null)

private fun getKlaxon(api: SpotifyAPI) = Klaxon()
        .converter(getFeaturedPlaylistsConverter(api))
        .converter(getPlaylistConverter(api))
        .converter(getAlbumConverter(api))
        .converter(getSavedTrackConverter(api))
        .converter(getPublicUserConverter(api))
