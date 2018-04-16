package com.adamratzman.spotify.endpoints.priv.player

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import org.json.JSONObject
import java.util.function.Supplier

/**
 * These endpoints allow for viewing and controlling user playback. Please view [the official documentation](https://developer.spotify.com/web-api/working-with-connect/)
 * for more information on how this works. This is in beta and is available for **premium users only**. Endpoints are **not** guaranteed to work
 */
class PlayerAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getDevices(): SpotifyRestAction<List<Device>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/player/devices").toInnerObject<Device>("devices", api)
        })
    }

    fun getCurrentContext(): SpotifyRestAction<CurrentlyPlayingContext?> {
        return toAction(Supplier {
            val obj: CurrentlyPlayingContext? = get("https://api.spotify.com/v1/me/player").toObject<CurrentlyPlayingContext>(api)
            if (obj?.timestamp == null) null else obj
        })
    }

    fun getRecentlyPlayed(): SpotifyRestAction<CursorBasedPagingObject<PlayHistory>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/player/recently-played").toCursorBasedPagingObject<PlayHistory>(api = api)
        })
    }

    fun getCurrentlyPlaying(): SpotifyRestAction<CurrentlyPlayingObject?> {
        return toAction(Supplier {
            val obj: CurrentlyPlayingObject? = get("https://api.spotify.com/v1/me/player/currently-playing").toObject<CurrentlyPlayingObject>(api)
            if (obj?.timestamp == null) null else obj
        })
    }

    fun pausePlayback(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/player/pause${if (deviceId != null) "?device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun seekPosition(positionMs: Long, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            if (positionMs < 0) throw IllegalArgumentException("Position must not be negative!")
            put("https://api.spotify.com/v1/me/player/seek?position_ms=$positionMs${if (deviceId != null) "&device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun setRepeatMode(state: PlayerRepeatState, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/player/repeat?state=${state.toString().toLowerCase()}${if (deviceId != null) "&device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun setVolume(volume: Int, deviceId: String? = null): SpotifyRestAction<Unit> {
        if (volume !in 0..100) throw IllegalArgumentException("Volume must be within 0 to 100 inclusive. Provided: $volume")
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/player/volume?volume_percent=$volume${if (deviceId != null) "&device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun skipToNextTrack(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post("https://api.spotify.com/v1/me/player/next${if (deviceId != null) "?device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun rewindToLastTrack(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post("https://api.spotify.com/v1/me/player/previous${if (deviceId != null) "?device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    /**
     * Start or resume playback.
     * **Note:** Only one of the following can be used: [albumId], [artistId], [playlist], or [tracksToPlay]. Else, you will
     * not see expected results.
     *
     * **Note also:** You can only use one of the following: [offsetNum] or [offsetTrackId]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * @param albumId an album id to play
     * @param artistId an artist id for whom to play
     * @param playlist a playlist id from which to play
     * @param tracksToPlay track ids to play. these are converted into URIs. Max 100
     * @param offsetNum Indicates from where in the context playback should start. Only available with use of [albumId] or [playlist]
     * or when [tracksToPlay] is used.
     * @param offsetTrackId Does the same as [offsetNum] but with a track id instead of place number
     * @param deviceId the device to play on
     *
     * @throws BadRequestException if more than one type of play type is specified or the offset is illegal.
     */
    fun startPlayback(albumId: String? = null, artistId: String? = null, playlist: PlaylistParams? = null,
                      offsetNum: Int? = null, offsetTrackId: String? = null, deviceId: String? = null, vararg tracksToPlay: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            val url = "https://api.spotify.com/v1/me/player/play${if (deviceId != null) "?device_id=${deviceId.encode()}" else ""}"
            val body = JSONObject()
            when {
                albumId != null -> body.put("context_uri", "spotify:album:$albumId")
                artistId != null -> body.put("context_uri", "spotify:artist:$artistId")
                playlist != null -> body.put("context_uri", "spotify:user:${playlist.author}:playlist:${playlist.id}")
                tracksToPlay.isNotEmpty() -> body.put("uris", tracksToPlay.map { "spotify:track:$it" })
            }
            if (body.keySet().isNotEmpty()) {
                if (offsetNum != null) body.put("offset", JSONObject().put("position", offsetNum))
                else if (offsetTrackId != null) body.put("offset", JSONObject().put("uri", "spotify:track:$offsetTrackId"))
                put(url, body.toString())
            }
            else put(url)
            Unit
        })
    }

    /**
     * Resumes playback on the current device, if [deviceId] is not specified.
     *
     * @param deviceId the device to play on
     */
    fun resumePlayback(deviceId: String? = null) = startPlayback(deviceId = deviceId)

    fun shufflePlayback(shuffle: Boolean = true, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/player/shuffle?state=$shuffle${if (deviceId != null) "&device_id=${deviceId.encode()}" else ""}")
            Unit
        })
    }

    fun transferPlayback(vararg deviceId: String, play: Boolean = true): SpotifyRestAction<Unit> {
        if (deviceId.size > 1) throw IllegalArgumentException("Although an array is accepted, only a single device_id is currently supported. Supplying more than one will  400 Bad Request")
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/player?deviceId=${deviceId.map { it.encode() }.joinToString(",")}&play=$play")
            Unit
        })
    }

    enum class PlayerRepeatState { TRACK, CONTEXT, OFF }
}