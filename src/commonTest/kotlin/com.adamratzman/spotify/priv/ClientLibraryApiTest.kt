/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClientLibraryApiTest {
    lateinit var api: SpotifyClientApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { it as? SpotifyClientApi }?.let { api = it }
        return ::api.isInitialized
    }

    @Test
    fun testLibraryTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

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
    }

    @Test
    fun testLibraryAlbums() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

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
    }

    @Test
    fun testInvalidInputs() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.remove(LibraryType.TRACK, "ajksdfkjasjfd") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.contains(LibraryType.TRACK, "adsfjk") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.add(LibraryType.TRACK, "wer") }

            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.remove(LibraryType.ALBUM, "elkars") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.contains(LibraryType.ALBUM, "") }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api.library.add(LibraryType.ALBUM, "oieriwkjrjkawer") }
        }
    }
}
