/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.endpoints.client.ClientFollowingApi
import com.adamratzman.spotify.endpoints.client.ClientLibraryApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.endpoints.client.ClientPlaylistApi
import com.adamratzman.spotify.endpoints.client.ClientProfileApi
import com.adamratzman.spotify.endpoints.client.ClientSearchApi
import com.adamratzman.spotify.endpoints.public.AlbumApi
import com.adamratzman.spotify.endpoints.public.ArtistApi
import com.adamratzman.spotify.endpoints.public.BrowseApi
import com.adamratzman.spotify.endpoints.public.EpisodeApi
import com.adamratzman.spotify.endpoints.public.FollowingApi
import com.adamratzman.spotify.endpoints.public.PlaylistApi
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.endpoints.public.ShowApi
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
 * @property defaultLimit The default amount of objects to retrieve in one request, for requests that support it.
 * @property json The Json serializer/deserializer instance
 * @property logger The Spotify event logger
 * @property requestTimeoutMillis The maximum time, in milliseconds, before terminating an http request
 * @property allowBulkRequests Allow splitting too-large requests into smaller, allowable api requests
 * @property refreshTokenProducer Pluggable producer that refreshes and replaces the current [token].
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
    var defaultLimit: Int,
    var allowBulkRequests: Boolean,
    var requestTimeoutMillis: Long?,
    var json: Json,
    var refreshTokenProducer: suspend (SpotifyApi<*, *>) -> Token
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
     * Change the current [Token]'s access token
     */
    fun updateTokenWith(tokenString: String) {
        updateToken {
            accessToken = tokenString
        }
    }

    /**
     * Modify the current [Token] via DSL
     */
    fun updateToken(modifier: Token.() -> Unit) {
        modifier(token)
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
     * @param state This provides protection against attacks such as cross-site request forgery.
     *
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String, state: String? = null): String {
        require(clientId != null)
        return getAuthUrlFull(
                *scopes,
                clientId = clientId,
                redirectUri = redirectUri,
                state = state
        )
    }
    
    fun getPkceAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String, codeChallenge: String, state: String? = null ): String {
        require(clientId != null)
        return getPkceAuthUrlFull(
                *scopes,
                clientId=clientId,
                redirectUri=redirectUri,
                codeChallenge=codeChallenge,
                state = state
        )
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
    suspend fun suspendRefreshToken() = refreshTokenProducer(this).apply { this@SpotifyApi.token = this }

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
         * @param state This provides protection against attacks such as cross-site request forgery.
         */
        fun getAuthUrlFull(
            vararg scopes: SpotifyScope,
            clientId: String,
            redirectUri: String,
            isImplicitGrantFlow: Boolean = false,
            shouldShowDialog: Boolean = false,
            state: String? = null
        ): String {
            return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
                    "&response_type=${if (isImplicitGrantFlow) "token" else "code"}" +
                    "&redirect_uri=$redirectUri" +
                    (state?.let { "&state=$it" } ?: "") +
                    if (scopes.isEmpty()) "" else "&scope=${scopes.joinToString("%20") { it.uri }}" +
                            if (shouldShowDialog) "&show_dialog=$shouldShowDialog" else ""
        }


        /**
         * Get the PKCE authorization url for the provided [clientId] and [redirectUri] application settings, when attempting to authorize with
         * specified [scopes]
         *
         * @param scopes Spotify scopes the api instance should be able to access for the user
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param codeChallenge The code challenge corresponding to your codeVerifier. **It is highly recommend to use
         * [getSpotifyPkceCodeChallenge] to get the code challenge from a code verifier (only available for JVM/Android).**
         * @param state This provides protection against attacks such as cross-site request forgery.
         */
        fun getPkceAuthUrlFull(
                vararg scopes: SpotifyScope,
                clientId: String,
                redirectUri: String,
                codeChallenge: String,
                state: String? = null
        ): String {
            return "https://accounts.spotify.com/authorize/?client_id=$clientId" +
                    "&response_type=code" +
                    "&redirect_uri=$redirectUri" +
                    "&code_challenge_method=S256" +
                    "&code_challenge=$codeChallenge" +
                    (state?.let { "&state=$it" } ?: "") +
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
        suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: GenericSpotifyApi?, json: Json): Token {
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
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json,
    refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)?
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
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        refreshTokenProducer ?: defaultAppApiTokenRefreshProducer
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
            options.defaultLimit,
            options.allowBulkRequests,
            options.requestTimeoutMillis,
            options.json,
            options.refreshTokenProducer
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

    companion object {
        private val defaultAppApiTokenRefreshProducer: suspend (SpotifyApi<*, *>) -> Token = { api ->
            require(api.clientId != null && api.clientSecret != null) { "Either the client id or the client secret is not set" }

            getCredentialedToken(api.clientId, api.clientSecret, api, api.json)
        }
    }
}

/**
 * An API instance created through client authentication, with access to private information
 * managed through the scopes exposed in [token]
 */
open class SpotifyClientApi(
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
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json,
    refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)?,
    val usesPkceAuth: Boolean
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
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        refreshTokenProducer ?: defaultClientApiTokenRefreshProducer
) {
    constructor(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        token: Token,
        usesPkceAuth: Boolean,
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
            options.defaultLimit,
            options.allowBulkRequests,
            options.requestTimeoutMillis,
            options.json,
            options.refreshTokenProducer,
            usesPkceAuth
    )

    override val albums: AlbumApi = AlbumApi(this)
    override val browse: BrowseApi = BrowseApi(this)
    override val artists: ArtistApi = ArtistApi(this)
    override val tracks: TrackApi = TrackApi(this)

    override val search: ClientSearchApi = ClientSearchApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/episodes/) for retrieving
     * information about one or more episodes from the Spotify catalog.
     *
     * @since 3.1.0
     */
    @SpotifyExperimentalHttpApi
    val episodes: EpisodeApi = EpisodeApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/shows/) for retrieving
     * information about one or more shows from the Spotify catalog.
     */
    @SpotifyExperimentalHttpApi
    val shows: ShowApi = ShowApi(this)

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

    private lateinit var userIdBacking: String

    private fun initiatizeUserIdBacking(): String {
        userIdBacking = users.getClientProfile().complete().id
        return userIdBacking
    }

    /**
     * The Spotify user id to which the api instance is connected
     */
    val userId: String get() = if (::userIdBacking.isInitialized) userIdBacking else initiatizeUserIdBacking()

    /**
     * Stop all automatic functions like refreshToken or clearCache and shut down the scheduled
     * executor
     * */
    fun shutdown() {
        runExecutableFunctions = false
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
    fun getAuthorizationUrl(vararg scopes: SpotifyScope, state: String? = null): String {
        require(clientId != null && clientSecret != null) { "Either the client id or the client secret is not set" }
        return redirectUri?.let { getAuthUrlFull(*scopes, clientId = clientId, redirectUri = it, state = state) }
                ?: throw IllegalArgumentException("The redirect uri must be set")
    }

    /**
     * Whether the current access token allows access to scope [scope]
     */
    fun hasScope(scope: SpotifyScope): Boolean? = hasScopes(scope)

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    fun hasScopes(scope: SpotifyScope, vararg scopes: SpotifyScope): Boolean? =
            if (token.scopes == null) null
            else !isTokenValid(false).isValid &&
                    token.scopes?.contains(scope) == true &&
                    scopes.all { token.scopes?.contains(it) == true }

    companion object {
        private val defaultClientApiTokenRefreshProducer: suspend (GenericSpotifyApi) -> Token = { api ->
            require(api.clientId != null && api.clientSecret != null) { "Either the client id or the client secret is not set" }
            api as SpotifyClientApi

            val response = executeTokenRequest(
                    HttpConnection(
                            "https://accounts.spotify.com/api/token",
                            HttpRequestMethod.POST,
                            getDefaultClientApiTokenBody(api),
                            null,
                            "application/x-www-form-urlencoded",
                            listOf(),
                            api
                    ), api.clientId, api.clientSecret
            )

            if (response.responseCode / 200 == 1) {
                api.logger.logInfo("Successfully refreshed the Spotify token")
                response.body.toObject(Token.serializer(), api, api.json)
            } else throw BadRequestException(
                    response.body.toObject(
                            AuthenticationError.serializer(),
                            api,
                            api.json
                    )
            )
        }

        private fun getDefaultClientApiTokenBody(api: SpotifyClientApi): Map<String, String?> {
            val map = mutableMapOf(
                    "grant_type" to "refresh_token",
                    "refresh_token" to api.token.refreshToken
            )

            if (api.usesPkceAuth) map += "client_id" to api.clientId

            return map
        }
    }
}

/**
 * An API instance created through implicit grant flow, with access to private information
 * managed through the scopes exposed in [token]. [token] is not refreshable and is only accessible for limited time.
 */
class SpotifyImplicitGrantApi(
    clientId: String?,
    clientSecret: String?,
    redirectUri: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    testTokenValidity: Boolean,
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json
) : SpotifyClientApi(
        clientId,
        clientSecret,
        redirectUri,
        token,
        useCache,
        cacheLimit,
        false,
        retryWhenRateLimited,
        enableLogger,
        testTokenValidity,
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        { throw IllegalStateException("You cannot refresh an implicit grant access token!") },
        false
)

@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyApi"))
typealias SpotifyAPI<T, B> = SpotifyApi<T, B>

typealias SpotifyClientAPI = SpotifyClientApi
@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyAppApi"))
typealias SpotifyAppAPI = SpotifyAppApi

/**
 * Represents a generic instance of the Spotify API client, with common functionality and information between
 * implementations of the API
 */
typealias GenericSpotifyApi = SpotifyApi<*, *>

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
suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: GenericSpotifyApi?, json: Json): Token = SpotifyApi.getCredentialedToken(clientId, clientSecret, api, json)

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
