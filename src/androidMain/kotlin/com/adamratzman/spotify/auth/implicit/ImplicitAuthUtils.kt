package com.adamratzman.spotify.auth.implicit

import android.app.Activity
import android.content.Intent
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore.Companion.activityBackOnImplicitAuth

// Starting implicit login activity

/**
 * Start Spotify implicit login activity within an existing activity.
 */
public inline fun <reified T : SpotifyImplicitLoginActivity> Activity.startSpotifyImplicitLoginActivity() {
    startSpotifyImplicitLoginActivity(T::class.java)
}

/**
 * Start Spotify implicit login activity within an existing activity.
 *
 * @param spotifyLoginImplementationClass Your implementation of [SpotifyImplicitLoginActivity], defining what to do on Spotify login
 */
public fun <T : SpotifyImplicitLoginActivity> Activity.startSpotifyImplicitLoginActivity(spotifyLoginImplementationClass: Class<T>) {
    startActivity(Intent(this, spotifyLoginImplementationClass))
}

/**
 * Basic implicit authentication guard - verifies that the user is logged in to Spotify and uses [SpotifyDefaultImplicitAuthHelper] to
 * handle re-authentication and redirection back to the activity.
 *
 * Note: this should only be used for small applications.
 *
 * @param spotifyImplicitLoginImplementationClass Your implementation of [SpotifyImplicitLoginActivity], defining what to do on Spotify login
 * @param classBackTo The activity to return to if re-authentication is necessary
 * @block The code block to execute
 */
public fun <T> Activity.guardValidImplicitSpotifyApi(
    spotifyImplicitLoginImplementationClass: Class<out SpotifyImplicitLoginActivity>,
    classBackTo: Class<out Activity>? = null,
    block: () -> T
): T? {
    return try {
        block()
    } catch (e: SpotifyException.ReAuthenticationNeededException) {
        activityBackOnImplicitAuth = classBackTo
        startSpotifyImplicitLoginActivity(spotifyImplicitLoginImplementationClass)
        null
    }
}

