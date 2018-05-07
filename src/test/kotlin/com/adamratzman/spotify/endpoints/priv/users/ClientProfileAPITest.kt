package com.adamratzman.spotify.kotlin.endpoints.priv.users

import clientApi
import org.junit.Test


internal class ClientProfileAPITest {
    @Test
    fun getUserProfile() {
        println(clientApi.clientProfile.getUserProfile().complete())
    }

}