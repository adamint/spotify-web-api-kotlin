package com.adamratzman.endpoints.public.profiles

import junit.framework.TestCase
import com.adamratzman.main.SpotifyAPI

class ProfilesAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()

    fun testGetProfile() {
        println(api.profiles.getProfile("adamratzman1"))
    }

}