/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.endpoints.client.ClientFollowingApi
import com.adamratzman.spotify.endpoints.client.ClientLibraryApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.endpoints.client.ClientPlaylistApi
import com.adamratzman.spotify.endpoints.client.ClientProfileApi
import com.adamratzman.spotify.endpoints.public.AlbumApi
import com.adamratzman.spotify.endpoints.public.ArtistApi
import com.adamratzman.spotify.endpoints.public.BrowseApi
import com.adamratzman.spotify.endpoints.public.FollowingApi
import com.adamratzman.spotify.endpoints.public.PlaylistApi
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.endpoints.public.TrackApi
import com.adamratzman.spotify.endpoints.public.UserApi
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.HttpResponse
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.base64ByteEncode
import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.TokenValidityResponse
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.asList
import com.adamratzman.spotify.utils.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmOverloads
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

/**
 * Base url for Spotify web api calls
 */
internal const val base = "https://api.spotify.com/v1"

/**
 * Represents an instance of the Spotify API client, with common
 * functionality and information between the [SpotifyClientApi] and [SpotifyAppApi]
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
 *
 */
sealed class SpotifyApi<T : SpotifyApi<T, B>, B : ISpotifyApiBuilder<T, B>>(
    val clientId: String?,
    val clientSecret: String?,
    var token: Token,
    useCache: Boolean,
    var cacheLimit: Int?,
    var automaticRefresh: Boolean,
    var retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean,
    var json: Json
) {
    var useCache = useCache
        set(value) {
            if (!value) clearCache()

            field = value
        }
    val logger = SpotifyLogger(enableLogger)
    val expireTime: Long get() = token.expiresAt
    var runExecutableFunctions = true

    abstract val search: SearchApi
    abstract val albums: AlbumApi
    abstract val browse: BrowseApi
    abstract val artists: ArtistApi
    abstract val playlists: PlaylistApi
    abstract val users: UserApi
    abstract val tracks: TrackApi
    abstract val following: FollowingApi

    init {
        if (testTokenValidity) {
            if (!isTokenValid().isValid)
                try {
                    refreshToken()
                } catch (e: BadRequestException) {
                    throw SpotifyException.AuthenticationException(
                            "Invalid token and refresh token supplied. Cannot refresh to a fresh token.",
                            e
                    )
                }
        }
    }

    /**
     * Obtain a map of all currently-cached requests
     */
    fun getCache() = endpoints.map { it.cache.cachedRequests.asList() }.flatten().toMap()

    /**
     * If the method used to create the [token] supports token refresh and
     * the information in [token] is accurate, attempt to refresh the token
     *
     * @return The old access token if refresh was successful
     * @throws BadRequestException if refresh fails
     */
    fun refreshToken(): Token = runBlocking {
        suspendRefreshToken()
    }

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
     * Return a new [B] with the parameters provided to this api instance
     */
    abstract fun getApiBuilderDsl(): B

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
        require(clientId != null)
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
    @JvmOverloads
    fun isTokenValid(makeTestRequest: Boolean = true): TokenValidityResponse = runBlocking {
        suspendIsTokenValid(makeTestRequest)
    }

    @JvmOverloads
    suspend fun suspendIsTokenValid(
        makeTestRequest: Boolean = true,
        context: CoroutineContext = Dispatchers.Default
    ): TokenValidityResponse {
        if (token.shouldRefresh()) return TokenValidityResponse(
                false,
                SpotifyException.AuthenticationException("Token needs to be refreshed (is it expired?)")
        )
        if (!makeTestRequest) return TokenValidityResponse(true, null)

        return try {
            browse.getAvailableGenreSeeds().suspendComplete(context)
            TokenValidityResponse(true, null)
        } catch (e: Exception) {
            TokenValidityResponse(false, e)
        }
    }

    /**
     * If the method used to create the [token] supports token refresh and
     * the information in [token] is accurate, attempt to refresh the token
     *
     * @return The old access token if refresh was successful
     * @throws BadRequestException if refresh fails
     */
    abstract suspend fun suspendRefreshToken(): Token

    companion object {
        /*
            Builder tools
         */

        /**
         * Get the authorization url for the provided [clientId] and [redirectUri] application settings, when attempting to authorize with
         * specified [scopes]
         *
         * @param scopes Spotify scopes the api instance should be able to access for the user
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
         */
        fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String {
            return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
                    "&response_type=code" +
                    "&redirect_uri=$redirectUri" +
                    if (scopes.isEmpty()) "" else "&scope=${scopes.joinToString("%20") { it.uri }}"
        }

        /**
         *
         * Get an application token (can only access public methods) that can be used to instantiate a new [SpotifyAppApi]
         *
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param api The Spotify Api instance, or null if one doesn't exist yet
         * @param json The json instance that will deserialize the response. If [api] is not null, [SpotifyApi.json] can be used
         */
        suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: SpotifyApi<*, *>?, json: Json): Token {
            val response = executeTokenRequest(
                    HttpConnection(
                            "https://accounts.spotify.com/api/token",
                            HttpRequestMethod.POST,
                            mapOf("grant_type" to "client_credentials"),
                            null,
                            "application/x-www-form-urlencoded",
                            listOf(),
                            api
                    ), clientId, clientSecret
            )

            if (response.responseCode / 200 == 1) return response.body.toObject(Token.serializer(), null, json)

            throw BadRequestException(response.body.toObject(AuthenticationError.serializer(), null, json))
        }

        // ==============================================

        /*
            App Api Builders
         */

        /**
         * Instantiate a new [SpotifyAppApiBuilder] using a Spotify [clientId] and [clientSecret], with the ability to configure
         * the api settings by providing a builder initialization [block]
         *
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param block Api settings block
         *
         * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
         */
        fun spotifyAppApi(clientId: String, clientSecret: String, block: SpotifyAppApiBuilder.() -> Unit = {}) =
                SpotifyAppApiBuilder().apply(block).apply {
                    credentials {
                        this.clientId = clientId
                        this.clientSecret = clientSecret
                    }
                }

        /**
         * Instantiate a new [SpotifyAppApiBuilder] by providing a builder initialization [block].
         *
         * **Note**: You **must** provide your app credentials in the [SpotifyAppApiBuilder.credentials] block
         *
         * @param block Api settings block
         *
         * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
         */
        fun spotifyAppApi(block: SpotifyAppApiBuilder.() -> Unit) = SpotifyAppApiBuilder().apply(block)

        /*
            Client Api Builders
         */

        /**
         * Instantiate a new [SpotifyClientApiBuilder] using a Spotify [clientId], [clientSecret], and [redirectUri], with the ability to configure
         * the api settings by providing a builder initialization [block]
         *
         * **Note**: If trying to build [SpotifyClientApi], you **must** provide client authorization in the [SpotifyClientApiBuilder.authorization]
         * block
         *
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param block Api settings block
         *
         * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
         */
        fun spotifyClientApi(
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            block: SpotifyClientApiBuilder.() -> Unit
        ) = SpotifyClientApiBuilder().apply(block).apply {
            credentials {
                this.clientId = clientId
                this.clientSecret = clientSecret
                this.redirectUri = redirectUri
            }
        }

        /**
         * Instantiate a new [SpotifyClientApiBuilder] by providing a builder initialization [block]
         *
         * **Note**: If trying to build [SpotifyClientApi], you **must** provide client authorization in the [SpotifyClientApiBuilder.authorization]
         * block
         *
         * @param block Api settings block
         *
         * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
         */
        fun spotifyClientApi(block: SpotifyClientApiBuilder.() -> Unit) = SpotifyClientApiBuilder().apply(block)
    }
}

/**
 * An API instance created with application credentials, not through
 * client authentication
 */
class SpotifyAppApi internal constructor(
    clientId: String?,
    clientSecret: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean,
    json: Json
) : SpotifyApi<SpotifyAppApi, SpotifyAppApiBuilder>(
        clientId,
        clientSecret,
        token,
        useCache,
        cacheLimit,
        automaticRefresh,
        retryWhenRateLimited,
        enableLogger,
        testTokenValidity,
        json
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
            options.testTokenValidity,
            options.json
    )

    override val search: SearchApi = SearchApi(this)
    override val albums: AlbumApi = AlbumApi(this)
    override val browse: BrowseApi = BrowseApi(this)
    override val artists: ArtistApi = ArtistApi(this)
    override val tracks: TrackApi = TrackApi(this)

    /**
     * Provides access to **public** Spotify [playlist endpoints](https://developer.spotify.com/documentation/web-api/reference/playlists/)
     */
    override val playlists: PlaylistApi = PlaylistApi(this)

    /**
     * Provides access to **public** Spotify [user information](https://developer.spotify.com/documentation/web-api/reference/users-profile/get-users-profile/)
     */
    override val users: UserApi = UserApi(this)

    /**
     * Provides access to **public** playlist [follower information](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)
     */
    override val following: FollowingApi = FollowingApi(this)

    override suspend fun suspendRefreshToken(): Token {
        require(clientId != null && clientSecret != null) { "Either the client id or the client secret is not set" }
        val currentToken = this.token

        token = getCredentialedToken(clientId, clientSecret, this, json)

        return currentToken
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
            clientId = this@SpotifyAppApi.clientId
            clientSecret = this@SpotifyAppApi.clientSecret
        }

        useCache = this@SpotifyAppApi.useCache
    }
}

/**
 * An API instance created through client authentication, with access to private information
 * managed through the scopes exposed in [token]
 */
class SpotifyClientApi internal constructor(
    clientId: String?,
    clientSecret: String?,
    var redirectUri: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean,
    json: Json
) : SpotifyApi<SpotifyClientApi, SpotifyClientApiBuilder>(
        clientId,
        clientSecret,
        token,
        useCache,
        cacheLimit,
        automaticRefresh,
        retryWhenRateLimited,
        enableLogger,
        testTokenValidity,
        json
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
            options.testTokenValidity,
            options.json
    )

    override val search: SearchApi = SearchApi(this)
    override val albums: AlbumApi = AlbumApi(this)
    override val browse: BrowseApi = BrowseApi(this)
    override val artists: ArtistApi = ArtistApi(this)
    override val tracks: TrackApi = TrackApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/playlists/) for retrieving
     * information about a user’s playlists and for managing a user’s playlists.
     * *Superset of [PlaylistApi]*
     */
    override val playlists: ClientPlaylistApi = ClientPlaylistApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/users-profile/) for
     * retrieving information about a user’s profile.
     * *Superset of [UserApi]*
     */
    override val users: ClientProfileApi = ClientProfileApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/follow/) for managing
     * the artists, users, and playlists that a Spotify user follows.
     * *Superset of [FollowingApi]*
     */
    override val following: ClientFollowingApi = ClientFollowingApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/personalization/) for
     * retrieving information about the user’s listening habits.

     */
    val personalization: ClientPersonalizationApi = ClientPersonalizationApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/library/) for
     * retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library.
     */
    val library: ClientLibraryApi = ClientLibraryApi(this)

    /**
     * Provides access to the **beta** [player api](https://developer.spotify.com/documentation/web-api/reference/player/),
     * including track playing and pausing endpoints.
     *
     * Please consult the [usage guide](https://developer.spotify.com/documentation/web-api/guides/using-connect-web-api/) before
     * calling any endpoint in this api.
     *
     * **These endpoints may break at any time.**
     */
    val player: ClientPlayerApi = ClientPlayerApi(this)

    /**
     * The Spotify user id to which the api instance is connected
     */
    val userId: String

    init {
        userId = users.getClientProfile().complete().id
    }

    /**
     * Stop all automatic functions like refreshToken or clearCache and shut down the scheduled
     * executor
     * */
    fun shutdown() {
        runExecutableFunctions = false
    }

    override suspend fun suspendRefreshToken(): Token {
        require(clientId != null && clientSecret != null) { "Either the client id or the client secret is not set" }

        val currentToken = this.token

        val response = executeTokenRequest(
                HttpConnection(
                        "https://accounts.spotify.com/api/token",
                        HttpRequestMethod.POST,
                        mapOf(
                                "grant_type" to "refresh_token",
                                "refresh_token" to token.refreshToken
                        ),
                        null,
                        "application/x-www-form-urlencoded",
                        listOf(),
                        this
                ), clientId, clientSecret
        )

        if (response.responseCode / 200 == 1) {
            val tempToken = response.body.toObject(Token.serializer(), this, json)
            this.token = tempToken.copy(
                    refreshToken = tempToken.refreshToken ?: this.token.refreshToken
            ).apply { scopes = tempToken.scopes }

            logger.logInfo("Successfully refreshed the Spotify token")
            return currentToken
        } else throw BadRequestException(
                response.body.toObject(
                        AuthenticationError.serializer(),
                        this,
                        json
                )
        )
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
            clientId = this@SpotifyClientApi.clientId
            clientSecret = this@SpotifyClientApi.clientSecret
            redirectUri = this@SpotifyClientApi.redirectUri
        }

        useCache = this@SpotifyClientApi.useCache
    }

    /**
     * Create a Spotify authorization URL from which client access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     *
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        require(clientId != null && clientSecret != null) { "Either the client id or the client secret is not set" }
        return redirectUri?.let { getAuthUrlFull(*scopes, clientId = clientId, redirectUri = it) }
                ?: throw IllegalArgumentException("The redirect uri must be set")
    }

    /**
     * Whether the current access token allows access to scope [scope]
     */
    fun hasScope(scope: SpotifyScope): Boolean = hasScopes(scope)

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    fun hasScopes(scope: SpotifyScope, vararg scopes: SpotifyScope): Boolean =
            !isTokenValid(false).isValid &&
                    token.scopes.contains(scope) &&
                    scopes.all { token.scopes.contains(it) }
}

@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyApi"))
typealias SpotifyAPI<T, B> = SpotifyApi<T, B>

typealias SpotifyClientAPI = SpotifyClientApi
@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyAppApi"))
typealias SpotifyAppAPI = SpotifyAppApi

/**
 * Get the authorization url for the provided [clientId] and [redirectUri] application settings, when attempting to authorize with
 * specified [scopes]
 *
 * @param scopes Spotify scopes the api instance should be able to access for the user
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 */
@Deprecated("Moved", ReplaceWith("SpotifyApi.getAuthUrlFull"))
fun getAuthUrlFull(vararg scopes: SpotifyScope, clientId: String, redirectUri: String): String = SpotifyApi.getAuthUrlFull(*scopes, clientId = clientId, redirectUri = redirectUri)

/**
 *
 * Get an application token (can only access public methods) that can be used to instantiate a new [SpotifyAppApi]
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param api The Spotify Api instance, or null if one doesn't exist yet
 * @param json The json instance that will deserialize the response. If [api] is not null, [SpotifyApi.json] can be used
 */
@Deprecated("Moved", ReplaceWith("SpotifyApi.getCredentialedToken"))
suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: SpotifyApi<*, *>?, json: Json): Token = SpotifyApi.getCredentialedToken(clientId, clientSecret, api, json)

internal suspend fun executeTokenRequest(
    httpConnection: HttpConnection,
    clientId: String,
    clientSecret: String
): HttpResponse {
    return httpConnection.execute(
            listOf(
                    HttpHeader(
                            "Authorization",
                            "Basic ${"$clientId:$clientSecret".base64ByteEncode()}"
                    )
            )
    )
}
