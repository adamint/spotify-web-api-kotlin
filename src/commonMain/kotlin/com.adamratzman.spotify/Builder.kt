/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyApi.Companion.getCredentialedToken
import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.serialization.nonstrictJson
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.urlEncodeBase64String
import com.soywiz.krypto.SHA256
import io.ktor.client.features.ServerResponseException
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.CancellationException
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
public fun getSpotifyAuthorizationUrl(
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
public fun getPkceAuthorizationUrl(
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
public fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    if (codeVerifier.length !in 43..128) throw IllegalArgumentException("Code verifier must be between 43 and 128 characters long")
    val sha256 = SHA256.digest(codeVerifier.toByteArray()).base64
    return sha256.urlEncodeBase64String()
}

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
 * Instantiate a new [SpotifyImplicitGrantApi] using a Spotify [clientId], and [token] retrieved from the implicit
 * grant flow.
 *
 * Use case: I have a token obtained after implicit grant authorization.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param token Token created from the hash response in the implicit grant callback
 *
 * @return [SpotifyImplicitGrantApi] that can immediately begin making calls
 */
public fun spotifyImplicitGrantApi(
    clientId: String?,
    token: Token,
    block: SpotifyApiOptions.() -> Unit = { }
): SpotifyImplicitGrantApi = SpotifyImplicitGrantApi(
    clientId,
    token,
    SpotifyApiOptions().apply(block)
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
 * Instantiate a new [SpotifyAppApiBuilder] using a Spotify [clientId] and [clientSecret], with the ability to configure
 * the api settings by providing a builder initialization [block]
 *
 * Use case: I am using the client credentials flow.
 * I only need access to public Spotify API endpoints, might have an existing token,
 * and might want to deal with advanced configuration.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param block Api settings block
 *
 * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
 */
public fun spotifyAppApi(
    clientId: String,
    clientSecret: String,
    block: SpotifyAppApiBuilder.() -> Unit = {}
): SpotifyAppApiBuilder = SpotifyAppApiBuilder().apply(block).apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }
}

/**
 * Instantiate a new [SpotifyAppApiBuilder] using a [Token]
 *
 * Use case: I am using the client credentials flow.
 * I only need access to public Spotify API endpoints, I have an existing token,
 * and I don't want to deal with advanced configuration.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authorization A [SpotifyUserAuthorization] that must contain one of the following: authorization code (preferred),
 * access token string (tokenString), [Token] object, **and** that may contain a refresh token (preferred)
 * with which to refresh the access token
 * @param block Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
 */
public fun spotifyAppApi(
    clientId: String?,
    clientSecret: String?,
    authorization: SpotifyUserAuthorization,
    block: SpotifyApiOptions.() -> Unit = {}
): SpotifyAppApiBuilder = SpotifyAppApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }
    authorization(authorization)
    options(block)
}

/**
 * Instantiate a new [SpotifyAppApiBuilder] by providing a builder initialization [block].
 *
 * **Note**: You **must** provide your app credentials in the [SpotifyAppApiBuilder.credentials] block
 *
 * Use case: I am using the client credentials flow.
 * I only need access to public Spotify API endpoints, and I want to use the [SpotifyAppApiBuilder] DSL
 * to configure everything myself.
 *
 * @param block Api settings block
 *
 * @return Configurable [SpotifyAppApiBuilder] that, when built, creates a new [SpotifyAppApi]
 */
public fun spotifyAppApi(block: SpotifyAppApiBuilder.() -> Unit): SpotifyAppApiBuilder =
    SpotifyAppApiBuilder().apply(block)

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
 * Use case: I am using the client authorization flow.
 * I want access to both public and client Spotify API endpoints, and I want
 * to configure authorization and other settings myself.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param block Api settings block
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
public fun spotifyClientApi(
    clientId: String,
    clientSecret: String,
    redirectUri: String,
    block: SpotifyClientApiBuilder.() -> Unit = {}
): SpotifyClientApiBuilder = SpotifyClientApiBuilder().apply(block).apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
    }
}

/**
 * Instantiate a new [SpotifyClientApiBuilder] using a Spotify [clientId], [clientSecret], and [redirectUri],
 * with an existing [SpotifyUserAuthorization].
 *
 * Use case: I am using the client authorization flow.
 * I want access to both public and client Spotify API endpoints and I want to configure [authorization]
 * and [block] without using the DSL.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param clientSecret Spotify [client secret](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authorization A [SpotifyUserAuthorization] that must contain one of the following: authorization code (preferred),
 * access token string (tokenString), [Token] object, **and** that may contain a refresh token (preferred)
 * with which to refresh the access token
 * @param block Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
public fun spotifyClientApi(
    clientId: String?,
    clientSecret: String?,
    redirectUri: String?,
    authorization: SpotifyUserAuthorization,
    block: SpotifyApiOptions.() -> Unit = {}
): SpotifyClientApiBuilder = SpotifyClientApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
    }
    authorization(authorization)
    options(block)
}

/**
 * Instantiate a new [SpotifyClientApiBuilder] by providing a builder initialization [block]
 *
 * **Note**: If trying to build [SpotifyClientApi], you **must** provide client authorization in the [SpotifyClientApiBuilder.authorization]
 * block
 *
 * Use case: I am using the client authorization flow.
 * I want access to both public and client Spotify API endpoints and I want to handle configuration
 * via the DSL myself.
 *
 * @param block Api settings block
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
public fun spotifyClientApi(block: SpotifyClientApiBuilder.() -> Unit): SpotifyClientApiBuilder =
    SpotifyClientApiBuilder().apply(block)

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
 * Instantiate a new [SpotifyClientApiBuilder]. This is for **PKCE authorization**.
 *
 * Use case: I am using the PKCE client authorization flow.
 *
 * @param clientId Spotify [client id](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param redirectUri Spotify [redirect uri](https://developer.spotify.com/documentation/general/guides/app-settings/)
 * @param authorization A [SpotifyUserAuthorization] that must contain one of the following: authorization code (preferred),
 * access token string (tokenString), [Token] object, **and** that may contain a refresh token (preferred)
 * with which to refresh the access token. Retrieved after PKCE client authorization flow. **You must provide a code verifier (plaintext).
 * @param block Override default API options such as the cache limit
 *
 * @return Configurable [SpotifyClientApiBuilder] that, when built, creates a new [SpotifyClientApi]
 */
public fun spotifyClientPkceApi(
    clientId: String?,
    redirectUri: String?,
    authorization: SpotifyUserAuthorization,
    block: SpotifyApiOptions.() -> Unit = {}
): SpotifyClientApiBuilder = SpotifyClientApiBuilder().apply {
    credentials {
        this.clientId = clientId
        this.redirectUri = redirectUri
    }

    authorization(authorization)
    options(block)
}

/**
 *  Spotify API builder
 */
public class SpotifyApiBuilder(
    private var clientId: String?,
    private var clientSecret: String?,
    private var redirectUri: String?
) {
    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    public var authorization: SpotifyUserAuthorization = SpotifyUserAuthorization()

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    public var options: SpotifyApiOptions = SpotifyApiOptions()

    /**
     * After API creation, set whether to test whether the token is valid by performing a lightweight request
     */
    public fun testTokenValidity(testTokenValidity: Boolean): SpotifyApiBuilder =
        apply { this.options.testTokenValidity = testTokenValidity }

    /**
     * Allows you to set the default amount of objects to retrieve in one request
     */
    public fun defaultLimit(defaultLimit: Int): SpotifyApiBuilder = apply { this.options.defaultLimit = defaultLimit }

    /**
     * Set the application client id
     */
    public fun clientId(clientId: String): SpotifyApiBuilder = apply { this.clientId = clientId }

    /**
     * Set the application client secret
     */
    public fun clientSecret(clientSecret: String): SpotifyApiBuilder = apply { this.clientSecret = clientSecret }

    /**
     * Set whether to cache requests. Default: true
     */
    public fun useCache(useCache: Boolean): SpotifyApiBuilder = apply { this.options.useCache = useCache }

    /**
     *  Set the maximum allowed amount of cached requests at one time. Null means no limit
     */
    public fun cacheLimit(cacheLimit: Int?): SpotifyApiBuilder = apply { this.options.cacheLimit = cacheLimit }

    /**
     * Set the application [redirect uri](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    public fun redirectUri(redirectUri: String?): SpotifyApiBuilder = apply { this.redirectUri = redirectUri }

    /**
     * Set a returned [authorization code](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
     */
    public fun authorizationCode(authorizationCode: String?): SpotifyApiBuilder =
        apply { this.authorization.authorizationCode = authorizationCode }

    /**
     * If you only have an access token, the api can be instantiated with it
     */
    public fun tokenString(tokenString: String?): SpotifyApiBuilder =
        apply { this.authorization.tokenString = tokenString }

    /**
     * Set the token to be used with this api instance
     */
    public fun token(token: Token?): SpotifyApiBuilder = apply { this.authorization.token = token }

    /**
     * Enable or disable automatic refresh of the Spotify access token
     */
    public fun automaticRefresh(automaticRefresh: Boolean): SpotifyApiBuilder =
        apply { this.options.automaticRefresh = automaticRefresh }

    /**
     * Set whether to block the current thread and wait until the API can retry the request
     */
    public fun retryWhenRateLimited(retryWhenRateLimited: Boolean): SpotifyApiBuilder =
        apply { this.options.retryWhenRateLimited = retryWhenRateLimited }

    /**
     * Set whether to enable to the exception logger
     */
    public fun enableLogger(enableLogger: Boolean): SpotifyApiBuilder =
        apply { this.options.enableLogger = enableLogger }

    /**
     * Set the maximum time, in milliseconds, before terminating an http request
     */
    public fun requestTimeoutMillis(requestTimeoutMillis: Long?): SpotifyApiBuilder =
        apply { this.options.requestTimeoutMillis = requestTimeoutMillis }

    /**
     * Set whether you want to allow splitting too-large requests into smaller, allowable api requests
     */
    public fun allowBulkRequests(allowBulkRequests: Boolean): SpotifyApiBuilder =
        apply { this.options.allowBulkRequests = allowBulkRequests }

    /**
     * Create a [SpotifyApi] instance with the given [SpotifyApiBuilder] parameters and the type -
     * [AuthorizationType.CLIENT] for client authentication, or otherwise [AuthorizationType.APPLICATION]
     */
    public suspend fun build(type: AuthorizationType): GenericSpotifyApi {
        return if (type == AuthorizationType.CLIENT) buildClient()
        else buildCredentialed()
    }

    /**
     * Create a new [SpotifyAppApi] that only has access to *public* endpoints and data
     */
    public suspend fun buildPublic(): SpotifyAppApi = buildCredentialed()

    /**
     * Create a new [SpotifyAppApi] that only has access to *public* endpoints and data
     */
    public suspend fun buildCredentialed(): SpotifyAppApi = spotifyAppApi {
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
    public suspend fun buildClient(): SpotifyClientApi = spotifyClientApi {
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
public enum class AuthorizationType {
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
 * @param T The type of [SpotifyApi] to be built
 * @param B The associated Api builder for [T]
 */
public interface ISpotifyApiBuilder<T : SpotifyApi<T, B>, B : ISpotifyApiBuilder<T, B>> {
    /**
     * A block in which Spotify application credentials (accessible via the Spotify [dashboard](https://developer.spotify.com/dashboard/applications))
     * should be put
     */
    public var credentials: SpotifyCredentials

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    public var authorization: SpotifyUserAuthorization

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    public var options: SpotifyApiOptions

    /**
     * A block in which Spotify application credentials (accessible via the Spotify [dashboard](https://developer.spotify.com/dashboard/applications))
     * should be put
     */
    public fun credentials(block: SpotifyCredentials.() -> Unit): ISpotifyApiBuilder<T, B> = apply {
        credentials = SpotifyCredentials().apply(block)
    }

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    public fun authorization(block: SpotifyUserAuthorization.() -> Unit): ISpotifyApiBuilder<T, B> = apply {
        authorization = SpotifyUserAuthorization().apply(block)
    }

    /**
     * Allows you to authenticate a [SpotifyClientApi] with an authorization code
     * or build [SpotifyApi] using a refresh token
     */
    public fun authentication(block: SpotifyUserAuthorization.() -> Unit): ISpotifyApiBuilder<T, B> = apply {
        authorization = SpotifyUserAuthorization().apply(block)
    }

    public fun authorization(authorization: SpotifyUserAuthorization): ISpotifyApiBuilder<T, B> =
        apply { this.authorization = authorization }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    public fun options(block: SpotifyApiOptions.() -> Unit): ISpotifyApiBuilder<T, B> = apply {
        options = SpotifyApiOptions().apply(block)
    }

    /**
     * Allows you to override default values for caching, token refresh, and logging
     */
    public fun options(options: SpotifyApiOptions): ISpotifyApiBuilder<T, B> = apply {
        this.options = options
    }

    /**
     * Build the [T] by provided information
     */
    public suspend fun build(enableDefaultTokenRefreshProducerIfNoneExists: Boolean = true): T
}

/**
 * Client interface exposing [getAuthorizationUrl]
 */
public interface ISpotifyClientApiBuilder : ISpotifyApiBuilder<SpotifyClientApi, SpotifyClientApiBuilder> {
    /**
     * Create a Spotify authorization URL from which API access can be obtained
     *
     * @param scopes The scopes that the application should have access to
     * @param state This provides protection against attacks such as cross-site request forgery.
     * @return Authorization URL that can be used in a browser
     */
    public fun getAuthorizationUrl(vararg scopes: SpotifyScope, state: String? = null): String
}

/**
 * [SpotifyClientApi] builder for api creation using client authorization
 */
public class SpotifyClientApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentials(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorization(),
    override var options: SpotifyApiOptions = SpotifyApiOptions()
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

    override suspend fun build(enableDefaultTokenRefreshProducerIfNoneExists: Boolean): SpotifyClientApi {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        // either application credentials, or a token is required
        require((clientId != null && clientSecret != null && redirectUri != null) || (clientId != null && redirectUri != null && authorization.pkceCodeVerifier != null) || authorization.token != null || authorization.tokenString != null) { "You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!" }
        val api = when {
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
                    clientId = clientId,
                    clientSecret = clientSecret,
                    redirectUri = redirectUri,
                    token = response.body.toObject(Token.serializer(), null, options.json),
                    usesPkceAuth = false,
                    enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                    spotifyApiOptions = options
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // BadRequestException -> ServerResponseException
                if ((e.cause as? ServerResponseException)?.response?.status?.value in 500..599) {
                    throw SpotifyException.BadRequestException("Spotify internal server error", e)
                } else throw SpotifyException.AuthenticationException(
                    "Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret, authCode=${authorization.authorizationCode})",
                    e
                )
            }
            authorization.authorizationCode != null && authorization.pkceCodeVerifier != null -> try {
                require(clientId != null && redirectUri != null) { "You need to specify a valid clientId and redirectUri in the credentials block!" }

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
                    clientId = clientId,
                    clientSecret = clientSecret,
                    redirectUri = redirectUri,
                    token = response.body.toObject(Token.serializer(), null, options.json),
                    usesPkceAuth = true,
                    enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                    spotifyApiOptions = options
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if ((e.cause as? ServerResponseException)?.response?.status?.value in 500..599) {
                    throw SpotifyException.BadRequestException("Spotify internal server error", e)
                } else throw SpotifyException.AuthenticationException(
                    "Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret, authCode=${authorization.authorizationCode})",
                    e
                )
            }
            authorization.token != null -> SpotifyClientApi(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                token = authorization.token!!,
                usesPkceAuth = authorization.pkceCodeVerifier != null,
                enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                spotifyApiOptions = options
            )
            authorization.tokenString != null -> SpotifyClientApi(
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                token = Token(
                    authorization.tokenString!!,
                    "client_credentials",
                    3600,
                    null,
                    null
                ),
                usesPkceAuth = false,
                enableDefaultTokenRefreshProducerIfNoneExists = false,
                spotifyApiOptions = options
            )
            else -> throw IllegalArgumentException(
                "At least one of: authorizationCode, tokenString, or token must be provided " +
                        "to build a SpotifyClientApi object"
            )
        }

        if (options.testTokenValidity) SpotifyApi.testTokenValidity(api)

        return api
    }
}

/**
 * App Api builder interface
 */
public interface ISpotifyAppApiBuilder : ISpotifyApiBuilder<SpotifyAppApi, SpotifyAppApiBuilder>

/**
 * [SpotifyAppApi] builder for api creation using client authorization
 */
public class SpotifyAppApiBuilder(
    override var credentials: SpotifyCredentials = SpotifyCredentials(),
    override var authorization: SpotifyUserAuthorization = SpotifyUserAuthorization(),
    override var options: SpotifyApiOptions = SpotifyApiOptions()
) : ISpotifyAppApiBuilder {
    /**
     * Build a public [SpotifyAppApi] using the provided credentials
     */
    override suspend fun build(enableDefaultTokenRefreshProducerIfNoneExists: Boolean): SpotifyAppApi {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        require((clientId != null && clientSecret != null) || authorization.token != null || authorization.tokenString != null) { "You didn't specify a client id or client secret in the credentials block!" }

        val api = when {
            authorization.token != null -> SpotifyAppApi(
                clientId = clientId,
                clientSecret = clientSecret,
                token = authorization.token!!,
                enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                spotifyApiOptions = options
            )
            authorization.tokenString != null -> SpotifyAppApi(
                clientId = clientId,
                clientSecret = clientSecret,
                token = Token(
                    authorization.tokenString!!,
                    "client_credentials",
                    3600,
                    null,
                    null
                ),
                enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                spotifyApiOptions = options
            )
            authorization.refreshTokenString != null -> SpotifyAppApi(
                clientId = clientId,
                clientSecret = clientSecret,
                token = Token(
                    "",
                    "",
                    0,
                    authorization.refreshTokenString!!
                ),
                enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                spotifyApiOptions = options
            )
            else -> try {
                require(clientId != null && clientSecret != null) { "Illegal credentials provided" }
                val token = getCredentialedToken(clientId, clientSecret, null, options.json)
                SpotifyAppApi(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    token = token,
                    enableDefaultTokenRefreshProducerIfNoneExists = enableDefaultTokenRefreshProducerIfNoneExists,
                    spotifyApiOptions = options
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if ((e.cause as? ServerResponseException)?.response?.status?.value in 500..599) {
                    throw SpotifyException.BadRequestException("Spotify internal server error", e)
                } else throw SpotifyException.AuthenticationException(
                    "Invalid credentials provided in the login process (clientId=$clientId, clientSecret=$clientSecret)",
                    e
                )
            }
        }

        if (options.testTokenValidity) SpotifyApi.testTokenValidity(api)
        return api
    }
}

/**
 * A holder for application-specific credentials
 *
 * @property clientId the client id of your Spotify application
 * @property clientSecret the client secret of your Spotify application
 * @property redirectUri nullable redirect uri (use if you're doing client authentication
 */
public class SpotifyCredentials {
    public var clientId: String? = null
    public var clientSecret: String? = null
    public var redirectUri: String? = null
}

/**
 * User-defined authorization parameters
 *
 * @param authorizationCode Only available when building [SpotifyClientApi]. Spotify auth code
 * @param token Build the API using an existing token. If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token
 * @param tokenString Build the API using an existing token (string). If you're building [SpotifyClientApi], this
 * will be your **access** token. If you're building [SpotifyApi], it will be your **refresh** token. There is a *very*
 * limited time constraint on these before the API automatically refreshes them
 * @param refreshTokenString Refresh token, given as a string, to be exchanged to Spotify for a new token
 * @param pkceCodeVerifier The code verifier generated that the client authenticated with (using its code challenge)
 */
public class SpotifyUserAuthorization(
    public var authorizationCode: String? = null,
    public var tokenString: String? = null,
    public var token: Token? = null,
    public var refreshTokenString: String? = null,
    public var pkceCodeVerifier: String? = null
)

/**
 * API Utilities
 *
 * @param useCache Set whether to cache requests. Default: true
 * @param cacheLimit The maximum amount of cached requests allowed at one time. Null means no limit
 * @param automaticRefresh Enable or disable automatic refresh of the Spotify access token
 * @param retryWhenRateLimited Set whether to block the current thread and wait until the API can retry the request
 * @param enableLogger Set whether to enable to the exception logger
 * @param testTokenValidity After API creation, test whether the token is valid by performing a lightweight request
 * @param defaultLimit The default amount of objects to retrieve in one request
 * @param json The Json serializer/deserializer instance
 * @param allowBulkRequests Allow splitting too-large requests into smaller, allowable api requests
 * @param requestTimeoutMillis The maximum time, in milliseconds, before terminating an http request
 * @param refreshTokenProducer Provide if you want to use your own logic when refreshing a Spotify token
 * @param onTokenRefresh Provide if you want to act on token refresh event
 * @param requiredScopes Scopes that your application requires to function (only applicable to [SpotifyClientApi] and [SpotifyImplicitGrantApi]).
 * @param proxyBaseUrl Provide if you have a proxy base URL that you would like to use instead of the Spotify API base
 * (https://api.spotify.com/v1).
 * @param retryOnInternalServerErrorTimes Whether and how often to retry once if an internal server error (500..599) has been received. Set to 0
 * to avoid retrying at all, or set to null to keep retrying until success.
 *
 */
public data class SpotifyApiOptions(
    public var useCache: Boolean = true,
    public var cacheLimit: Int? = 200,
    public var automaticRefresh: Boolean = true,
    public var retryWhenRateLimited: Boolean = true,
    public var enableLogger: Boolean = true,
    public var testTokenValidity: Boolean = false,
    public var defaultLimit: Int = 50,
    public var allowBulkRequests: Boolean = true,
    public var requestTimeoutMillis: Long? = null,
    public var json: Json = nonstrictJson,
    public var refreshTokenProducer: (suspend (GenericSpotifyApi) -> Token)? = null,
    public var onTokenRefresh: (suspend (GenericSpotifyApi) -> Unit)? = null,
    public var requiredScopes: List<SpotifyScope>? = null,
    public var proxyBaseUrl: String? = null,
    public var retryOnInternalServerErrorTimes: Int? = 5
)
