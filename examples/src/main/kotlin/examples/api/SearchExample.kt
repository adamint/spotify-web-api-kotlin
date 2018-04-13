package examples.api


import api
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market
import java.util.concurrent.TimeUnit

fun getSearchArtistBlocking() {
    try {
        val search = api.search.searchArtist("amir", limit = 10, market = Market.FR).complete()
        println(search.items[0].name)
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }
}

fun getSearchTrackAsync() {
    try {
        api.search.searchTrack("quand je serai grand", market = Market.FR).queueAfter(666, TimeUnit.MILLISECONDS, {
            println(it.items)
        })
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}