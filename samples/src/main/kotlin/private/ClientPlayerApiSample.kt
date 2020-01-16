/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package private

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyClientApi
import com.adamratzman.spotify.annotations.SpotifyExperimentalFunctionApi
import com.adamratzman.spotify.endpoints.client.ClientPlayerApi.PlayerRepeatState.TRACK

@SpotifyExperimentalFunctionApi
fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI")) {
        authorization {
            tokenString = System.getenv("SPOTIFY_TOKEN_STRING")
        }
    }.build()

    // print all devices connected to this Spotify account
    println(api.player.getDevices().complete())

    // get the current context track position
    println(api.player.getCurrentContext().complete()?.progressMs)

    // get the 20 most recently played (there is a small lag) tracks
    println(api.player.getRecentlyPlayed().complete().map { it.track.name })

    // get the currenty played PlaybackActions
    println(api.player.getCurrentlyPlaying().complete()?.actions)

    // play song "I'm Good" by the Mowgli's
    api.player.startPlayback(tracksToPlay = listOf(api.search.searchTrack("I'm Good the Mowgli's").complete()[0].uri.uri)).complete()

    // pause playback on the current device.
    api.player.pause().complete()

    // seek the beginning of the track currently playing
    api.player.seek(0).complete()

    // resume playback
    api.player.resume().complete()

    // set repeat the current track
    api.player.setRepeatMode(TRACK).complete()

    // set volume to 50%
    api.player.setVolume(50).complete()

    // skip to the next track
    api.player.skipForward().complete()

    // skip back to the last track that was in the user queue
    api.player.skipBehind().complete()

    // toggle shuffling
    api.player.toggleShuffle(shuffle = true).complete()

    // transfer playback
    api.player.transferPlayback(api.player.getDevices().complete().first().id!!).complete()
}
