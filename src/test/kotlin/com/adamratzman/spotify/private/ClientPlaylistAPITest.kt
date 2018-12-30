/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPlaylistAPITest : Spek({
    describe("Client playlist test") {
        val cp = (api as? SpotifyClientAPI)?.playlists
        val playlistsBefore = cp?.getClientPlaylists()?.complete()
        val createdPlaylist = cp?.createPlaylist("this is a test playlist", "description")
                ?.complete()

        createdPlaylist ?: return@describe
        it("get playlists for user, then see if we can create/delete playlists") {
            assertTrue(cp.getClientPlaylists().complete().size - 1 == playlistsBefore?.size)
        }
        it("edit playlists") {
            cp.changePlaylistDescription(createdPlaylist.id, "test playlist", false,
                    true, "description 2").complete()

            cp.addTracksToPlaylist(createdPlaylist.id, "3WDIhWoRWVcaHdRwMEHkkS", "7FjZU7XFs7P9jHI9Z0yRhK").complete()

            cp.uploadPlaylistCover(createdPlaylist.id, imageUrl = "https://developer.spotify.com/assets/WebAPI_intro.png").complete()

            var updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!
            val fullPlaylist = updatedPlaylist.toFullPlaylist().complete()!!

            assertTrue(updatedPlaylist.collaborative && updatedPlaylist.public == false &&
                    updatedPlaylist.name == "test playlist" && fullPlaylist.description == "description 2")

            assertTrue(updatedPlaylist.tracks.total == 2 && updatedPlaylist.images.isNotEmpty())

            cp.reorderTracks(updatedPlaylist.id, 1, insertionPoint = 0).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assertTrue(updatedPlaylist.toFullPlaylist().complete()?.tracks?.items?.get(0)?.track?.id == "7FjZU7XFs7P9jHI9Z0yRhK")

            cp.removeAllPlaylistTracks(updatedPlaylist.id).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assertTrue(updatedPlaylist.tracks.total == 0)
        }

        it("remove playlist tracks") {
            val trackIdOne = "3WDIhWoRWVcaHdRwMEHkkS"
            val trackIdTwo = "7FjZU7XFs7P9jHI9Z0yRhK"
            cp.addTracksToPlaylist(createdPlaylist.id, trackIdOne, trackIdOne, trackIdTwo, trackIdTwo).complete()

            assertTrue(cp.getPlaylistTracks(createdPlaylist.id).complete().items.size == 4)

            cp.removePlaylistTrack(createdPlaylist.id, trackIdOne).complete()

            assertEquals(listOf(trackIdTwo, trackIdTwo), cp.getPlaylistTracks(createdPlaylist.id).complete().items.map { it.track.id })
        }

        it("destroy (unfollow) playlist") {
            cp.deletePlaylist(createdPlaylist.id).complete()
            assertTrue(cp.getClientPlaylist(createdPlaylist.id).complete() == null)
        }
    }
})