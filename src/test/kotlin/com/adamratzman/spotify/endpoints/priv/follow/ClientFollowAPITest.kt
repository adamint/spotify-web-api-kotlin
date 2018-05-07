package com.adamratzman.spotify.kotlin.endpoints.priv.follow

import clientApi
import org.junit.Test

class ClientFollowAPITest {
    @Test
    fun isFollowingUser() {
        clientApi.clientFollowing.isFollowingUser("asdfasdfasdfsfasdfasdf").complete().let { println(it) }
    }

    @Test
    fun isFollowingArtist() {
        println(clientApi.clientFollowing.isFollowingArtist("4IS4EyXNmiI2w5SRCjMtEF").complete())
    }

    @Test
    fun followingUsers() {
        println(clientApi.clientFollowing.isFollowingUsers("adamratzman").complete())
    }

    @Test
    fun getFollowedArtists() {
        println(clientApi.clientFollowing.getFollowedArtists().complete())
    }

    @Test
    fun followingArtists() {
        println(clientApi.clientFollowing.isFollowingArtists("7wjeXCtRND2ZdKfMJFu6JC").complete())
    }

    @Test
    fun followUsers() {
        println(clientApi.clientFollowing.followUsers("adamratzman").complete())
    }

    @Test
    fun followArtists() {
        println(clientApi.clientFollowing.followArtists("6rfl53MP8HSoiugpqzA50Zh").complete())
    }

    @Test
    fun followPlaylist() {
        println(clientApi.clientFollowing.followPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh").complete())
    }

    @Test
    fun unfollowUsers() {
        println(clientApi.clientFollowing.unfollowUsers("adamratzman").complete())
    }

    @Test
    fun unfollowArtists() {
        println(clientApi.clientFollowing.unfollowArtists("6rl53MP8HSoiugpqzA50Zh").complete())
    }

    @Test
    fun unfollowPlaylist() {
        println(clientApi.clientFollowing.unfollowPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh").complete())
    }

}