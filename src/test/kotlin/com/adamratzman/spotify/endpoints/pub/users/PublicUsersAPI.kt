package com.adamratzman.spotify.kotlin.endpoints.pub.users

import api
import junit.framework.TestCase

class PublicUsersAPI : TestCase() {
    fun testGetProfile() {
        println(api.users.getProfile("adamratzman1").complete())
        println(api.users.getProfile("adamratzman66").complete())
    }
}