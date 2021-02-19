package com.adamratzman.spotify.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity

/**
 * Wrapper around spotify-auth's [LoginActivity] that allows configuration of the authentication process, along with
 * callbacks on successful and failed authentication. Pair this with [SpotifyDefaultCredentialStore] to easily store credentials.
 *
 * @property state The state to use to verify the login request.
 * @property clientId Your application's Spotify client id.
 * @property clientId Your application's Spotify client secret.
 * @property redirectUri Your application's Spotify redirect id - NOTE that this should be an android scheme (such as spotifyapp://authback)
 * and that this must be registered in your manifest.
 * @property useDefaultRedirectHandler Disable if you will not be using [useDefaultRedirectHandler] but will be setting [SpotifyDefaultAuthHelper.activityBack].
 *
 */
public abstract class AbstractSpotifyLoginActivity : Activity() {
    public abstract val state: Int
    public abstract val clientId: String
    public abstract val redirectUri: String
    public open val useDefaultRedirectHandler: Boolean = true

    /**
     * Return the scopes that you are going to request from the user here.
     */
    public abstract fun getRequestingScopes(): List<SpotifyScope>

    /**
     * Override this to define what to do after authentication has been successfully completed. A valid, usable
     * [spotifyApi] is provided to you. You may likely want to use [SpotifyDefaultCredentialStore] to store/retrieve this token.
     *
     * @param spotifyApi Valid, usable [SpotifyImplicitGrantApi] that you can use to make requests.
     */
    public abstract fun onSuccessfulAuthentication(spotifyApi: SpotifyImplicitGrantApi)

    /**
     * Override this to define what to do after authentication has failed. You may want to use [SpotifyDefaultCredentialStore] to remove any stored token.
     */
    public abstract fun onAuthenticationFailed(errorMessage: String)


    /**
     * Override this to define what to do after [onSuccessfulAuthentication] has run.
     * The default behavior is to finish the activity, and redirect the user back to the activity set on [SpotifyDefaultAuthHelper.activityBack]
     * only if [guardValidSpotifyApi] has been used or if [SpotifyDefaultAuthHelper.activityBack] has been set.
     */
    public open fun redirectAfterOnSuccessAuthentication() {
        if (useDefaultRedirectHandler && SpotifyDefaultAuthHelper.activityBack != null) {
            startActivity(Intent(this, SpotifyDefaultAuthHelper.activityBack))
            SpotifyDefaultAuthHelper.activityBack = null
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        triggerLoginActivity()
    }

    /**
     * Trigger the actual spotify-auth login activity to authenticate the user.
     */
    public fun triggerLoginActivity() {
        val authorizationRequest = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)
            .setScopes(getRequestingScopes().map { it.uri }.toTypedArray())
            .setState(state.toString())
            .build()

        AuthorizationClient.openLoginActivity(this, state, authorizationRequest)
    }

    /**
     * Processes the result of [LoginActivity], invokes callbacks, then finishes.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == state) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val token = Token(
                        response.accessToken,
                        response.type.name,
                        response.expiresIn
                    )
                    val api = spotifyImplicitGrantApi(
                        clientId = clientId,
                        token = token
                    )

                    onSuccessfulAuthentication(api)
                    redirectAfterOnSuccessAuthentication()
                }
                //AuthorizationResponse.Type.CODE -> TODO()
                //AuthorizationResponse.Type.UNKNOWN -> TODO()
                AuthorizationResponse.Type.ERROR -> onAuthenticationFailed(response.error)
                AuthorizationResponse.Type.EMPTY -> onAuthenticationFailed(response.error)
            }
            finish()
        }

    }
}