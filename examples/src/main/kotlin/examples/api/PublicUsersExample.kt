package examples.api

import api
import com.adamratzman.spotify.utils.BadRequestException
import java.util.concurrent.TimeUnit

fun getProfileAsync() {
    try {
        api.users.getProfile("adamratzman1").queue {
            println("Followers: ${it.followers.total}")
        }
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}