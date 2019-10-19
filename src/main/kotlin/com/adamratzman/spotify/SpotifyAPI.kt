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
import com.adamratzman.spotify.endpoints.public.PlaylistAPI
import com.adamratzman.spotify.endpoints.public.SearchAPI
import com.adamratzman.spotify.endpoints.public.TracksAPI
import com.adamratzman.spotify.endpoints.public.UserAPI
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.HttpResponse
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.byteEncode
import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.TokenValidityResponse
import com.adamratzman.spotify.models.serialization.toObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

internal const val base = "https://api.spotify.com/v1"

/**
 * Represents an instance of the Spotify API client, with common
 * functionality and information between the [SpotifyClientAPI] and [SpotifyAppAPI]
 * implementations of the API
 *
 * @property clientId The application client id found on the application [dashboard](https://developer.spotify.com/dashboard/applications)
 * @property clientSecret The application client secret found on the application [dashboard](https://developer.spotify.com/dashboard/applications)
 * @property token The access token associated with this API instance
 * @property useCache Whether to use the built-in cache to avoid making unnecessary calls to
 * the Spotify API
 * @property cacheLimit The maximum amount of cached requests allowed at one time. Null means no limit
 *
 * @property search Provides access to the Spotify [search endpoint](https://developer.spotify.com/documentation/web-api/reference/search/search/)
 * @property albums Provides access to Spotify [album endpoints](https://developer.spotify.com/documentation/web-api/reference/albums/)
 * @property browse Provides access to Spotify [browse endpoints](https://developer.spotify.com/documentation/web-api/reference/browse/)
 * @property artists Provides access to Spotify [artist endpoints](https://developer.spotify.com/documentation/web-api/reference/artists/)
 * @property tracks Provides access to Spotify [track endpoints](https://developer.spotify.com/documentation/web-api/reference/tracks/)
 *
 * @property logger The Spotify event logger
 * @property moshi The serializer/deserializer associated with this API instance
 *
 */
abstract class SpotifyAPI internal constructor(
    val clientId: String,
    val clientSecret: String,
    var token: Token,
    useCache: Boolean,
    var cacheLimit: Int?,
    var automaticRefresh: Boolean,
    var retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean
) {
    var useCache = useCache
        set(value) {
            if (!value) clearCache()

            field = value
        }

    val logger = SpotifyLogger(enableLogger)

    val expireTime: Long get() = System.currentTimeMillis() + token.expiresIn * 1000

    val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(0)

    abstract val search: SearchAPI
    abstract val albums: AlbumAPI
    abstract val browse: BrowseAPI
    abstract val artists: ArtistsAPI
    abstract val playlists: PlaylistAPI
    abstract val users: UserAPI
    abstract val tracks: TracksAPI
    abstract val following: FollowingAPI

    init {
        if (testTokenValidity) {
            try {
                isTokenValid(true)
            } catch (e: Exception) {
                throw SpotifyException("Invalid token provided on initialization", e)
            }
        }
    }

    /**
     * Obtain a map of all currently-cached requests
     */
    fun getCache() = endpoints.map { it.cache.cachedRequests.toList() }.flatten().toMap()

    /**
     * If the method used to create the [token] supports token refresh and
     * the information in [token] is accurate, attempt to refresh the token
     *
     * @return The old access token if refresh was successful
     * @throws BadRequestException if refresh fails
     */
    abstract fun refreshToken(): Token

    /**
     * A list of all endpoints included in this api type
     */
    abstract val endpoints: List<SpotifyEndpoint>

    /**
     * If the cache is enabled, clear all stored queries in the cache
     */
    fun clearCache() = clearCaches(*endpoints.toTypedArray())

    /**
     * Return a new [SpotifyApiBuilder] with the parameters provided to this api instance
     */
    abstract fun getApiBuilder(): SpotifyApiBuilder

    /**
     * Return a new [SpotifyApiBuilderDsl] with the parameters provided to this api instance
     */
    abstract fun getApiBuilderDsl(): ISpotifyApiBuilder

    internal val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private fun clearCaches(vararg endpoints: SpotifyEndpoint) {
        endpoints.forEach { it.cache.clear() }
    }

    /**
     * Allows enabling and disabling the logger
     *
     * @param enable Whether to enable the logger
     */
    fun useLogger(enable: Boolean) {
        logger.enabled = enable
    }

    /**
     * Create a Spotify authorization URL from which client access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     * @param redirectUri The redirect uri specified on the Spotify developer dashboard; where to
     * redirect the browser after authentication
     *
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String): String {
        return getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)
    }

    /**
     * Tests whether the current [token] is actually valid. By default, an endpoint is called *once* to verify
     * validity.
     *
     * @param makeTestRequest Whether to also make an endpoint request to verify authentication.
     *
     * @return [TokenValidityResponse] containing whether this token is valid, and if not, an Exception explaining why
     */
    fun isTokenValid(makeTestRequest: Boolean = true): TokenValidityResponse {
        if (token.shouldRefresh()) return TokenValidityResponse(
            false,
            SpotifyException("Token needs to be refreshed (is it expired?)")
        )
        if (!makeTestRequest) return TokenValidityResponse(true, null)

        return try {
            browse.getAvailableGenreSeeds().complete()
            TokenValidityResponse(true, null)
        } catch (e: Exception) {
            TokenValidityResponse(false, e)
        }
    }
}

/**
 * An API instance created with application credentials, not through
 * client authentication
 */
class SpotifyAppAPI internal constructor(
    clientId: String,
    clientSecret: String,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean
) : SpotifyAPI(
    clientId,
    clientSecret,
    token,
    useCache,
    cacheLimit,
    automaticRefresh,
    retryWhenRateLimited,
    enableLogger,
    testTokenValidity
) {
    constructor(
        clientId: String,
        clientSecret: String,
        token: Token,
        options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
    ) : this(
        clientId,
        clientSecret,
        token,
        options.useCache,
        options.cacheLimit,
        options.automaticRefresh,
        options.retryWhenRateLimited,
        options.enableLogger,
        options.testTokenValidity
    )

    override val search: SearchAPI = SearchAPI(this)
    override val albums: AlbumAPI = AlbumAPI(this)
    override val browse: BrowseAPI = BrowseAPI(this)
    override val artists: ArtistsAPI = ArtistsAPI(this)
    override val tracks: TracksAPI = TracksAPI(this)

    /**
     * Provides access to **public** Spotify [playlist endpoints](https://developer.spotify.com/documentation/web-api/reference/playlists/)
     */
    override val playlists: PlaylistAPI = PlaylistAPI(this)

    /**
     * Provides access to **public** Spotify [user information](https://developer.spotify.com/documentation/web-api/reference/users-profile/get-users-profile/)
     */
    override val users: UserAPI = UserAPI(this)

    /**
     * Provides access to **public** playlist [follower information](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)
     */
    override val following: FollowingAPI = FollowingAPI(this)

    override fun refreshToken(): Token {
        if (clientId != "not-set" && clientSecret != "not-set") {
            val currentToken = this.token

            token = getCredentialedToken(clientId, clientSecret, this)

            return currentToken
        }
        throw BadRequestException("Either the client id or the client secret is not set")
    }

    override val endpoints: List<SpotifyEndpoint>
        get() = listOf(
            search,
            albums,
            browse,
            artists,
            playlists,
            users,
            tracks,
            following
        )

    override fun getApiBuilder() = SpotifyApiBuilder(
        clientId,
        clientSecret,
        null
    ).apply { useCache(useCache) }

    override fun getApiBuilderDsl() = spotifyAppApi {
        credentials {
            clientId = this@SpotifyAppAPI.clientId
            clientSecret = this@SpotifyAppAPI.clientSecret
        }

        useCache = this@SpotifyAppAPI.useCache
    }
}

/**
 * An API instance created through client authentication, with access to private information
 * managed through the scopes exposed in [token]
 */
class SpotifyClientAPI internal constructor(
    clientId: String,
    clientSecret: String,
    var redirectUri: String,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean
) : SpotifyAPI(
    clientId,
    clientSecret,
    token,
    useCache,
    cacheLimit,
    automaticRefresh,
    retryWhenRateLimited,
    enableLogger,
    testTokenValidity
) {
    constructor(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        token: Token,
        options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
    ) : this(
        clientId,
        clientSecret,
        redirectUri,
        token,
        options.useCache,
        options.cacheLimit,
        options.automaticRefresh,
        options.retryWhenRateLimited,
        options.enableLogger,
        options.testTokenValidity
    )

    override val search: SearchAPI = SearchAPI(this)
    override val albums: AlbumAPI = AlbumAPI(this)
    override val browse: BrowseAPI = BrowseAPI(this)
    override val artists: ArtistsAPI = ArtistsAPI(this)
    override val tracks: TracksAPI = TracksAPI(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/playlists/) for retrieving
     * information about a user’s playlists and for managing a user’s playlists.
     * *Superset of [PlaylistAPI]*
     */
    override val playlists: ClientPlaylistAPI = ClientPlaylistAPI(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/users-profile/) for
     * retrieving information about a user’s profile.
     * *Superset of [UserAPI]*
     */
    override val users: ClientUserAPI = ClientUserAPI(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/follow/) for managing
     * the artists, users, and playlists that a Spotify user follows.
     * *Superset of [FollowingAPI]*
     */
    override val following: ClientFollowingAPI = ClientFollowingAPI(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/personalization/) for
     * retrieving information about the user’s listening habits.

     */
    val personalization: ClientPersonalizationAPI = ClientPersonalizationAPI(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/library/) for
     * retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library.
     */
    val library: ClientLibraryAPI = ClientLibraryAPI(this)

    /**
     * Provides access to the **beta** [player api](https://developer.spotify.com/documentation/web-api/reference/player/),
     * including track playing and pausing endpoints.
     *
     * Please consult the [usage guide](https://developer.spotify.com/documentation/web-api/guides/using-connect-web-api/) before
     * calling any endpoint in this api.
     *
     * **These endpoints may break at any time.**
     */
    val player: ClientPlayerAPI = ClientPlayerAPI(this)

    /**
     * The Spotify user id to which the api instance is connected
     */
    val userId: String

    init {
        userId = users.getUserProfile().complete().id
    }

    /**
     * Stop all automatic functions like refreshToken or clearCache and shut down the scheduled
     * executor
     * */
    fun shutdown() = executor.shutdown()

    override fun refreshToken(): Token {
        val currentToken = this.token

        val response = executeTokenRequest(
            HttpConnection(
                url = "https://accounts.spotify.com/api/token",
                method = HttpRequestMethod.POST,
                bodyMap = mapOf(
                    "grant_type" to "refresh_token",
                    "refresh_token" to token.refreshToken
                ),
                bodyString = null,
                contentType = "application/x-www-form-urlencoded",
                api = this
            ), clientId, clientSecret
        )

        if (response.responseCode / 200 == 1) {
            val tempToken = response.body.toObject<Token>(this)
            this.token = tempToken.copy(
                refreshToken = tempToken.refreshToken ?: this.token.refreshToken,
                scopes = tempToken.scopes
            )
            logger.logInfo("Successfully refreshed the Spotify token")
            return currentToken
        } else throw BadRequestException(response.body.toObject<AuthenticationError>(this))
    }

    override val endpoints: List<SpotifyEndpoint>
        get() = listOf(
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

    override fun getApiBuilder() = SpotifyApiBuilder(
        clientId,
        clientSecret,
        redirectUri
    ).apply {
        redirectUri(redirectUri)
        useCache(useCache)
    }

    override fun getApiBuilderDsl() = spotifyClientApi {
        credentials {
            clientId = this@SpotifyClientAPI.clientId
            clientSecret = this@SpotifyClientAPI.clientSecret
            redirectUri = this@SpotifyClientAPI.redirectUri
        }

        useCache = this@SpotifyClientAPI.useCache
    }

    /**
     * Create a Spotify authorization URL from which client access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     * @param redirectUri The redirect uri specified on the Spotify developer dashboard; where to
     * redirect the browser after authentication
     *
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        return getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)
    }

    /**
     * Whether the current access token allows access to scope [scope]
     */
    fun hasScope(scope: SpotifyScope): Boolean = hasScopes(scope)

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    fun hasScopes(scope: SpotifyScope, vararg scopes: SpotifyScope): Boolean =
        !isTokenValid(false).isValid && (scopes.toList() + scope).all { token.scopes.contains(it) }
}

fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String {
    return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
            "&response_type=code" +
            "&redirect_uri=$redirectUri" +
            if (scopes.isEmpty()) "" else "&scope=${scopes.joinToString("%20") { it.uri }}"
}

fun getCredentialedToken(clientId: String, clientSecret: String, api: SpotifyAPI?): Token {
    val response = executeTokenRequest(
        HttpConnection(
            url = "https://accounts.spotify.com/api/token",
            method = HttpRequestMethod.POST,
            bodyMap = mapOf("grant_type" to "client_credentials"),
            bodyString = null,
            contentType = "application/x-www-form-urlencoded",
            api = api
        ), clientId, clientSecret
    )

    if (response.responseCode / 200 == 1) return response.body.toObject(null)

    throw BadRequestException(response.body.toObject<AuthenticationError>(null))
}

internal fun executeTokenRequest(httpConnection: HttpConnection, clientId: String, clientSecret: String): HttpResponse {
    return httpConnection.execute(
        listOf(
            HttpHeader(
                "Authorization",
                "Basic ${"$clientId:$clientSecret".byteEncode()}"
            )
        )
    )
}