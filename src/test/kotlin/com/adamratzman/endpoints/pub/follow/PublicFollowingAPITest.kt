package com.adamratzman.endpoints.pub.follow

import com.adamratzman.main.api
import org.junit.Test

import org.junit.Assert.*

class PublicFollowingAPITest {
    @Test
    fun doUsersFollowPlaylist() {
        println(api.publicFollowing.doUsersFollowPlaylist("jmperezperez", "3cEYpjA9oz9GiPac4AsH4n", "jmperezperez", "elogain"))
    }

}