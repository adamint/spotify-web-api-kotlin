/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.api
import com.adamratzman.spotify.endpoints.client.SpotifyTrackPositions
import com.adamratzman.spotify.utils.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ClientPlaylistApiTest : Spek({
    describe("Client playlist test") {
        val cp = (api as? SpotifyClientApi)?.playlists
        val playlistsBefore = cp?.getClientPlaylists()?.getAllItemsNotNull()?.complete()
        val createdPlaylist = cp?.createClientPlaylist("this is a test playlist", "description")
                ?.complete()

        createdPlaylist ?: return@describe
        it("get playlists for user, then see if we can create/delete playlists") {
            assertEquals(cp.getClientPlaylists().getAllItemsNotNull().complete().size - 1, playlistsBefore?.size)
        }

        it("add, remove >100 tracks works correctly with chunking") {
            val usTop50Uri = "com.adamratzman.spotify:playlist:37i9dQZEVXbLRQDuF5jeBp"
            val globalTop50Uri = "com.adamratzman.spotify:playlist:37i9dQZEVXbMDoHDwVN2tF"
            val globalViral50Uri = "com.adamratzman.spotify:playlist:37i9dQZEVXbLiRSasKsNU9"

            val tracks = runBlocking {
                listOf(
                        GlobalScope.async { api.playlists.getPlaylist(usTop50Uri).complete()!!.tracks.getAllItemsNotNull().complete().toList() },
                        GlobalScope.async { api.playlists.getPlaylist(globalTop50Uri).complete()!!.tracks.getAllItemsNotNull().suspendComplete().toList() },
                        GlobalScope.async { api.playlists.getPlaylist(globalViral50Uri).complete()!!.tracks.getAllItemsNotNull().suspendComplete().toList() }
                ).awaitAll().flatten().mapNotNull { it.track?.uri?.uri }
            }

            api.allowBulkRequests = true

            /*val playlistSize = { cp.getClientPlaylist(createdPlaylist.id).complete()!!.tracks.total }
            val sizeBefore = playlistSize()
            cp.addTracksToClientPlaylist(createdPlaylist.id, tracks=*tracks.toTypedArray()).complete()
            assertEquals(sizeBefore.plus(tracks.size), playlistSize())
            cp.removeTracksFromClientPlaylist(createdPlaylist.id, tracks=*tracks.toTypedArray()).complete()
            assertEquals(sizeBefore, playlistSize())*/

            api.allowBulkRequests = false
        }

        it("edit playlists") {
            cp.changeClientPlaylistDetails(
                    createdPlaylist.id, "test playlist", public = false,
                    collaborative = true, description = "description 2"
            ).complete()

            cp.addTracksToClientPlaylist(createdPlaylist.id, "3WDIhWoRWVcaHdRwMEHkkS", "7FjZU7XFs7P9jHI9Z0yRhK").complete()

            cp.uploadClientPlaylistCover(
                    createdPlaylist.id,
                    imageUrl = "https://developer.com.adamratzman.spotify.com/assets/WebAPI_intro.png"
            ).complete()

            var updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!
            val fullPlaylist = updatedPlaylist.toFullPlaylist().complete()!!

            assertTrue(
                    updatedPlaylist.collaborative && updatedPlaylist.public == false &&
                            updatedPlaylist.name == "test playlist" && fullPlaylist.description == "description 2"
            )

            assertTrue(updatedPlaylist.tracks.total == 2 && updatedPlaylist.images.isNotEmpty())

            cp.reorderClientPlaylistTracks(updatedPlaylist.id, 1, insertionPoint = 0).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assertTrue(updatedPlaylist.toFullPlaylist().complete()?.tracks?.items?.get(0)?.track?.id == "7FjZU7XFs7P9jHI9Z0yRhK")

            cp.removeAllClientPlaylistTracks(updatedPlaylist.id).complete()

            updatedPlaylist = cp.getClientPlaylist(createdPlaylist.id).complete()!!

            assertTrue(updatedPlaylist.tracks.total == 0)
        }

        it("remove playlist tracks") {
            val trackIdOne = "3WDIhWoRWVcaHdRwMEHkkS"
            val trackIdTwo = "7FjZU7XFs7P9jHI9Z0yRhK"
            cp.addTracksToClientPlaylist(createdPlaylist.id, trackIdOne, trackIdOne, trackIdTwo, trackIdTwo).complete()

            assertTrue(cp.getPlaylistTracks(createdPlaylist.id).complete().items.size == 4)

            cp.removeTrackFromClientPlaylist(createdPlaylist.id, trackIdOne).complete()

            assertEquals(
                    listOf(trackIdTwo, trackIdTwo),
                    cp.getPlaylistTracks(createdPlaylist.id).complete().items.map { it.track?.id })

            cp.addTrackToClientPlaylist(createdPlaylist.id, trackIdOne).complete()

            cp.removeTrackFromClientPlaylist(createdPlaylist.id, trackIdTwo, SpotifyTrackPositions(1)).complete()

            assertEquals(
                    listOf(trackIdTwo, trackIdOne),
                    cp.getPlaylistTracks(createdPlaylist.id).complete().items.map { it.track?.id })

            cp.setClientPlaylistTracks(createdPlaylist.id, trackIdOne, trackIdOne, trackIdTwo, trackIdTwo).complete()

            cp.removeTracksFromClientPlaylist(createdPlaylist.id, trackIdOne, trackIdTwo).complete()

            assertTrue(cp.getPlaylistTracks(createdPlaylist.id).complete().items.isEmpty())

            cp.setClientPlaylistTracks(createdPlaylist.id, trackIdTwo, trackIdOne, trackIdTwo, trackIdTwo, trackIdOne)
                    .complete()

            cp.removeTracksFromClientPlaylist(
                    createdPlaylist.id, Pair(trackIdOne, SpotifyTrackPositions(4)),
                    Pair(trackIdTwo, SpotifyTrackPositions(0))
            ).complete()

            assertEquals(
                    listOf(trackIdOne, trackIdTwo, trackIdTwo),
                    cp.getPlaylistTracks(createdPlaylist.id).complete().items.map { it.track?.id })

            assertFailsWith<SpotifyException.BadRequestException> {
                cp.removeTracksFromClientPlaylist(createdPlaylist.id, Pair(trackIdOne, SpotifyTrackPositions(3))).complete()
            }
        }

        it("destroy (unfollow) playlist") {
            cp.deleteClientPlaylist(createdPlaylist.id).complete()
            assertTrue(cp.getClientPlaylist(createdPlaylist.id).complete() == null)
        }
    }
})
