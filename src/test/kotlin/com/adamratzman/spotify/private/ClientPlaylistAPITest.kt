package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPlaylistAPITest : Spek({
    describe("Client playlist test") {
        val cp = (api as? SpotifyClientAPI)?.clientPlaylists
        val createdPlaylist = cp?.createPlaylist("this is a test playlist", "description")
                ?.complete()

        it("get playlists for user, then see if we can create/delete playlists") {
            createdPlaylist ?: return@it
            val playlists = cp.getClientPlaylists().complete()
            assert(createdPlaylist.tracks.items.isEmpty())
            assert(cp.getClientPlaylists().complete().total - 1 == playlists.total)

            // need to add playlist removal!
        }
        it("edit playlists") {
            createdPlaylist ?: return@it
            cp.changePlaylistDescription(createdPlaylist.id, "test playlist", false, true, "description 2")
                    .complete()

            val updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()
            assert(updatedPlaylist != null && updatedPlaylist.collaborative
            && updatedPlaylist.public == false && updatedPlaylist.name == "test playlist" && updatedPlaylist.ful)

            cp.changePlaylistDescription()
        }
    }
})