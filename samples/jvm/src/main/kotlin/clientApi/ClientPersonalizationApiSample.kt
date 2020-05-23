/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package clientApi

import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi.TimeRange.LONG_TERM
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi.TimeRange.SHORT_TERM
import com.adamratzman.spotify.spotifyClientApi

fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI"),
            SpotifyUserAuthorization(tokenString = System.getenv("SPOTIFY_TOKEN_STRING")))
            .build()

    // print your top 30 artists over the last few years
    println(api.personalization.getTopArtists(30, timeRange = LONG_TERM).complete().items.map { it.name })

    // print your top 20 tracks in the last 4 weeks
    println(api.personalization.getTopTracks(timeRange = SHORT_TERM).complete().items.map { track -> "${track.name} by ${track.artists.joinToString(", ") { it.name }}" })
}
