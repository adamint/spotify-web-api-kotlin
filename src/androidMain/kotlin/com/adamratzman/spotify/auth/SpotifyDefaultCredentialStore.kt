/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyApiOptions
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyClientPkceApi
import com.adamratzman.spotify.spotifyImplicitGrantApi
import com.adamratzman.spotify.utils.logToConsole
import kotlinx.coroutines.runBlocking

/**
 * Provided credential store for holding current Spotify token credentials, allowing you to easily store and retrieve
 * Spotify tokens. Recommended in most use-cases.
 *
 * @param clientId The client id associated with your application
 * @param applicationContext The application context - you can obtain this by storing your application context statically (such as with a companion object)
 *
 */
@RequiresApi(Build.VERSION_CODES.M)
public class SpotifyDefaultCredentialStore(
    private val clientId: String,
    private val redirectUri: String,
    applicationContext: Context
) {
    public companion object {
        /**
         * The key used with spotify token expiry in [EncryptedSharedPreferences]
         */
        public const val SpotifyTokenExpiryKey: String = "spotifyTokenExpiry"

        /**
         * The key used with spotify access token in [EncryptedSharedPreferences]
         */
        public const val SpotifyAccessTokenKey: String = "spotifyAccessToken"

        /**
         * The key used with spotify refresh token in [EncryptedSharedPreferences]
         */
        public const val SpotifyRefreshTokenKey: String = "spotifyRefreshToken"

        /**
         * The activity to return to if re-authentication is necessary on implicit authentication. Null except during authentication when using [guardValidImplicitSpotifyApi]
         */
        public var activityBackOnImplicitAuth: Class<out Activity>? = null
    }

    public var credentialTypeStored: CredentialType? = null

    /**
     * The [EncryptedSharedPreferences] that this API saves to/retrieves from.
     */
    @Suppress("DEPRECATION")
    public val encryptedPreferences: SharedPreferences = EncryptedSharedPreferences
        .create(
            "spotify-api-encrypted-preferences",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    /**
     * Get/set when the Spotify access token will expire, in milliseconds from UNIX epoch. This will be one hour from authentication.
     */
    public var spotifyTokenExpiresAt: Long?
        get() {
            val expiry = encryptedPreferences.getLong(SpotifyTokenExpiryKey, -1)
            return if (expiry == -1L) null else expiry
        }
        set(value) {
            if (value == null) encryptedPreferences.edit().remove(SpotifyTokenExpiryKey).apply()
            else encryptedPreferences.edit().putLong(SpotifyTokenExpiryKey, value).apply()
        }

    /**
     * Get/set the Spotify access token (the access token string, not the wrapped [Token]).
     */
    public var spotifyAccessToken: String?
        get() = encryptedPreferences.getString(SpotifyAccessTokenKey, null)
        set(value) = encryptedPreferences.edit().putString(SpotifyAccessTokenKey, value).apply()

    /**
     * Get/set the Spotify refresh token.
     */
    public var spotifyRefreshToken: String?
        get() = encryptedPreferences.getString(SpotifyRefreshTokenKey, null)
        set(value) = encryptedPreferences.edit().putString(SpotifyRefreshTokenKey, value).apply()

    /**
     * Get/set the Spotify [Token] obtained from [spotifyToken].
     * If the token has expired according to [spotifyTokenExpiresAt], this will return null.
     */
    public var spotifyToken: Token?
        get() {
            val tokenExpiresAt = spotifyTokenExpiresAt ?: return null
            val accessToken = spotifyAccessToken ?: return null
            if (tokenExpiresAt < System.currentTimeMillis()) return null

            val refreshToken = spotifyRefreshToken
            return Token(
                accessToken,
                "Bearer",
                (tokenExpiresAt - System.currentTimeMillis()).toInt() / 1000,
                refreshToken
            )
        }
        set(token) {
            if (token == null) {
                spotifyAccessToken = null
                spotifyTokenExpiresAt = null
                spotifyRefreshToken = null

                credentialTypeStored = null
            } else {
                spotifyAccessToken = token.accessToken
                spotifyTokenExpiresAt = token.expiresAt
                spotifyRefreshToken = token.refreshToken

                credentialTypeStored =
                    if (token.refreshToken != null) CredentialType.Pkce else CredentialType.ImplicitGrant
            }
        }

    /**
     * Create a new [SpotifyImplicitGrantApi] instance using the [spotifyToken] stored using this credential store.
     *
     * @param block Applied configuration to the [SpotifyImplicitGrantApi]
     */
    public fun getSpotifyImplicitGrantApi(block: ((SpotifyApiOptions).() -> Unit)? = null): SpotifyImplicitGrantApi? {
        val token = spotifyToken ?: return null
        return spotifyImplicitGrantApi(clientId, token, block ?: {})
    }

    /**
     * Create a new [SpotifyClientApi] instance using the [spotifyToken] stored using this credential store.
     *
     * @param block Applied configuration to the [SpotifyImplicitGrantApi]
     */
    public fun getSpotifyClientPkceApi(block: ((SpotifyApiOptions).() -> Unit)? = null): SpotifyClientApi? {
        val token = spotifyToken ?: return null
        return runBlocking {
            spotifyClientPkceApi(
                clientId,
                redirectUri,
                SpotifyUserAuthorization(token = token),
                block ?: {}
            ).build().apply {
                val previousAfterTokenRefresh = spotifyApiOptions.afterTokenRefresh
                spotifyApiOptions.afterTokenRefresh = {
                    spotifyToken = this.token
                    logToConsole("Refreshed Spotify PKCE token in credential store... $token")
                    previousAfterTokenRefresh?.invoke(this)
                }
            }
        }
    }

    /**
     * Sets [spotifyToken] using [SpotifyApi.token]. This wraps around [spotifyToken]'s setter.
     *
     * @param api A valid [GenericSpotifyApi]
     */
    public fun setSpotifyApi(api: GenericSpotifyApi) {
        spotifyToken = api.token
    }

    /**
     * Clear the [SharedPreferences] instance corresponding to the Spotify credentials.
     */
    @SuppressLint("ApplySharedPref")
    public fun clear(): Boolean = try {
        encryptedPreferences.edit().clear().commit()
    } catch (e: Exception) {
        // This might crash, encrypted preferences is still alpha...
        false
    }
}

public enum class CredentialType {
    ImplicitGrant,
    Pkce
}

@RequiresApi(Build.VERSION_CODES.M)
public fun Application.getDefaultCredentialStore(clientId: String, redirectUri: String): SpotifyDefaultCredentialStore {
    return SpotifyDefaultCredentialStore(clientId, redirectUri, applicationContext)
}
