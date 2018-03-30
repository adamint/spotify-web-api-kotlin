package com.adamratzman.main

val api = SpotifyAPI.Builder("79d455af5aea45c094c5cea04d167ac1",
        "SECRET")
        .build()
val clientApi = SpotifyClientAPI.Builder("79d455af5aea45c094c5cea04d167ac1",
        "SECRET", "REDIRECT")
        .buildToken("TOKEN")