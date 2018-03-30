package com.adamratzman.endpoints.priv.follow

import com.adamratzman.main.clientApi
import org.junit.Test

import org.junit.Assert.*

class FollowingAPITest {
    @Test
    fun followingUsers() {
        println(clientApi.userFollowing.followingUsers("adamratzman"))
    }

    @Test
    fun getFollowedArtists() {
        println(clientApi.userFollowing.getFollowedArtists())
    }

    @Test
    fun followingArtists() {
        println(clientApi.userFollowing.followingArtists("7wjeXCtRND2ZdKfMJFu6JC"))
    }

}