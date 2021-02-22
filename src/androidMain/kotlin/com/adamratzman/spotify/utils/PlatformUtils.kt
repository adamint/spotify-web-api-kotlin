/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

@SuppressLint("SimpleDateFormat")
internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format).format(Date(date))
}

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.ANDROID

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()


// safeLet retrieved from: https://stackoverflow.com/a/35522422/6422820
private fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? =
    if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null

internal fun toast(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
    safeLet(context, message, duration) { safeContext, safeMessage, safeDuration ->
        (safeContext as? Activity)?.runOnUiThread {
            Toast.makeText(safeContext, safeMessage, safeDuration).show()
        }
    }
}

internal fun logToConsole(message: String) {
    Log.i("spotify-web-api-kotlin", message)
}