package com.adamratzman.spotify.auth.pkce

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

public fun Intent?.isSpotifyPkceAuthIntent(redirectUri: String): Boolean {
    return this != null
            && (dataString?.startsWith("$redirectUri/?code=") == true || dataString?.startsWith("$redirectUri/?error=") == true)
}

/**
 * Start Spotify PKCE login activity within an existing activity.
 *
 * @param spotifyLoginImplementationClass Your implementation of [AbstractSpotifyPkceLoginActivity], defining what to do on Spotify PKCE login
 */
@RequiresApi(Build.VERSION_CODES.M)
public fun Activity.startSpotifyClientPkceLoginActivity(spotifyLoginImplementationClass: Class<out AbstractSpotifyPkceLoginActivity>) {
    val intent = Intent(this, spotifyLoginImplementationClass)
    startActivity(intent)
}