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
import com.adamratzman.spotify.http.CacheState
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.HttpResponse
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.SpotifyRequest
import com.adamratzman.spotify.http.base64ByteEncode
import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.TokenValidityResponse
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.asList
import kotlin.jvm.JvmOverloads
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
public sealed class SpotifyApi<T : SpotifyApi<T, B>, B : ISpotifyApiBuilder<T, B>>(
    public val clientId: String?,
    public val clientSecret: String?,
    public var token: Token,
    useCache: Boolean,
    public var cacheLimit: Int?,
    public var automaticRefresh: Boolean,
    public var retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    public var defaultLimit: Int,
    public var allowBulkRequests: Boolean,
    public var requestTimeoutMillis: Long?,
    public var json: Json,
    public var refreshTokenProducer: suspend (SpotifyApi<*, *>) -> Token,
    public var onTokenRefresh: (suspend (SpotifyApi<*, *>) -> Unit)?,
    requiredScopes: List<SpotifyScope>?
) {
    public var useCache: Boolean = useCache
        set(value) {
            if (!value) clearCache()

            field = value
        }
    public val logger: SpotifyLogger = SpotifyLogger(enableLogger)
    public val expireTime: Long get() = token.expiresAt
    public var runExecutableFunctions: Boolean = true

    public abstract val search: SearchApi
    public abstract val albums: AlbumApi
    public abstract val browse: BrowseApi
    public abstract val artists: ArtistApi
    public abstract val playlists: PlaylistApi
    public abstract val users: UserApi
    public abstract val tracks: TrackApi
    public abstract val following: FollowingApi

    init {
        if (requiredScopes != null) {
            val tokenScopes = token.scopes ?: listOf()
            if (!tokenScopes.containsAll(requiredScopes)) {
                val missingScopes = requiredScopes.filter { it !in tokenScopes }

                throw IllegalStateException(
                        "Expected authorized scopes $requiredScopes, but was missing the following scopes: $missingScopes"
                )
            }
        }
    }

    /**
     * Obtain a map of all currently-cached requests
     */
    public fun getCache(): Map<SpotifyRequest, CacheState> = endpoints.map { it.cache.cachedRequests.asList() }.flatten().toMap()

    /**
     * Change the current [Token]'s access token
     */
    public fun updateTokenWith(tokenString: String) {
        updateToken {
            accessToken = tokenString
        }
    }

    /**
     * Modify the current [Token] via DSL
     */
    public fun updateToken(modifier: Token.() -> Unit) {
        modifier(token)
    }

    /**
     * A list of all endpoints included in this api type
     */
    public abstract val endpoints: List<SpotifyEndpoint>

    /**
     * If the cache is enabled, clear all stored queries in the cache
     */
    public fun clearCache(): Unit = clearCaches(*endpoints.toTypedArray())

    /**
     * Return a new [SpotifyApiBuilder] with the parameters provided to this api instance
     */
    public abstract fun getApiBuilder(): SpotifyApiBuilder

    /**
     * Return a new [B] with the parameters provided to this api instance
     */
    public abstract fun getApiBuilderDsl(): B

    private fun clearCaches(vararg endpoints: SpotifyEndpoint) {
        endpoints.forEach { it.cache.clear() }
    }

    /**
     * Allows enabling and disabling the logger
     *
     * @param enable Whether to enable the logger
     */
    public fun useLogger(enable: Boolean) {
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
    public fun getAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String, state: String? = null): String {
        require(clientId != null)
        return getAuthUrlFull(
                *scopes,
                clientId = clientId,
                redirectUri = redirectUri,
                state = state
        )
    }

    public fun getPkceAuthorizationUrl(vararg scopes: SpotifyScope, redirectUri: String, codeChallenge: String, state: String? = null): String {
        require(clientId != null)
        return getPkceAuthUrlFull(
                *scopes,
                clientId = clientId,
                redirectUri = redirectUri,
                codeChallenge = codeChallenge,
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
    public suspend fun isTokenValid(
        makeTestRequest: Boolean = true
    ): TokenValidityResponse {
        if (token.shouldRefresh()) return TokenValidityResponse(
                false,
                SpotifyException.AuthenticationException("Token needs to be refreshed (is it expired?)")
        )
        if (!makeTestRequest) return TokenValidityResponse(true, null)

        return try {
            browse.getAvailableGenreSeeds()
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
    public suspend fun refreshToken(): Token = refreshTokenProducer(this).apply {
        this@SpotifyApi.token = this
        onTokenRefresh?.let { it(this@SpotifyApi) }
    }

    public companion object {
        internal suspend fun testTokenValidity(api: GenericSpotifyApi) {
            if (!api.isTokenValid().isValid) {
                try {
                    api.refreshToken()
                } catch (e: BadRequestException) {
                    throw SpotifyException.AuthenticationException(
                            "Invalid token and refresh token supplied. Cannot refresh to a fresh token.",
                            e
                    )
                }
            }
        }

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
        public fun getAuthUrlFull(
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
        public fun getPkceAuthUrlFull(
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
        public suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: GenericSpotifyApi?, json: Json): Token {
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
public class SpotifyAppApi internal constructor(
    clientId: String?,
    clientSecret: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json,
    refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)?,
    onTokenRefresh: (suspend (GenericSpotifyApi) -> Unit)?,
    requiredScopes: List<SpotifyScope>?
) : SpotifyApi<SpotifyAppApi, SpotifyAppApiBuilder>(
        clientId,
        clientSecret,
        token,
        useCache,
        cacheLimit,
        automaticRefresh,
        retryWhenRateLimited,
        enableLogger,
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        refreshTokenProducer ?: defaultAppApiTokenRefreshProducer,
        onTokenRefresh,
        requiredScopes
) {
    public constructor(
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
            options.defaultLimit,
            options.allowBulkRequests,
            options.requestTimeoutMillis,
            options.json,
            options.refreshTokenProducer,
            options.onTokenRefresh,
            options.requiredScopes
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

    override fun getApiBuilder(): SpotifyApiBuilder = SpotifyApiBuilder(
            clientId,
            clientSecret,
            null
    ).apply { useCache(useCache) }

    override fun getApiBuilderDsl(): SpotifyAppApiBuilder = spotifyAppApi {
        credentials {
            clientId = this@SpotifyAppApi.clientId
            clientSecret = this@SpotifyAppApi.clientSecret
        }

        useCache = this@SpotifyAppApi.useCache
    }

    public companion object {
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
public open class SpotifyClientApi(
    clientId: String?,
    clientSecret: String?,
    public var redirectUri: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    automaticRefresh: Boolean,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json,
    refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)?,
    public val usesPkceAuth: Boolean,
    onTokenRefresh: (suspend (GenericSpotifyApi) -> Unit)?,
    requiredScopes: List<SpotifyScope>?
) : SpotifyApi<SpotifyClientApi, SpotifyClientApiBuilder>(
        clientId,
        clientSecret,
        token,
        useCache,
        cacheLimit,
        automaticRefresh,
        retryWhenRateLimited,
        enableLogger,
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        refreshTokenProducer ?: defaultClientApiTokenRefreshProducer,
        onTokenRefresh,
        requiredScopes
) {
    public constructor(
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
            options.defaultLimit,
            options.allowBulkRequests,
            options.requestTimeoutMillis,
            options.json,
            options.refreshTokenProducer,
            usesPkceAuth,
            options.onTokenRefresh,
            options.requiredScopes
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
    public val episodes: EpisodeApi = EpisodeApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/shows/) for retrieving
     * information about one or more shows from the Spotify catalog.
     */
    @SpotifyExperimentalHttpApi
    public val shows: ShowApi = ShowApi(this)

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
    public val personalization: ClientPersonalizationApi = ClientPersonalizationApi(this)

    /**
     * Provides access to [endpoints](https://developer.spotify.com/documentation/web-api/reference/library/) for
     * retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library.
     */
    public val library: ClientLibraryApi = ClientLibraryApi(this)

    /**
     * Provides access to the **beta** [player api](https://developer.spotify.com/documentation/web-api/reference/player/),
     * including track playing and pausing endpoints.
     *
     * Please consult the [usage guide](https://developer.spotify.com/documentation/web-api/guides/using-connect-web-api/) before
     * calling any endpoint in this api.
     *
     * **These endpoints may break at any time.**
     */
    public val player: ClientPlayerApi = ClientPlayerApi(this)

    private lateinit var userIdBacking: String

    private suspend fun initiatizeUserIdBacking(): String {
        userIdBacking = users.getClientProfile().id
        return userIdBacking
    }

    /**
     * The Spotify user id to which the api instance is connected
     */
    public suspend fun getUserId(): String = if (::userIdBacking.isInitialized) userIdBacking else initiatizeUserIdBacking()

    /**
     * Stop all automatic functions like refreshToken or clearCache and shut down the scheduled
     * executor
     * */
    public fun shutdown() {
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

    override fun getApiBuilder(): SpotifyApiBuilder = SpotifyApiBuilder(
            clientId,
            clientSecret,
            redirectUri
    ).apply {
        redirectUri(redirectUri)
        useCache(useCache)
    }

    override fun getApiBuilderDsl(): SpotifyClientApiBuilder = spotifyClientApi {
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
    public fun getAuthorizationUrl(vararg scopes: SpotifyScope, state: String? = null): String {
        require(clientId != null && clientSecret != null) { "Either the client id or the client secret is not set" }
        return redirectUri?.let { getAuthUrlFull(*scopes, clientId = clientId, redirectUri = it, state = state) }
                ?: throw IllegalArgumentException("The redirect uri must be set")
    }

    /**
     * Whether the current access token allows access to scope [scope]
     */
    public suspend fun hasScope(scope: SpotifyScope): Boolean? = hasScopes(scope)

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    public suspend fun hasScopes(scope: SpotifyScope, vararg scopes: SpotifyScope): Boolean? =
            if (token.scopes == null) null
            else !isTokenValid(false).isValid &&
                    token.scopes?.contains(scope) == true &&
                    scopes.all { token.scopes?.contains(it) == true }

    public companion object {
        private val defaultClientApiTokenRefreshProducer: suspend (GenericSpotifyApi) -> Token = { api ->
            api as SpotifyClientApi

            require(api.clientId != null) { "The client id is not set" }

            val response = if (!api.usesPkceAuth) {
                require(api.clientSecret != null) { "The client secret is not set" }
                executeTokenRequest(
                        HttpConnection(
                                "https://accounts.spotify.com/api/token",
                                HttpRequestMethod.POST,
                                getDefaultClientApiTokenBody(api),
                                null,
                                "application/x-www-form-urlencoded",
                                listOf(),
                                api
                        ), api.clientId, api.clientSecret)
            } else {
                HttpConnection(
                        "https://accounts.spotify.com/api/token",
                        HttpRequestMethod.POST,
                        getDefaultClientApiTokenBody(api),
                        null,
                        "application/x-www-form-urlencoded",
                        listOf(),
                        api
                ).execute()
            }

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
public class SpotifyImplicitGrantApi(
    clientId: String?,
    clientSecret: String?,
    redirectUri: String?,
    token: Token,
    useCache: Boolean,
    cacheLimit: Int?,
    retryWhenRateLimited: Boolean,
    enableLogger: Boolean,
    defaultLimit: Int,
    allowBulkRequests: Boolean,
    requestTimeoutMillis: Long?,
    json: Json,
    requiredScopes: List<SpotifyScope>?
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
        defaultLimit,
        allowBulkRequests,
        requestTimeoutMillis,
        json,
        { throw IllegalStateException("You cannot refresh an implicit grant access token!") },
        false,
        null,
        requiredScopes
)

@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyApi"))
public typealias SpotifyAPI<T, B> = SpotifyApi<T, B>

public typealias SpotifyClientAPI = SpotifyClientApi
@Deprecated("API name has been updated for kotlin convention consistency", ReplaceWith("SpotifyAppApi"))
public typealias SpotifyAppAPI = SpotifyAppApi

/**
 * Represents a generic instance of the Spotify API client, with common functionality and information between
 * implementations of the API
 */
public typealias GenericSpotifyApi = SpotifyApi<*, *>

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
public suspend fun getCredentialedToken(clientId: String, clientSecret: String, api: GenericSpotifyApi?, json: Json): Token = SpotifyApi.getCredentialedToken(clientId, clientSecret, api, json)

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
