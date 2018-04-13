package com.adamratzman.spotify.kotlin.endpoints.pub.follow

import com.adamratzman.spotify.main.api
import org.junit.Test

class PublicFollowingAPITest {
    @Test
    fun doUsersFollowPlaylist() {
        println(api.publicFollowing.doUsersFollowPlaylist("jmperezperez", "3cEYpjA9oz9GiPac4AsH4n", "jmperezperez", "elogain").complete())
    }

}