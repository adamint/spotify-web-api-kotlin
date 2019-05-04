/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpConnection
import com.adamratzman.spotify.http.HttpHeader
import com.adamratzman.spotify.http.HttpRequestMethod
import com.adamratzman.spotify.http.byteEncode
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.models.serialization.toObject

// Kotlin DSL builder
fun spotifyApi(block: SpotifyApiBuilder.() -> Unit) = SpotifyApiBuilder().apply(block)

// Java-friendly builder
class SpotifyApiBuilderJava(val clientId: String, val clientSecret: String) {
    var redirectUri: String? = null
    var authorizationCode: String? = null
    var tokenString: String? = null
    var token: Token? = null
    var useCache: Boolean = true

    fun useCache(useCache: Boolean) = apply { this.useCache = useCache }

    fun redirectUri(redirectUri: String?) = apply { this.redirectUri = redirectUri }

    fun authorizationCode(authorizationCode: String?) = apply { this.authorizationCode = authorizationCode }

    fun tokenString(tokenString: String?) = apply { this.tokenString = tokenString }

    fun token(token: Token?) = apply { this.token = token }

    fun buildCredentialed() = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilderJava.clientId
            clientSecret = this@SpotifyApiBuilderJava.clientSecret
        }
        authentication {
            token = this@SpotifyApiBuilderJava.token
            tokenString = this@SpotifyApiBuilderJava.tokenString
        }
    }.buildCredentialed()

    fun buildClient(automaticRefresh: Boolean = false) = spotifyApi {
        credentials {
            clientId = this@SpotifyApiBuilderJava.clientId
            clientSecret = this@SpotifyApiBuilderJava.clientSecret
            redirectUri = this@SpotifyApiBuilderJava.redirectUri
        }
        authentication {
            authorizationCode = this@SpotifyApiBuilderJava.authorizationCode
            tokenString = this@SpotifyApiBuilderJava.tokenString
            token = this@SpotifyApiBuilderJava.token
        }
    }.buildClient(automaticRefresh)
}

/**
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

class SpotifyApiBuilder {
    private var credentials: SpotifyCredentials = SpotifyCredentials(null, null, null)
    private var authentication = SpotifyUserAuthorizationBuilder()
    var useCache: Boolean = true

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

    fun getAuthorizationUrl(vararg scopes: SpotifyScope): String {
        if (credentials.redirectUri == null || credentials.clientId == null) {
            throw IllegalArgumentException("You didn't specify a redirect uri or client id in the credentials block!")
        }
        return getAuthUrlFull(*scopes, clientId = credentials.clientId!!, redirectUri = credentials.redirectUri!!)
    }

    fun buildCredentialedAsync(consumer: (SpotifyAPI) -> Unit) = Runnable { consumer(buildCredentialed()) }.run()

    fun buildCredentialed(): SpotifyAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        if ((clientId == null || clientSecret == null) && (authentication.token == null && authentication.tokenString == null)) {
            throw IllegalArgumentException("You didn't specify a client id or client secret in the credentials block!")
        }
        return when {
            authentication.token != null -> {
                SpotifyAppAPI(clientId ?: "not-set", clientSecret
                        ?: "not-set", authentication.token!!, useCache)
            }
            authentication.tokenString != null -> {
                SpotifyAppAPI(
                        clientId ?: "not-set",
                        clientSecret ?: "not-set",
                        Token(
                                authentication.tokenString!!, "client_credentials",
                                60000, null, null
                        ),
                        useCache
                )
            }
            else -> try {
                if (clientId == null || clientSecret == null) throw IllegalArgumentException("Illegal credentials provided")
                val token = getCredentialedToken(clientId, clientSecret)
                        ?: throw IllegalArgumentException("Invalid credentials provided")
                SpotifyAppAPI(clientId, clientSecret, token, useCache)
            } catch (e: Exception) {
                throw SpotifyException("Invalid credentials provided in the login process", e)
            }
        }
    }

    fun buildClientAsync(consumer: (SpotifyClientAPI) -> Unit, automaticRefresh: Boolean = false) =
            Runnable { consumer(buildClient(automaticRefresh)) }.run()

    fun buildClient(automaticRefresh: Boolean = false): SpotifyClientAPI =
            buildClient(
                    authentication.authorizationCode, authentication.tokenString,
                    authentication.token, automaticRefresh
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
            token: Token? = null,
            automaticRefresh: Boolean = false
    ): SpotifyClientAPI {
        val clientId = credentials.clientId
        val clientSecret = credentials.clientSecret
        val redirectUri = credentials.redirectUri

        if ((clientId == null || clientSecret == null || redirectUri == null) && (token == null && tokenString == null)) {
            throw IllegalArgumentException("You need to specify a valid clientId, clientSecret, and redirectUri in the credentials block!")
        }
        return when {
            authorizationCode != null -> try {
                SpotifyClientAPI(
                        clientId ?: throw IllegalArgumentException(),
                        clientSecret ?: throw IllegalArgumentException(),
                        HttpConnection(
                                url = "https://accounts.spotify.com/api/token",
                                method = HttpRequestMethod.POST,
                                body = "grant_type=authorization_code&code=$authorizationCode&redirect_uri=$redirectUri",
                                contentType = "application/x-www-form-urlencoded",
                                api = null
                        ).execute(
                                HttpHeader(
                                        "Authorization",
                                        "Basic ${"$clientId:$clientSecret".byteEncode()}"
                                )
                        ).body.toObject(null),
                        automaticRefresh,
                        redirectUri ?: throw IllegalArgumentException(),
                        useCache
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
                    useCache
            )
            tokenString != null -> SpotifyClientAPI(
                    clientId ?: "not-set", clientSecret ?: "not-set", Token(
                    tokenString, "client_credentials", 1000,
                    null, null
            ), false, redirectUri ?: "not-set",
                    useCache
            )
            else -> throw IllegalArgumentException(
                    "At least one of: authorizationCode, tokenString, or token must be provided " +
                            "to build a SpotifyClientAPI object"
            )
        }
    }
}