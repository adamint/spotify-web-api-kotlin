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
    /**
     * Get information about a user’s available devices.
     */
    fun getDevices(): SpotifyRestAction<List<Device>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/player/devices").toString()).toInnerObject<List<Device>>(
                "devices", api
            )
        })
    }

    /**
     * Get information about the user’s current playback state, including track, track progress, and active device.
     */
    fun getCurrentContext(): SpotifyRestAction<CurrentlyPlayingContext?> {
        return toAction(Supplier {
            val obj = catch {
                get(EndpointBuilder("/me/player").toString())
                    .toObject<CurrentlyPlayingContext>(api)
            }
            if (obj?.timestamp == null) null else obj
        })
    }

    /**
     * Get tracks from the current user’s recently played tracks.
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param before The timestamp (retrieved via cursor) **not including**, but before which, tracks will have been played. This can be combined with [limit]
     * @param after The timestamp (retrieved via cursor) **not including**, after which, the retrieval starts. This can be combined with [limit]
     *
     */
    fun getRecentlyPlayed(
        limit: Int? = null,
        before: String? = null,
        after: String? = null
    ): SpotifyRestActionPaging<PlayHistory, CursorBasedPagingObject<PlayHistory>> {
        return toActionPaging(Supplier {
            get(
                EndpointBuilder("/me/player/recently-played")
                    .with("limit", limit).with("before", before).with("after", after).toString()
            ).toCursorBasedPagingObject<PlayHistory>(
                endpoint = this
            )
        })
    }

    /**
     * Get the object currently being played on the user’s Spotify account.
     */
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

    /**
     * Pause playback on the user’s account.
     *
     * @param deviceId the device to play on
     */
    fun pause(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/player/pause").with("device_id", deviceId).toString())
            Unit
        })
    }

    /**
     * Seeks to the given position in the user’s currently playing track.
     *
     * @param positionMs The position in milliseconds to seek to. Must be a positive number. Passing in a position
     * that is greater than the length of the track will cause the player to start playing the next song.
     * @param deviceId the device to play on
     */
    fun seek(positionMs: Long, deviceId: String? = null): SpotifyRestAction<Unit> {
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

    /**
     * Set the repeat mode for the user’s playback. Options are repeat-track, repeat-context, and off.
     *
     * @param state mode to describe how to repeat in the current context
     * @param deviceId the device to play on
     */
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

    /**
     * Set the volume for the user’s current playback device.
     *
     * @param volume The volume to set. Must be a value from 0 to 100 inclusive.
     * @param deviceId the device to play on
     */
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

    /**
     * Skips to next track in the user’s queue.
     *
     * @param deviceId the device to play on
     */
    fun skipForward(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post(EndpointBuilder("/me/player/next").with("device_id", deviceId).toString())
            Unit
        })
    }

    /**
     * Skips to previous track in the user’s queue.
     *
     * @param deviceId the device to play on
     */
    fun skipBehind(deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            post(EndpointBuilder("/me/player/previous").with("device_id", deviceId).toString())
            Unit
        })
    }

    /**
     * Start or resume playback.
     * **Note:** Only one of the following can be used: [album], [artist], [playlist], or [tracksToPlay]. Else, you will
     * not see expected results.
     *
     * **Note also:** You can only use one of the following: [offsetNum] or [offsetTrackId]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * @param album an album id or uri to play
     * @param artist an artist id or uri for whom to play
     * @param playlist a playlist id or uri from which to play
     * @param tracksToPlay track ids or uris to play. these are converted into URIs. Max 100
     * @param offsetNum Indicates from where in the context playback should start. Only available with use of [album] or [playlist]
     * or when [tracksToPlay] is used.
     * @param offsetTrackId Does the same as [offsetNum] but with a track id or uri instead of place number
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
    fun resume(deviceId: String? = null) = startPlayback(deviceId = deviceId)

    /**
     * Toggle shuffle on or off for user’s playback.
     *
     * @param deviceId the device to play on
     */
    fun toggleShuffle(shuffle: Boolean = true, deviceId: String? = null): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/player/shuffle").with("state", shuffle).with("device_id", deviceId).toString())
            Unit
        })
    }

    /**
     * Transfer playback to a new device and determine if it should start playing.
     *
     * @param deviceId the device to play on
     * @param play whether to immediately start playback on the transferred device
     */
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

    /**
     * What state the player can repeat in.
     */
    enum class PlayerRepeatState {
        /**
         * Repeat the current track
         */
        TRACK,
        /**
         * Repeat the current context
         */
        CONTEXT,
        /**
         * Will turn repeat off
         */
        OFF
    }
}
