package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.models.SpotifyUri
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.Subscription
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.types.CrossfadeState
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.PlaybackSpeed
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState
import kotlinx.serialization.Serializable

public class PlayerApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) {
    public fun getCrossfadeState(callback: ((CrossfadeStateWrapper) -> Unit)? = null): CallResult<CrossfadeState, CrossfadeStateWrapper> {
        return spotifyAppRemoteWrapper.playerApi.crossfadeState
            .toLibraryCallResult({ CrossfadeStateWrapper(it.isEnabled, it.duration) }, callback)
    }

    public fun getPlayerState(callback: ((PlayerStateWrapper) -> Unit)? = null): CallResult<PlayerState, PlayerStateWrapper> {
        return spotifyAppRemoteWrapper.playerApi.playerState
            .toLibraryCallResult({ playerState -> playerState.toPlayerStateWrapper() }, callback)
    }

    public fun pause(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.pause()
            .toLibraryCallResult({ }, callback)
    }

    public fun play(
        uri: SpotifyUri,
        streamType: StreamTypeWrapper? = null,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return if (streamType == null) {
            spotifyAppRemoteWrapper.playerApi.play(uri.uri).toLibraryCallResult({ }, callback)
        } else spotifyAppRemoteWrapper.playerApi.play(uri.uri, PlayerApi.StreamType.valueOf(streamType.id))
            .toLibraryCallResult({ }, callback)
    }

    public fun queue(uri: SpotifyUri, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.queue(uri.uri)
            .toLibraryCallResult({ }, callback)
    }

    public fun resume(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.resume()
            .toLibraryCallResult({ }, callback)
    }

    public fun seekTo(positionMs: Long, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.seekTo(positionMs)
            .toLibraryCallResult({ }, callback)
    }

    public fun seekToRelativePosition(positionMs: Long, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.seekToRelativePosition(positionMs)
            .toLibraryCallResult({ }, callback)
    }

    public fun setPodcastPlaybackSpeed(
        playbackSpeed: PodcastPlaybackSpeedWrapper,
        callback: ((Unit) -> Unit)? = null
    ): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.setPodcastPlaybackSpeed(
            PlaybackSpeed.PodcastPlaybackSpeed.valueOf(
                playbackSpeed.id
            )
        )
            .toLibraryCallResult({ }, callback)
    }

    public fun setRepeat(repeatMode: RepeatModeWrapper, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.setRepeat(repeatMode.id)
            .toLibraryCallResult({ }, callback)
    }

    public fun setShuffle(shouldEnableShuffle: Boolean, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.setShuffle(shouldEnableShuffle)
            .toLibraryCallResult({ }, callback)
    }

    public fun skipToNext(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.skipNext()
            .toLibraryCallResult({ }, callback)
    }

    public fun skipToPreviousOrRestart(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.skipPrevious()
            .toLibraryCallResult({ }, callback)
    }

    public fun skipToIndex(uri: SpotifyUri, index: Int, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.skipToIndex(uri.uri, index)
            .toLibraryCallResult({ }, callback)
    }

    public fun subscribeToPlayerContext(): Subscription<PlayerContext, PlayerContextWrapper> {
        return Subscription(spotifyAppRemoteWrapper.playerApi.subscribeToPlayerContext()) { playerContext ->
            PlayerContextWrapper(
                playerContext.subtitle,
                playerContext.title,
                playerContext.type,
                playerContext.uri
            )
        }
    }

    public fun subscribeToPlayerState(): Subscription<PlayerState, PlayerStateWrapper> {
        return Subscription(spotifyAppRemoteWrapper.playerApi.subscribeToPlayerState()) { playerState ->
            playerState.toPlayerStateWrapper()
        }
    }

    public fun toggleRepeat(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.toggleRepeat()
            .toLibraryCallResult({ }, callback)
    }

    public fun toggleShuffle(callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.playerApi.toggleShuffle()
            .toLibraryCallResult({ }, callback)
    }
}

/**
 * Represents the state of Spotify's audio crossfade setting. This setting enable crossfading between tracks.
 *
 * @param isEnabled Whether crossfade is enabled.
 * @param durationInMillis How long the crossfade is for.
 */
@Serializable
public data class CrossfadeStateWrapper(
    val isEnabled: Boolean,
    val durationInMillis: Int
)

/**
 * The kind of stream type (only alarm is specified).
 */
public enum class StreamTypeWrapper(internal val id: String) {
    Alarm("ALARM")
}

/**
 * The speed at which to play the podcast.
 *
 * @
 */
public enum class PodcastPlaybackSpeedWrapper(public val speedMultiplier: Float, internal val id: String) {
    Playback100Percent(1f, "PLAYBACK_SPEED_100"),
    Playback120Percent(1.2f, "PLAYBACK_SPEED_120"),
    Playback150Percent(1.5f, "PLAYBACK_SPEED_150"),
    Playback200Percent(2f, "PLAYBACK_SPEED_200"),
    Playback300Percent(3f, "PLAYBACK_SPEED_300"),
    Playback50Percent(0.5f, "PLAYBACK_SPEED_50"),
    Playback80Percent(0.8f, "PLAYBACK_SPEED_80"),
}

/**
 * What kind of repeat mode to set/that is set.
 */
public enum class RepeatModeWrapper(internal val id: Int) {
    Off(0),
    One(1),
    All(2)
}

@Serializable
public data class PlayerContextWrapper(
    val subtitle: String?,
    val title: String,
    val type: String,
    val uri: String
)

@Serializable
public data class PlayerStateWrapper(
    val isPaused: Boolean,
    val playerOptions: PlayerOptionsWrapper,
    val playbackPositionInMillis: Long,
    val playerRestrictions: PlayerRestrictionsWrapper,
    val playbackSpeed: Float,
    val track: PlayerTrackWrapper
)

@Serializable
public data class PlayerOptionsWrapper(
    val isShuffling: Boolean,
    val repeatMode: RepeatModeWrapper
)

@Serializable
public data class PlayerRestrictionsWrapper(
    val canRepeatContext: Boolean,
    val canRepeatTrack: Boolean,
    val canSeek: Boolean,
    val canSkipNext: Boolean,
    val canSkipPrev: Boolean,
    val canToggleShuffle: Boolean
)

@Serializable
public data class PlayerTrackWrapper(
    val album: PlayerAlbumWrapper,
    val artist: PlayerArtistWrapper,
    val artists: List<PlayerArtistWrapper>,
    val durationInMillis: Long,
    val imageUri: ImageUriWrapper?,
    val isEpisode: Boolean,
    val isPodcast: Boolean,
    val name: String,
    val uri: String
)

@Serializable
public data class PlayerAlbumWrapper(
    val name: String,
    val uri: String
)

@Serializable
public data class PlayerArtistWrapper(
    val name: String,
    val uri: String
)

private fun PlayerState.toPlayerStateWrapper(): PlayerStateWrapper {
    return PlayerStateWrapper(
        isPaused,
        PlayerOptionsWrapper(
            playbackOptions.isShuffling,
            RepeatModeWrapper.values().first { it.id == playbackOptions.repeatMode }),
        playbackPosition,
        with(playbackRestrictions) {
            PlayerRestrictionsWrapper(
                canRepeatContext,
                canRepeatTrack,
                canSeek,
                canSkipNext,
                canSkipPrev,
                canToggleShuffle
            )
        },
        playbackSpeed,
        with(track) {
            PlayerTrackWrapper(
                with(album) {
                    PlayerAlbumWrapper(name, uri)
                },
                with(artist) {
                    PlayerArtistWrapper(name, uri)
                },
                artists.map { PlayerArtistWrapper(it.name, it.uri) },
                duration,
                imageUri.raw?.let { ImageUriWrapper(it) },
                isEpisode,
                isPodcast,
                name,
                uri
            )
        }
    )
}