/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package appApi

import com.adamratzman.spotify.spotifyAppApi

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get artist Cody G's total follower count
    println(api.artists.getArtist("com.adamratzman.spotify:artist:26HkLAAIMh5qOFet57d1rg").complete()!!.followers.total)

    // get artist Cody G and a non-existant artist
    println(api.artists.getArtists("com.adamratzman.spotify:artist:26HkLAAIMh5qOFet57d1rg", "nonexistantartist"))

    // get all of artist Cody G's albums
    println(api.artists.getArtistAlbums("com.adamratzman.spotify:artist:26HkLAAIMh5qOFet57d1rg").getAllItems().complete().filterNotNull().map { it.name })

    // get Cody G's top tracks, in descending order, in France
    println(api.artists.getArtistTopTracks("com.adamratzman.spotify:artist:26HkLAAIMh5qOFet57d1rg").complete().map { it.name })

    // get Cody G's related artists
    println(api.artists.getRelatedArtists("com.adamratzman.spotify:artist:26HkLAAIMh5qOFet57d1rg").complete().map { it.name })
}
