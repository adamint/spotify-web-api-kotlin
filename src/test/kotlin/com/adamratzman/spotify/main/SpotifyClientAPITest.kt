package com.adamratzman.spotify.main

import junit.framework.TestCase


class SpotifyClientAPITest : TestCase() {
    fun testCreation() {
       val api= SpotifyAPI.Builder("", "").build()
        val client = api.authorizeUser("",
                redirectUri = "https://ardentbot.com")
        println(client.clientLibrary.getSavedTracks().complete().items)
        // println(api.)
     //   Assert.assertArrayEquals(api.token!!.getScopes().toTypedArray(), arrayOf(SpotifyClientAPI.Scope.UGC_IMAGE_UPLOAD, SpotifyClientAPI.Scope.PLAYLIST_MODIFY_PRIVATE))
    }
}