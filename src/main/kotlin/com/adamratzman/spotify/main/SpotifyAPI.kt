/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.main

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
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toObjectNullable
import com.beust.klaxon.Klaxon
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal val base = "https://api.spotify.com/v1"

// Kotlin DSL builder
fun spotifyApi(block: SpotifyApiBuilder.() -> Unit) = SpotifyApiBuilder().apply(block)

// Java-friendly builder
class SpotifyApiBuilderJava(val clientId: String, val clientSecret: String) {
    var redirectUri: String? = null
    var authorizationCode: String? = null
    var tokenString: String? = null
    var token: Token? = null
    var useCache: Boolean = true

    fun useCache(useCache: Boolean) = apply { this.useCache = useCache }

    fun redirectUri(redirectUri: String?) = apply { this.redirectUri = redirectUri }

    fun authorizationCode(authorizationCode: String?) = apply { this.authorizationCode = authorizationCode }

    fun tokenString(tokenString: String?) = apply { this.tokenString = tokenString }

    fun token(token: Token?) = apply { this.token = token }

    fun buildCredentialed() = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilderJava.clientId
            clientSecret = this@SpotifyApiBuilderJava.clientSecret
        }
        authentication {
            token = this@SpotifyApiBuilderJava.token
            tokenString = this@SpotifyApiBuilderJava.tokenString
        }
    }.buildCredentialed()

    fun buildClient(automaticRefresh: Boolean = false) = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilderJava.clientId
            clientSecret = this@SpotifyApiBuilderJava.clientSecret
            redirectUri = this@SpotifyApiBuilderJava.redirectUri
        }
        authentication {
            authorizationCode = this@SpotifyApiBuilderJava.authorizationCode
            tokenString = this@SpotifyApiBuilderJava.tokenString
            token = this@SpotifyApiBuilderJava.token
        }
    }.buildClient(automaticRefresh)
}

/**
 * @property clientId the client id of your Spotify application
 * @property clientSecret the client secret of your Spotify application
 * @property redirectUri nullable redirect uri (use if you're doing client authentication
 */
class SpotifyCredentialsBuilder {
    var clientId: String? = null
    var clientSecret: String? = null
    var redirectUri: String? = null

    fun build() =
            if (clientId?.isNotEmpty() == false || clientSecret?.isNotEmpty() == false) throw IllegalArgumentException("clientId or clientSecret is empty")
            else SpotifyCredentials(clientId, clientSecret, redirectUri)
}

data class SpotifyCredentials(val clientId: String?, val clientSecret: String?, val redirectUri: String?)

/**
 * Authentication methods.
 *
 * @property authorizationCode Only available when building [SpotifyClientAPI]. Spotify auth code
 * @property token Build the API using an existing token. If you're building [SpotifyClientAPI], this
 * will be your **access** token. If you're building [SpotifyAPI], it will be your **refresh** token
 * @property tokenString Build the API using an existing token (string). If you're building [SpotifyClientAPI], this
 * will be your **access** token. If you're building [SpotifyAPI], it will be your **refresh** token. There is a *very*
 * limited time constraint on these before the API automatically refreshes them
 */
class SpotifyUserAuthorizationBuilder(
        var authorizationCode: String? = null,
        var tokenString: String? = null,
        var token: Token? = null
)

class SpotifyApiBuilder {
    private var credentials: SpotifyCredentials = SpotifyCredentials(null, null, null)
    private var authentication = SpotifyUserAuthorizationBuilder()
    var useCache: Boolean = true

    fun credentials(block: SpotifyCredentialsBuilder.() -> Unit) {
        credentials = SpotifyCredentialsBuilder().apply(block).build()
    }

    /**
     * Allows you to authenticate a [SpotifyClientAPI] with an authorization code
     * or build [SpotifyAPI] using a refresh token
     */
    fun authentication(block: SpotifyUserAuthorizationBuilder.() -> Unit) {
        authentication = SpotifyUserAuthorizationBuilder().apply(block)
    }

    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        if (credentials.redirectUri == null || credentials.clientId == null) {
            throw IllegalArgumentException("You didn't specify a redirect uri or client id in the credentials block!")
        }
        return getAuthUrlFull(*scopes, clientId = credentials.clientId!!, redirectUri = credentials.redirectUri!!)
    }

    fun buildCredentialedAsync(consumer: (SpotifyAPI) -> Unit) = Runnable { consumer(buildCredentialed()) }.run()

    fun buildCredentialed(): SpotifyAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        if ((clientId == null || clientSecret == null) && (authentication.token == null && authentication.tokenString == null)) {
            throw IllegalArgumentException("You didn't specify a client id or client secret in the credentials block!")
        }
        return when {
            authentication.token != null -> {
                SpotifyAppAPI(clientId ?: "not-set", clientSecret ?: "not-set", authentication.token!!, useCache)
            }
            authentication.tokenString != null -> {
                SpotifyAppAPI(
                        clientId ?: "not-set",
                        clientSecret ?: "not-set",
                        Token(
                                authentication.tokenString!!, "client_credentials",
                                60000, null, null
                        ),
                        useCache
                )
            }
            else -> try {
                if (clientId == null || clientSecret == null) throw IllegalArgumentException("Illegal credentials provided")
                val token = getCredentialedToken(clientId, clientSecret)
                        ?: throw IllegalArgumentException("Invalid credentials provided")
                SpotifyAppAPI(clientId, clientSecret, token, useCache)
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
        }
    }

    fun buildClientAsync(consumer: (SpotifyClientAPI) -> Unit, automaticRefresh: Boolean = false) =
            Runnable { consumer(buildClient(automaticRefresh)) }.run()

    fun buildClient(automaticRefresh: Boolean = false): SpotifyClientAPI =
            buildClient(
                    authentication.authorizationCode, authentication.tokenString,
                    authentication.token, automaticRefresh
            )

    /**
     * Build the client api by providing an authorization code, token string, or token object. Only one of the following
     * needs to be provided
     *
     * @param authorizationCode Spotify authorization code retrieved after authentication
     * @param tokenString Spotify authorization token
     * @param token [Token] object (useful if you already have exchanged an authorization code yourself
     * @param automaticRefresh automatically refresh the token. otherwise, the authorization will eventually expire. **only** valid when
     * [authorizationCode] or [token] is provided
     */
    private fun buildClient(
            authorizationCode: String? = null,
            tokenString: String? = null,
            token: Token? = null,
            automaticRefresh: Boolean = false
    ): SpotifyClientAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        if ((clientId == null || clientSecret == null || redirectUri == null) && (token == null && tokenString == null)) {
            throw IllegalArgumentException("You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!")
        }
        return when {
            authorizationCode != null -> try {
                SpotifyClientAPI(
                        clientId ?: throw IllegalArgumentException(),
                        clientSecret ?: throw IllegalArgumentException(),
                        HttpConnection(
                                url = "https://accounts.spotify.com/api/token",
                                method = HttpRequestMethod.POST,
                                body = "grant_type=authorization_code&code=$authorizationCode&redirect_uri=$redirectUri",
                                contentType = "application/x-www-form-urlencoded",
                                api = null
                        ).execute(
                                HttpHeader(
                                        "Authorization",
                                        "Basic ${"$clientId:$clientSecret".byteEncode()}"
                                )
                        ).body.toObject(null),
                        automaticRefresh,
                        redirectUri ?: throw IllegalArgumentException(),
                        useCache
                )
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
            token != null -> SpotifyClientAPI(
                    clientId ?: "not-set",
                    clientSecret ?: "not-set",
                    token,
                    automaticRefresh,
                    redirectUri ?: "not-set",
                    useCache
            )
            tokenString != null -> SpotifyClientAPI(
                    clientId ?: "not-set", clientSecret ?: "not-set", Token(
                    tokenString, "client_credentials", 1000,
                    null, null
            ), false, redirectUri ?: "not-set",
                    useCache
            )
            else -> throw IllegalArgumentException(
                    "At least one of: authorizationCode, tokenString, or token must be provided " +
                            "to build a SpotifyClientAPI object"
            )
        }
    }
}

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
}

private fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String {
    return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
            "&response_type=code" +
            "&redirect_uri=$redirectUri" +
            if (scopes.isEmpty()) "" else "&scope=${scopes.joinToString("%20") { it.uri }}"
}

private fun getCredentialedToken(clientId: String, clientSecret: String) =
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
