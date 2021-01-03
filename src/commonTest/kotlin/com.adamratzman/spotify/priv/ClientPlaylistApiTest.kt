/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.SpotifyTrackPositions
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Platform
import com.adamratzman.spotify.utils.platform
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClientPlaylistApiTest {
    lateinit var api: SpotifyClientApi
    lateinit var createdPlaylist: Playlist
    lateinit var playlistsBefore: List<SimplePlaylist>

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { clientApi ->
                api = clientApi
                playlistsBefore = api.playlists.getClientPlaylists().getAllItemsNotNull()
                createdPlaylist = api.playlists.createClientPlaylist("this is a test playlist", "description")

            }
            println("Built API")
        }
    }

    fun testPrereq() = ::api.isInitialized

    private suspend fun tearDown() {
        if (::createdPlaylist.isInitialized) {
            coroutineScope {
                api.playlists.getClientPlaylists().getAllItemsNotNull()
                        .filter { it.name == "this is a test playlist" }
                        .map {
                            async {
                                if (api.following.isFollowingPlaylist(it.id)) {
                                    api.playlists.deleteClientPlaylist(it.id)
                                }
                            }
                        }
                        .awaitAll()
            }
        }
    }

    @Test
    fun testGetClientPlaylists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertEquals(
                    api.playlists.getClientPlaylists().getAllItemsNotNull().size - 1,
                    playlistsBefore.size
            )

            tearDown()
        }
    }

    @Suppress("UNUSED_VARIABLE")
    @Test
    fun testAddAndRemoveChunkedTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val usTop50Uri = "spotify:playlist:37i9dQZEVXbLRQDuF5jeBp"
            val globalTop50Uri = "spotify:playlist:37i9dQZEVXbMDoHDwVN2tF"
            val globalViral50Uri = "spotify:playlist:37i9dQZEVXbLiRSasKsNU9"

            val tracks = listOf(
                    async { api.playlists.getPlaylist(usTop50Uri)!!.tracks.getAllItemsNotNull() },
                    async { api.playlists.getPlaylist(globalTop50Uri)!!.tracks.getAllItemsNotNull() },
                    async { api.playlists.getPlaylist(globalViral50Uri)!!.tracks.getAllItemsNotNull() }
            ).awaitAll().flatten().mapNotNull { it.track?.uri?.uri }

            api.spotifyApiOptions.allowBulkRequests = true

            /*val playlistSize = { api.playlists.getClientPlaylist(createdPlaylist.id)!!.tracks.total }
            val sizeBefore = playlistSize()
            api.playlists.addTracksToClientPlaylist(createdPlaylist.id, tracks=*tracks.toTypedArray())
            assertEquals(sizeBefore.plus(tracks.size), playlistSize())
            api.playlists.removeTracksFromClientPlaylist(createdPlaylist.id, tracks=*tracks.toTypedArray())
            assertEquals(sizeBefore, playlistSize())*/

            api.spotifyApiOptions.allowBulkRequests = false

            tearDown()
        }
    }

    @Test
    fun testEditPlaylists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            api.playlists.changeClientPlaylistDetails(
                    createdPlaylist.id, "test playlist", public = false,
                    collaborative = true, description = "description 2"
            )

            api.playlists.addTracksToClientPlaylist(createdPlaylist.id, "3WDIhWoRWVcaHdRwMEHkkS", "7FjZU7XFs7P9jHI9Z0yRhK")

            if (platform != Platform.ANDROID) {
                api.playlists.uploadClientPlaylistCover(
                        createdPlaylist.id,
                        imageUrl = "https://developer.spotify.com/assets/WebAPI_intro.png"
                )
            }

            var updatedPlaylist = api.playlists.getClientPlaylist(createdPlaylist.id)!!
            val fullPlaylist = updatedPlaylist.toFullPlaylist()!!

            assertTrue(
                    updatedPlaylist.collaborative && updatedPlaylist.public == false &&
                            updatedPlaylist.name == "test playlist" && fullPlaylist.description == "description 2"
            )

            assertTrue(updatedPlaylist.tracks.total == 2 && updatedPlaylist.images.isNotEmpty())

            api.playlists.reorderClientPlaylistTracks(updatedPlaylist.id, 1, insertionPoint = 0)

            updatedPlaylist = api.playlists.getClientPlaylist(createdPlaylist.id)!!

            assertTrue(updatedPlaylist.toFullPlaylist()?.tracks?.items?.get(0)?.track?.id == "7FjZU7XFs7P9jHI9Z0yRhK")

            api.playlists.removeAllClientPlaylistTracks(updatedPlaylist.id)

            updatedPlaylist = api.playlists.getClientPlaylist(createdPlaylist.id)!!

            assertTrue(updatedPlaylist.tracks.total == 0)

            tearDown()
        }
    }

    @Test
    fun testRemovePlaylistTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            val trackIdOne = "3WDIhWoRWVcaHdRwMEHkkS"
            val trackIdTwo = "7FjZU7XFs7P9jHI9Z0yRhK"
            api.playlists.addTracksToClientPlaylist(createdPlaylist.id, trackIdOne, trackIdOne, trackIdTwo, trackIdTwo)

            assertTrue(api.playlists.getPlaylistTracks(createdPlaylist.id).items.size == 4)

            api.playlists.removeTrackFromClientPlaylist(createdPlaylist.id, trackIdOne)

            assertEquals(
                    listOf(trackIdTwo, trackIdTwo),
                    api.playlists.getPlaylistTracks(createdPlaylist.id).items.map { it.track?.id })

            api.playlists.addTrackToClientPlaylist(createdPlaylist.id, trackIdOne)

            api.playlists.removeTrackFromClientPlaylist(createdPlaylist.id, trackIdTwo, SpotifyTrackPositions(1))

            assertEquals(
                    listOf(trackIdTwo, trackIdOne),
                    api.playlists.getPlaylistTracks(createdPlaylist.id).items.map { it.track?.id })

            api.playlists.setClientPlaylistTracks(createdPlaylist.id, trackIdOne, trackIdOne, trackIdTwo, trackIdTwo)

            api.playlists.removeTracksFromClientPlaylist(createdPlaylist.id, trackIdOne, trackIdTwo)

            assertTrue(api.playlists.getPlaylistTracks(createdPlaylist.id).items.isEmpty())

            api.playlists.setClientPlaylistTracks(createdPlaylist.id, trackIdTwo, trackIdOne, trackIdTwo, trackIdTwo, trackIdOne)

            api.playlists.removeTracksFromClientPlaylist(
                    createdPlaylist.id, Pair(trackIdOne, SpotifyTrackPositions(4)),
                    Pair(trackIdTwo, SpotifyTrackPositions(0))
            )

            assertEquals(
                    listOf(trackIdOne, trackIdTwo, trackIdTwo),
                    api.playlists.getPlaylistTracks(createdPlaylist.id).items.map { it.track?.id })

            assertFailsWithSuspend<SpotifyException.BadRequestException> {
                api.playlists.removeTracksFromClientPlaylist(createdPlaylist.id, Pair(trackIdOne, SpotifyTrackPositions(3)))
            }

            tearDown()
        }
    }
}
