/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.client.LibraryType
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientLibraryApiTest : Spek({
    describe("Client Library tests") {
        if (api !is SpotifyClientApi) return@describe

        it("library track tests") {
            val testTrack = "3yi3SEVFj0mSiYVu8xT9sF"
            if (api.library.contains(LibraryType.TRACK, testTrack).complete()) {
                api.library.remove(LibraryType.TRACK, testTrack).complete()
            }

            assertFalse(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertFalse(
                api.library.getSavedTracks().getAllItemsNotNull().complete()
                    .map { it.track.id }.contains(testTrack)
            )

            api.library.add(LibraryType.TRACK, testTrack).complete()

            assertTrue(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertTrue(
                api.library.getSavedTracks().getAllItemsNotNull().complete()
                    .map { it.track.id }.contains(testTrack)
            )

            api.library.remove(LibraryType.TRACK, testTrack).complete()

            assertFalse(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertFalse(
                api.library.getSavedTracks().getAllItemsNotNull().complete()
                    .map { it.track.id }.contains(testTrack)
            )
        }

        it("library album tests") {
            val testAlbum = "1UAt4G020TgW3lb2CkXr2N"
            if (api.library.contains(LibraryType.ALBUM, testAlbum).complete()) {
                api.library.remove(LibraryType.ALBUM, testAlbum).complete()
            }

            assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertFalse(
                api.library.getSavedAlbums().getAllItemsNotNull().complete()
                    .map { it.album.id }.contains(testAlbum)
            )

            api.library.add(LibraryType.ALBUM, testAlbum).complete()

            assertTrue(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertTrue(
                api.library.getSavedAlbums().getAllItemsNotNull().complete()
                    .map { it.album.id }.contains(testAlbum)
            )

            api.library.remove(LibraryType.ALBUM, testAlbum).complete()

            assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertFalse(
                api.library.getSavedAlbums().getAllItemsNotNull().complete()
                    .map { it.album.id }.contains(testAlbum)
            )
        }

        it("invalid inputs") {
            assertFailsWith<SpotifyException.BadRequestException> { api.library.remove(LibraryType.TRACK, "ajksdfkjasjfd").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.library.contains(LibraryType.TRACK, "adsfjk").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.library.add(LibraryType.TRACK, "wer").complete() }

            assertFailsWith<SpotifyException.BadRequestException> { api.library.remove(LibraryType.ALBUM, "elkars").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.library.contains(LibraryType.ALBUM, "").complete() }
            assertFailsWith<SpotifyException.BadRequestException> { api.library.add(LibraryType.ALBUM, "oieriwkjrjkawer").complete() }
        }
    }
})
