package com.adamratzman.endpoints.priv.player

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toCursorBasedPagingObject
import com.adamratzman.main.toInnerObject
import com.adamratzman.main.toObject
import com.adamratzman.obj.*

/**
 * This endpoint is in beta per the spotify documentation. These methods may or may not work
 */
class PlayerAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getDevices(): List<Device> {
        return get("https://api.spotify.com/v1/me/player/devices").toInnerObject("devices")
    }
    fun getCurrentContext(): CurrentlyPlayingContext? {
        val obj: CurrentlyPlayingContext? = get("https://api.spotify.com/v1/me/player").toObject<CurrentlyPlayingContext>()
        return if (obj?.timestamp == null) null else obj
    }
    fun getRecentlyPlayed(): CursorBasedPagingObject<PlayHistory> {
        return get("https://api.spotify.com/v1/me/player/recently-played").toCursorBasedPagingObject()
    }
    fun getCurrentlyPlaying(): CurrentlyPlayingObject? {
        println(get("https://api.spotify.com/v1/me/player/currently-playing"))
        val obj: CurrentlyPlayingObject? = get("https://api.spotify.com/v1/me/player/currently-playing").toObject<CurrentlyPlayingObject>()
        return if (obj?.timestamp == null) null else obj
    }
}