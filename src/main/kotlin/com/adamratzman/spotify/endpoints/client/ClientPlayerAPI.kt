/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.SpotifyRestActionPaging
import com.adamratzman.spotify.utils.AlbumURI
import com.adamratzman.spotify.utils.ArtistURI
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.CurrentlyPlayingContext
import com.adamratzman.spotify.utils.CurrentlyPlayingObject
import com.adamratzman.spotify.utils.CursorBasedPagingObject
import com.adamratzman.spotify.utils.Device
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.PlayHistory
import com.adamratzman.spotify.utils.PlaylistURI
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.TrackURI
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toCursorBasedPagingObject
import com.adamratzman.spotify.utils.toInnerObject
import com.adamratzman.spotify.utils.toObject
import com.beust.klaxon.JsonObject
import java.util.function.Supplier

/**
 * These endpoints allow for viewing and controlling user playback. Please view [the official documentation](https://developer.spotify.com/web-api/working-with-connect/)
 * for more information on how this works. This is in beta and is available for **premium users only**. Endpoints are **not** guaranteed to work
 */
class ClientPlayerAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getDevices(): SpotifyRestAction<List<Device>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/player/devices").toString()).toInnerObject<List<Device>>(
                "devices", api
            )
        })
    }

    fun getCurrentContext(): SpotifyRestAction<CurrentlyPlayingContext?> {
        return toAction(Supplier {
            val obj = catch {
                get(EndpointBuilder("/me/player").toString())
                    .toObject<CurrentlyPlayingContext>(api)
            }
            if (obj?.timestamp == null) null else obj
        })
    }

    fun getRecentlyPlayed(): SpotifyRestActionPaging<PlayHistory, CursorBasedPagingObject<PlayHistory>> {
        return toActionPaging(Supplier {
            get(EndpointBuilder("/me/player/recently-played").toString()).toCursorBasedPagingObject<PlayHistory>(
                endpoint = this
            )
        })
    }

    fun getCurrentlyPlaying(): SpotifyRestAction<CurrentlyPlayingObject?> {
        return toAction(Supplier {
            val obj =
                catch {
                    get(EndpointBuilder("/me/player/currently-playing").toString())
                        .toObject<CurrentlyPlayingObject>(api)
                }
            if (obj?.timestamp == null) null else obj
        })
    }

    fun pausePlayback(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/player/pause").with("device_id", deviceId).toString())
            Unit
        })
    }

    fun seekPosition(positionMs: Long, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            if (positionMs < 0) throw IllegalArgumentException("Position must not be negative!")
            put(
                EndpointBuilder("/me/player/seek").with("position_ms", positionMs).with(
                    "device_id",
                    deviceId
                ).toString()
            )
            Unit
        })
    }

    fun setRepeatMode(state: PlayerRepeatState, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(
                EndpointBuilder("/me/player/repeat").with("state", state.toString().toLowerCase()).with(
                    "device_id",
                    deviceId
                ).toString()
            )
            Unit
        })
    }

    fun setVolume(volume: Int, deviceId: String? = null): SpotifyRestAction<Unit> {
        if (volume !in 0..100) throw IllegalArgumentException("Volume must be within 0 to 100 inclusive. Provided: $volume")
        return toAction(Supplier {
            put(
                EndpointBuilder("/me/player/volume").with("volume_percent", volume).with(
                    "device_id",
                    deviceId
                ).toString()
            )
            Unit
        })
    }

    fun skipToNextTrack(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post(EndpointBuilder("/me/player/next").with("device_id", deviceId).toString())
            Unit
        })
    }

    fun rewindToLastTrack(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post(EndpointBuilder("/me/player/previous").with("device_id", deviceId).toString())
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
    fun startPlayback(
        album: String? = null,
        artist: String? = null,
        playlist: PlaylistURI? = null,
        offsetNum: Int? = null,
        offsetTrackId: String? = null,
        deviceId: String? = null,
        vararg tracksToPlay: String
    ): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            val url = EndpointBuilder("/me/player/play").with("device_id", deviceId).toString()
            val body = JsonObject()
            when {
                album != null -> body["context_uri"] = AlbumURI(album).uri
                artist != null -> body["context_uri"] = ArtistURI(artist).uri
                playlist != null -> body["context_uri"] = playlist.uri
                tracksToPlay.isNotEmpty() -> body["uris"] = tracksToPlay.map { TrackURI(it).uri }
            }
            if (body.keys.isNotEmpty()) {
                if (offsetNum != null) body["offset"] = JsonObject().apply { this["position"] = offsetNum }
                else if (offsetTrackId != null) body["offset"] =
                    JsonObject().apply { this["uri"] = TrackURI(offsetTrackId).uri }
                put(url, body.toJsonString())
            } else put(url)
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
            put(EndpointBuilder("/me/player/shuffle").with("state", shuffle).with("device_id", deviceId).toString())
            Unit
        })
    }

    fun transferPlayback(vararg deviceId: String, play: Boolean = true): SpotifyRestAction<Unit> {
        if (deviceId.size > 1) throw IllegalArgumentException("Although an array is accepted, only a single device_id is currently supported. Supplying more than one will  400 Bad Request")
        return toAction(Supplier {
            put(
                EndpointBuilder("/me/player").with("device_ids", deviceId.joinToString(",") { it.encode() })
                    .with("play", play).toString()
            )
            Unit
        })
    }

    enum class PlayerRepeatState { TRACK, CONTEXT, OFF }
}
