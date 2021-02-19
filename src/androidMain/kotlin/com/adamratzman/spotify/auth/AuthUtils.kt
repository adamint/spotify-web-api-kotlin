package com.adamratzman.spotify.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.adamratzman.spotify.SpotifyApiOptions
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.auth.SpotifyDefaultAuthHelper.activityBack
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi

// Starting login activity

/**
 * Start Spotify login activity within an existing activity.
 */
public inline fun <reified T : AbstractSpotifyLoginActivity> Activity.startSpotifyLoginActivity() {
    startSpotifyLoginActivity(T::class.java)
}

/**
 * Start Spotify login activity within an existing activity.
 *
 * @param spotifyLoginImplementationClass Your implementation of [AbstractSpotifyLoginActivity], defining what to do on Spotify login
 */
public fun <T : AbstractSpotifyLoginActivity> Activity.startSpotifyLoginActivity(spotifyLoginImplementationClass: Class<T>) {
    startActivity(Intent(this, spotifyLoginImplementationClass))
}


/**
 * Basic authentication guard - verifies that the user is logged in to Spotify and uses [SpotifyDefaultAuthHelper] to
 * handle re-authentication and redirection back to the activity.
 *
 * Note: this should only be used for small applications.
 *
 * @param spotifyLoginImplementationClass Your implementation of [AbstractSpotifyLoginActivity], defining what to do on Spotify login
 * @param classBackTo The activity to return to if re-authentication is necessary
 * @block The code block to execute
 */
public fun <T> Activity.guardValidSpotifyApi(
    spotifyLoginImplementationClass: Class<out AbstractSpotifyLoginActivity>,
    classBackTo: Class<out Activity>? = null,
    block: () -> T
): T? {
    return try {
        block()
    } catch (e: SpotifyException.ReAuthenticationNeededException) {
        activityBack = classBackTo
        startSpotifyLoginActivity(spotifyLoginImplementationClass)
        null
    }
}

/**
 * Default authentiction helper for Android. Contains static variables useful in authentication.
 *
 */
public object SpotifyDefaultAuthHelper {
    /**
     * The activity to return to if re-authentication is necessary. Null except during authentication when using [guardValidSpotifyApi]
     */
    public var activityBack: Class<out Activity>? = null
}

/**
 * Provided credential store for holding current Spotify token credentials, allowing you to easily store and retrieve
 * Spotify tokens. Recommended in most use-cases.
 *
 * @param clientId The client id associated with your application
 * @param applicationContext The application context - you can obtain this by storing your application context statically (such as with a companion object)
 *
 */
@RequiresApi(Build.VERSION_CODES.M)
public class SpotifyDefaultCredentialStore constructor(private val clientId: String, applicationContext: Context) {
    public companion object {
        /**
         * The key used with spotify token expiry in [EncryptedSharedPreferences]
         */
        public const val SpotifyTokenExpiryKey: String = "spotifyTokenExpiry"

        /**
         * The key used with spotify access token in [EncryptedSharedPreferences]
         */
        public const val SpotifyAccessTokenKey: String = "spotifyAccessToken"

    }

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
     *
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

    public var spotifyAccessToken: String?
        get() = encryptedPreferences.getString(SpotifyAccessTokenKey, null)
        set(value) = encryptedPreferences.edit().putString(SpotifyAccessTokenKey, value).apply()

    public var spotifyToken: Token?
        get() {
            val tokenExpiresAt = spotifyTokenExpiresAt ?: return null
            val accessToken = spotifyAccessToken ?: return null
            if (tokenExpiresAt < System.currentTimeMillis()) return null

            return Token(accessToken, "Bearer", (tokenExpiresAt - System.currentTimeMillis()).toInt() / 1000)
        }
        set(token) {
            if (token == null) {
                spotifyAccessToken = null
                spotifyTokenExpiresAt = null
            } else {
                spotifyAccessToken = token.accessToken
                spotifyTokenExpiresAt = token.expiresAt
            }
        }

    public fun getSpotifyImplicitGrantApi(block: ((SpotifyApiOptions).() -> Unit)? = null): SpotifyImplicitGrantApi? {
        val token = spotifyToken ?: return null
        return spotifyImplicitGrantApi(clientId, token, block ?: {})
    }

    public fun setSpotifyImplicitGrantApi(api: SpotifyImplicitGrantApi) {
        spotifyToken = api.token
    }

    @SuppressLint("ApplySharedPref")
    public fun clear(): Boolean = try {
        encryptedPreferences.edit().clear().commit()
    } catch (e: Exception) {
        // This might crash, encrypted preferences is still alpha...
        false
    }
}




