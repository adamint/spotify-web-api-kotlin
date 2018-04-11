package com.adamratzman.main

val clientId = ""
val clientSecret = ""

val api = SpotifyAPI.Builder(clientId,
        clientSecret)
        .build()
val clientApi = SpotifyClientAPI.Builder(clientId,
        clientSecret, "REDIRECT")
        .buildToken("TOKEN")