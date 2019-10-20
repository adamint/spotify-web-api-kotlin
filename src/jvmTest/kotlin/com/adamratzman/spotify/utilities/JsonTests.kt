/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.api
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.Track
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.Json
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue

@UnstableDefault
class JsonTests : Spek({
    describe("json serialization tests") {
        it("artist serialization") {
            assertTrue(
                Json.stringify(
                    Artist.serializer().nullable,
                    api.artists.getArtist("spotify:artist:5WUlDfRSoLAfcVSX1WnrxN").complete()
                ).isNotEmpty()
            )
        }
        it("track serialization") {
            assertTrue(Json.stringify(Track.serializer().nullable, api.tracks.getTrack("spotify:track:6kcHg7XL6SKyPNd78daRBL").complete()).isNotEmpty())
        }
        it("album serialization") {
            assertTrue(Json.stringify(Album.serializer().nullable, api.albums.getAlbum("spotify:album:6ggQNps98xaXMY0OZWevEH").complete()).isNotEmpty())
        }
    }
})