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

public abstract class AbstractSpotifyLoginActivity : Activity() {
    public abstract val state: Int
    public abstract val clientId: String
    public abstract val redirectUri: String
    public abstract val useDefaultRedirectHandler: Boolean
    public abstract fun getRequestingScopes(): List<SpotifyScope>

    public abstract fun onSuccessfulAuthentication(spotifyApi: SpotifyImplicitGrantApi)
    public abstract fun onAuthenticationFailed(errorMessage: String)
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

    public fun triggerLoginActivity() {
        val authorizationRequest =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)
                .setScopes(getRequestingScopes().map { it.uri }.toTypedArray())
                .setState(state.toString())
                .build()
        AuthorizationClient.openLoginActivity(this, state, authorizationRequest)
    }

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
                    ) {
                        refreshTokenProducer = { throw SpotifyException.ReAuthenticationNeededException() }
                    }

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