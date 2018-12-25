package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPlaylistAPITest : Spek({
    describe("Client playlist test") {
        val cp = (api as? SpotifyClientAPI)?.clientPlaylists
        val playlistsBefore = cp?.getClientPlaylists()?.complete()
        val createdPlaylist = cp?.createPlaylist("this is a test playlist", "description")
                ?.complete()
        it("get playlists for user, then see if we can create/delete playlists") {
            createdPlaylist ?: return@it
            assert(cp.getClientPlaylists().complete().total - 1 == playlistsBefore?.total)
        }
        it("edit playlists") {
            createdPlaylist ?: return@it

            cp.changePlaylistDescription(createdPlaylist.id, "test playlist", false,
                    true, "description 2").complete()

            cp.addTracksToPlaylist(createdPlaylist.id, "3WDIhWoRWVcaHdRwMEHkkS", "7FjZU7XFs7P9jHI9Z0yRhK").complete()

            cp.uploadPlaylistCover(createdPlaylist.id, imageUrl = "https://developer.spotify.com/assets/WebAPI_intro.png").complete()

            var updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!
            val fullPlaylist = updatedPlaylist.toFullPlaylist().complete()!!

            assert(updatedPlaylist.collaborative && updatedPlaylist.public == false
                    && updatedPlaylist.name == "test playlist" && fullPlaylist.description == "description 2")

            assert(updatedPlaylist.tracks.total == 2 && updatedPlaylist.images.isNotEmpty())

            cp.reorderTracks(updatedPlaylist.id, 1, insertionPoint = 0).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assert(updatedPlaylist.toFullPlaylist().complete()?.tracks?.items?.get(0)?.track?.id == "7FjZU7XFs7P9jHI9Z0yRhK")

            cp.removeAllPlaylistTracks(updatedPlaylist.id).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assert(updatedPlaylist.tracks.total == 0)
        }

        it("destroy (unfollow) playlist") {
            createdPlaylist ?: return@it
            cp.deletePlaylist(createdPlaylist.id).complete()
            assert(cp.getClientPlaylist(createdPlaylist.id).complete() == null)
        }
    }
})