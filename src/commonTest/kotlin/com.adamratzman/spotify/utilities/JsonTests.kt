/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.api

private val gson = Gson()

class JsonTests : Spek({
    describe("json serialization tests") {
        it("artist serialization") {
            assertDoesNotThrow {
                assertTrue(gson.toJson(api.artists.getArtist("spotify:artist:5WUlDfRSoLAfcVSX1WnrxN").complete()).isNotEmpty())
            }
        }
        it("track serialization") {
            assertDoesNotThrow {
                assertTrue(gson.toJson(api.tracks.getTrack("spotify:track:6kcHg7XL6SKyPNd78daRBL").complete()).isNotEmpty())
            }
        }
        it("album serialization") {
            assertDoesNotThrow {
                println(gson.toJson(api.albums.getAlbum("spotify:album:6ggQNps98xaXMY0OZWevEH").complete()))
                assertTrue(gson.toJson(api.albums.getAlbum("spotify:album:6ggQNps98xaXMY0OZWevEH").complete()).isNotEmpty())
            }
        }
    }
})