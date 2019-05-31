/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.serialization.toObject

// Kotlin DSL builder
/**
 * A builder DSL to create a new [SpotifyAPI]
 */
fun spotifyApi(block: SpotifyApiBuilderDsl.() -> Unit) = SpotifyApiBuilderDsl().apply(block)

/**
 * Spotify traditional Java style API builder
 */
class SpotifyApiBuilder(
        val clientId: String,
        val clientSecret: String,
        var redirectUri: String? = null,
        var authorizationCode: String? = null,
        var tokenString: String? = null,
        var token: Token? = null,
        var useCache: Boolean = true,
        var cacheLimit: Int? = 1000,
        var automaticRefresh: Boolean = true,
        var retryWhenRateLimited: Boolean = false,
        var enableLogger: Boolean = false
) {
    /**
     * Instantiate the builder with the application [clientId] and [clientSecret]
     */
    constructor(clientId: String, clientSecret: String) : this(clientId, clientSecret, null)

    /**
     * Instantiate the builder with the application [clientId], [clientSecret], and whether to use a cache
     */
    constructor(clientId: String, clientSecret: String, useCache: Boolean) : this(clientId, clientSecret, null, useCache = useCache)

    /**
     * Instantiate the builder with the application [clientId], [clientSecret], and application
     * [redirectUri]
     */
    constructor(clientId: String, clientSecret: String, redirectUri: String) : this(clientId, clientSecret, redirectUri, null)

    /**
     * Instantiate the builder with the application [clientId], [clientSecret], application
     * [redirectUri], and an [authorizationCode]
     */
    constructor(clientId: String, clientSecret: String, redirectUri: String?, authorizationCode: String, useCache: Boolean) :
            this(clientId, clientSecret, redirectUri, authorizationCode, null, useCache = useCache)

    /**
     * Instantiate the builder with the application [clientId], [clientSecret], application
     * [redirectUri], and an access token string ([tokenString])
     */
    constructor(clientId: String, clientSecret: String, redirectUri: String?, tokenString: String) :
            this(clientId, clientSecret, redirectUri, null, tokenString)

    /**
     * Instantiate the builder with the application [clientId], [clientSecret], application
     * [redirectUri], and a [token]
     */
    constructor(clientId: String, clientSecret: String, redirectUri: String?, token: Token, useCache: Boolean) :
            this(clientId, clientSecret, redirectUri, null, null, token, useCache)

    /**
     * Set whether to cache requests. Default: true
     */
    fun useCache(useCache: Boolean) = apply { this.useCache = useCache }

    /**
     *  Set the maximum allowed amount of cached requests at one time. Null means no limit
     */
    fun cacheLimit(cacheLimit: Int?) = apply { this.cacheLimit = cacheLimit }

    /**
     * Set the application [redirect uri](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    fun redirectUri(redirectUri: String?) = apply { this.redirectUri = redirectUri }

    /**
     * Set a returned [authorization code](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    fun authorizationCode(authorizationCode: String?) = apply { this.authorizationCode = authorizationCode }

    /**
     * If you only have an access token, the api can be instantiated with it
     */
    fun tokenString(tokenString: String?) = apply { this.tokenString = tokenString }

    /**
     * Set the token to be used with this api instance
     */
    fun token(token: Token?) = apply { this.token = token }

    /**
     * Enable or disable automatic refresh of the Spotify access token
     */
    fun automaticRefresh(automaticRefresh: Boolean) = apply { this.automaticRefresh = automaticRefresh }

    /**
     * Set whether to block the current thread and wait until the API can retry the request
     */
    fun retryWhenRateLimited(retryWhenRateLimited: Boolean) = apply { this.retryWhenRateLimited = retryWhenRateLimited }

    /**
     * Set whether to enable to the exception logger
     */
    fun enableLogger(enableLogger: Boolean) = apply { this.enableLogger = enableLogger }

    /**
     * Create a [SpotifyAPI] instance with the given [SpotifyApiBuilder] parameters and the type -
     * [AuthorizationType.CLIENT] for client authentication, or otherwise [AuthorizationType.APPLICATION]
     */
    fun build(type: AuthorizationType): SpotifyAPI {
        return if (type == AuthorizationType.CLIENT) buildClient()
        else buildCredentialed()
    }

    /**
     * Create a new [SpotifyAppAPI] that only has access to *public* endpoints and data
     */
    fun buildPublic() = buildCredentialed()

    /**
     * Create a new [SpotifyAppAPI] that only has access to *public* endpoints and data
     */
    fun buildCredentialed(): SpotifyAPI = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
        }
        authentication {
            token = this@SpotifyApiBuilder.token
            tokenString = this@SpotifyApiBuilder.tokenString
        }

        automaticRefresh = this@SpotifyApiBuilder.automaticRefresh
        retryWhenRateLimited = this@SpotifyApiBuilder.retryWhenRateLimited
        enableLogger = this@SpotifyApiBuilder.enableLogger
    }.buildCredentialed()

    /**
     * Create a new [SpotifyClientAPI] that has access to public endpoints, in addition to endpoints
     * requiring scopes contained in the client authorization request
     */
    fun buildClient(): SpotifyClientAPI = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
            redirectUri = this@SpotifyApiBuilder.redirectUri
        }
        authentication {
            authorizationCode = this@SpotifyApiBuilder.authorizationCode
            tokenString = this@SpotifyApiBuilder.tokenString
            token = this@SpotifyApiBuilder.token
        }

        automaticRefresh = this@SpotifyApiBuilder.automaticRefresh
        retryWhenRateLimited = this@SpotifyApiBuilder.retryWhenRateLimited
        enableLogger = this@SpotifyApiBuilder.enableLogger
    }.buildClient()
}

/**
 * A holder for application-specific credentials
 *
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

/**
 * Spotify API mutable parameters
 *
 * @property credentials A holder for application-specific credentials
 * @property authentication A holder for authentication methods. At least one needs to be provided in order to create
 * a **client** api
 * @property useCache Set whether to cache requests. Default: true
 * @property cacheLimit The maximum amount of cached requests allowed at one time. Null means no limit
 * @property automaticRefresh Enable or disable automatic refresh of the Spotify access token
 * @property retryWhenRateLimited Set whether to block the current thread and wait until the API can retry the request
 * @property enableLogger Set whether to enable to the exception logger
 */
class SpotifyApiBuilderDsl {
    private var credentials: SpotifyCredentials = SpotifyCredentials(null, null, null)
    private var authentication = SpotifyUserAuthorizationBuilder()
    var useCache: Boolean = true
    var cacheLimit: Int? = 1000
    var automaticRefresh: Boolean = true
    var retryWhenRateLimited: Boolean = false
    var enableLogger: Boolean = false

    /**
     * A block in which Spotify application credentials (accessible via the Spotify [dashboard](https://developer.spotify.com/dashboard/applications))
     * should be put
     */
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

    /**
     * Create a Spotify authorization URL from which client access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     *
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        if (credentials.redirectUri == null || credentials.clientId == null) {
            throw IllegalArgumentException("You didn't specify a redirect uri or client id in the credentials block!")
        }
        return getAuthUrlFull(*scopes, clientId = credentials.clientId!!, redirectUri = credentials.redirectUri!!)
    }

    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     *
     * Provide a consumer object to be executed after the api has been successfully built
     */
    fun buildCredentialedAsync(consumer: (SpotifyAPI) -> Unit) = Runnable { consumer(buildCredentialed()) }.run()

    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     */
    fun buildCredentialed(): SpotifyAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        if ((clientId == null || clientSecret == null) && (authentication.token == null && authentication.tokenString == null)) {
            throw IllegalArgumentException("You didn't specify a client id or client secret in the credentials block!")
        }
        return when {
            authentication.token != null -> {
                SpotifyAppAPI(clientId ?: "not-set", clientSecret
                        ?: "not-set", authentication.token!!, useCache, cacheLimit, false, retryWhenRateLimited, enableLogger)
            }
            authentication.tokenString != null -> {
                SpotifyAppAPI(
                        clientId ?: "not-set",
                        clientSecret ?: "not-set",
                        Token(
                                authentication.tokenString!!, "client_credentials",
                                60000, null, null
                        ),
                        useCache,
                        cacheLimit,
                        automaticRefresh,
                        retryWhenRateLimited,
                        enableLogger
                )
            }
            else -> try {
                if (clientId == null || clientSecret == null) throw IllegalArgumentException("Illegal credentials provided")
                val token = getCredentialedToken(clientId, clientSecret, null)
                SpotifyAppAPI(clientId, clientSecret, token, useCache, cacheLimit, automaticRefresh, retryWhenRateLimited, enableLogger)
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
        }
    }

    /**
     * Build the client api using a provided authorization code, token string, or token object (only one of which
     * is necessary)
     *
     * Provide a consumer object to be executed after the client has been successfully built
     */
    fun buildClientAsync(consumer: (SpotifyClientAPI) -> Unit) =
            Runnable { consumer(buildClient()) }.run()

    /**
     * Build the client api using a provided authorization code, token string, or token object (only one of which
     * is necessary)
     */
    fun buildClient(): SpotifyClientAPI =
            buildClient(
                    authentication.authorizationCode, authentication.tokenString,
                    authentication.token
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
            token: Token? = null
    ): SpotifyClientAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        if ((clientId == null || clientSecret == null || redirectUri == null) && (token == null && tokenString == null)) {
            throw IllegalArgumentException("You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!")
        }
        return when {
            authorizationCode != null -> try {
                clientId ?: throw IllegalArgumentException()
                clientSecret ?: throw IllegalArgumentException()
                redirectUri ?: IllegalArgumentException()

                val response = executeTokenRequest(HttpConnection(
                        url = "https://accounts.spotify.com/api/token",
                        method = HttpRequestMethod.POST,
                        bodyMap = null,
                        bodyString = "grant_type=authorization_code&code=$authorizationCode&redirect_uri=$redirectUri",
                        contentType = "application/x-www-form-urlencoded",
                        api = null
                ), clientId, clientSecret)

                SpotifyClientAPI(
                        clientId,
                        clientSecret,
                        response.body.toObject(null),
                        automaticRefresh,
                        redirectUri ?: throw IllegalArgumentException(),
                        useCache,
                        cacheLimit,
                        retryWhenRateLimited,
                        enableLogger
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
                    useCache,
                    cacheLimit,
                    retryWhenRateLimited,
                    enableLogger
            )
            tokenString != null -> SpotifyClientAPI(
                    clientId ?: "not-set",
                    clientSecret ?: "not-set",
                    Token(
                            tokenString,
                            "client_credentials",
                            1000,
                            null,
                            null
                    ),
                    false,
                    redirectUri ?: "not-set",
                    useCache,
                    cacheLimit,
                    retryWhenRateLimited,
                    enableLogger
            )
            else -> throw IllegalArgumentException(
                    "At least one of: authorizationCode, tokenString, or token must be provided " +
                            "to build a SpotifyClientAPI object"
            )
        }
    }
}

enum class AuthorizationType {
    CLIENT, APPLICATION
}