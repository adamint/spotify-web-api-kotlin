/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.auth.implicit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.LoginActivity

/**
 * Wrapper around spotify-auth's [LoginActivity] that allows configuration of the authentication process, along with
 * callbacks on successful and failed authentication. Pair this with [SpotifyDefaultCredentialStore] to easily store credentials.
 * Inherits from [Activity]. If instead you want to inherit from [AppCompatActivity], please use [AbstractSpotifyAppCompatImplicitLoginActivity].
 *
 */
public abstract class AbstractSpotifyAppImplicitLoginActivity : SpotifyImplicitLoginActivity, Activity() {
    @Suppress("LeakingThis")
    public override val activity: Activity = this
    public override val useDefaultRedirectHandler: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        triggerLoginActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        processActivityResult(requestCode, resultCode, intent)
    }
}
