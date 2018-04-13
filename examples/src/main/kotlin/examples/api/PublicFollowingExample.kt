package examples.api

import api
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market

fun getUsersFollowingPlaylistBlocking() {
    try {
        println(api.publicFollowing.doUsersFollowPlaylist("jmperezperez", "3cEYpjA9oz9GiPac4AsH4n", "jmperezperez", "elogain").complete())
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }

}