/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.auth.pkce

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.adamratzman.spotify.R
import com.adamratzman.spotify.SpotifyApiOptions
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.adamratzman.spotify.auth.getDefaultCredentialStore
import com.adamratzman.spotify.getSpotifyPkceAuthorizationUrl
import com.adamratzman.spotify.getSpotifyPkceCodeChallenge
import com.adamratzman.spotify.spotifyClientPkceApi
import com.adamratzman.spotify.utils.logToConsole
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

/**
 * This class hooks into spotify-web-api-kotlin to provide PKCE authorization for Android application. Paired with [SpotifyDefaultCredentialStore] to easily store credentials.
 * To use, you must extend this class and follow the instructions in the spotify-web-api-kotlin README.
 *
 * @property state The state to use to verify the login request.
 * @property clientId Your application's Spotify client id.
 * @property clientId Your application's Spotify client secret.
 * @property redirectUri Your application's Spotify redirect id - NOTE that this should be an android scheme (such as spotifyapp://authback)
 * and that this must be registered in your manifest.
 * @property scopes the scopes that you are going to request from the user here.
 * @property pkceCodeVerifier The code verifier generated that the client will be authenticated with (using its code challenge).
 * Must be between 43-128 alphanumeric characters
 * @property options Provide if you would like to customize the returned [SpotifyClientApi].
 */
@RequiresApi(Build.VERSION_CODES.M)
public abstract class AbstractSpotifyPkceLoginActivity : AppCompatActivity() {
    public abstract val clientId: String
    public abstract val redirectUri: String
    public abstract val scopes: List<SpotifyScope>
    public open val pkceCodeVerifier: String = (0..96).joinToString("") {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random().toString()
    }
    public open val state: String = Random.nextLong().toString()
    public open val options: ((SpotifyApiOptions).() -> Unit)? = null

    private lateinit var authorizationIntent: Intent
    private lateinit var credentialStore: SpotifyDefaultCredentialStore

    /**
     * Get the code challenge for the [pkceCodeVerifier] that will be used to confirm token identity.
     */
    public fun getPkceCodeChallenge(): String = getSpotifyPkceCodeChallenge(pkceCodeVerifier)

    /**
     * Get the authorization url that the client will be redirected to during PKCE authorization.
     */
    public fun getAuthorizationUrl(): Uri = getSpotifyPkceAuthorizationUrl(
        *scopes.toTypedArray(),
        clientId = clientId,
        redirectUri = redirectUri,
        codeChallenge = getPkceCodeChallenge(),
        state = state
    ).let { Uri.parse(it) }

    /**
     * The callback that will be executed after successful PKCE authorization.
     *
     * @param api The built [SpotifyClientApi] corresponding to the retrieved token from PKCE auth.
     */
    public abstract fun onSuccess(api: SpotifyClientApi)

    /**
     * The callback that will be executed after unsuccessful PKCE authorization.
     *
     * @param exception The root cause of the auth failure.
     */
    public abstract fun onFailure(exception: Exception)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spotify_pkce_auth_layout)

        credentialStore = application.getDefaultCredentialStore(clientId, redirectUri)

        // This activity is recreated on every launch, therefore we need to make sure not to
        // launch the activity when a Spotify intent result has been received
        if (intent?.isSpotifyPkceAuthIntent(redirectUri) == false) {
            authorizationIntent = Intent(Intent.ACTION_VIEW, getAuthorizationUrl())
            credentialStore.currentSpotifyPkceCodeVerifier = pkceCodeVerifier
            startActivity(authorizationIntent)
            finish()
        }
    }

    /**
     * User has accepted Spotify permissions at the website and has been redirected to the app, though the app was not open
     */
    override fun onResume() {
        super.onResume()
        if (intent?.isSpotifyPkceAuthIntent(redirectUri) == true) {
            runBlocking { handleSpotifyAuthenticationResponse(AuthorizationResponse.fromUri(intent?.data)) }
        }
    }

    /**
     * User accepted Spotify permissions at the website and has been redirected to the app
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.data != null) setIntent(intent)
    }

    /**
     * Handle the authentication response, only allowing a "code" as response type
     */
    private suspend fun handleSpotifyAuthenticationResponse(response: AuthorizationResponse) {
        logToConsole("Got pkce auth response of ${response.type}")
        if (response.type != AuthorizationResponse.Type.CODE) {
            if (response.type == AuthorizationResponse.Type.TOKEN ||
                response.type == AuthorizationResponse.Type.ERROR ||
                response.type == AuthorizationResponse.Type.EMPTY ||
                response.type == AuthorizationResponse.Type.UNKNOWN
            ) {
                logToConsole("Got invalid response type... executing error handler")
                onFailure(
                    IllegalStateException("Received response type ${response.type} which is not code.")
                )
            }

            finish()
        } else {
            val authorizationCode = response.code
            if (authorizationCode.isNullOrBlank()) {
                logToConsole("Auth code was null or blank... executing error handler")
                onFailure(
                    IllegalStateException("Authorization code was null or blank.")
                )
            } else {
                try {
                    logToConsole("Building client PKCE api...")
                    setProgressBarVisible()
                    val api = spotifyClientPkceApi(
                        clientId = clientId,
                        redirectUri = redirectUri,
                        authorization = SpotifyUserAuthorization(
                            authorizationCode = authorizationCode,
                            pkceCodeVerifier = credentialStore.currentSpotifyPkceCodeVerifier
                        ),
                        options ?: {}
                    ).build()

                    logToConsole("Successfully built client PKCE api")
                    if (api.token.accessToken.isNotBlank()) {
                        credentialStore.spotifyToken = api.token
                        setProgressBarInvisible()
                        logToConsole("Successful PKCE auth. Executing success handler..")
                        onSuccess(api)
                    } else {
                        setProgressBarInvisible()
                        logToConsole("Failed PKCE auth - API token was blank. Executing success handler..")
                        onFailure(
                            IllegalArgumentException("API token was blank")
                        )
                    }
                } catch (exception: Exception) {
                    setProgressBarInvisible()
                    logToConsole("Got error in authorization... executing error handler")
                    onFailure(exception)
                }
            }

            setProgressBarInvisible()
            finish()
        }
    }

    private fun setProgressBarInvisible() {
        findViewById<FrameLayout>(R.id.progress_overlay).visibility = View.INVISIBLE
    }

    private fun setProgressBarVisible() {
        findViewById<FrameLayout>(R.id.progress_overlay).visibility = View.VISIBLE
    }
}
