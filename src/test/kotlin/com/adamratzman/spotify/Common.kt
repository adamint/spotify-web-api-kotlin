package com.adamratzman.spotify

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyClientAPI

val api = if (System.getProperty("spotifyRedirectUri") == null) SpotifyAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret")).build()
else SpotifyClientAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret"),
        System.getProperty("spotifyRedirectUri")).buildToken((System.getProperty("spotifyTokenString")))