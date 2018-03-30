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

    @Test
    fun followUsers() {
        println(clientApi.userFollowing.followUsers("adamratzman"))
    }

    @Test
    fun followArtists() {
        println(clientApi.userFollowing.followArtists("6rl53MP8HSoiugpqzA50Zh"))
    }

    @Test
    fun followPlaylist() {
        println(clientApi.userFollowing.followPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh"))
    }

    @Test
    fun unfollowUsers() {
        println(clientApi.userFollowing.unfollowUsers("adamratzman"))
    }

    @Test
    fun unfollowArtists() {
        println(clientApi.userFollowing.unfollowArtists("6rl53MP8HSoiugpqzA50Zh"))
    }

    @Test
    fun unfollowPlaylist() {
        println(clientApi.userFollowing.unfollowPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh"))
    }

}