/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:Suppress("LeakingThis")

package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.endpoints.client.*
import com.adamratzman.spotify.endpoints.pub.*
import com.adamratzman.spotify.http.*
import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.TokenValidityResponse
import com.adamratzman.spotify.models.serialization.nonstrictJson
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.asList
import com.adamratzman.spotify.utils.base64ByteEncode
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmOverloads

/**
 * Represents an instance of the Spotify API client, with common
 * functionality and information between the [SpotifyClientApi] and [SpotifyAppApi]
 * implementations of the API
 *
 * @param clientId The application client id found on the application [dashboard](https://developer.spotify.com/dashboard/applications)
 * @param clientSecret The application client secret found on the application [dashboard](https://developer.spotify.com/dashboard/applications)
 * @param token The access token associated with this API instance
 * @param spotifyApiOptions Configurable Spotify API options.
 *
 * @property search Provides access to the Spotify [search endpoint](https://developer.spotify.com/documentation/web-api/reference/search/search/)
 * @property albums Provides access to Spotify [album endpoints](https://developer.spotify.com/documentation/web-api/reference/albums/)
 * @property browse Provides access to Spotify [browse endpoints](https://developer.spotify.com/documentation/web-api/reference/browse/)
 * @property artists Provides access to Spotify [artist endpoints](https://developer.spotify.com/documentation/web-api/reference/artists/)
 * @property tracks Provides access to Spotify [track endpoints](https://developer.spotify.com/documentation/web-api/reference/tracks/)
 * @property episodes Provides access to Spotify [episode endpoints](https://developer.spotify.com/documentation/web-api/reference/episodes/)
 * @property shows Provides access to Spotify [show endpoints](https://developer.spotify.com/documentation/web-api/reference/shows/)
 * @property markets Provides access to Spotify [market endpoints](https://developer.spotify.com/documentation/web-api/reference/#category-markets)
 */
public sealed class SpotifyApi<T : SpotifyApi<T, B>, B : ISpotifyApiBuilder<T, B>>(
    public val clientId: String?,
    public val clientSecret: String?,
    public var token: Token,
    public var spotifyApiOptions: SpotifyApiOptions
) {
    public var useCache: Boolean = spotifyApiOptions.useCache
        set(value) {
            if (!value) clearCache()

            field = value
        }
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
    public abstract val episodes: EpisodeApi
    public abstract val shows: ShowApi
    public abstract val markets: MarketsApi

    /**
     * Base url for Spotify web api calls
     */
    internal val spotifyApiBase = "https://api.spotify.com/v1"

    internal val defaultEndpoint get() = tracks

    init {
        spotifyApiOptions.requiredScopes?.let { requiredScopes ->
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
    public fun getCache(): Map<SpotifyRequest, CacheState> =
        endpoints.map { it.cache.cachedRequests.asList() }.flatten().toMap()

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

    public fun getSpotifyPkceAuthorizationUrl(
        vararg scopes: SpotifyScope,
        redirectUri: String,
        codeChallenge: String,
        state: String? = null
    ): String {
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
        if (token.shouldRefresh()) {
            return TokenValidityResponse(
                false,
                SpotifyException.AuthenticationException("Token needs to be refreshed (is it expired?)")
            )
        }
        if (!makeTestRequest) return TokenValidityResponse(true, null)

        return try {
            browse.getAvailableGenreSeeds()
            TokenValidityResponse(true, null)
        } catch (e: Exception) {
            TokenValidityResponse(false, e)
        }
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
    public fun isTokenValidRestAction(makeTestRequest: Boolean = true): SpotifyRestAction<TokenValidityResponse> =
        SpotifyRestAction {
            isTokenValid(makeTestRequest)
        }

    /**
     * If the method used to create the [token] supports token refresh and
     * the information in [token] is accurate, attempt to refresh the token
     *
     * @return The old access token if refresh was successful
     * @throws BadRequestException if refresh fails
     * @throws IllegalStateException if [SpotifyApiOptions.refreshTokenProducer] is null
     */
    public suspend fun refreshToken(): Token {
        val oldToken = token
        val refreshedToken = spotifyApiOptions.refreshTokenProducer?.invoke(this)
            ?: throw SpotifyException.ReAuthenticationNeededException(IllegalStateException("The refreshTokenProducer is null."))

        token = refreshedToken
        // Spotify may not provide a new refresh token
        if (token.refreshToken == null) token.refreshToken = oldToken.refreshToken
        
        spotifyApiOptions.onTokenRefresh?.invoke(this@SpotifyApi)
        spotifyApiOptions.afterTokenRefresh?.invoke(this@SpotifyApi)
        
        return oldToken
    }

    /**
     * If the method used to create the [token] supports token refresh and
     * the information in [token] is accurate, attempt to refresh the token
     *
     * @return The old access token if refresh was successful
     * @throws BadRequestException if refresh fails
     * @throws IllegalStateException if [SpotifyApiOptions.refreshTokenProducer] is null
     */
    public fun refreshTokenRestAction(): SpotifyRestAction<Token> = SpotifyRestAction { refreshToken() }

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
                    if (scopes.isEmpty()) {
                        ""
                    } else {
                        "&scope=${scopes.joinToString("%20") { it.uri }}" +
                                if (shouldShowDialog) "&show_dialog=$shouldShowDialog" else ""
                    }
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
         * @param json The json instance that will deserialize the response.
         */
        public suspend fun getCredentialedToken(
            clientId: String,
            clientSecret: String,
            api: GenericSpotifyApi?,
            json: Json = api?.spotifyApiOptions?.json ?: Json.Default
        ): Token {
            val response = executeTokenRequest(
                HttpRequest(
                    "https://accounts.spotify.com/api/token",
                    HttpRequestMethod.POST,
                    mapOf("grant_type" to "client_credentials"),
                    null,
                    "application/x-www-form-urlencoded",
                    listOf(),
                    api
                ),
                clientId,
                clientSecret
            )

            if (response.responseCode / 200 == 1) return response.body.toObject(Token.serializer(), null, json)

            throw BadRequestException(response.body.toObject(AuthenticationError.serializer(), null, json))
        }

        /**
         *
         * Get an application token (can only access public methods) that can be used to instantiate a new [SpotifyAppApi]
         *
         * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
         * @param api The Spotify Api instance, or null if one doesn't exist yet
         * @param json The json instance that will deserialize the response.
         */
        public fun getCredentialedTokenRestAction(
            clientId: String,
            clientSecret: String,
            api: GenericSpotifyApi?,
            json: Json = api?.spotifyApiOptions?.json ?: Json.Default
        ): SpotifyRestAction<Token> = SpotifyRestAction { getCredentialedToken(clientId, clientSecret, api, json) }
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
    enableDefaultTokenRefreshProducerIfNoneExists: Boolean = true,
    spotifyApiOptions: SpotifyApiOptions
) : SpotifyApi<SpotifyAppApi, SpotifyAppApiBuilder>(
    clientId,
    clientSecret,
    token,
    spotifyApiOptions.apply {
        if (enableDefaultTokenRefreshProducerIfNoneExists && refreshTokenProducer == null) {
            refreshTokenProducer = defaultAppApiTokenRefreshProducer
        }
    }
) {
    override val search: SearchApi = SearchApi(this)
    override val albums: AlbumApi = AlbumApi(this)
    override val browse: BrowseApi = BrowseApi(this)
    override val artists: ArtistApi = ArtistApi(this)
    override val tracks: TrackApi = TrackApi(this)
    override val episodes: EpisodeApi = EpisodeApi(this)
    override val shows: ShowApi = ShowApi(this)
    override val markets: MarketsApi = MarketsApi(this)

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

            getCredentialedToken(api.clientId, api.clientSecret, api, api.spotifyApiOptions.json)
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
    public val usesPkceAuth: Boolean,
    enableDefaultTokenRefreshProducerIfNoneExists: Boolean,
    spotifyApiOptions: SpotifyApiOptions
) : SpotifyApi<SpotifyClientApi, SpotifyClientApiBuilder>(
    clientId,
    clientSecret,
    token,
    spotifyApiOptions.apply {
        if (enableDefaultTokenRefreshProducerIfNoneExists && refreshTokenProducer == null) {
            refreshTokenProducer = defaultClientApiTokenRefreshProducer
        }
    }
) {
    public constructor(
        clientId: String?,
        clientSecret: String?,
        token: Token,
        spotifyApiOptions: SpotifyApiOptions
    ) : this(
        clientId,
        clientSecret,
        null,
        token,
        false,
        false,
        spotifyApiOptions
    )

    override val albums: AlbumApi = AlbumApi(this)
    override val browse: BrowseApi = BrowseApi(this)
    override val artists: ArtistApi = ArtistApi(this)
    override val tracks: TrackApi = TrackApi(this)
    override val search: SearchApi = SearchApi(this)
    override val markets: MarketsApi = MarketsApi(this)

    override val episodes: ClientEpisodeApi = ClientEpisodeApi(this)
    override val shows: ClientShowApi = ClientShowApi(this)

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

    private var userIdBacking: String? = null

    private suspend fun initiatizeUserIdBacking(): String {
        userIdBacking = users.getClientProfile().id
        return userIdBacking!!
    }

    /**
     * The Spotify user id to which the api instance is connected
     */
    public suspend fun getUserId(): String =
        if (userIdBacking != null) userIdBacking!! else initiatizeUserIdBacking()

    /**
     * The Spotify user id to which the api instance is connected
     */
    public fun getUserIdRestAction(): SpotifyRestAction<String> = SpotifyRestAction { getUserId() }

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
     * Whether the current access token allows access to scope [scope]
     */
    public fun hasScopeRestAction(scope: SpotifyScope): SpotifyRestAction<Boolean?> =
        SpotifyRestAction { hasScope(scope) }

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    public suspend fun hasScopes(scope: SpotifyScope, vararg scopes: SpotifyScope): Boolean? =
        if (token.scopes == null) {
            null
        } else {
            isTokenValid(false).isValid &&
                    token.scopes?.contains(scope) == true &&
                    scopes.all { token.scopes?.contains(it) == true }
        }

    /**
     * Whether the current access token allows access to all of the provided scopes
     */
    public fun hasScopesRestAction(scope: SpotifyScope, vararg scopes: SpotifyScope): SpotifyRestAction<Boolean?> =
        SpotifyRestAction {
            hasScopes(scope, *scopes)
        }

    public companion object {
        private val defaultClientApiTokenRefreshProducer: suspend (GenericSpotifyApi) -> Token = { api ->
            api as SpotifyClientApi

            require(api.clientId != null) { "The client id is not set" }

            refreshSpotifyClientToken(api.clientId, api.clientSecret, api.token.refreshToken, api.usesPkceAuth)
        }
    }
}

/**
 * An API instance created through implicit grant flow, with access to private information
 * managed through the scopes exposed in [token]. [token] is not refreshable and is only accessible for limited time.
 */
public class SpotifyImplicitGrantApi(
    clientId: String?,
    token: Token,
    spotifyApiOptions: SpotifyApiOptions
) : SpotifyClientApi(
    clientId,
    null,
    token,
    spotifyApiOptions
)

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
 * @param json The json instance that will deserialize the response.
 */
@Deprecated("Moved", ReplaceWith("SpotifyApi.getCredentialedToken"))
public suspend fun getCredentialedToken(
    clientId: String,
    clientSecret: String,
    api: GenericSpotifyApi?,
    json: Json = api?.spotifyApiOptions?.json ?: Json.Default
): Token = SpotifyApi.getCredentialedToken(clientId, clientSecret, api, json)

internal suspend fun executeTokenRequest(
    httpRequest: HttpRequest,
    clientId: String,
    clientSecret: String
): HttpResponse {
    return httpRequest.execute(
        listOf(
            HttpHeader(
                "Authorization",
                "Basic ${"$clientId:$clientSecret".base64ByteEncode()}"
            )
        )
    )
}

/**
 * Refresh a Spotify client token
 *
 * @param clientId The Spotify application client id.
 * @param clientSecret The Spotify application client secret (not needed for PKCE).
 * @param refreshToken The refresh token.
 * @param usesPkceAuth Whether this token was created using PKCE auth or not.
 */
public suspend fun refreshSpotifyClientToken(
    clientId: String,
    clientSecret: String?,
    refreshToken: String?,
    usesPkceAuth: Boolean
): Token {
    fun getDefaultClientApiTokenBody(): Map<String, String?> {
        val map = mutableMapOf(
            "grant_type" to "refresh_token",
            "refresh_token" to refreshToken
        )

        if (usesPkceAuth) map += "client_id" to clientId

        return map
    }

    val response = if (!usesPkceAuth) {
        require(clientSecret != null) { "The client secret is not set" }
        executeTokenRequest(
            HttpRequest(
                "https://accounts.spotify.com/api/token",
                HttpRequestMethod.POST,
                getDefaultClientApiTokenBody(),
                null,
                "application/x-www-form-urlencoded",
                listOf(),
                null
            ),
            clientId,
            clientSecret
        )
    } else {
        HttpRequest(
            "https://accounts.spotify.com/api/token",
            HttpRequestMethod.POST,
            getDefaultClientApiTokenBody(),
            null,
            "application/x-www-form-urlencoded",
            listOf(),
            null
        ).execute()
    }

    return if (response.responseCode in 200..399) {
        response.body.toObject(Token.serializer(), null, nonstrictJson)
    } else {
        throw BadRequestException(
            response.body.toObject(
                AuthenticationError.serializer(),
                null,
                nonstrictJson
            )
        )
    }
}

/**
 * Refresh a Spotify client token
 *
 * @param clientId The Spotify application client id.
 * @param clientSecret The Spotify application client secret (not needed for PKCE).
 * @param refreshToken The refresh token.
 * @param usesPkceAuth Whether this token was created using PKCE auth or not.
 */
public fun refreshSpotifyClientTokenRestAction(
    clientId: String,
    clientSecret: String?,
    refreshToken: String?,
    usesPkceAuth: Boolean
): SpotifyRestAction<Token> =
    SpotifyRestAction { refreshSpotifyClientToken(clientId, clientSecret, refreshToken, usesPkceAuth) }
