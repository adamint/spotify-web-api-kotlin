import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyClientAPI

val id = ""
val secret = ""
val token = ""
val clientApi = SpotifyClientAPI.Builder(id, secret, "https://apple.com")
        .buildToken(token)
val api = SpotifyAPI.Builder(id, secret).build()