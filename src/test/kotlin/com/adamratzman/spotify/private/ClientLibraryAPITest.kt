/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.utils.BadRequestException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientLibraryAPITest : Spek({
    describe("Client Library tests") {
        if (api !is SpotifyClientAPI) return@describe
        it("library track tests") {
            val testTrack = "3yi3SEVFj0mSiYVu8xT9sF"
            if (api.library.contains(LibraryType.TRACK, testTrack).complete()) {
                api.library.remove(LibraryType.TRACK, testTrack)
            }

            assertFalse(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertFalse(api.library.getSavedTracks().getAllItems().complete()
                .map { it.track.id }.contains(testTrack)
            )

            api.library.add(LibraryType.TRACK, testTrack).complete()

            assertTrue(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertTrue(api.library.getSavedTracks().getAllItems().complete()
                .map { it.track.id }.contains(testTrack)
            )

            api.library.remove(LibraryType.TRACK, testTrack).complete()

            assertFalse(api.library.contains(LibraryType.TRACK, testTrack).complete())
            assertFalse(api.library.getSavedTracks().getAllItems().complete()
                .map { it.track.id }.contains(testTrack)
            )
        }

        it("library album tests") {
            val testAlbum = "1UAt4G020TgW3lb2CkXr2N"
            if (api.library.contains(LibraryType.ALBUM, testAlbum).complete()) {
                api.library.remove(LibraryType.ALBUM, testAlbum).complete()
            }

            assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertFalse(api.library.getSavedAlbums().getAllItems().complete()
                .map { it.album.id }.contains(testAlbum)
            )

            api.library.add(LibraryType.ALBUM, testAlbum).complete()

            assertTrue(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertTrue(api.library.getSavedAlbums().getAllItems().complete()
                .map { it.album.id }.contains(testAlbum)
            )

            api.library.remove(LibraryType.ALBUM, testAlbum).complete()

            assertFalse(api.library.contains(LibraryType.ALBUM, testAlbum).complete())
            assertFalse(api.library.getSavedAlbums().getAllItems().complete()
                .map { it.album.id }.contains(testAlbum)
            )
        }

        it("invalid inputs") {
            assertThrows<BadRequestException> { api.library.remove(LibraryType.TRACK, "ajksdfkjasjfd").complete() }
            assertThrows<BadRequestException> { api.library.contains(LibraryType.TRACK, "adsfjk").complete() }
            assertThrows<BadRequestException> { api.library.add(LibraryType.TRACK, "wer").complete() }

            assertThrows<BadRequestException> { api.library.remove(LibraryType.ALBUM, "elkars").complete() }
            assertThrows<BadRequestException> { api.library.contains(LibraryType.ALBUM, "").complete() }
            assertThrows<BadRequestException> { api.library.add(LibraryType.ALBUM, "oieriwkjrjkawer").complete() }
        }
    }
})