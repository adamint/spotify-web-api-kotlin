/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.notifications

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import androidx.fragment.app.Fragment

/**
 * Register a Spotify broadcast receiver (receiving notifications from the Spotify app) for the specified notification types.
 *
 * This should be used in a [Fragment] or [Activity].
 *
 * Note that "Device Broadcast Status" must be enabled in the Spotify app and the active Spotify device must be the Android
 * device that your app is on to receive notifications.
 *
 * @param receiver An instance of your implementation of [AbstractSpotifyBroadcastReceiver]
 * @param notificationTypes The notification types that you would like to subscribe to.
 */
public fun Context.registerSpotifyBroadcastReceiver(
    receiver: AbstractSpotifyBroadcastReceiver,
    vararg notificationTypes: SpotifyBroadcastType
) {
    val filter = IntentFilter()
    notificationTypes.forEach { filter.addAction(it.id) }

    registerReceiver(receiver, filter)
}
