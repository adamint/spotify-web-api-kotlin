/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.notifications.AbstractSpotifyBroadcastReceiver.Companion.BaseSpotifyNotificationId
import com.adamratzman.spotify.utils.logToConsole

/**
 * If you are developing an Android application and want to know what is happening in the Spotify app,
 * you can subscribe to broadcast notifications from it. The Spotify app can posts sticky media broadcast notifications
 * that can be read by any app on the same Android device. The media notifications contain information about what is
 * currently being played in the Spotify App, as well as the playback position and the playback status of the app.
 *
 * Note that media notifications need to be enabled manually in the Spotify app
 *
 * You need to extend this class and register it, whether through the manifest or fragment/activity to receive notifications, as
 * well as overriding [onPlaybackStateChanged], [onQueueChanged], and/or [onMetadataChanged].
 *
 */
public abstract class AbstractSpotifyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeSentInMs = intent.getLongExtra("timeSent", 0L)

        when (intent.action) {
            SpotifyBroadcastType.PlaybackStateChanged.id -> onPlaybackStateChanged(
                SpotifyPlaybackStateChangedData(
                    intent.getBooleanExtra("playing", false),
                    intent.getIntExtra("playbackPosition", 0),
                    timeSentInMs
                )
            )
            SpotifyBroadcastType.QueueChanged.id -> onQueueChanged(SpotifyQueueChangedData(timeSentInMs))
            SpotifyBroadcastType.MetadataChanged.id -> onMetadataChanged(
                SpotifyMetadataChangedData(
                    PlayableUri(intent.getStringExtra("id")!!),
                    intent.getStringExtra("artist")!!,
                    intent.getStringExtra("album")!!,
                    intent.getStringExtra("track")!!,
                    intent.getIntExtra("length", 0),
                    timeSentInMs
                )
            )
        }
    }

    /**
     * A metadata change intent is sent when a new track starts playing.
     *
     * @param data The data associated with this broadcast.
     */
    public open fun onMetadataChanged(data: SpotifyMetadataChangedData) {
        sendUnregisteredNotificationMessage(data.type.id)
    }

    /**
     * A playback state change is sent whenever the user presses play/pause, or when seeking the track position.
     *
     * @param data The data associated with this broadcast.
     */
    public open fun onPlaybackStateChanged(data: SpotifyPlaybackStateChangedData) {
        sendUnregisteredNotificationMessage(data.type.id)
    }

    /**
     * A queue change is sent whenever the play queue is changed.
     *
     * @param data The data associated with this broadcast.
     */
    public open fun onQueueChanged(data: SpotifyQueueChangedData) {
        sendUnregisteredNotificationMessage(data.type.id)
    }

    private fun sendUnregisteredNotificationMessage(action: String) {
        logToConsole("Unregistered notification $action has no handler.")
    }

    public companion object {
        public const val BaseSpotifyNotificationId: String = "com.spotify.music"
    }
}

/**
 * Broadcast receiver types. These must be turned on manually in the Spotify app settings.
 */
public enum class SpotifyBroadcastType(public val id: String) {
    PlaybackStateChanged("$BaseSpotifyNotificationId.playbackstatechanged"),
    QueueChanged("$BaseSpotifyNotificationId.queuechanged"),
    MetadataChanged("$BaseSpotifyNotificationId.metadatachanged")
}

/**
 * Data from a broadcast event
 *
 * @param type The type of the broadcast event
 */
public abstract class SpotifyBroadcastEventData(public val type: SpotifyBroadcastType)

/**
 * A metadata change intent is sent when a new track starts playing. It uses the intent action com.spotify.music.metadatachanged.
 *
 * @param playableUri A Spotify URI for the track or playable.
 * @param artistName The track artist.
 * @param albumName The album name.
 * @param trackName The track name.
 * @param trackLengthInSec Length of the track, in seconds.
 * @param timeSentInMs When the notification was sent.
 */
public data class SpotifyMetadataChangedData(
    val playableUri: PlayableUri,
    val artistName: String,
    val albumName: String,
    val trackName: String,
    val trackLengthInSec: Int,
    val timeSentInMs: Long
) : SpotifyBroadcastEventData(SpotifyBroadcastType.MetadataChanged)

/**
 * A playback state change is sent whenever the user presses play/pause, or when seeking the track position. It uses the intent action com.spotify.music.playbackstatechanged.
 *
 * @param playing True if playing, false if paused.
 * @param positionInMs The current playback position in milliseconds.
 * @param timeSentInMs When the notification was sent.
 */
public data class SpotifyPlaybackStateChangedData(
    val playing: Boolean,
    val positionInMs: Int,
    val timeSentInMs: Long
) : SpotifyBroadcastEventData(SpotifyBroadcastType.PlaybackStateChanged)

/**
 * A queue change is sent whenever the play queue is changed. It uses the intent action com.spotify.music.queuechanged.
 *
 * @param timeSentInMs When the notification was sent.
 */
public class SpotifyQueueChangedData(
    public val timeSentInMs: Long
) : SpotifyBroadcastEventData(SpotifyBroadcastType.QueueChanged)
