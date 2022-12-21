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
        if (api.library.contains(LibraryType.Track, testTrack)) {
            api.library.remove(LibraryType.Track, testTrack)
        }

        assertFalse(api.library.contains(LibraryType.Track, testTrack))
        assertFalse(
            api.library.getSavedTracks().getAllItemsNotNull()
                .map { it.track.id }.contains(testTrack)
        )

        api.library.add(LibraryType.Track, testTrack)

        assertTrue(api.library.contains(LibraryType.Track, testTrack))
        assertTrue(
            api.library.getSavedTracks().getAllItemsNotNull()
                .map { it.track.id }.contains(testTrack)
        )

        api.library.remove(LibraryType.Track, testTrack)

        assertFalse(api.library.contains(LibraryType.Track, testTrack))
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
        if (api.library.contains(LibraryType.Album, testAlbum)) {
            api.library.remove(LibraryType.Album, testAlbum)
        }

        assertFalse(api.library.contains(LibraryType.Album, testAlbum))
        assertFalse(
            api.library.getSavedAlbums().getAllItemsNotNull()
                .map { it.album.id }.contains(testAlbum)
        )

        api.library.add(LibraryType.Album, testAlbum)

        assertTrue(api.library.contains(LibraryType.Album, testAlbum))
        assertTrue(
            api.library.getSavedAlbums().getAllItemsNotNull()
                .map { it.album.id }.contains(testAlbum)
        )

        api.library.remove(LibraryType.Album, testAlbum)

        assertFalse(api.library.contains(LibraryType.Album, testAlbum))
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
        if (api.library.contains(LibraryType.Episode, testEpisode)) {
            api.library.remove(LibraryType.Episode, testEpisode)
        }

        assertFalse(api.library.contains(LibraryType.Episode, testEpisode))
        assertFalse(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )

        api.library.add(LibraryType.Episode, testEpisode)

        assertTrue(api.library.contains(LibraryType.Episode, testEpisode))
        assertTrue(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )

        api.library.remove(LibraryType.Episode, testEpisode)

        assertFalse(api.library.contains(LibraryType.Episode, testEpisode))
        assertFalse(
            api.library.getSavedEpisodes().getAllItemsNotNull().map { it.episode.id }.contains(testEpisode)
        )
    }

    @Test
    fun testLibraryShows(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testLibraryShows.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        val testShow = "6z4NLXyHPga1UmSJsPK7G1"
        if (api.library.contains(LibraryType.Show, testShow)) {
            api.library.remove(LibraryType.Show, testShow)
        }

        assertFalse(api.library.contains(LibraryType.Show, testShow))
        assertFalse(
            api.library.getSavedShows().getAllItemsNotNull().map { it.show.id }.contains(testShow)
        )

        api.library.add(LibraryType.Show, testShow)

        assertTrue(api.library.contains(LibraryType.Show, testShow))
        assertTrue(
            api.library.getSavedShows().getAllItemsNotNull().map { it.show.id }.contains(testShow)
        )

        api.library.remove(LibraryType.Show, testShow)

        assertFalse(api.library.contains(LibraryType.Show, testShow))
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
                LibraryType.Track,
                "ajksdfkjasjfd"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.Track,
                "adsfjk"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> { api.library.add(LibraryType.Track, "wer") }

        // album
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.Album,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.Album,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.Album,
                "oieriwkjrjkawer"
            )
        }

        // shows
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.Show,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.Show,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.Show,
                "oieriwkjrjkawer"
            )
        }

        // episodes
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.remove(
                LibraryType.Episode,
                "elkars"
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.contains(
                LibraryType.Episode,
                ""
            )
        }
        assertFailsWith<SpotifyException.BadRequestException> {
            api.library.add(
                LibraryType.Episode,
                "oieriwkjrjkawer"
            )
        }
    }
}
