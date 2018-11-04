package com.adamratzman.spotify.private

import com.adamratzman.spotify.main.SpotifyClientAPI

val clientAPI = SpotifyClientAPI.Builder(System.getProperty("clientId"), System.getProperty("clientSecret"),
        System.getProperty("spotifyRedirectUri"))
        .buildToken(System.getProperty("spotifyAuthCode"))