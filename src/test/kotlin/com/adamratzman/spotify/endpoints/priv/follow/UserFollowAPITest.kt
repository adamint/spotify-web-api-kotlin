package com.adamratzman.spotify.kotlin.endpoints.priv.follow

import com.adamratzman.spotify.main.clientApi
import org.junit.Test

class UserFollowAPITest {
    @Test
    fun isFollowingUser() {
        clientApi.userFollowing.isFollowingUser("asdfasdfasdfsfasdfasdf").complete().let { println(it) }
    }

    @Test
    fun isFollowingArtist() {
        println(clientApi.userFollowing.isFollowingArtist("4IS4EyXNmiI2w5SRCjMtEF").complete())
    }

    @Test
    fun followingUsers() {
        println(clientApi.userFollowing.isFollowingUsers("adamratzman").complete())
    }

    @Test
    fun getFollowedArtists() {
        println(clientApi.userFollowing.getFollowedArtists().complete())
    }

    @Test
    fun followingArtists() {
        println(clientApi.userFollowing.isFollowingArtists("7wjeXCtRND2ZdKfMJFu6JC").complete())
    }

    @Test
    fun followUsers() {
        println(clientApi.userFollowing.followUsers("adamratzman").complete())
    }

    @Test
    fun followArtists() {
        println(clientApi.userFollowing.followArtists("6rfl53MP8HSoiugpqzA50Zh").complete())
    }

    @Test
    fun followPlaylist() {
        println(clientApi.userFollowing.followPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh").complete())
    }

    @Test
    fun unfollowUsers() {
        println(clientApi.userFollowing.unfollowUsers("adamratzman").complete())
    }

    @Test
    fun unfollowArtists() {
        println(clientApi.userFollowing.unfollowArtists("6rl53MP8HSoiugpqzA50Zh").complete())
    }

    @Test
    fun unfollowPlaylist() {
        println(clientApi.userFollowing.unfollowPlaylist("digster.fr", "495KyiFkFAlBdD98jvw7fh").complete())
    }

}