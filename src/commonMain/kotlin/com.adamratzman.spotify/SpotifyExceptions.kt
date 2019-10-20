package com.adamratzman.spotify

open class SpotifyException(message: String, cause: Throwable? = null) : Exception(message, cause)