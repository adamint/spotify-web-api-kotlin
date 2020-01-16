/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package public

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi
import com.adamratzman.spotify.endpoints.public.SearchApi.SearchType.ALBUM
import com.adamratzman.spotify.endpoints.public.SearchApi.SearchType.TRACK

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // search tracks and albums with the word "name" in them
    println(api.search.search("name", TRACK, ALBUM).complete())

    // get the first result of playlist "Today's Top Hits"
    println(api.search.searchPlaylist("Today's Top Hits").complete().items[0])
}
