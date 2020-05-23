/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package appApi

import com.adamratzman.spotify.endpoints.public.TrackAttribute
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Locale
import com.adamratzman.spotify.utils.Market

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get and print all Spotify genre seeds, along with the total amount of seeds
    val seeds = api.browse.getAvailableGenreSeeds().complete()

    println("Total seeds: ${seeds.size} | Seeds: $seeds")

    // get and print top 20 Spotify categories
    println(api.browse.getCategoryList(limit = 20).complete().items.map { it.name })

    // get and print all Spotify categories in the French market, with German strings
    println(api.browse.getCategoryList(market = Market.FR, locale = Locale.de_DE).getAllItems().complete().filterNotNull().map { "${it.name} (${it.id})" })

    // get the "Pop" category
    println(api.browse.getCategory("pop").complete())

    // get 35 new featured album releases, starting with the 4th featured album (index=3)
    println(api.browse.getNewReleases(limit = 35, offset = 3).getAllItems().complete().filterNotNull().map { release -> "${release.name} by ${release.artists.map { it.name }}" })

    // get available genre seeds
    println(api.browse.getAvailableGenreSeeds().complete())

    // get featured playlists message
    println(api.browse.getFeaturedPlaylists().complete().message)

    // get and print recommendation seeds and tracks
    val recommendationResponse = api.browse.getTrackRecommendations(
            seedTracks = listOf("spotify:track:2LYAG9jlH9rul11nalRxR0"),
            seedGenres = listOf("indie-pop"),
            minAttributes = listOf(TrackAttribute.create(TuneableTrackAttribute.Danceability, 0.6f))
    ).complete()

    println("Recommendation tracks: ${recommendationResponse.tracks.map { track -> "${track.name} by ${track.artists.map { it.name }}" }}")
}
