/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClientLibraryApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testLibraryTracks(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testLibraryTracks.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testTrack = "3yi3SEVFj0mSiYVu8xT9sF"
        if (api.library.contains(LibraryType.TRACK, testTrack)) {
            api.library.remove(LibraryType.TRACK, testTrack)
        }

        assertFalse(api.library.contains(LibraryType.TRACK, testTrack))
        assertFalse(
            api.library.getSavedTracks().getAllItemsNotNull()
                .map { it.track.id }.contains(testTrack)
        )

        api.library.add(LibraryType.TRACK, testTrack)

        assertTrue(api.library.contains(LibraryType.TRACK, testTrack))
        assertTrue(
            api.library.getSavedTracks().getAllItemsNotNull()
                .map { it.track.id }.contains(testTrack)
        )

        api.library.remove(LibraryType.TRACK, testTrack)

        assertFalse(api.library.contains(LibraryType.TRACK, testTrack))
        assertFalse(
            api.library.getSavedTracks().getAllItemsNotNull()
                .map { it.track.id }.contains(testTrack)
        )
    }

    @Test
    fun testLibraryAlbums(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testLibraryAlbums.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testAlbum = "1UAt4G020TgW3lb2CkXr2N"
        if (api.library.contains(LibraryType.ALBUM, testAlbum)) {
            api.library.remove(LibraryType.ALBUM, testAlbum)
        }

        assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum))
        assertFalse(
            api.library.getSavedAlbums().getAllItemsNotNull()
                .map { it.album.id }.contains(testAlbum)
        )

        api.library.add(LibraryType.ALBUM, testAlbum)

        assertTrue(api.library.contains(LibraryType.ALBUM, testAlbum))
        assertTrue(
            api.library.getSavedAlbums().getAllItemsNotNull()
                .map { it.album.id }.contains(testAlbum)
        )

        api.library.remove(LibraryType.ALBUM, testAlbum)

        assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum))
        assertFalse(
            api.library.getSavedAlbums().getAllItemsNotNull()
                .map { it.album.id }.contains(testAlbum)
        )
    }

    @Test
    fun testLibraryEpisodes(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testLibraryEpisodes.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testEpisode = "5outVI1srKZtqwPrthvkKb"
        if (api.library.contains(LibraryType.EPISODE, testEpisode)) {
            api.library.remove(LibraryType.EPISODE, testEpisode)
        }

        assertFalse(api.library.contains(LibraryType.EPISODE, testEpisode))
        assertFalse(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )

        api.library.add(LibraryType.EPISODE, testEpisode)

        assertTrue(api.library.contains(LibraryType.EPISODE, testEpisode))
        assertTrue(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )

        api.library.remove(LibraryType.EPISODE, testEpisode)

        assertFalse(api.library.contains(LibraryType.EPISODE, testEpisode))
        assertFalse(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )
    }

    @Test
    fun testLibraryShows(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testLibraryShows.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testShow = "6z4NLXyHPga1UmSJsPK7G1"
        if (api.library.contains(LibraryType.SHOW, testShow)) {
            api.library.remove(LibraryType.SHOW, testShow)
        }

        assertFalse(api.library.contains(LibraryType.SHOW, testShow))
        assertFalse(
            api.library.getSavedShows().getAllItemsNotNull().map { it.show.id }.contains(testShow)
        )

        api.library.add(LibraryType.SHOW, testShow)

        assertTrue(api.library.contains(LibraryType.SHOW, testShow))
        assertTrue(
            api.library.getSavedShows().getAllItemsNotNull().map { it.show.id }.contains(testShow)
        )

        api.library.remove(LibraryType.SHOW, testShow)

        assertFalse(api.library.contains(LibraryType.SHOW, testShow))
        assertFalse(
            api.library.getSavedShows().getAllItemsNotNull().map { it.show.id }.contains(testShow)
        )
    }

    @Test
    fun testInvalidInputs(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testInvalidInputs.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        // tracks
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.TRACK,
                "ajksdfkjasjfd"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.TRACK,
                "adsfjk"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> { api.library.add(LibraryType.TRACK, "wer") }

        // album
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.ALBUM,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.ALBUM,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.ALBUM,
                "oieriwkjrjkawer"
            )
        }

        // shows
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.SHOW,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.SHOW,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.SHOW,
                "oieriwkjrjkawer"
            )
        }

        // episodes
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.EPISODE,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.EPISODE,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.EPISODE,
                "oieriwkjrjkawer"
            )
        }
    }
}
