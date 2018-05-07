package com.adamratzman.spotify.kotlin.endpoints.pub.follow

import api
import org.junit.Test

class PublicClientFollowAPITest {
    @Test
    fun doUsersFollowPlaylist() {
        println(api.publicFollowing.doUsersFollowPlaylist("jmperezperez", "3cEYpjA9oz9GiPac4AsH4n", "jmperezperez", "elogasdfjjadsfain").complete())
    }

}