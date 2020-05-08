package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.models.Token
import org.w3c.dom.url.URLSearchParams
import kotlin.browser.window

/**
 * Parse the current url into a valid [Token] to be used when instantiating a new [SpotifyImplicitGrantApi]
 */
fun parseSpotifyCallbackHashToToken() = parseSpotifyCallbackHashToToken(window.location.hash.substring(1))

/**
 * Parse the hash string into a valid [Token] to be used when instantiating a new [SpotifyImplicitGrantApi]
 *
 * @param hashString The Spotify hash string containing access_token, token_type, and expires_in.
 */
fun parseSpotifyCallbackHashToToken(hashString: String): Token {
    val hash = URLSearchParams(hashString)

    return Token(
            hash.get("access_token") ?: throw IllegalStateException("access_token is not part of the hash!"),
            hash.get("token_type") ?: throw IllegalStateException("token_type is not part of the hash!"),
            hash.get("expires_in")?.toIntOrNull() ?: throw IllegalStateException("expires_in is not part of the hash!")
    )
}