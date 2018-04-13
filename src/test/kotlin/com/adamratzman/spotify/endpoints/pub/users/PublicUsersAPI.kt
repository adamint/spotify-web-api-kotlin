package com.adamratzman.spotify.kotlin.endpoints.pub.users

import junit.framework.TestCase
import com.adamratzman.spotify.main.api

class PublicUsersAPI : TestCase() {
    fun testGetProfile() {
        println(api.users.getProfile("adamratzman1").complete())
        println(api.users.getProfile("adamratzman66").complete())
    }
}