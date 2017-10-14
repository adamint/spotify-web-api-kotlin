package com.adamratzman.main

import junit.framework.TestCase
import org.junit.Assert

val api = SpotifyClientAPI.Builder("79d455af5aea45c094c5cea04d167ac1",
        "", "https://ardentbot.com/spotify")
        .build("", true)

class SpotifyClientAPITest : TestCase() {
    fun testCreation() {
        Assert.assertArrayEquals(api.token!!.getScopes().toTypedArray(), arrayOf(SpotifyClientAPI.Scope.UGC_IMAGE_UPLOAD, SpotifyClientAPI.Scope.PLAYLIST_MODIFY_PRIVATE))
    }
}