/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable

@Serializable
class SpotifySearchResult(
    val albums: PagingObject<SimpleAlbum>?,
    val artists: PagingObject<Artist>?,
    val playlists: PagingObject<SimplePlaylist>?,
    val tracks: PagingObject<Track>?
)
