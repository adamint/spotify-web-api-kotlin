/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import kotlinx.serialization.Serializable

@Serializable
class SpotifySearchResult(
    val artists: PagingObject<Artist>?,
    val albums: PagingObject<SimpleAlbum>?,
    val tracks: PagingObject<Track>?,
    val playlists: PagingObject<SimplePlaylist>?
)