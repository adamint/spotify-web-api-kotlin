package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.utils.AlbumURI
import com.adamratzman.spotify.utils.ArtistURI
import com.adamratzman.spotify.utils.TrackURI
import com.adamratzman.spotify.utils.UserURI
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UrisTests : Spek({
    describe("Uris tests") {
        describe("TrackURI tests") {
            it("Create track with invalid input") {

                assertDoesNotThrow {
                    TrackURI("invalid")
                }

                assertThrows<IllegalArgumentException> {
                    TrackURI("invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    TrackURI("invalid").id
                }

                assertThrows<IllegalArgumentException> {
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

                assertDoesNotThrow {
                    UserURI("invalid")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("invalid").id
                }

                assertThrows<IllegalArgumentException> {
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

                assertThrows<IllegalArgumentException> {
                    UserURI.PlaylistURI("invalid")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI.PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI.PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:invalid")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI.PlaylistURI("spotify:user:invalid:playlist:66wcLiS5R50akaQ3onDyZd")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI.PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd")
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("invalid").id
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").id
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:invalid").id
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:invalid:playlist:66wcLiS5R50akaQ3onDyZd").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:invalid:playlist:66wcLiS5R50akaQ3onDyZd").id
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").uri
                }

                assertThrows<IllegalArgumentException> {
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").id
                }
            }

            it("Create playlist with valid input") {
                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd",
                        UserURI.PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        UserURI.PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("66wcLiS5R50akaQ3onDyZd").uri
                    )
                }

                assertDoesNotThrow {
                    assertEquals(
                        "66wcLiS5R50akaQ3onDyZd",
                        UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").PlaylistURI("66wcLiS5R50akaQ3onDyZd").id
                    )
                }
            }
        }

        describe("AlbumURI tests") {
            it("Create album with invalid input") {

                assertDoesNotThrow {
                    AlbumURI("invalid")
                }

                assertThrows<IllegalArgumentException> {
                    AlbumURI("invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    AlbumURI("invalid").id
                }

                assertThrows<IllegalArgumentException> {
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

                assertDoesNotThrow {
                    ArtistURI("invalid")
                }

                assertThrows<IllegalArgumentException> {
                    ArtistURI("invalid").uri
                }

                assertThrows<IllegalArgumentException> {
                    ArtistURI("invalid").id
                }

                assertThrows<IllegalArgumentException> {
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