/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.models.ExternalUrl

internal fun getExternalUrls(externalUrlsString: Map<String, String>) = externalUrlsString.map { ExternalUrl(it.key, it.value) }
