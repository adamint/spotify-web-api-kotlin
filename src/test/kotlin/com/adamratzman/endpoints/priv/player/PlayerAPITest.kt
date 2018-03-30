package com.adamratzman.endpoints.priv.player

import com.adamratzman.main.clientApi
import org.junit.Test

import org.junit.Assert.*

class PlayerAPITest {
    @Test
    fun getDevices() {
        println(clientApi.player.getDevices())
    }

    @Test
    fun getCurrentlyPlaying() {
        println(clientApi.player.getCurrentlyPlaying())
    }

    @Test
    fun getCurrentContext() {
        println(clientApi.player.getCurrentContext())
    }

    @Test
    fun getRecentlyPlayed() {
        println(clientApi.player.getRecentlyPlayed())
    }

}