/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

// Kotlin DSL builders

fun spotifyAppApi(block: SpotifyAppApiBuilder.() -> Unit) = SpotifyAppApiBuilder().apply(block)
fun spotifyClientApi(block: SpotifyClientApiBuilder.() -> Unit) = SpotifyClientApiBuilder().apply(block)

/**
 *  Spotify API builder
 */
class SpotifyApiBuilder(
    private var clientId: String,
    private var clientSecret: String,
    private var redirectUri: String?
) {
    var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build()
    var options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()

    /**
     * Set whether to enable all utilities ([automaticRefresh], [retryWhenRateLimited], [useCache], [enableLogger], [testTokenValidity])
     */
    fun enableAllOptions() = apply { this.options = SpotifyApiOptionsBuilder(enableAllOptions = true).build() }

    /**
     * After API creation, set whether to test whether the token is valid by performing a lightweight request
     */
    fun testTokenValidity(testTokenValidity: Boolean) = apply { this.options.testTokenValidity = testTokenValidity }

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
    fun useCache(useCache: Boolean) = apply { this.options.useCache = useCache }

    /**
     *  Set the maximum allowed amount of cached requests at one time. Null means no limit
     */
    fun cacheLimit(cacheLimit: Int?) = apply { this.options.cacheLimit = cacheLimit }

    /**
     * Set the application [redirect uri](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    fun redirectUri(redirectUri: String?) = apply { this.redirectUri = redirectUri }

    /**
     * Set a returned [authorization code](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    fun authorizationCode(authorizationCode: String?) =
        apply { this.authorization.authorizationCode = authorizationCode }

    /**
     * If you only have an access token, the api can be instantiated with it
     */
    fun tokenString(tokenString: String?) = apply { this.authorization.tokenString = tokenString }

    /**
     * Set the token to be used with this api instance
     */
    fun token(token: Token?) = apply { this.authorization.token = token }

    /**
     * Enable or disable automatic refresh of the Spotify access token
     */
    fun automaticRefresh(automaticRefresh: Boolean) = apply { this.options.automaticRefresh = automaticRefresh }

    /**
     * Set whether to block the current thread and wait until the API can retry the request
     */
    fun retryWhenRateLimited(retryWhenRateLimited: Boolean) =
        apply { this.options.retryWhenRateLimited = retryWhenRateLimited }

    /**
     * Set whether to enable to the exception logger
     */
    fun enableLogger(enableLogger: Boolean) = apply { this.options.enableLogger = enableLogger }

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
    fun buildCredentialed(): SpotifyAPI = spotifyAppApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
        }
        authorization(authorization)
        options(options)
    }.build()

    /**
     * Create a new [SpotifyClientAPI] that has access to public endpoints, in addition to endpoints
     * requiring scopes contained in the client authorization request
     */
    fun buildClient(): SpotifyClientAPI = spotifyClientApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
            redirectUri = this@SpotifyApiBuilder.redirectUri
        }
        authorization(authorization)
        options(options)
    }.build()
}

enum class AuthorizationType {
    CLIENT,
    APPLICATION;
}

interface ISpotifyApiBuilder {
    var credentials: SpotifyCredentials
    var authorization: SpotifyUserAuthorization
    var options: SpotifyApiOptions

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
    fun authorization(block: SpotifyUserAuthorizationBuilder.() -> Unit) {
        authorization = SpotifyUserAuthorizationBuilder().apply(block).build()
    }

    /**
     * Allows you to authenticate a [SpotifyClientAPI] with an authorization code
     * or build [SpotifyAPI] using a refresh token
     */
    fun authentication(block: SpotifyUserAuthorizationBuilder.() -> Unit) {
        authorization = SpotifyUserAuthorizationBuilder().apply(block).build()
    }

    fun authorization(authorization: SpotifyUserAuthorization) = apply { this.authorization = authorization }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun utilities(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun config(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun options(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    fun options(options: SpotifyApiOptions) = apply { this.options = options }
}

interface ISpotifyClientApiBuilder : ISpotifyApiBuilder {
    /**
     * Build the client api by providing an authorization code, token string, or token object. Only one of the following
     * needs to be provided
     *
     * @param authorizationCode Spotify authorization code retrieved after authentication
     * @param tokenString Spotify authorization token
     * @param token [Token] object (useful if you already have exchanged an authorization code yourself
     */
    fun build(): SpotifyClientAPI

    /**
     * Build the client api using a provided authorization code, token string, or token object (only one of which
     * is necessary)
     *
     * Provide a consumer object to be executed after the client has been successfully built
     */
    suspend fun buildAsync(consumer: (SpotifyClientAPI) -> Unit)

    /**
     * Create a Spotify authorization URL from which API access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String
}

class SpotifyClientApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentialsBuilder().build(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build(),
    override var options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
) : ISpotifyClientApiBuilder {
    override fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        if (credentials.redirectUri == null || credentials.clientId == null) {
            throw IllegalArgumentException("You didn't specify a redirect uri or client id in the credentials block!")
        }
        return getAuthUrlFull(*scopes, clientId = credentials.clientId!!, redirectUri = credentials.redirectUri!!)
    }

    override fun build(): SpotifyClientAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        if ((clientId == null || clientSecret == null || redirectUri == null) && (authorization.token == null && authorization.tokenString == null)) {
            throw IllegalArgumentException("You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!")
        }
        return when {
            authorization.authorizationCode != null -> try {
                clientId ?: throw IllegalArgumentException()
                clientSecret ?: throw IllegalArgumentException()
                redirectUri ?: throw IllegalArgumentException()

                val response = executeTokenRequest(
                    HttpConnection(
                        "https://accounts.spotify.com/api/token",
                        HttpRequestMethod.POST,
                        mapOf(
                            "grant_type" to "authorization_code",
                            "code" to authorization.authorizationCode,
                            "redirect_uri" to redirectUri
                        ),
                        null,
                        "application/x-www-form-urlencoded",
                        listOf()
                    ), clientId, clientSecret
                )

                SpotifyClientAPI(
                    clientId,
                    clientSecret,
                    redirectUri,
                    response.body.toObject(null),
                    options.useCache,
                    options.cacheLimit,
                    options.automaticRefresh,
                    options.retryWhenRateLimited,
                    options.enableLogger,
                    options.testTokenValidity
                )
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
            authorization.token != null -> SpotifyClientAPI(
                clientId ?: "",
                clientSecret ?: "",
                redirectUri ?: "",
                authorization.token!!,
                options.useCache,
                options.cacheLimit,
                options.automaticRefresh,
                options.retryWhenRateLimited,
                options.enableLogger,
                options.testTokenValidity
            )
            authorization.tokenString != null -> SpotifyClientAPI(
                clientId ?: "",
                clientSecret ?: "",
                redirectUri ?: "",
                Token(
                    authorization.tokenString!!,
                    "client_credentials",
                    1000,
                    null,
                    null
                ),
                options.useCache,
                options.cacheLimit,
                false,
                options.retryWhenRateLimited,
                options.enableLogger,
                options.testTokenValidity
            )
            else -> throw IllegalArgumentException(
                "At least one of: authorizationCode, tokenString, or token must be provided " +
                        "to build a SpotifyClientAPI object"
            )
        }
    }

    override suspend fun buildAsync(consumer: (SpotifyClientAPI) -> Unit) = coroutineScope {
        withContext(Dispatchers.Default) { consumer(build()) }
    }
}

interface ISpotifyAppApiBuilder : ISpotifyApiBuilder {
    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     *
     * @param consumer Consumer to be executed after the api has been successfully built
     */
    suspend fun buildAsync(consumer: (SpotifyAPI) -> Unit)

    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     */
    fun build(): SpotifyAPI
}

class SpotifyAppApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentialsBuilder().build(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build(),
    override var options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
) : ISpotifyAppApiBuilder {
    /**
     * Create a new [SpotifyAppAPI] that only has access to *public* endpoints and data
     */
    fun buildPublic() = build()

    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     *
     * Provide a consumer object to be executed after the api has been successfully built
     */
    override suspend fun buildAsync(consumer: (SpotifyAPI) -> Unit) = coroutineScope {
        withContext(Dispatchers.Default) { consumer(build()) }
    }

    /**
     * Build a public [SpotifyAppAPI] using the provided credentials
     */
    override fun build(): SpotifyAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        if ((clientId == null || clientSecret == null) && (authorization.token == null && authorization.tokenString == null)) {
            throw IllegalArgumentException("You didn't specify a client id or client secret in the credentials block!")
        }
        return when {
            authorization.token != null -> {
                SpotifyAppAPI(
                    clientId ?: "not-set",
                    clientSecret ?: "not-set",
                    authorization.token!!,
                    options.useCache,
                    options.cacheLimit,
                    false,
                    options.retryWhenRateLimited,
                    options.enableLogger,
                    options.testTokenValidity
                )
            }
            authorization.tokenString != null -> {
                SpotifyAppAPI(
                    clientId ?: "not-set",
                    clientSecret ?: "not-set",
                    Token(
                        authorization.tokenString!!, "client_credentials",
                        60000, null, null
                    ),
                    options.useCache,
                    options.cacheLimit,
                    false,
                    options.retryWhenRateLimited,
                    options.enableLogger,
                    options.testTokenValidity
                )
            }
            else -> try {
                if (clientId == null || clientSecret == null) throw IllegalArgumentException("Illegal credentials provided")
                val token = getCredentialedToken(clientId, clientSecret, null)
                SpotifyAppAPI(
                    clientId,
                    clientSecret,
                    token,
                    options.useCache,
                    options.cacheLimit,
                    false,
                    options.retryWhenRateLimited,
                    options.enableLogger,
                    options.testTokenValidity
                )
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
        }
    }
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
    var authorizationCode: String?,
    var tokenString: String?,
    var token: Token?
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
 * @property enableAllOptions Whether to enable all provided utilities
 */
class SpotifyApiOptionsBuilder(
    var useCache: Boolean = true,
    var cacheLimit: Int? = 200,
    var automaticRefresh: Boolean = true,
    var retryWhenRateLimited: Boolean = true,
    var enableLogger: Boolean = true,
    var testTokenValidity: Boolean = false,
    var enableAllOptions: Boolean = false
) {
    fun build() =
        if (enableAllOptions)
            SpotifyApiOptions(
                true,
                200,
                automaticRefresh = false,
                retryWhenRateLimited = true,
                enableLogger = true,
                testTokenValidity = true
            )
        else
            SpotifyApiOptions(
                useCache,
                cacheLimit,
                automaticRefresh,
                retryWhenRateLimited,
                enableLogger,
                testTokenValidity
            )
}

data class SpotifyApiOptions(
    var useCache: Boolean,
    var cacheLimit: Int?,
    var automaticRefresh: Boolean,
    var retryWhenRateLimited: Boolean,
    var enableLogger: Boolean,
    var testTokenValidity: Boolean
)

typealias SpotifyUtilities = SpotifyApiOptions
typealias SpotifyUtilitiesBuilder = SpotifyApiOptionsBuilder