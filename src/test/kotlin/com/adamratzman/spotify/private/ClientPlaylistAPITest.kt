package com.adamratzman.spotify.private

import com.adamratzman.spotify.api
import com.adamratzman.spotify.main.SpotifyClientAPI
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPlaylistAPITest : Spek({
    describe("Client playlist test") {
        val cp = (api as? SpotifyClientAPI)?.clientPlaylists
        it("get playlists for user, then see if we can create/delete playlists") {
            cp ?: return@it
            val playlists = cp.getClientPlaylists().complete()
            val createdPlaylist = cp.createPlaylist("this is a test playlist", "description")
                    .complete()
            assert(createdPlaylist.tracks.items.isEmpty())
            assert(cp.getClientPlaylists().complete().total - 1 == playlists.total)

            // need to add playlist removal!
        }
        it("edit playlists") {
            cp ?: return@it

        }
    }
})