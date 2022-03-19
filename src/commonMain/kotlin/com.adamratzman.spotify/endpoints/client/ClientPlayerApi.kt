/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.ContextUri
import com.adamratzman.spotify.models.CurrentlyPlayingContext
import com.adamratzman.spotify.models.CurrentlyPlayingObject
import com.adamratzman.spotify.models.CurrentlyPlayingType
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.Device
import com.adamratzman.spotify.models.PlayHistory
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.ResultEnum
import com.adamratzman.spotify.models.serialization.mapToJsonString
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toInnerObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.toAlbumUri
import com.adamratzman.spotify.models.toArtistUri
import com.adamratzman.spotify.models.toEpisodeUri
import com.adamratzman.spotify.models.toLocalTrackUri
import com.adamratzman.spotify.models.toPlaylistUri
import com.adamratzman.spotify.models.toShowUri
import com.adamratzman.spotify.models.toTrackUri
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.jsonMap
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * These endpoints allow for viewing and controlling user playback. Please view [the official documentation](https://developer.spotify.com/web-api/working-with-connect/)
 * for more information on how this works. This is in beta and is available for **premium users only**. Endpoints are **not** guaranteed to work and are subject to change!
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/)**
 */
public class ClientPlayerApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get information about a user’s available devices.
     *
     * **Requires** the [SpotifyScope.USER_READ_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-a-users-available-devices/)**
     */
    public suspend fun getDevices(): List<Device> {
        requireScopes(SpotifyScope.USER_READ_PLAYBACK_STATE)

        return get(endpointBuilder("/me/player/devices").toString()).toInnerObject(
            ListSerializer(Device.serializer()),
            "devices",
            json
        )
    }

    /**
     * Get information about a user’s available devices.
     *
     * **Requires** the [SpotifyScope.USER_READ_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-a-users-available-devices/)**
     */
    public fun getDevicesRestAction(): SpotifyRestAction<List<Device>> = SpotifyRestAction { getDevices() }

    /**
     * Get information about the user’s current playback state, including track, track progress, and active device.
     *
     * **Requires** the [SpotifyScope.USER_READ_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-information-about-the-users-current-playback/)**
     *
     * @param additionalTypes A list of types to return in addition to [CurrentlyPlayingType.TRACK]. Ad type not allowed.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     */
    public suspend fun getCurrentContext(
        additionalTypes: List<CurrentlyPlayingType> = listOf(
            CurrentlyPlayingType.TRACK,
            CurrentlyPlayingType.EPISODE
        ),
        market: Market? = null
    ): CurrentlyPlayingContext? {
        requireScopes(SpotifyScope.USER_READ_PLAYBACK_STATE)

        val obj = catch {
            getNullable(
                endpointBuilder("/me/player")
                    .with("additional_types", additionalTypes.joinToString(",") { it.identifier })
                    .with("market", market?.name)
                    .toString()
            )?.toObject(CurrentlyPlayingContext.serializer(), api, json)
        }
        return if (obj?.timestamp == null) null else obj
    }

    /**
     * Get information about the user’s current playback state, including track, track progress, and active device.
     *
     * **Requires** the [SpotifyScope.USER_READ_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-information-about-the-users-current-playback/)**
     *
     * @param additionalTypes A list of types to return in addition to [CurrentlyPlayingType.TRACK]. Ad type not allowed.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     */
    public fun getCurrentContextRestAction(
        additionalTypes: List<CurrentlyPlayingType> = listOf(
            CurrentlyPlayingType.TRACK,
            CurrentlyPlayingType.EPISODE
        ),
        market: Market? = null
    ): SpotifyRestAction<CurrentlyPlayingContext?> =
        SpotifyRestAction { getCurrentContext(additionalTypes, market) }

    /**
     * Get tracks from the current user’s recently played tracks. Note: Currently doesn't support podcast episodes.
     *
     * **Requires** the [SpotifyScope.USER_READ_RECENTLY_PLAYED] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-recently-played/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param before The timestamp (retrieved via cursor) **not including**, but before which, tracks will have been played. This can be combined with [limit]
     * @param after The timestamp (retrieved via cursor) **not including**, after which, the retrieval starts. This can be combined with [limit]
     *
     */
    public suspend fun getRecentlyPlayed(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        before: String? = null,
        after: String? = null
    ): CursorBasedPagingObject<PlayHistory> {
        requireScopes(SpotifyScope.USER_READ_RECENTLY_PLAYED)

        return get(
            endpointBuilder("/me/player/recently-played")
                .with("limit", limit).with("before", before).with("after", after).toString()
        ).toCursorBasedPagingObject(PlayHistory::class, PlayHistory.serializer(), api = api, json = json)
    }

    /**
     * Get tracks from the current user’s recently played tracks. Note: Currently doesn't support podcast episodes.
     *
     * **Requires** the [SpotifyScope.USER_READ_RECENTLY_PLAYED] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-recently-played/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param before The timestamp (retrieved via cursor) **not including**, but before which, tracks will have been played. This can be combined with [limit]
     * @param after The timestamp (retrieved via cursor) **not including**, after which, the retrieval starts. This can be combined with [limit]
     *
     */
    public fun getRecentlyPlayedRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        before: String? = null,
        after: String? = null
    ): SpotifyRestAction<CursorBasedPagingObject<PlayHistory>> =
        SpotifyRestAction { getRecentlyPlayed(limit, before, after) }

    /**
     * Get the object currently being played on the user’s Spotify account.
     *
     * **Requires** *either* the [SpotifyScope.USER_READ_PLAYBACK_STATE] or [SpotifyScope.USER_READ_CURRENTLY_PLAYING] scopes
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-the-users-currently-playing-track/)**
     *
     * @param additionalTypes A list of types to return in addition to [CurrentlyPlayingType.TRACK]. Ad type not allowed.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     */
    public suspend fun getCurrentlyPlaying(
        additionalTypes: List<CurrentlyPlayingType> = listOf(
            CurrentlyPlayingType.TRACK,
            CurrentlyPlayingType.EPISODE
        ),
        market: Market? = null
    ): CurrentlyPlayingObject? {
        requireScopes(SpotifyScope.USER_READ_PLAYBACK_STATE, SpotifyScope.USER_READ_CURRENTLY_PLAYING, anyOf = true)

        return try {
            val obj =
                catch {
                    get(
                        endpointBuilder("/me/player/currently-playing")
                            .with("additional_types", additionalTypes.joinToString(",") { it.identifier })
                            .with("market", market?.name)
                            .toString()
                    )
                        .toObject(CurrentlyPlayingObject.serializer(), api, json)
                }
            if (obj?.timestamp == null) null else obj
        } catch (pe: SpotifyException.ParseException) {
            pe.printStackTrace()
            null
        }
    }

    /**
     * Get the object currently being played on the user’s Spotify account.
     *
     * **Requires** *either* the [SpotifyScope.USER_READ_PLAYBACK_STATE] or [SpotifyScope.USER_READ_CURRENTLY_PLAYING] scopes
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/get-the-users-currently-playing-track/)**
     *
     * @param additionalTypes A list of types to return in addition to [CurrentlyPlayingType.TRACK]. Ad type not allowed.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     */
    public fun getCurrentlyPlayingRestAction(
        additionalTypes: List<CurrentlyPlayingType> = listOf(
            CurrentlyPlayingType.TRACK,
            CurrentlyPlayingType.EPISODE
        ),
        market: Market? = null
    ): SpotifyRestAction<CurrentlyPlayingObject?> =
        SpotifyRestAction { getCurrentlyPlaying(additionalTypes, market) }

    /**
     * Pause playback on the user’s account.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/pause-a-users-playback/)**
     *
     * @param deviceId The device to play on
     */
    public suspend fun pause(deviceId: String? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        put(endpointBuilder("/me/player/pause").with("device_id", deviceId).toString())
    }

    /**
     * Pause playback on the user’s account.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/pause-a-users-playback/)**
     *
     * @param deviceId The device to play on
     */
    public fun pauseRestAction(deviceId: String? = null): SpotifyRestAction<Unit> {
        return SpotifyRestAction { pause(deviceId) }
    }

    /**
     * Seeks to the given position in the user’s currently playing track.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/seek-to-position-in-currently-playing-track/)**
     *
     * @param positionMs The position in milliseconds to seek to. Must be a positive number. Passing in a position
     * that is greater than the length of the track will cause the player to start playing the next song.
     * @param deviceId The device to play on
     */
    public suspend fun seek(positionMs: Long, deviceId: String? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)
        require(positionMs >= 0) { "Position must not be negative!" }
        put(
            endpointBuilder("/me/player/seek").with("position_ms", positionMs).with(
                "device_id",
                deviceId
            ).toString()
        )
    }

    /**
     * Seeks to the given position in the user’s currently playing track.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/seek-to-position-in-currently-playing-track/)**
     *
     * @param positionMs The position in milliseconds to seek to. Must be a positive number. Passing in a position
     * that is greater than the length of the track will cause the player to start playing the next song.
     * @param deviceId The device to play on
     */
    public fun seekRestAction(positionMs: Long, deviceId: String? = null): SpotifyRestAction<Unit> = SpotifyRestAction {
        seek(positionMs, deviceId)
    }

    /**
     * Set the repeat mode for the user’s playback. Options are [PlayerRepeatState.TRACK], [PlayerRepeatState.CONTEXT], and [PlayerRepeatState.OFF].
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/set-repeat-mode-on-users-playback/)**
     *
     * @param state mode to describe how to repeat in the current context
     * @param deviceId The device to play on
     */
    public suspend fun setRepeatMode(state: PlayerRepeatState, deviceId: String? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)
        put(
            endpointBuilder("/me/player/repeat").with("state", state.toString().lowercase()).with(
                "device_id",
                deviceId
            ).toString()
        )
    }

    /**
     * Set the repeat mode for the user’s playback. Options are [PlayerRepeatState.TRACK], [PlayerRepeatState.CONTEXT], and [PlayerRepeatState.OFF].
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/set-repeat-mode-on-users-playback/)**
     *
     * @param state mode to describe how to repeat in the current context
     * @param deviceId The device to play on
     */
    public fun setRepeatModeRestAction(state: PlayerRepeatState, deviceId: String? = null): SpotifyRestAction<Unit> =
        SpotifyRestAction {
            setRepeatMode(state, deviceId)
        }

    /**
     * Set the volume for the user’s current playback device.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/set-volume-for-users-playback/)**
     *
     * @param volumePercent The volume to set. Must be a value from 0 to 100 inclusive.
     * @param deviceId The device to play on
     */
    public suspend fun setVolume(volumePercent: Int, deviceId: String? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)
        require(volumePercent in 0..100) { "Volume must be within 0 to 100 inclusive. Provided: $volumePercent" }
        put(
            endpointBuilder("/me/player/volume").with("volume_percent", volumePercent).with(
                "device_id",
                deviceId
            ).toString()
        )
    }

    /**
     * Set the volume for the user’s current playback device.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/set-volume-for-users-playback/)**
     *
     * @param volumePercent The volume to set. Must be a value from 0 to 100 inclusive.
     * @param deviceId The device to play on
     */
    public fun setVolumeRestAction(volumePercent: Int, deviceId: String? = null): SpotifyRestAction<Unit> =
        SpotifyRestAction {
            setVolume(volumePercent, deviceId)
        }

    /**
     * Skips to the next track in the user’s queue.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/skip-users-playback-to-next-track/)**
     *
     * @param deviceId The device to play on
     */
    public suspend fun skipForward(deviceId: String? = null): String {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        return post(endpointBuilder("/me/player/next").with("device_id", deviceId).toString())
    }

    /**
     * Skips to the next track in the user’s queue.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/skip-users-playback-to-next-track/)**
     *
     * @param deviceId The device to play on
     */
    public fun skipForwardRestAction(deviceId: String? = null): SpotifyRestAction<String> = SpotifyRestAction {
        skipForward(deviceId)
    }

    /**
     * Skips to previous track in the user’s queue.
     *
     * Note that this will ALWAYS skip to the previous track, regardless of the current track’s progress.
     * Returning to the start of the current track should be performed using [seek]
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/skip-users-playback-to-previous-track/)**
     *
     * @param deviceId The device to play on
     */
    public suspend fun skipBehind(deviceId: String? = null): String {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        return post(endpointBuilder("/me/player/previous").with("device_id", deviceId).toString())
    }

    /**
     * Skips to previous track in the user’s queue.
     *
     * Note that this will ALWAYS skip to the previous track, regardless of the current track’s progress.
     * Returning to the start of the current track should be performed using [seek]
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/skip-users-playback-to-previous-track/)**
     *
     * @param deviceId The device to play on
     */
    public fun skipBehindRestAction(deviceId: String? = null): SpotifyRestAction<String> =
        SpotifyRestAction { skipBehind(deviceId) }

    /**
     * Start or resume playback.
     *
     * **Note:** You can only use one of the following: [offsetIndex], [offsetLocalTrackId], [offsetTrackId], [offsetEpisodeId]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param artistId Start playing an artist
     * @param playlistId Start playing a playlist
     * @param albumId Start playing an album
     * @param artistId Start playing an artist
     *
     * @param offsetLocalTrackId Start playing at a local track in the given/current context
     * @param offsetTrackId Start playing at a track in the given/current context
     * @param offsetEpisodeId Start playing at an episode in the given/current context
     *
     * @param offsetIndex Indicates from where in the given/current context playback should start. Zero-based indexing.
     *
     * @param localTrackIdsToPlay A list of local track ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     * @param trackIdsToPlay A list of track ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     * @param episodeIdsToPlay A list of episode ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     *
     * @param deviceId The device to play on
     *
     * @throws BadRequestException if more than one type of play type is specified or the offset is illegal.
     */
    public suspend fun startPlayback(
        // context uris
        artistId: String? = null,
        playlistId: String? = null,
        albumId: String? = null,
        showId: String? = null,
        // offset playables
        offsetLocalTrackId: String? = null,
        offsetTrackId: String? = null,
        offsetEpisodeId: String? = null,
        // offset num
        offsetIndex: Int? = null,
        // ids of playables to play
        trackIdsToPlay: List<String>? = null,
        localTrackIdsToPlay: List<String>? = null,
        episodeIdsToPlay: List<String>? = null,
        deviceId: String? = null
    ) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        if (listOfNotNull(artistId, playlistId, albumId, showId).size > 1) {
            throw IllegalArgumentException("Only one of: artistId, playlistId, albumId, showId can be specified.")
        }
        val contextUri =
            artistId?.toArtistUri() ?: playlistId?.toPlaylistUri() ?: albumId?.toAlbumUri() ?: showId?.toShowUri()

        if (listOfNotNull(offsetLocalTrackId, offsetTrackId, offsetEpisodeId, offsetIndex).size > 1) {
            throw IllegalArgumentException("Only one of: offsetXXId or offsetIndex can be specified.")
        }

        val offsetPlayableUri =
            offsetLocalTrackId?.toLocalTrackUri() ?: offsetTrackId?.toTrackUri() ?: offsetEpisodeId?.toEpisodeUri()
        val playableUrisToPlay =
            localTrackIdsToPlay?.map { it.toLocalTrackUri() } ?: trackIdsToPlay?.map { it.toTrackUri() }
            ?: episodeIdsToPlay?.map { it.toEpisodeUri() }

        startPlayback(
            contextUri,
            offsetIndex,
            offsetPlayableUri,
            playableUrisToPlay,
            deviceId
        )
    }

    /**
     * Start or resume playback.
     *
     * **Note:** You can only use one of the following: [offsetIndex], [offsetLocalTrackId], [offsetTrackId], [offsetEpisodeId]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param artistId Start playing an artist
     * @param playlistId Start playing a playlist
     * @param albumId Start playing an album
     * @param artistId Start playing an artist
     *
     * @param offsetLocalTrackId Start playing at a local track in the given/current context
     * @param offsetTrackId Start playing at a track in the given/current context
     * @param offsetEpisodeId Start playing at an episode in the given/current context
     *
     * @param offsetIndex Indicates from where in the given/current context playback should start. Zero-based indexing.
     *
     * @param localTrackIdsToPlay A list of local track ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     * @param trackIdsToPlay A list of track ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     * @param episodeIdsToPlay A list of episode ids to play. Max 100 combined between [localTrackIdsToPlay], [trackIdsToPlay], and [episodeIdsToPlay]
     *
     * @param deviceId The device to play on
     *
     * @throws BadRequestException if more than one type of play type is specified or the offset is illegal.
     */
    public fun startPlaybackRestAction(
        // context uris
        artistId: String? = null,
        playlistId: String? = null,
        albumId: String? = null,
        showId: String? = null,
        // offset playables
        offsetLocalTrackId: String? = null,
        offsetTrackId: String? = null,
        offsetEpisodeId: String? = null,
        // offset num
        offsetIndex: Int? = null,
        // ids of playables to play
        trackIdsToPlay: List<String>? = null,
        localTrackIdsToPlay: List<String>? = null,
        episodeIdsToPlay: List<String>? = null,
        deviceId: String? = null
    ): SpotifyRestAction<Unit> = SpotifyRestAction {
        startPlayback(
            artistId,
            playlistId,
            albumId,
            showId,
            offsetLocalTrackId,
            offsetTrackId,
            offsetEpisodeId,
            offsetIndex,
            trackIdsToPlay,
            localTrackIdsToPlay,
            episodeIdsToPlay,
            deviceId
        )
    }

    /**
     * Start or resume playback.
     *
     * **Note:** You can only use one of the following: [offsetIndex], [offsetPlayableUri]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param contextUri Start playing an album, artist, show, or playlist
     * @param playableUrisToPlay [PlayableUri] (Track, Local track, or Episode URIs) uris to play. these are converted into URIs. Max 100
     * @param offsetIndex Indicates from where in the given/current context playback should start. Only available when [playableUrisToPlay] is used.
     * @param offsetPlayableUri Start playing at a track/local track/episode uri in the given/current context instead of index ([offsetIndex])
     * @param deviceId The device to play on
     *
     * @throws BadRequestException if more than one type of play type is specified or the offset is illegal.
     */
    public suspend fun startPlayback(
        contextUri: ContextUri? = null,
        offsetIndex: Int? = null,
        offsetPlayableUri: PlayableUri? = null,
        playableUrisToPlay: List<PlayableUri>? = null,
        deviceId: String? = null
    ) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        val url = endpointBuilder("/me/player/play").with("device_id", deviceId).toString()
        val body = jsonMap()
        when {
            contextUri != null -> body += buildJsonObject { put("context_uri", contextUri.uri) }
            playableUrisToPlay?.isNotEmpty() == true -> body += buildJsonObject {
                put(
                    "uris", JsonArray(
                        playableUrisToPlay.map { it.uri }.map(::JsonPrimitive)
                    )
                )
            }
        }
        if (body.keys.isNotEmpty()) {
            if (offsetIndex != null) body += buildJsonObject {
                put(
                    "offset",
                    buildJsonObject { put("position", offsetIndex) })
            }
            else if (offsetPlayableUri != null) body += buildJsonObject {
                put("offset", buildJsonObject { put("uri", offsetPlayableUri.uri) })
            }
            put(url, body.mapToJsonString())
        } else put(url)
    }

    /**
     * Start or resume playback.
     *
     * **Note:** You can only use one of the following: [offsetIndex], [offsetPlayableUri]
     *
     * **Specify nothing to play to simply resume playback**
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param contextUri Start playing an album, artist, show, or playlist
     * @param playableUrisToPlay [PlayableUri] (Track, Local track, or Episode URIs) uris to play. these are converted into URIs. Max 100
     * @param offsetIndex Indicates from where in the given/current context playback should start. Only available when [playableUrisToPlay] is used.
     * @param offsetPlayableUri Start playing at a track/local track/episode uri in the given/current context instead of index ([offsetIndex])
     * @param deviceId The device to play on
     *
     * @throws BadRequestException if more than one type of play type is specified or the offset is illegal.
     */
    public fun startPlaybackRestAction(
        contextUri: ContextUri? = null,
        offsetIndex: Int? = null,
        offsetPlayableUri: PlayableUri? = null,
        playableUrisToPlay: List<PlayableUri>? = null,
        deviceId: String? = null
    ): SpotifyRestAction<Unit> =
        SpotifyRestAction { startPlayback(contextUri, offsetIndex, offsetPlayableUri, playableUrisToPlay, deviceId) }

    /**
     * Resumes playback on the current device, if [deviceId] is not specified.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param deviceId The device to play on
     */
    public suspend fun resume(deviceId: String? = null): Unit = startPlayback(deviceId = deviceId)

    /**
     * Resumes playback on the current device, if [deviceId] is not specified.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/start-a-users-playback/)**
     *
     * @param deviceId The device to play on
     */
    public fun resumeRestAction(deviceId: String? = null): SpotifyRestAction<Unit> =
        SpotifyRestAction { resume(deviceId) }

    /**
     * Toggle shuffle on or off for user’s playback.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/toggle-shuffle-for-users-playback/)**
     *
     * @param deviceId The device to play on
     * @param shuffle Whether to enable shuffling of playback
     */
    public suspend fun toggleShuffle(shuffle: Boolean, deviceId: String? = null): String {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        return put(endpointBuilder("/me/player/shuffle").with("state", shuffle).with("device_id", deviceId).toString())
    }

    /**
     * Toggle shuffle on or off for user’s playback.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/toggle-shuffle-for-users-playback/)**
     *
     * @param deviceId The device to play on
     * @param shuffle Whether to enable shuffling of playback
     */
    public fun toggleShuffleRestAction(shuffle: Boolean, deviceId: String? = null): SpotifyRestAction<String> =
        SpotifyRestAction {
            toggleShuffle(shuffle, deviceId)
        }

    /**
     * Transfer playback to a new device and determine if it should start playing.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/transfer-a-users-playback/)**
     *
     * @param deviceId The device to play on
     * @param play Whether to immediately start playback on the transferred device
     */
    public suspend fun transferPlayback(deviceId: String, play: Boolean? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)
        //    require(deviceId.size <= 1) { "Although an array is accepted, only a single device_id is currently supported. Supplying more than one will  400 Bad Request" }
        val json = jsonMap()
        play?.let { json += buildJsonObject { put("play", it) } }
        json += buildJsonObject { put("device_ids", JsonArray(listOf(deviceId).map(::JsonPrimitive))) }
        put(endpointBuilder("/me/player").toString(), json.mapToJsonString())
    }

    /**
     * Transfer playback to a new device and determine if it should start playing.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/transfer-a-users-playback/)**
     *
     * @param deviceId The device to play on
     * @param play Whether to immediately start playback on the transferred device
     */
    public fun transferPlaybackRestAction(deviceId: String, play: Boolean? = null): SpotifyRestAction<Unit> =
        SpotifyRestAction {
            transferPlayback(deviceId, play)
        }

    /**
     * Add an item to the end of the user’s current playback queue.
     * Please note that all items in the queue will be played before resuming the current playlist/album playing, if there is one.
     *
     * This method is provided **AS-IS** until Spotify
     * exposes device queue. Please consider managing the player queue within your application.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-add-to-queue)**
     *
     * @param uri The uri of the item to add to the queue. Must be a track or an episode uri.
     * @param deviceId The device to play on.
     */
    public suspend fun addItemToEndOfQueue(uri: PlayableUri, deviceId: String? = null) {
        requireScopes(SpotifyScope.USER_MODIFY_PLAYBACK_STATE)

        post(endpointBuilder("/me/player/queue").with("uri", uri.uri).with("device_id", deviceId).toString())
    }

    /**
     * Add an item to the end of the user’s current playback queue.
     *
     * **Requires** the [SpotifyScope.USER_MODIFY_PLAYBACK_STATE] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-add-to-queue)**
     *
     * @param uri The uri of the item to add to the queue. Must be a track or an episode uri.
     * @param deviceId The device to play on.
     */
    public fun addItemToEndOfQueueRestAction(uri: PlayableUri, deviceId: String? = null): SpotifyRestAction<Unit> {
        return SpotifyRestAction {
            addItemToEndOfQueue(uri, deviceId)
        }
    }

    /**
     * What state the player can repeat in.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/player/set-repeat-mode-on-users-playback/)**
     */
    public enum class PlayerRepeatState(public val identifier: String) : ResultEnum {
        /**
         * Repeat the current track
         */
        TRACK("track"),

        /**
         * Repeat the current context
         */
        CONTEXT("context"),

        /**
         * Will turn repeat off
         */
        OFF("off");

        override fun retrieveIdentifier(): String = identifier
    }
}
