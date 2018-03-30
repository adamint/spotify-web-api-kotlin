package com.adamratzman.endpoints.pub.users

import junit.framework.TestCase
import com.adamratzman.main.api
import com.adamratzman.main.clientApi

class PublicUsersAPI : TestCase() {
    fun testGetProfile() {
        println(api.users.getProfile("adamratzman1"))
    }

}