/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utilities

import com.adamratzman.spotify.models.AlbumUri
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.LocalTrackUri
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.SpotifyTrackUri
import com.adamratzman.spotify.models.SpotifyUri
import com.adamratzman.spotify.models.SpotifyUriException
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.runBlockingTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UrisTests {
    @Test
    fun testSpotifyTrackUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                SpotifyTrackUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                SpotifyTrackUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                SpotifyTrackUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }

            assertEquals(
                "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                SpotifyTrackUri("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
            )

            assertEquals(
                "1Z9UVqWuRJ7zToOiVnlXRO",
                SpotifyTrackUri("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").id
            )

            assertEquals(
                "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                SpotifyTrackUri("1Z9UVqWuRJ7zToOiVnlXRO").uri
            )

            assertEquals(
                "1Z9UVqWuRJ7zToOiVnlXRO",
                SpotifyTrackUri("1Z9UVqWuRJ7zToOiVnlXRO").id
            )
        }
    }

    @Test
    fun testLocalTrackUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                LocalTrackUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                LocalTrackUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                LocalTrackUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }

            assertFailsWith<SpotifyUriException> {
                LocalTrackUri("artist:album:name:id").uri
            }

            assertEquals(
                "spotify:local:artist:album:name:id",
                LocalTrackUri("spotify:local:artist:album:name:id").uri
            )

            assertEquals(
                "artist:album:name:id",
                LocalTrackUri("spotify:local:artist:album:name:id").id
            )
        }
    }

    @Test
    fun testTrackUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                PlayableUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                PlayableUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                PlayableUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }

            val trackUri = PlayableUri("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO")
            assertEquals(
                SpotifyTrackUri::class,
                trackUri::class
            )
            assertEquals(
                "spotify:track:1Z9UVqWuRJ7zToOiVnlXRO",
                trackUri.uri
            )
            assertEquals(
                "1Z9UVqWuRJ7zToOiVnlXRO",
                trackUri.id
            )

            assertEquals(
                SpotifyTrackUri::class,
                trackUri::class
            )
        }
    }

    @Test
    fun testUserUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                UserUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                UserUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                UserUri("a:invalid").id
            }

            assertFailsWith<SpotifyUriException> {
                UserUri("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
            }

            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            )

            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                SpotifyUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            )

            assertEquals(
                UserUri::class,
                SpotifyUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83")::class
            )

            assertEquals(
                "7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").id
            )

            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("7r7uq6qxa4ymx3wnjd9mm6i83").uri
            )

            assertEquals(
                "7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("7r7uq6qxa4ymx3wnjd9mm6i83").id
            )

            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "7r7uq6qxa4ymx3wnjd9mm6i83",
                UserUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
            )

            assertEquals(
                "spotify:user:",
                UserUri("spotify:user:").uri
            )

            assertEquals(
                "",
                UserUri("spotify:user:").id
            )
        }
    }

    @Test
    fun testPlaylistUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                PlaylistUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                PlaylistUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                PlaylistUri("a:invalid").id
            }

            assertFailsWith<SpotifyUriException> {
                PlaylistUri("spotify:track:1Z9UVqWuRJ7zToOiVnlXRO").uri
            }

            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("spotify:playlist:66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("spotify:playlist:66wcLiS5R50akaQ3onDyZd").id
            )

            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("66wcLiS5R50akaQ3onDyZd").id
            )

            assertEquals(
                "spotify:playlist:66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").uri
            )

            assertEquals(
                "66wcLiS5R50akaQ3onDyZd",
                PlaylistUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83:playlist:66wcLiS5R50akaQ3onDyZd").id
            )
        }
    }

    @Test
    fun testAlbumUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                AlbumUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                AlbumUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                AlbumUri("a:invalid").id
            }

            assertFailsWith<SpotifyUriException> {
                AlbumUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }

            assertEquals(
                "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumUri("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").uri
            )

            assertEquals(
                "0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumUri("spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ").id
            )

            assertEquals(
                "spotify:album:0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumUri("0W0ag2P4h1Fmp7PnGJVvIJ").uri
            )

            assertEquals(
                "0W0ag2P4h1Fmp7PnGJVvIJ",
                AlbumUri("0W0ag2P4h1Fmp7PnGJVvIJ").id
            )
        }
    }

    @Test
    fun testArtistUri() {
        runBlockingTest {
            assertFailsWith<SpotifyUriException> {
                ArtistUri("a:invalid")
            }

            assertFailsWith<SpotifyUriException> {
                ArtistUri("a:invalid").uri
            }

            assertFailsWith<SpotifyUriException> {
                ArtistUri("a:invalid").id
            }

            assertFailsWith<SpotifyUriException> {
                ArtistUri("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83").uri
            }

            assertEquals(
                "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                ArtistUri("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").uri
            )

            assertEquals(
                "1XLjkBxFokuDTlHt0mQkRe",
                ArtistUri("spotify:artist:1XLjkBxFokuDTlHt0mQkRe").id
            )

            assertEquals(
                "spotify:artist:1XLjkBxFokuDTlHt0mQkRe",
                ArtistUri("1XLjkBxFokuDTlHt0mQkRe").uri
            )

            assertEquals(
                "1XLjkBxFokuDTlHt0mQkRe",
                ArtistUri("1XLjkBxFokuDTlHt0mQkRe").id
            )
        }
    }

    @Test
    fun testUriSerialization() {
        runBlockingTest {
            val spotifyUri: SpotifyUri =
                Json.decodeFromString(SpotifyUri.serializer(), "\"spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83\"")
            assertEquals(
                UserUri::class,
                spotifyUri::class
            )
            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                spotifyUri.uri
            )

            val userUri = Json.decodeFromString(UserUri.serializer(), "\"spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83\"")
            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                userUri.uri
            )

            assertFailsWith<SpotifyUriException> {
                Json.decodeFromString(SpotifyUri.serializer(), "\"7r7uq6qxa4ymx3wnjd9mm6i83\"")
            }

            assertEquals(
                "spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83",
                userUri.uri
            )
        }
    }

    @Test
    fun testUriTypes() {
        assertTrue {
            SpotifyUri.isType<UserUri>("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83")
        }

        assertFalse {
            SpotifyUri.isType<UserUri>("7r7uq6qxa4ymx3wnjd9mm6i83")
        }

        assertTrue {
            SpotifyUri.canBeType<UserUri>("spotify:user:7r7uq6qxa4ymx3wnjd9mm6i83")
        }

        assertTrue {
            SpotifyUri.canBeType<UserUri>("7r7uq6qxa4ymx3wnjd9mm6i83")
        }
    }
}
