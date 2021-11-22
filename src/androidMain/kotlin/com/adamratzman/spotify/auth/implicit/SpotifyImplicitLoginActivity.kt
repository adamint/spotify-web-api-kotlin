/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.auth.implicit

import android.app.Activity
import android.content.Intent
import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore.Companion.activityBackOnImplicitAuth
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi
import com.adamratzman.spotify.utils.logToConsole
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity

/**
 * Wrapper around spotify-auth's [LoginActivity] that allows configuration of the authentication process, along with
 * callbacks on successful and failed authentication. Pair this with [SpotifyDefaultCredentialStore] to easily store credentials.
 * To use, you must extend from either [AbstractSpotifyAppImplicitLoginActivity] or [AbstractSpotifyAppCompatImplicitLoginActivity]
 *
 * @property state The state to use to verify the login request.
 * @property clientId Your application's Spotify client id.
 * @property clientId Your application's Spotify client secret.
 * @property redirectUri Your application's Spotify redirect id - NOTE that this should be an android scheme (such as spotifyapp://authback)
 * and that this must be registered in your manifest.
 * @property useDefaultRedirectHandler Disable if you will not be using [useDefaultRedirectHandler] but will be setting [SpotifyDefaultImplicitAuthHelper.activityBackOnImplicitAuth].
 */
public interface SpotifyImplicitLoginActivity {
    public val activity: Activity

    public val state: Int
    public val clientId: String
    public val redirectUri: String
    public val useDefaultRedirectHandler: Boolean

    /**
     * Return the scopes that you are going to request from the user here.
     */
    public fun getRequestingScopes(): List<SpotifyScope>

    /**
     * Override this to define what to do after authentication has been successfully completed. A valid, usable
     * [spotifyApi] is provided to you. You may likely want to use [SpotifyDefaultCredentialStore] to store/retrieve this token.
     *
     * @param spotifyApi Valid, usable [SpotifyImplicitGrantApi] that you can use to make requests.
     */
    public fun onSuccess(spotifyApi: SpotifyImplicitGrantApi)

    /**
     * Override this to define what to do after authentication has failed. You may want to use [SpotifyDefaultCredentialStore] to remove any stored token.
     */
    public fun onFailure(errorMessage: String)

    /**
     * Override this to define what to do after [onSuccess] has run.
     * The default behavior is to finish the activity, and redirect the user back to the activity set on [SpotifyDefaultCredentialStore.activityBackOnImplicitAuth]
     * only if [guardValidImplicitSpotifyApi] has been used or if [SpotifyDefaultCredentialStore.activityBackOnImplicitAuth] has been set.
     */
    public fun redirectAfterOnSuccessAuthentication() {
        if (useDefaultRedirectHandler && activityBackOnImplicitAuth != null) {
            activity.startActivity(Intent(activity, activityBackOnImplicitAuth))
            activityBackOnImplicitAuth = null
        }
        activity.finish()
    }

    /**
     * Trigger the actual spotify-auth login activity to authenticate the user.
     */
    public fun triggerLoginActivity() {
        val authorizationRequest = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)
            .setScopes(getRequestingScopes().map { it.uri }.toTypedArray())
            .setState(state.toString())
            .build()
        logToConsole("Triggering spotify-auth login for url ${authorizationRequest.toUri().path}")
        AuthorizationClient.openLoginActivity(activity, state, authorizationRequest)
    }

    /**
     * Processes the result of [LoginActivity], invokes callbacks, then finishes.
     */
    public fun processActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == state) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            logToConsole("Got implicit auth response of ${response.type}")
            when {
                response.type == AuthorizationResponse.Type.TOKEN -> {
                    val token = Token(
                        response.accessToken,
                        response.type.name,
                        response.expiresIn
                    )
                    val api = spotifyImplicitGrantApi(
                        clientId = clientId,
                        token = token
                    )
                    logToConsole("Built implicit grant api. Executing success handler..")
                    onSuccess(api)
                    redirectAfterOnSuccessAuthentication()
                }
                // AuthorizationResponse.Type.CODE -> TODO()
                // AuthorizationResponse.Type.UNKNOWN -> TODO()
                response.type == AuthorizationResponse.Type.ERROR -> {
                    logToConsole("Got error in authorization... executing error handler")
                    onFailure(response.error ?: "Generic authentication error")
                }
                response.type == AuthorizationResponse.Type.EMPTY -> {
                    logToConsole("Got empty authorization... executing error handler")
                    onFailure(response.error ?: "Authentication empty")
                }
            }
            activity.finish()
        }
    }
}
