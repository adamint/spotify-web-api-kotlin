/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

class SpotifySearchResult(
    val artists: List<Artist>?,
    val albums: List<SimpleAlbum>?,
    val tracks: List<Track>?,
    val playlists: List<SimplePlaylist>?
)