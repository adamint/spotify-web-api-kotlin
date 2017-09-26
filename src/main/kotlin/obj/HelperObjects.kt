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

data class SimpleTrackLinkedResult(val href: String, val items: List<SimpleTrack>)
data class SimpleAlbumLinkedResult(val href: String, val items: List<SimpleAlbum>)
data class PlaylistTrackLinkedResult(val href: String, val items: List<PlaylistTrack>)

data class MessageResult<out T>(val message: String?, val playlists: PlaylistPagingObject)

data class PlaylistPagingObject(val href: String, val items: List<Playlist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class PlaylistTrackPagingObject(val href: String, val items: List<PlaylistTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimpleTrackPagingObject(val href: String, val items: List<SimpleTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class ArtistPagingObject(val href: String, val items: List<Artist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimpleAlbumPagingObject(val href: String, val items: List<SimpleAlbum>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SpotifyCategoryPagingObject(val href: String, val items: List<SpotifyCategory>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class AlbumPagingObject(val href: String, val items: List<Album>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimplePlaylistPagingObject(val href: String, val items: List<SimplePlaylist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
