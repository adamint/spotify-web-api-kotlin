package com.adamratzman.spotify.endpoints.priv.player

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*

/**
 * This endpoint is in beta per the spotify documentation. These methods may or may not work
 */
class PlayerAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getDevices(): List<Device> {
        return get("https://api.spotify.com/v1/me/player/devices").toInnerObject("devices", api)
    }

    fun getCurrentContext(): CurrentlyPlayingContext? {
        val obj: CurrentlyPlayingContext? = get("https://api.spotify.com/v1/me/player").toObject<CurrentlyPlayingContext>(api)
        return if (obj?.timestamp == null) null else obj
    }

    fun getRecentlyPlayed(): CursorBasedPagingObject<PlayHistory> {
        return get("https://api.spotify.com/v1/me/player/recently-played").toCursorBasedPagingObject(api = api)
    }

    fun getCurrentlyPlaying(): CurrentlyPlayingObject? {
        val obj: CurrentlyPlayingObject? = get("https://api.spotify.com/v1/me/player/currently-playing").toObject<CurrentlyPlayingObject>(api)
        return if (obj?.timestamp == null) null else obj
    }

    fun pausePlayback(deviceId: String? = null) {
        put("https://api.spotify.com/v1/me/player/pause${if (deviceId != null) "?device_id=$deviceId" else ""}")
    }

    fun seekPosition(positionMs: Long, deviceId: String? = null) {
        if (positionMs < 0) throw IllegalArgumentException("Position must not be negative!")
        put("https://api.spotify.com/v1/me/player/seek?position_ms=$positionMs${if (deviceId != null) "&device_id=$deviceId" else ""}")
    }

    fun setRepeatMode(state: PlayerRepeatState, deviceId: String? = null) {
        put("https://api.spotify.com/v1/me/player/repeat?state=${state.toString().toLowerCase()}${if (deviceId != null) "&device_id=$deviceId" else ""}")
    }

    fun setVolume(volume: Int, deviceId: String? = null) {
        if (volume !in 0..100) throw IllegalArgumentException("Volume must be within 0 to 100 inclusive. Provided: $volume")
        put("https://api.spotify.com/v1/me/player/volume?volume_percent=$volume${if (deviceId != null) "&device_id=$deviceId" else ""}")
    }

    fun skipToNextTrack(deviceId: String? = null) {
        post("https://api.spotify.com/v1/me/player/next${if (deviceId != null) "?device_id=$deviceId" else ""}")
    }

    fun rewindToLastTrack(deviceId: String? = null) {
        post("https://api.spotify.com/v1/me/player/previous${if (deviceId != null) "?device_id=$deviceId" else ""}")
    }

    fun startPlayback(deviceId: String? = null) {
        put("https://api.spotify.com/v1/me/player/play${if (deviceId != null) "?device_id=$deviceId" else ""}")
    }

    fun resumePlayback(deviceId: String? = null) = startPlayback(deviceId)

    fun shufflePlayback(shuffle: Boolean = true, deviceId: String? = null) {
        put("https://api.spotify.com/v1/me/player/shuffle?state=$shuffle${if (deviceId != null) "&device_id=$deviceId" else ""}")
    }

    fun transferPlayback(vararg deviceId: String, play: Boolean = true) {
        if (deviceId.size > 1) throw IllegalArgumentException("Although an array is accepted, only a single device_id is currently supported. Supplying more than one will return 400 Bad Request")
        put("https://api.spotify.com/v1/me/player?deviceId=${deviceId.joinToString(",")}&play=$play")
    }

    enum class PlayerRepeatState { TRACK, CONTEXT, OFF }
}