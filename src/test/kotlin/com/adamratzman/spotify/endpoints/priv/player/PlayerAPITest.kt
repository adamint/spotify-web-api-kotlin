package com.adamratzman.spotify.kotlin.endpoints.priv.player

import com.adamratzman.spotify.endpoints.priv.player.PlayerAPI
import com.adamratzman.spotify.main.clientApi
import org.junit.Test

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

    @Test
    fun pausePlayback() {
        println(clientApi.player.pausePlayback())
    }

    @Test
    fun seekPosition() {
        println(clientApi.player.seekPosition(2))
    }

    @Test
    fun setRepeatMode() {
        println(clientApi.player.setRepeatMode(PlayerAPI.PlayerRepeatState.CONTEXT))
    }

    @Test
    fun setVolume() {
        println(clientApi.player.setVolume(100))
    }

    @Test
    fun skipToNextTrack() {
        println(clientApi.player.skipToNextTrack())
    }

    @Test
    fun rewindToLastTrack() {
        println(clientApi.player.rewindToLastTrack())
    }

    @Test
    fun startPlayback() {
        println(clientApi.player.startPlayback())
    }

    @Test
    fun shufflePlayback() {
        println(clientApi.player.shufflePlayback())
    }

    @Test
    fun transferPlayback() {
        println(clientApi.player.transferPlayback("this_is_a_fake_device"))
    }

}