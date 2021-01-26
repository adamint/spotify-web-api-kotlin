package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.models.SpotifyTrackUri
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPlayerApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testGetDevices() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.player.getDevices().isNotEmpty())
        }
    }

    @Test
    fun testGetCurrentContext() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()
            api.player.startPlayback(
                playableUrisToPlay = listOf(SpotifyTrackUri("spotify:track:6WcinC5nKan2DMFUfjVerX")),
                deviceId = device.id
            )
            val getCurrentContext = suspend { api.player.getCurrentContext() }
            var context = getCurrentContext()
            assertTrue(context != null && context.isPlaying && context.track?.id == "6WcinC5nKan2DMFUfjVerX")
            api.player.pause()
            context = getCurrentContext()!!

            assertTrue(!context.isPlaying && context.track?.id != null)

            val playlist = api.playlists.getPlaylist("37i9dQZF1DXcBWIGoYBM5M")!!
            api.player.startPlayback(
                collectionUri = playlist.uri
            )
            context = getCurrentContext()
            assertTrue(context != null && context.isPlaying && context.track?.id == playlist.tracks.items.first().track!!.id)
            api.player.pause()


        }
    }

    @Test
    fun testGetRecentlyPlayed() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()
            api.player.getRecentlyPlayed()
        }
    }


}