package obj

import main.SpotifyAPI
import main.gson
import org.jsoup.Jsoup

abstract class Endpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        var connection = Jsoup.connect(url).ignoreContentType(true)
        if (api.token != null) connection = connection.header("Authorization", "Bearer ${api.token.access_token}")
        val document = connection.ignoreHttpErrors(true).execute()
        if (document.statusCode() != 200) {
            throw BadRequestException(gson.fromJson(document.body().removePrefix("{\n  \"error\" : ").removeSuffix("}"), ErrorObject::class.java))
        }
        return document.body()
    }
}


enum class Market(var code: String) {
    US("US")
}

data class LinkedResult<out T>(val href: String, val items: List<T>)

data class MessageResult<out T>(val message: String?, val playlists: PagingObject<T>)

data class PagingObject<out E>(val href: String, val items: List<E>, val limit: Int, val next: String?, val offset: Int, val previous: String?, val total: Int)