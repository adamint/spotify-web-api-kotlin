import com.adamratzman.spotify.main.spotifyApi
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.Track

fun main(args:Array<String>){
    val api = spotifyApi {
        credentials {
            clientId  = ""
            clientSecret = ""
        }
    }.buildCredentialed()

    api.search.searchTrack("High Hopes", 4, market = Market.US).queue { items ->
        items
    }
}