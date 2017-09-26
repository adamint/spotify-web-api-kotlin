package endpoints.profiles

import main.SpotifyAPI
import main.toObject
import obj.Endpoint
import obj.SpotifyPublicUser

class ProfilesAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getProfile(userId: String): SpotifyPublicUser {
        return get("https://api.spotify.com/v1/users/$userId").toObject()
    }
}