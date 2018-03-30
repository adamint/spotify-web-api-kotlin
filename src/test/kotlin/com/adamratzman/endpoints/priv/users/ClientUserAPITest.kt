package com.adamratzman.endpoints.priv.users

import com.adamratzman.main.clientApi
import org.junit.Test


internal class ClientUserAPITest {
    @Test
    fun getUserProfile() {
        println(clientApi.userProfile.getUserProfile())
    }

}