/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyApi.Companion.getCredentialedToken
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.serialization.nonstrictJson
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.runBlocking
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

// Kotlin DSL builders and top-level utilities

// ==============================================

// Get Spotify client authorization url
/**
 * Get the authorization url for the provided [clientId] and [redirectUri] application settings, when attempting to authorize with
 * specified [scopes]
 *
 * @param scopes Spotify scopes the api instance should be able to access for the user
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param isImplicitGrantFlow Whether the authorization url should be for the Implicit Grant flow, otherwise for Authorization Code flo
 * @param shouldShowDialog If [isImplicitGrantFlow] is true, whether or not to force the user to approve the app again if they’ve already done so.
 * @param state This provides protection against attacks such as cross-site request forgery.
 */
fun getSpotifyAuthorizationUrl(
    vararg scopes: SpotifyScope,
    clientId: String,
    redirectUri: String,
    isImplicitGrantFlow: Boolean = false,
    shouldShowDialog: Boolean = false,
    state: String? = null
): String {
    return SpotifyApi.getAuthUrlFull(
            *scopes,
            clientId = clientId,
            redirectUri = redirectUri,
            isImplicitGrantFlow = isImplicitGrantFlow,
            shouldShowDialog = shouldShowDialog,
            state = state
    )
}

/**
 * Get the PKCE authorization url for the provided [clientId] and [redirectUri] application settings, when attempting to authorize with
 * specified [scopes]
 *
 * @param scopes Spotify scopes the api instance should be able to access for the user
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param state This provides protection against attacks such as cross-site request forgery.
 * @param codeChallenge In order to generate the code challenge, your app should hash the code verifier using the SHA256 algorithm.
 * Then, base64url encode the hash that you generated.
 *
 */
fun getPkceAuthorizationUrl(
    vararg scopes: SpotifyScope,
    clientId: String,
    redirectUri: String,
    codeChallenge: String,
    state: String? = null
): String {
    return SpotifyApi.getPkceAuthUrlFull(
            *scopes,
            clientId = clientId,
            redirectUri = redirectUri,
            codeChallenge = codeChallenge,
            state = state
    )
}

/**
 * A utility to get the pkce code challenge for a corresponding code verifier. Only available on JVM/Android
 */
expect fun getSpotifyPkceCodeChallenge(codeVerifier: String): String

// ==============================================

// Implicit grant builder
/*
 ____________________________
/ This is Implicit Grant     \
\ authorization              /
 ----------------------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
 */

/**
 * Instantiate a new [SpotifyImplicitGrantApi] using a Spotify [clientId], [redirectUri], and [token] retrieved from the implicit
 * grant flow
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param token Token created from the hash response in the implicit grant callback
 * @param options Override default API options such as the cache limit
 *
 * @return [SpotifyImplicitGrantApi] that can immediately begin making calls
 */
fun spotifyImplicitGrantApi(
    clientId: String?,
    redirectUri: String?,
    token: Token,
    options: SpotifyApiOptionsBuilder = SpotifyApiOptionsBuilder()
) = SpotifyImplicitGrantApi(
                clientId,
                null,
                redirectUri,
                token,
                options.useCache,
                options.cacheLimit,
                options.retryWhenRateLimited,
                options.enableLogger,
                options.testTokenValidity,
                options.defaultLimit,
                options.allowBulkRequests,
                options.requestTimeoutMillis,
                options.json
        )

// App Api builders

/*
 ____________________________
/ This is Client Credentials \
\ authorization              /
 ----------------------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
 */

/**
 * Instantiate a new [SpotifyAppApiBuilder] using a Spotify [clientId] and [clientSecret]
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param options Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
 */
fun spotifyAppApi(
    clientId: String,
    clientSecret: String,
    options: SpotifyApiOptionsBuilder? = null
) = spotifyAppApi(clientId, clientSecret) {
    options?.let { this.options = it.build() }
}

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
fun spotifyAppApi(
    clientId: String,
    clientSecret: String,
    block: SpotifyAppApiBuilder.() -> Unit
) = SpotifyAppApiBuilder().apply(block).apply {
            credentials {
                this.clientId = clientId
                this.clientSecret = clientSecret
            }
        }

/**
 * Instantiate a new [SpotifyAppApiBuilder] using a [Token]
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param apiToken The access token that will be associated with the built API instance
 * @param options Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
 */
fun spotifyAppApi(
    clientId: String?,
    clientSecret: String?,
    apiToken: Token,
    options: SpotifyApiOptionsBuilder? = null
) = SpotifyAppApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }
    authentication {
        token = apiToken
    }

    options?.let { this.options = it.build() }
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

// Client Api Builders
/*
 ____________________________
/ This is Authorization Code \
\ authorization              /
 ----------------------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
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
 * Instantiate a new [SpotifyClientApiBuilder] using a [Token]
 *
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param apiToken The access token that will be associated with the built API instance
 * @param options Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
fun spotifyClientApi(
    clientId: String?,
    clientSecret: String?,
    redirectUri: String?,
    apiToken: Token,
    options: SpotifyApiOptionsBuilder? = null
) = SpotifyClientApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
    }

    authentication {
        token = apiToken
    }

    options?.let { this.options = it.build() }
}

/**
 * Instantiate a new [SpotifyClientApiBuilder] using an [authCode]
 *
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authCode The authorization code retrieved after OAuth authorization
 * @param options Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
fun spotifyClientApi(
    clientId: String?,
    clientSecret: String?,
    redirectUri: String?,
    authCode: String,
    options: SpotifyApiOptionsBuilder? = null
) = SpotifyClientApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
    }

    authentication {
        authorizationCode = authCode
    }

    options?.let { this.options = it.build() }
}

/**
 * Instantiate a new [SpotifyClientApiBuilder] using a Spotify [clientId], [clientSecret], and [redirectUri],
 * with an existing [SpotifyUserAuthorization] and with the ability to configure the api settings by providing a
 * builder initialization [block]
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authorization A [SpotifyUserAuthorization] that must contain one of the following: authorization code (preferred),
 * access token string (tokenString), [Token] object, **and** that may contain a refresh token (preferred)
 * with which to refresh the access token
 * @param options Override default API options such as the cache limit
 * @param block Api settings block
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
fun spotifyClientApi(
    clientId: String,
    clientSecret: String,
    redirectUri: String,
    authorization: SpotifyUserAuthorization,
    options: SpotifyApiOptionsBuilder? = null,
    block: SpotifyClientApiBuilder.() -> Unit = {}
) = SpotifyClientApiBuilder().apply(block).apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
    }
    options?.let { this.options = options.build() }
    this.authorization = authorization
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

// PKCE Client Api Builders
/*
 ____________________________
/ This is Authorization Code \
\ authorization (PKCE)       /
 ----------------------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
 */

/**
 * Instantiate a new [SpotifyClientApiBuilder] using an [authCode] and [codeVerifier]. This is for **PKCE authorization**.
 *
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authCode The authorization code retrieved after OAuth authorization
 * @param codeVerifier Your code verifier plaintext.
 * @param options Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
fun spotifyClientPkceApi(
    clientId: String?,
    redirectUri: String?,
    authCode: String,
    codeVerifier: String,
    options: SpotifyApiOptionsBuilder? = null
) = SpotifyClientApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.redirectUri = redirectUri
    }

    authentication {
        authorizationCode = authCode
        pkceCodeVerifier = codeVerifier
    }

    options?.let { this.options = it.build() }
}

/**
 *  Spotify API builder
 */
class SpotifyApiBuilder(
    private var clientId: String?,
    private var clientSecret: String?,
    private var redirectUri: String?
) {
    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build()

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
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
     * Allows you to set the default amount of objects to retrieve in one request
     */
    fun defaultLimit(defaultLimit: Int) = apply { this.options.defaultLimit = defaultLimit }

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
     * Set the maximum time, in milliseconds, before terminating an http request
     */
    fun requestTimeoutMillis(requestTimeoutMillis: Long?) = apply { this.options.requestTimeoutMillis = requestTimeoutMillis }

    /**
     * Set whether you want to allow splitting too-large requests into smaller, allowable api requests
     */
    fun allowBulkRequests(allowBulkRequests: Boolean) = apply { this.options.allowBulkRequests = allowBulkRequests }

    /**
     * Create a [SpotifyApi] instance with the given [SpotifyApiBuilder] parameters and the type -
     * [AuthorizationType.CLIENT] for client authentication, or otherwise [AuthorizationType.APPLICATION]
     */
    fun build(type: AuthorizationType): GenericSpotifyApi {
        return if (type == AuthorizationType.CLIENT) buildClient()
        else buildCredentialed()
    }

    /**
     * Create a new [SpotifyAppApi] that only has access to *public* endpoints and data
     */
    fun buildPublic() = buildCredentialed()

    /**
     * Create a new [SpotifyAppApi] that only has access to *public* endpoints and data
     */
    fun buildCredentialed(): SpotifyAppApi = spotifyAppApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
        }
        authorization(authorization)
        options(options)
    }.build()

    /**
     * Create a new [SpotifyClientApi] that has access to public endpoints, in addition to endpoints
     * requiring scopes contained in the client authorization request
     */
    fun buildClient(): SpotifyClientApi = spotifyClientApi {
        credentials {
            clientId = this@SpotifyApiBuilder.clientId
            clientSecret = this@SpotifyApiBuilder.clientSecret
            redirectUri = this@SpotifyApiBuilder.redirectUri
        }
        authorization(authorization)
        options(options)
    }.build()
}

/**
 * The type of Spotify authorization used to build an Api instance
 */
enum class AuthorizationType {
    /**
     * Authorization through explicit affirmative action taken by a client (user) allowing the application to access a/multiple [SpotifyScope]
     *
     * [Spotify application settings page](https://developer.spotify.com/documentation/general/guides/app-settings/)
     */
    CLIENT,

    /**
     * Authorization through application client id and secret, allowing access only to public endpoints and data
     *
     * [Spotify application settings page](https://developer.spotify.com/documentation/general/guides/app-settings/)
     */
    APPLICATION;
}

/**
 * Spotify Api builder interface
 *
 * @property T The type of [SpotifyApi] to be built
 * @property B The associated Api builder for [T]
 */
interface ISpotifyApiBuilder<T : SpotifyApi<T, B>, B : ISpotifyApiBuilder<T, B>> {
    /**
     * A block in which Spotify application credentials (accessible via the Spotify [dashboard](https://developer.spotify.com/dashboard/applications))
     * should be put
     */
    var credentials: SpotifyCredentials

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    var authorization: SpotifyUserAuthorization

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    var options: SpotifyApiOptions

    /**
     * A block in which Spotify application credentials (accessible via the Spotify [dashboard](https://developer.spotify.com/dashboard/applications))
     * should be put
     */
    fun credentials(block: SpotifyCredentialsBuilder.() -> Unit) {
        credentials = SpotifyCredentialsBuilder().apply(block).build()
    }

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    fun authorization(block: SpotifyUserAuthorizationBuilder.() -> Unit) {
        authorization = SpotifyUserAuthorizationBuilder().apply(block).build()
    }

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    fun authentication(block: SpotifyUserAuthorizationBuilder.() -> Unit) {
        authorization = SpotifyUserAuthorizationBuilder().apply(block).build()
    }

    fun authorization(authorization: SpotifyUserAuthorization) = apply { this.authorization = authorization }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    @Deprecated("Succeeded by the options method", ReplaceWith("options"))
    fun utilities(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    @Deprecated("Succeeded by the options method", ReplaceWith("options"))
    fun config(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun options(block: SpotifyApiOptionsBuilder.() -> Unit) {
        options = SpotifyApiOptionsBuilder().apply(block).build()
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun options(options: SpotifyApiOptions) = apply { this.options = options }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    fun options(options: SpotifyApiOptionsBuilder) = apply { this.options = options.build() }

    /**
     * Build the [T] by provided information
     */
    suspend fun suspendBuild(): T

    /**
     * Build the [T] by provided information
     */
    fun build(): T = runBlocking { suspendBuild() }

    /**
     * Build the [T] by provided information
     *
     * Provide a consumer object to be executed after the api has been successfully built
     */
    fun buildAsyncAt(scope: CoroutineScope, consumer: (T) -> Unit): Job = with(scope) { buildAsync(consumer) }

    /**
     * Build the [T] by provided information
     *
     * Provide a consumer object to be executed after the api has been successfully built
     */
    fun CoroutineScope.buildAsync(consumer: (T) -> Unit): Job = launch {
        consumer(suspendBuild())
    }
}

/**
 * Client interface exposing [getAuthorizationUrl]
 */
interface ISpotifyClientApiBuilder : ISpotifyApiBuilder<SpotifyClientApi, SpotifyClientApiBuilder> {
    /**
     * Create a Spotify authorization URL from which API access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     * @param state This provides protection against attacks such as cross-site request forgery.
     * @return Authorization URL that can be used in a browser
     */
    fun getAuthorizationUrl(vararg scopes: SpotifyScope, state: String? = null): String
}

/**
 * [SpotifyClientApi] builder for api creation using client authorization
 */
class SpotifyClientApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentialsBuilder().build(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build(),
    override var options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
) : ISpotifyClientApiBuilder {
    override fun getAuthorizationUrl(vararg scopes: SpotifyScope, state: String?): String {
        require(credentials.redirectUri != null && credentials.clientId != null) { "You didn't specify a redirect uri or client id in the credentials block!" }
        return SpotifyApi.getAuthUrlFull(
                *scopes,
                clientId = credentials.clientId!!,
                redirectUri = credentials.redirectUri!!,
                state = state
        )
    }

    override suspend fun suspendBuild(): SpotifyClientApi {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        // either application credentials, or a token is required
        require((clientId != null && clientSecret != null && redirectUri != null) || (clientId != null && redirectUri != null && authorization.pkceCodeVerifier != null) || authorization.token != null || authorization.tokenString != null) { "You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!" }
        return when {
            authorization.authorizationCode != null && authorization.pkceCodeVerifier == null -> try {
                require(clientId != null && clientSecret != null && redirectUri != null) { "You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!" }

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
                                listOf(),
                                null
                        ), clientId, clientSecret
                )

                SpotifyClientApi(
                        clientId,
                        clientSecret,
                        redirectUri,
                        response.body.toObject(Token.serializer(), null, options.json),
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
                        false,
                        options.onTokenRefresh
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw SpotifyException.AuthenticationException("Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret, authCode=${authorization.authorizationCode})", e)
            }
            authorization.authorizationCode != null && authorization.pkceCodeVerifier != null -> try {
                require(clientId != null && redirectUri != null) { "You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!" }

                val response = HttpConnection(
                        "https://accounts.spotify.com/api/token",
                        HttpRequestMethod.POST,
                        mapOf(
                                "grant_type" to "authorization_code",
                                "code" to authorization.authorizationCode,
                                "redirect_uri" to redirectUri,
                                "client_id" to clientId,
                                "code_verifier" to authorization.pkceCodeVerifier
                        ),
                        null,
                        "application/x-www-form-urlencoded",
                        listOf(),
                        null
                ).execute()

                SpotifyClientApi(
                        clientId,
                        clientSecret,
                        redirectUri,
                        response.body.toObject(Token.serializer(), null, options.json),
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
                        true,
                        options.onTokenRefresh
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw SpotifyException.AuthenticationException("Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret, authCode=${authorization.authorizationCode})", e)
            }
            authorization.token != null -> SpotifyClientApi(
                    clientId,
                    clientSecret,
                    redirectUri,
                    authorization.token!!,
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
                    false,
                    options.onTokenRefresh
            )
            authorization.tokenString != null -> SpotifyClientApi(
                    clientId,
                    clientSecret,
                    redirectUri,
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
                    options.testTokenValidity,
                    options.defaultLimit,
                    options.allowBulkRequests,
                    options.requestTimeoutMillis,
                    options.json,
                    options.refreshTokenProducer,
                    false,
                    options.onTokenRefresh
            )
            else -> throw IllegalArgumentException(
                    "At least one of: authorizationCode, tokenString, or token must be provided " +
                            "to build a SpotifyClientApi object"
            )
        }
    }
}

/**
 * App Api builder interface
 */
interface ISpotifyAppApiBuilder : ISpotifyApiBuilder<SpotifyAppApi, SpotifyAppApiBuilder>

/**
 * [SpotifyAppApi] builder for api creation using client authorization
 */
class SpotifyAppApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentialsBuilder().build(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorizationBuilder().build(),
    override var options: SpotifyApiOptions = SpotifyApiOptionsBuilder().build()
) : ISpotifyAppApiBuilder {
    /**
     * Build a public [SpotifyAppApi] using the provided credentials
     */
    override suspend fun suspendBuild(): SpotifyAppApi {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        require((clientId != null && clientSecret != null) || authorization.token != null || authorization.tokenString != null) { "You didn't specify a client id or client secret in the credentials block!" }
        return when {
            authorization.token != null -> SpotifyAppApi(
                    clientId,
                    clientSecret,
                    authorization.token!!,
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
                    options.onTokenRefresh
            )
            authorization.tokenString != null -> {
                SpotifyAppApi(
                        clientId,
                        clientSecret,
                        Token(
                                authorization.tokenString!!, "client_credentials",
                                60000, null, null
                        ),
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
                        options.onTokenRefresh
                )
            }
            authorization.refreshTokenString != null -> {
                SpotifyAppApi(
                        clientId,
                        clientSecret,
                        Token("", "", 0, authorization.refreshTokenString!!),
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
                        options.onTokenRefresh
                )
            }
            else -> try {
                require(clientId != null && clientSecret != null) { "Illegal credentials provided" }
                val token = getCredentialedToken(clientId, clientSecret, null, options.json)
                SpotifyAppApi(
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
                        options.refreshTokenProducer,
                        options.onTokenRefresh
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw SpotifyException.AuthenticationException("Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret)", e)
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
 * @property authorizationCode Only available when building [SpotifyClientApi]. Spotify auth code
 * @property token Build the API using an existing token. If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token
 * @property tokenString Build the API using an existing token (string). If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token. There is a *very*
 * limited time constraint on these before the API automatically refreshes them
 */
class SpotifyUserAuthorizationBuilder(
    var authorizationCode: String? = null,
    var tokenString: String? = null,
    var token: Token? = null,
    var refreshTokenString: String? = null,
    var pkceCodeVerifier: String? = null
) {
    fun build() = SpotifyUserAuthorization(authorizationCode, tokenString, token, refreshTokenString, pkceCodeVerifier)
}

/**
 * User-defined authorization parameters
 *
 * @property authorizationCode Only available when building [SpotifyClientApi]. Spotify auth code
 * @property token Build the API using an existing token. If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token
 * @property tokenString Build the API using an existing token (string). If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token. There is a *very*
 * limited time constraint on these before the API automatically refreshes them
 * @property refreshTokenString Refresh token, given as a string, to be exchanged to Spotify for a new token
 * @property pkceCodeVerifier The code verifier generated that the client authenticated with (using its code challenge)
 */
data class SpotifyUserAuthorization(
    var authorizationCode: String? = null,
    var tokenString: String? = null,
    var token: Token? = null,
    var refreshTokenString: String? = null,
    var pkceCodeVerifier: String? = null
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
 * @property defaultLimit The default amount of objects to retrieve in one request
 * @property json The Json serializer/deserializer instance
 * @property enableAllOptions Whether to enable all provided utilities
 * @property allowBulkRequests Allow splitting too-large requests into smaller, allowable api requests
 * @property requestTimeoutMillis The maximum time, in milliseconds, before terminating an http request
 * @property refreshTokenProducer Provide if you want to use your own logic when refreshing a Spotify token
 * @property onTokenRefresh Provide if you want to act on token refresh event
 *
 */
class SpotifyApiOptionsBuilder(
    var useCache: Boolean = true,
    var cacheLimit: Int? = 200,
    var automaticRefresh: Boolean = true,
    var retryWhenRateLimited: Boolean = true,
    var enableLogger: Boolean = true,
    var testTokenValidity: Boolean = false,
    var enableAllOptions: Boolean = false,
    var defaultLimit: Int = 50,
    var allowBulkRequests: Boolean = true,
    var requestTimeoutMillis: Long? = null,
    var json: Json = nonstrictJson,
    var refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)? = null,
    var onTokenRefresh: (suspend (GenericSpotifyApi) -> Unit)? = null
) {
    fun build() =
            if (enableAllOptions)
                SpotifyApiOptions(
                        true,
                        200,
                        automaticRefresh = true,
                        retryWhenRateLimited = true,
                        enableLogger = true,
                        testTokenValidity = true,
                        defaultLimit = 50,
                        allowBulkRequests = true,
                        requestTimeoutMillis = requestTimeoutMillis,
                        json = json,
                        refreshTokenProducer = refreshTokenProducer,
                        onTokenRefresh = onTokenRefresh
                )
            else
                SpotifyApiOptions(
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
                        refreshTokenProducer,
                        onTokenRefresh
                )
}

/**
 * API Utilities
 *
 * @property useCache Set whether to cache requests. Default: true
 * @property cacheLimit The maximum amount of cached requests allowed at one time. Null means no limit. Default: 200
 * @property automaticRefresh Enable or disable automatic refresh of the Spotify access token when it expires. Default: true
 * @property retryWhenRateLimited Set whether to block the current thread and wait until the API can retry the request. Default: true
 * @property enableLogger Set whether to enable to the exception logger. Default: true
 * @property testTokenValidity After API creation, test whether the token is valid by performing a lightweight request. Default: false
 * @property defaultLimit The default amount of objects to retrieve in one request. Default: 50
 * @property json The Json serializer/deserializer instance.
 * @property allowBulkRequests Allow splitting too-large requests into smaller, allowable api requests. Default: true
 * @property requestTimeoutMillis The maximum time, in milliseconds, before terminating an http request. Default: 100000ms
 * @property refreshTokenProducer Provide if you want to use your own logic when refreshing a Spotify token.
 * @property onTokenRefresh Provide if you want to act on token refresh event
 *
 */

data class SpotifyApiOptions(
    var useCache: Boolean,
    var cacheLimit: Int?,
    var automaticRefresh: Boolean,
    var retryWhenRateLimited: Boolean,
    var enableLogger: Boolean,
    var testTokenValidity: Boolean,
    var defaultLimit: Int,
    var allowBulkRequests: Boolean,
    var requestTimeoutMillis: Long?,
    var json: Json,
    var refreshTokenProducer: (suspend (SpotifyApi<*, *>) -> Token)?,
    var onTokenRefresh: (suspend (SpotifyApi<*, *>) -> Unit)?
)

@Deprecated("Name has been replaced by `options`", ReplaceWith("SpotifyApiOptions"))
typealias SpotifyUtilities = SpotifyApiOptions
@Deprecated("Name has been replaced by `options`", ReplaceWith("SpotifyApiOptionsBuilder"))
typealias SpotifyUtilitiesBuilder = SpotifyApiOptionsBuilder
