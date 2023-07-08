/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.arePlayerTestsEnabled
import com.adamratzman.spotify.models.CurrentlyPlayingType
import com.adamratzman.spotify.models.Episode
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.SpotifyContextType
import com.adamratzman.spotify.models.SpotifyTrackUri
import com.adamratzman.spotify.models.toAlbumUri
import com.adamratzman.spotify.models.toArtistUri
import com.adamratzman.spotify.models.toEpisodeUri
import com.adamratzman.spotify.models.toPlaylistUri
import com.adamratzman.spotify.models.toShowUri
import com.adamratzman.spotify.models.toTrackUri
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// we need to give a decent tolerance to ensure the action has actually been performed for these endpoints
private const val playbackRelatedDelayMs: Long = 2000

// use a static playlist
private const val testPlaylistId: String = "5EcI5L8emMhpq2r7P7msC8"

class ClientPlayerApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetDevices(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetDevices.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(api.player.getDevices().isNotEmpty())
    }

    @Test
    fun testGetCurrentContext(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetCurrentContext.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()
        api.player.startPlayback(
            playableUrisToPlay = listOf(SpotifyTrackUri("spotify:track:6WcinC5nKan2DMFUfjVerX")),
            deviceId = device.id
        )
        delay(playbackRelatedDelayMs)
        val getCurrentContext = suspend { api.player.getCurrentContext() }
        var context = getCurrentContext()
        assertTrue(context != null && context.isPlaying && context.item?.id == "6WcinC5nKan2DMFUfjVerX")
        api.player.pause()
        context = getCurrentContext()!!

        assertTrue(!context.isPlaying)
        assertNotNull(context.item?.id)

        val playlist = api.playlists.getPlaylist(testPlaylistId)!!
        api.player.startPlayback(
            contextUri = playlist.uri
        )
        delay(playbackRelatedDelayMs)
    }

    @Test
    fun testGetRecentlyPlayed(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetRecentlyPlayed.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        api.player.getRecentlyPlayed()
    }

    @Test
    fun testGetCurrentlyPlaying(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetCurrentlyPlaying.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()

        val trackId = "1VsVY1ySdH3nVSWnLT5vCf"
        api.player.startPlayback(
            playableUrisToPlay = listOf(PlayableUri("spotify:track:$trackId")),
            deviceId = device.id
        )
        delay(playbackRelatedDelayMs)
        val currentlyPlayingObjectTrack = api.player.getCurrentlyPlaying()
        assertNotNull(currentlyPlayingObjectTrack)
        assertTrue(currentlyPlayingObjectTrack.isPlaying && currentlyPlayingObjectTrack.context == null)

        val playlistId = "3DhwYIoAZ8mXlxiBkCuOx7"
        api.player.startPlayback(contextUri = playlistId.toPlaylistUri())
        delay(playbackRelatedDelayMs)
        val currentlyPlayingObjectPlaylist = api.player.getCurrentlyPlaying()
        assertNotNull(currentlyPlayingObjectPlaylist)
        assertTrue(currentlyPlayingObjectPlaylist.isPlaying)
        assertEquals(playlistId, currentlyPlayingObjectPlaylist.context?.uri?.id)
        assertEquals(SpotifyContextType.Playlist, currentlyPlayingObjectPlaylist.context?.type)

        api.player.pause()
        delay(playbackRelatedDelayMs)
    }

    @Test
    fun testAddItemToEndOfQueue(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testAddItemToEndOfQueue.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()
        val playlist = api.playlists.getPlaylist("098OivbzwUNzzDShgF6U4A")!!
        api.player.startPlayback(playlistId = playlist.id) // two tracks
        val trackId = "1VsVY1ySdH3nVSWnLT5vCf"
        api.player.addItemToEndOfQueue(trackId.toTrackUri(), device.id)
        delay(playbackRelatedDelayMs)
        api.player.skipForward() // skip first
        delay(playbackRelatedDelayMs)
        // we have nothing in the queue so the next in queue gets played before resuming playlist
        assertEquals(trackId, api.player.getCurrentlyPlaying()?.item?.uri?.id)
    }

    @Test
    fun testSeek(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testSeek.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()

        val trackId = "1VsVY1ySdH3nVSWnLT5vCf"
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
        }.toDouble(DurationUnit.MILLISECONDS)

        delay(playbackRelatedDelayMs)
        assertTrue(api.player.getCurrentlyPlaying()!!.progressMs!! >= playbackRelatedDelayMs - delay)
        api.player.skipForward()

        delay(playbackRelatedDelayMs)
    }

    /*
    // TODO add back once this isn't flaky anymore
    @Test
    fun testSetPlaybackOptions() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!
            val device = api!!.player.getDevices().first()
            val volume = 50
            api!!.player.setRepeatMode(ClientPlayerApi.PlayerRepeatState.OFF, device.id)
            api!!.player.setVolume(volume, device.id)
            api!!.player.toggleShuffle(shuffle = true)
            val context = api!!.player.getCurrentContext()!!
            assertEquals(ClientPlayerApi.PlayerRepeatState.OFF, context.repeatState)
            assertEquals(volume, context.device.volumePercent)
            assertEquals(true, context.shuffleState)
            api!!.player.toggleShuffle(shuffle = false)
            assertEquals(false, api!!.player.getCurrentContext()!!.shuffleState)
        }
    }*/

    @Test
    fun testStartPlayback(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testStartPlayback.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()

        val playlistUri = "spotify:playlist:$testPlaylistId".toPlaylistUri()
        val artistUri = "spotify:artist:0MlOPi3zIDMVrfA9R04Fe3".toArtistUri()
        val showUri = "spotify:show:6z4NLXyHPga1UmSJsPK7G1".toShowUri()
        val albumUri = "spotify:album:7qmzJKB20IS9non9kBkPgF".toAlbumUri()
        val trackId = "4DlkGrHnPtcgOu0z9aDprZ"

        // play from a context
        api.player.startPlayback(contextUri = playlistUri, deviceId = device.id)
        api.player.skipForward()
        delay(playbackRelatedDelayMs)
        assertEquals(playlistUri, api.player.getCurrentContext()?.context?.uri)

        api.player.startPlayback(contextUri = artistUri, deviceId = device.id)
        delay(playbackRelatedDelayMs)
        assertEquals(artistUri, api.player.getCurrentContext()?.context?.uri)

        api.player.startPlayback(contextUri = showUri, deviceId = device.id)

        delay(playbackRelatedDelayMs)

        assertEquals(
            CurrentlyPlayingType.Episode,
            api.player.getCurrentlyPlaying()!!.currentlyPlayingType
        )
        assertEquals(
            showUri.id,
            (api.player.getCurrentlyPlaying()!!.item as? Episode)?.show?.id
        )

        api.player.startPlayback(contextUri = albumUri, deviceId = device.id)
        delay(playbackRelatedDelayMs)
        assertEquals(albumUri, api.player.getCurrentContext()?.context?.uri)
    }

    @Test
    fun testSkipForwardBackward(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testSkipForwardBackward.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()

        val playlist = api.playlists.getPlaylist(testPlaylistId)!!
        api.player.startPlayback(
            contextUri = playlist.uri,
            deviceId = device.id
        )
        delay(playbackRelatedDelayMs)

        api.player.skipForward()
        delay(playbackRelatedDelayMs)
        api.player.skipBehind()
        api.player.pause()
        delay(playbackRelatedDelayMs)
    }

    @Test
    fun testTransferPlayback(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testTransferPlayback.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        if (api.player.getDevices().size < 2) {
            println("Active devices < 2 (${api.player.getDevices()}), so skipping transfer playback test")
            return@runTestOnDefaultDispatcher
        }
        val devices = api.player.getDevices()
        val fromDevice = devices.first()
        val toDevice = devices[1]

        api.player.startPlayback(
            playableUrisToPlay = listOf(PlayableUri("spotify:track:1VsVY1ySdH3nVSWnLT5vCf")),
            deviceId = fromDevice.id
        )
        delay(playbackRelatedDelayMs)

        api.player.transferPlayback(
            deviceId = toDevice.id!!
        )
        delay(playbackRelatedDelayMs)

        assertEquals(toDevice.id, api.player.getCurrentContext()!!.device.id)
    }

    @Test
    fun testGetCurrentQueue(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetCurrentQueue.name)
        if (!arePlayerTestsEnabled()) return@runTestOnDefaultDispatcher
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val device = api.player.getDevices().first()

        val trackId = "1VsVY1ySdH3nVSWnLT5vCf"
        api.player.startPlayback(
            playableUrisToPlay = listOf(PlayableUri("spotify:track:$trackId")),
            deviceId = device.id
        )
        delay(playbackRelatedDelayMs)

        api.player.getUserQueue().let { playedSingleTrackNoQueueQueueResponse ->
            assertEquals(trackId, playedSingleTrackNoQueueQueueResponse.currentlyPlaying?.asTrack?.id)
        }

        api.player.skipForward(deviceId = device.id)
        delay(playbackRelatedDelayMs)

        val episodeId = "4gQNhlqd3jg5QTh7umdRXT"
        api.player.startPlayback(
            playableUrisToPlay = listOf(episodeId.toEpisodeUri(), trackId.toTrackUri()),
            deviceId = device.id
        )
        delay(playbackRelatedDelayMs)

        api.player.getUserQueue().let { playingWithQueueResponse ->
            assertEquals(episodeId, playingWithQueueResponse.currentlyPlaying?.id)
            assertEquals(trackId, playingWithQueueResponse.queue[0].id)
        }

    }
}
