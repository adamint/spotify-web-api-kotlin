package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.ClientPlayerApi
import com.adamratzman.spotify.models.CollectionUri
import com.adamratzman.spotify.models.PlayHistoryContext
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.SpotifyTrackUri
import com.adamratzman.spotify.runBlockingTest
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
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
            delay(1000)
            val getCurrentContext = suspend { api.player.getCurrentContext() }
            var context = getCurrentContext()
            assertTrue(context != null && context.isPlaying && context.track?.id == "6WcinC5nKan2DMFUfjVerX")
            api.player.pause()
            context = getCurrentContext()!!

            assertTrue(!context.isPlaying)
            assertNotNull(context.track?.id)

            val playlist = api.playlists.getPlaylist("37i9dQZF1DXcBWIGoYBM5M")!!
            api.player.startPlayback(
                collectionUri = playlist.uri
            )
            delay(1000)
            context = getCurrentContext()
            assertTrue(context != null && context.isPlaying && context.track?.id == playlist.tracks.items.first().track!!.id)
            api.player.pause()
        }
    }

    @Test
    fun testGetRecentlyPlayed() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            api.player.getRecentlyPlayed()
        }
    }

    @Test
    fun testGetCurrentlyPlaying() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()

            val trackId = "7lPN2DXiMsVn7XUKtOW1CS"
            api.player.startPlayback(
                playableUrisToPlay = listOf(PlayableUri("spotify:track:$trackId")),
                deviceId = device.id
            )
            delay(1000)
            val currentlyPlayingObjectTrack = api.player.getCurrentlyPlaying()
            assertNotNull(currentlyPlayingObjectTrack)
            assertTrue(currentlyPlayingObjectTrack.isPlaying && currentlyPlayingObjectTrack.context == null)

            val playlistId = "3DhwYIoAZ8mXlxiBkCuOx7"
            api.player.startPlayback(collectionUri = CollectionUri("spotify:playlist:3DhwYIoAZ8mXlxiBkCuOx7"))
            delay(1000)
            val currentlyPlayingObjectPlaylist = api.player.getCurrentlyPlaying()
            assertNotNull(currentlyPlayingObjectPlaylist)
            assertTrue(currentlyPlayingObjectPlaylist.isPlaying)
            assertEquals(playlistId, currentlyPlayingObjectPlaylist.context?.uri?.id)
            assertEquals(PlayHistoryContext.ContextType.PLAYLIST, currentlyPlayingObjectPlaylist.context?.type)

            api.player.pause()
        }
    }

    @Test
    fun testSeek() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()

            val trackId = "7lPN2DXiMsVn7XUKtOW1CS"
            val track = api.tracks.getTrack(trackId)!!
            api.player.startPlayback(
                playableUrisToPlay = listOf(PlayableUri("spotify:track:$trackId")),
                deviceId = device.id
            )
            api.player.pause()

            val skipTo = track.length / 2
            val delay = measureTime {
                api.player.seek(skipTo.toLong())
                api.player.resume()
            }.inMilliseconds

            val waitTime = 3000
            delay(waitTime.toLong())
            assertTrue(api.player.getCurrentlyPlaying()!!.progressMs!! >= waitTime - delay)
            api.player.skipForward()
        }
    }

    @Test
    fun testSetPlaybackOptions() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()
            api.player.setRepeatMode(ClientPlayerApi.PlayerRepeatState.OFF, device.id)
            api.player.setVolume(50, device.id)
            val context = api.player.getCurrentContext()!!
            assertEquals(ClientPlayerApi.PlayerRepeatState.OFF, context.repeatState)

        }
    }

    @Test
    fun testSkipForwardBackward() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest
            val device = api.player.getDevices().first()

            val playlist = api.playlists.getPlaylist("37i9dQZF1DXcBWIGoYBM5M")!!
            api.player.startPlayback(
                collectionUri = playlist.uri,
                deviceId = device.id
            )
            delay(1000)

            api.player.skipForward()
            delay(500)
            assertEquals(playlist.tracks[1].track!!.id, api.player.getCurrentlyPlaying()!!.track.id)

            api.player.skipBehind()
            delay(500)
            assertEquals(playlist.tracks[0].track!!.id, api.player.getCurrentlyPlaying()!!.track.id)

            api.player.pause()
        }
    }


}