package endpoints.profiles

import junit.framework.TestCase
import main.SpotifyAPI

class ProfilesAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()

    fun testGetProfile() {
        println(api.profiles.getProfile("adamratzman1"))
    }

}