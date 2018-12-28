import com.adamratzman.spotify.main.spotifyApi
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.Track

fun main(args:Array<String>){
    val api = spotifyApi {
        credentials {
            clientId  = "79d455af5aea45c094c5cea04d167ac1"
            clientSecret = "2ed0a8e3a946471e928c5e80b83c184d"
        }
    }.buildCredentialed()

    api.search.searchTrack("High Hopes", 4, market = Market.US).queue { items ->
        items.
    }
}