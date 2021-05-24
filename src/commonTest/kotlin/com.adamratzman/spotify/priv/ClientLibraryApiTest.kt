/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClientLibraryApiTest {
    var api: SpotifyClientApi? = null

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = api != null

    @Test
    fun testLibraryTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            val testTrack = "3yi3SEVFj0mSiYVu8xT9sF"
            if (api!!.library.contains(LibraryType.TRACK, testTrack)) {
                api!!.library.remove(LibraryType.TRACK, testTrack)
            }

            assertFalse(api!!.library.contains(LibraryType.TRACK, testTrack))
            assertFalse(
                api!!.library.getSavedTracks().getAllItemsNotNull()
                    .map { it.track.id }.contains(testTrack)
            )

            api!!.library.add(LibraryType.TRACK, testTrack)

            assertTrue(api!!.library.contains(LibraryType.TRACK, testTrack))
            assertTrue(
                api!!.library.getSavedTracks().getAllItemsNotNull()
                    .map { it.track.id }.contains(testTrack)
            )

            api!!.library.remove(LibraryType.TRACK, testTrack)

            assertFalse(api!!.library.contains(LibraryType.TRACK, testTrack))
            assertFalse(
                api!!.library.getSavedTracks().getAllItemsNotNull()
                    .map { it.track.id }.contains(testTrack)
            )
        }
    }

    @Test
    fun testLibraryAlbums() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            val testAlbum = "1UAt4G020TgW3lb2CkXr2N"
            if (api!!.library.contains(LibraryType.ALBUM, testAlbum)) {
                api!!.library.remove(LibraryType.ALBUM, testAlbum)
            }

            assertFalse(api!!.library.contains(LibraryType.ALBUM, testAlbum))
            assertFalse(
                api!!.library.getSavedAlbums().getAllItemsNotNull()
                    .map { it.album.id }.contains(testAlbum)
            )

            api!!.library.add(LibraryType.ALBUM, testAlbum)

            assertTrue(api!!.library.contains(LibraryType.ALBUM, testAlbum))
            assertTrue(
                api!!.library.getSavedAlbums().getAllItemsNotNull()
                    .map { it.album.id }.contains(testAlbum)
            )

            api!!.library.remove(LibraryType.ALBUM, testAlbum)

            assertFalse(api!!.library.contains(LibraryType.ALBUM, testAlbum))
            assertFalse(
                api!!.library.getSavedAlbums().getAllItemsNotNull()
                    .map { it.album.id }.contains(testAlbum)
            )
        }
    }

    @Test
    fun testLibraryEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            val testEpisode = "4YYj9Yqas3Ss5kKulNKHjp"
            if (api!!.library.contains(LibraryType.EPISODE, testEpisode)) {
                api!!.library.remove(LibraryType.EPISODE, testEpisode)
            }

            assertFalse(api!!.library.contains(LibraryType.EPISODE, testEpisode))
            assertFalse(
                api!!.library.getSavedEpisodes().getAllItemsNotNull()
                    .map { it.episode.id }.contains(testEpisode)
            )

            api!!.library.add(LibraryType.EPISODE, testEpisode)

            assertTrue(api!!.library.contains(LibraryType.EPISODE, testEpisode))
            assertTrue(
                api!!.library.getSavedEpisodes().getAllItemsNotNull()
                    .map { it.episode.id }.contains(testEpisode)
            )

            api!!.library.remove(LibraryType.EPISODE, testEpisode)

            assertFalse(api!!.library.contains(LibraryType.EPISODE, testEpisode))
            assertFalse(
                api!!.library.getSavedEpisodes().getAllItemsNotNull()
                    .map { it.episode.id }.contains(testEpisode)
            )
        }
    }

    @Test
    fun testLibraryShows() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            val testShow = "5Fsq9rBZmD8wO1HiZJ0tl3"
            if (api!!.library.contains(LibraryType.SHOW, testShow)) {
                api!!.library.remove(LibraryType.SHOW, testShow)
            }

            assertFalse(api!!.library.contains(LibraryType.SHOW, testShow))
            assertFalse(
                api!!.library.getSavedShows().getAllItemsNotNull()
                    .map { it.show.id }.contains(testShow)
            )

            api!!.library.add(LibraryType.SHOW, testShow)

            assertTrue(api!!.library.contains(LibraryType.SHOW, testShow))
            assertTrue(
                api!!.library.getSavedShows().getAllItemsNotNull()
                    .map { it.show.id }.contains(testShow)
            )

            api!!.library.remove(LibraryType.SHOW, testShow)

            assertFalse(api!!.library.contains(LibraryType.SHOW, testShow))
            assertFalse(
                api!!.library.getSavedShows().getAllItemsNotNull()
                    .map { it.show.id }.contains(testShow)
            )
        }
    }

    @Test
    fun testInvalidInputs() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api!!.library.remove(
                    LibraryType.TRACK,
                    "ajksdfkjasjfd"
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api!!.library.contains(
                    LibraryType.TRACK,
                    "adsfjk"
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> { api!!.library.add(LibraryType.TRACK, "wer") }

            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api!!.library.remove(
                    LibraryType.ALBUM,
                    "elkars"
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api!!.library.contains(
                    LibraryType.ALBUM,
                    ""
                )
            }
            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api!!.library.add(
                    LibraryType.ALBUM,
                    "oieriwkjrjkawer"
                )
            }
        }
    }
}
