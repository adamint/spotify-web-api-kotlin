/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package private

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyClientApi

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

    // create and print a new playlist called "Test Playlist"
    val playlist = api.playlists.createClientPlaylist(
            "Test Playlist",
            "My test playlist description",
            true
    ).complete()

    println(playlist)

    // add "Reflections" by MisterWives to the end of your newly-created playlist
    api.playlists.addTrackToClientPlaylist(playlist.id, "spotify:track:2PtBhfoPZ6VYtXkrE5FrCH").complete()

    // change the playlist description to "My awesome playlist"
    api.playlists.changeClientPlaylistDetails(playlist.id, description = "My awesome playlist").complete()

    // get all your client playlists
    println(api.playlists.getClientPlaylists(limit = 50).getAllItems().complete())

    // get the number of tracks in a client playlist
    println(api.playlists.getClientPlaylist(playlist.id).complete()?.tracks?.total)

    // see https://developer.spotify.com/documentation/web-api/reference/playlists/reorder-playlists-tracks/
    // for an example of re-ordering tracks

    // replace the tracks in a client playlist with two songs by Lorde
    api.playlists.setClientPlaylistTracks(playlist.id, "spotify:track:6ie2Bw3xLj2JcGowOlcMhb", "spotify:track:2dLLR6qlu5UJ5gk0dKz0h3").complete()

    // remove the song we just added from our client playlist
    api.playlists.removeTrackFromClientPlaylist(playlist.id, "spotify:track:6ie2Bw3xLj2JcGowOlcMhb").complete()

    // remove all playlist tracks
    api.playlists.removeAllClientPlaylistTracks(playlist.id)

    // upload a client playlist cover
    api.playlists.uploadClientPlaylistCover(playlist.id, imageUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png").complete()

    // delete a client playlist
    api.playlists.deleteClientPlaylist(playlist.id).complete()
}
