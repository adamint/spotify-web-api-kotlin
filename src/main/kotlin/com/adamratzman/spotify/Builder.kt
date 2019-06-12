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
    private var clientId: String,
    private var clientSecret: String
) {
    private var redirectUri: String? = null
    private var authorizationCode: String? = null
    private var tokenString: String? = null
    private var token: Token? = null
    private var useCache: Boolean = true
    private var cacheLimit: Int? = 200
    private var automaticRefresh: Boolean = true
    private var retryWhenRateLimited: Boolean = false
    private var enableLogger: Boolean = false
    private var testTokenValidity: Boolean = false
    private var enableAllUtilities: Boolean = false

    /**
     * Set whether to enable all utilities ([automaticRefresh], [retryWhenRateLimited], [useCache], [enableLogger], [testTokenValidity])
     */
    fun enableAllUtilities(enableAllUtilities: Boolean) = apply { this.enableAllUtilities = enableAllUtilities }

    /**
     * After API creation, set whether to test whether the token is valid by performing a lightweight request
     */
    fun testTokenValidity(testTokenValidity: Boolean) = apply { this.testTokenValidity = testTokenValidity }

    /**
     * Set the application client id
     */
    fun clientId(clientId: String) = apply { this.clientId = clientId }

    /**
     * Set the application client secret
     */
    fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

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
        utilities {
            useCache = this@SpotifyApiBuilder.useCache
            cacheLimit = this@SpotifyApiBuilder.cacheLimit
            automaticRefresh = this@SpotifyApiBuilder.automaticRefresh
            retryWhenRateLimited = this@SpotifyApiBuilder.retryWhenRateLimited
            enableLogger = this@SpotifyApiBuilder.enableLogger
            testTokenValidity = this@SpotifyApiBuilder.testTokenValidity
            enableAllUtilities = this@SpotifyApiBuilder.enableAllUtilities
        }
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
        utilities {
            useCache = this@SpotifyApiBuilder.useCache
            cacheLimit = this@SpotifyApiBuilder.cacheLimit
            automaticRefresh = this@SpotifyApiBuilder.automaticRefresh
            retryWhenRateLimited = this@SpotifyApiBuilder.retryWhenRateLimited
            enableLogger = this@SpotifyApiBuilder.enableLogger
            testTokenValidity = this@SpotifyApiBuilder.testTokenValidity
            enableAllUtilities = this@SpotifyApiBuilder.enableAllUtilities
        }
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
 * Authentication methods
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
) {
    fun build() = SpotifyUserAuthorization(authorizationCode, tokenString, token)
}

data class SpotifyUserAuthorization(
    val authorizationCode: String?,
    val tokenString: String?,
    val token: Token?
)

/**
 * API Utilities
 *
 * @property useCache Set whether to cache requests. Default: true
 * @property cacheLimit The maximum amount of cached requests allowed at one time. Null means no limit
 * @property automaticRefresh Enable or disable automatic refresh of the Spotify access token
 * @property retryWhenRateLimited Set whether to block the current thread and wait until the API can retry the request
 * @property enableLogger Set whether to enable to the exception logger
 * @property testTokenValidity After API creation, test whether the token is valid by performing a lightweight request
 * @property enableAllUtilities Whether to enable all provided utilities
 */
class SpotifyUtilitiesBuilder(
    var useCache: Boolean = true,
    var cacheLimit: Int? = 200,
    var automaticRefresh: Boolean = true,
    var retryWhenRateLimited: Boolean = true,
    var enableLogger: Boolean = true,
    var testTokenValidity: Boolean = false,
    var enableAllUtilities: Boolean = false
) {
    fun build() =
            if (enableAllUtilities)
                SpotifyUtilities(true,
                        200,
                        automaticRefresh = true,
                        retryWhenRateLimited = true,
                        enableLogger = true,
                        testTokenValidity = true
                )
            else
                SpotifyUtilities(
                        useCache,
                        cacheLimit,
                        automaticRefresh,
                        retryWhenRateLimited,
                        enableLogger,
                        testTokenValidity
                )
}

data class SpotifyUtilities(
    val useCache: Boolean,
    val cacheLimit: Int?,
    val automaticRefresh: Boolean,
    val retryWhenRateLimited: Boolean,
    val enableLogger: Boolean,
    val testTokenValidity: Boolean
)

/**
 * Spotify API mutable parameters
 *
 * @property credentials A holder for application-specific credentials
 * @property authentication A holder for authentication methods. At least one needs to be provided in order to create
 * a **client** api
 * @property utilities A holder for API utilities such as caching and token refresh
 */
class SpotifyApiBuilderDsl {
    private var credentials: SpotifyCredentials = SpotifyCredentialsBuilder().build()
    private var authentication: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build()
    private var utilities: SpotifyUtilities = SpotifyUtilitiesBuilder().build()

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
        authentication = SpotifyUserAuthorizationBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun utilities(block: SpotifyUtilitiesBuilder.() -> Unit) {
        utilities = SpotifyUtilitiesBuilder().apply(block).build()
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
                SpotifyAppAPI(
                        clientId ?: "not-set",
                        clientSecret ?: "not-set",
                        authentication.token!!,
                        utilities.useCache,
                        utilities.cacheLimit,
                        false,
                        utilities.retryWhenRateLimited,
                        utilities.enableLogger,
                        utilities.testTokenValidity
                )
            }
            authentication.tokenString != null -> {
                SpotifyAppAPI(
                        clientId ?: "not-set",
                        clientSecret ?: "not-set",
                        Token(
                                authentication.tokenString!!, "client_credentials",
                                60000, null, null
                        ),
                        utilities.useCache,
                        utilities.cacheLimit,
                        false,
                        utilities.retryWhenRateLimited,
                        utilities.enableLogger,
                        utilities.testTokenValidity
                )
            }
            else -> try {
                if (clientId == null || clientSecret == null) throw IllegalArgumentException("Illegal credentials provided")
                val token = getCredentialedToken(clientId, clientSecret, null)
                SpotifyAppAPI(
                        clientId,
                        clientSecret,
                        token,
                        utilities.useCache,
                        utilities.cacheLimit,
                        false,
                        utilities.retryWhenRateLimited,
                        utilities.enableLogger,
                        utilities.testTokenValidity
                )
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
                    authentication.authorizationCode,
                    authentication.tokenString,
                    authentication.token
            )

    /**
     * Build the client api by providing an authorization code, token string, or token object. Only one of the following
     * needs to be provided
     *
     * @param authorizationCode Spotify authorization code retrieved after authentication
     * @param tokenString Spotify authorization token
     * @param token [Token] object (useful if you already have exchanged an authorization code yourself
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
                        utilities.automaticRefresh,
                        redirectUri ?: throw IllegalArgumentException("No redirect uri provided"),
                        utilities.useCache,
                        utilities.cacheLimit,
                        utilities.retryWhenRateLimited,
                        utilities.enableLogger,
                        utilities.testTokenValidity
                )
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
            token != null -> SpotifyClientAPI(
                    clientId ?: "not-set",
                    clientSecret ?: "not-set",
                    token,
                    utilities.automaticRefresh,
                    redirectUri ?: "not-set",
                    utilities.useCache,
                    utilities.cacheLimit,
                    utilities.retryWhenRateLimited,
                    utilities.enableLogger,
                    utilities.testTokenValidity
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
                    utilities.useCache,
                    utilities.cacheLimit,
                    utilities.retryWhenRateLimited,
                    utilities.enableLogger,
                    utilities.testTokenValidity
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