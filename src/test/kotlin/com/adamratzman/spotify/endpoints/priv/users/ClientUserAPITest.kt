package com.adamratzman.spotify.kotlin.endpoints.priv.users

import com.adamratzman.spotify.main.clientApi
import org.junit.Test


internal class ClientUserAPITest {
    @Test
    fun getUserProfile() {
        println(clientApi.userProfile.getUserProfile().complete())
    }

}