package com.adamratzman.spotify.public

import com.adamratzman.spotify.main.SpotifyAPI

val api = SpotifyAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret")).build()
