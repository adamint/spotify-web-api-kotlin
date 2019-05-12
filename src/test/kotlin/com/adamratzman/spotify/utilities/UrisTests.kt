/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.models.AlbumURI
import com.adamratzman.spotify.models.ArtistURI
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.PlaylistURI
import com.adamratzman.spotify.models.TrackURI
import com.adamratzman.spotify.models.UserURI
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UrisTests : Spek({
    describe("Uris tests") {
        describe("TrackURI tests") {
            it("Create track with invalid input") {

                assertThrows<BadRequestException> {
                    TrackURI("a:invalid")
                }

                assertThrows<BadRequestException> {
                    TrackURI("a:invalid").uri
                }

                assertThrows<BadRequestException> {
                    TrackURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                }
            }

            it("Create track with valid input") {

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                        TrackURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "1Z9UVqWuRJ7zToOiVnlXRO",
                        TrackURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                        TrackURI("1Z9UVqWuRJ7zToOiVnlXRO").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "1Z9UVqWuRJ7zToOiVnlXRO",
                        TrackURI("1Z9UVqWuRJ7zToOiVnlXRO").id
                    )
                }
            }
        }

        describe("UserURI") {
            it("Create user with invalid input") {

                assertThrows<BadRequestException> {
                    UserURI("a:invalid")
                }

                assertThrows<BadRequestException> {
                    UserURI("a:invalid").uri
                }

                assertThrows<BadRequestException> {
                    UserURI("a:invalid").id
                }

                assertThrows<BadRequestException> {
                    UserURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
                }
            }

            it("Create user with valid input") {

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "7r7uq6qxa4ymx3wnjd9mm6i83",
                        UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
                    )
                }
            }
        }

        describe("PlaylistURI") {
            it("Create playlist with invalid input") {

                assertThrows<BadRequestException> {
                    PlaylistURI("a:invalid")
                }

                assertThrows<BadRequestException> {
                    PlaylistURI("a:invalid").uri
                }

                assertThrows<BadRequestException> {
                    PlaylistURI("a:invalid").id
                }

                assertThrows<BadRequestException> {
                    PlaylistURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
                }
            }

            it("Create playlist with valid input") {
                assertDoesNotThrow {
                    assertEquals(
                        "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("66wcLiS5R50akaQ3onDyZd").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
                    )
                }
            }
        }

        describe("AlbumURI tests") {
            it("Create album with invalid input") {

                assertThrows<BadRequestException> {
                    AlbumURI("a:invalid")
                }

                assertThrows<BadRequestException> {
                    AlbumURI("a:invalid").uri
                }

                assertThrows<BadRequestException> {
                    AlbumURI("a:invalid").id
                }

                assertThrows<BadRequestException> {
                    AlbumURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                }
            }

            it("Create album with valid input") {

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                        AlbumURI("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "0W0ag2P4h1Fmp7PnGJVvIJ",
                        AlbumURI("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                        AlbumURI("0W0ag2P4h1Fmp7PnGJVvIJ").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "0W0ag2P4h1Fmp7PnGJVvIJ",
                        AlbumURI("0W0ag2P4h1Fmp7PnGJVvIJ").id
                    )
                }
            }
        }

        describe("ArtistURI tests") {
            it("Create artist with invalid input") {

                assertThrows<BadRequestException> {
                    ArtistURI("a:invalid")
                }

                assertThrows<BadRequestException> {
                    ArtistURI("a:invalid").uri
                }

                assertThrows<BadRequestException> {
                    ArtistURI("a:invalid").id
                }

                assertThrows<BadRequestException> {
                    ArtistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                }
            }

            it("Create artist with valid input") {

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                        ArtistURI("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "1XLjkBxFokuDTlHt0mQkRe",
                        ArtistURI("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                        ArtistURI("1XLjkBxFokuDTlHt0mQkRe").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "1XLjkBxFokuDTlHt0mQkRe",
                        ArtistURI("1XLjkBxFokuDTlHt0mQkRe").id
                    )
                }
            }
        }
    }
})
