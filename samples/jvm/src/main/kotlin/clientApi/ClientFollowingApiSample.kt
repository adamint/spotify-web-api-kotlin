/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package clientApi

import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.spotifyClientApi

@SpotifyExperimentalHttpApi
fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI"),
            SpotifyUserAuthorization(tokenString = System.getenv("SPOTIFY_TOKEN_STRING")))
            .build()

    // see if you're following playlist "I'm Good Radio"
    println(api.following.isFollowingPlaylist("com.adamratzman.spotify:playlist:37i9dQZF1E8L0j2hm3kP31").complete())

    // see if you're following users adamratzman1 and adamratzman
    println(api.following.isFollowingUsers("adamratzman1", "adamratzman").complete())

    // see if you're following artist Louane
    println(api.following.isFollowingArtist("com.adamratzman.spotify:artist:7wjeXCtRND2ZdKfMJFu6JC").complete())

    // get all followed artists, limiting 1 each request
    println(api.following.getFollowedArtists(limit = 1).getAllItems().complete())

    // follow and unfollow, if you weren't previously following, the artist Louane

    val isFollowingLouane = api.following.isFollowingArtist("com.adamratzman.spotify:artist:7wjeXCtRND2ZdKfMJFu6JC").complete()

    if (isFollowingLouane) api.following.unfollowArtist("com.adamratzman.spotify:artist:7wjeXCtRND2ZdKfMJFu6JC").complete()
    api.following.followArtist("com.adamratzman.spotify:artist:7wjeXCtRND2ZdKfMJFu6JC").complete()

    if (!isFollowingLouane) api.following.unfollowArtist("com.adamratzman.spotify:artist:7wjeXCtRND2ZdKfMJFu6JC").complete()

    // follow and unfollow, if you weren't previously following, the user adamratzman1

    val isFollowingAdam = api.following.isFollowingUser("adamratzman").complete()

    if (isFollowingAdam) api.following.unfollowUser("adamratzman").complete()
    api.following.followUser("adamratzman").complete()

    if (!isFollowingAdam) api.following.unfollowUser("adamratzman").complete()

    // follow and unfollow, if you weren't previously following, the playlist Today's Top Hits

    val isFollowingTodaysTopHits = api.following.isFollowingPlaylist("com.adamratzman.spotify:playlist:37i9dQZF1DXcBWIGoYBM5M").complete()

    if (isFollowingTodaysTopHits) api.following.unfollowPlaylist("com.adamratzman.spotify:playlist:37i9dQZF1DXcBWIGoYBM5M").complete()
    api.following.followPlaylist("com.adamratzman.spotify:playlist:37i9dQZF1DXcBWIGoYBM5M").complete()

    if (!isFollowingTodaysTopHits) api.following.unfollowPlaylist("com.adamratzman.spotify:playlist:37i9dQZF1DXcBWIGoYBM5M").complete()
}
