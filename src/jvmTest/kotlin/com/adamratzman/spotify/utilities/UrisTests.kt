/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.models.AlbumURI
import com.adamratzman.spotify.models.ArtistURI
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.PlaylistURI
import com.adamratzman.spotify.models.TrackURI
import com.adamratzman.spotify.models.UserURI
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UrisTests : Spek({
    describe("Uris tests") {
        describe("TrackURI tests") {
            it("Create track with invalid input") {
                assertFailsWith<BadRequestException> {
                    TrackURI("a:invalid")
                }

                assertFailsWith<BadRequestException> {
                    TrackURI("a:invalid").uri
                }

                assertFailsWith<BadRequestException> {
                    TrackURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                }
            }

            it("Create track with valid input") {
                assertEquals(
                    "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                    TrackURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
                )

                assertEquals(
                    "1Z9UVqWuRJ7zToOiVnlXRO",
                    TrackURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").id
                )

                assertEquals(
                    "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                    TrackURI("1Z9UVqWuRJ7zToOiVnlXRO").uri
                )

                assertEquals(
                    "1Z9UVqWuRJ7zToOiVnlXRO",
                    TrackURI("1Z9UVqWuRJ7zToOiVnlXRO").id
                )
            }
        }

        describe("UserURI") {
            it("Create user with invalid input") {
                assertFailsWith<BadRequestException> {
                    UserURI("a:invalid")
                }

                assertFailsWith<BadRequestException> {
                    UserURI("a:invalid").uri
                }

                assertFailsWith<BadRequestException> {
                    UserURI("a:invalid").id
                }

                assertFailsWith<BadRequestException> {
                    UserURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
                }
            }

            it("Create user with valid input") {
                assertEquals(
                    "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
                )

                assertEquals(
                    "7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").id
                )

                assertEquals(
                    "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").uri
                )

                assertEquals(
                    "7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("7r7uq6qxa4ymx3wnjd9mm6i83").id
                )

                assertEquals(
                    "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
                )

                assertEquals(
                    "7r7uq6qxa4ymx3wnjd9mm6i83",
                    UserURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
                )
            }
        }
    }

    describe("PlaylistURI") {
        it("Create playlist with invalid input") {
            assertFailsWith<BadRequestException> {
                PlaylistURI("a:invalid")
            }

            assertFailsWith<BadRequestException> {
                PlaylistURI("a:invalid").uri
            }

            assertFailsWith<BadRequestException> {
                PlaylistURI("a:invalid").id
            }

            assertFailsWith<BadRequestException> {
                PlaylistURI("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
            }
        }

        it("Create playlist with valid input") {
            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("spotify:playlist:66wcLiS5R50akaQ3onDyZd").id
            )

            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("66wcLiS5R50akaQ3onDyZd").id
            )

            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
            )
        }
    }

    describe("AlbumURI tests") {
        it("Create album with invalid input") {
            assertFailsWith<BadRequestException> {
                AlbumURI("a:invalid")
            }

            assertFailsWith<BadRequestException> {
                AlbumURI("a:invalid").uri
            }

            assertFailsWith<BadRequestException> {
                AlbumURI("a:invalid").id
            }

            assertFailsWith<BadRequestException> {
                AlbumURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }
        }

        it("Create album with valid input") {
            assertEquals(
                "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumURI("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").uri
            )

            assertEquals(
                "0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumURI("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").id
            )

            assertEquals(
                "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumURI("0W0ag2P4h1Fmp7PnGJVvIJ").uri
            )

            assertEquals(
                "0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumURI("0W0ag2P4h1Fmp7PnGJVvIJ").id
            )
        }
    }

    describe("ArtistURI tests") {
        it("Create artist with invalid input") {
            assertFailsWith<BadRequestException> {
                ArtistURI("a:invalid")
            }

            assertFailsWith<BadRequestException> {
                ArtistURI("a:invalid").uri
            }

            assertFailsWith<BadRequestException> {
                ArtistURI("a:invalid").id
            }

            assertFailsWith<BadRequestException> {
                ArtistURI("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }
        }

        it("Create artist with valid input") {
            assertEquals(
                "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                ArtistURI("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").uri
            )

            assertEquals(
                "1XLjkBxFokuDTlHt0mQkRe",
                ArtistURI("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").id
            )

            assertEquals(
                "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                ArtistURI("1XLjkBxFokuDTlHt0mQkRe").uri
            )

            assertEquals(
                "1XLjkBxFokuDTlHt0mQkRe",
                ArtistURI("1XLjkBxFokuDTlHt0mQkRe").id
            )
        }
    }
})
