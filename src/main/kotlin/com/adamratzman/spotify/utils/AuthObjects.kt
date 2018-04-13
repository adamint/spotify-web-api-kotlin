package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyClientAPI

data class Token(val access_token: String, val token_type: String, val expires_in: Int, val refresh_token: String?, val scope: String?) {
    fun getScopes(): List<SpotifyClientAPI.Scope> {
        val scopes = mutableListOf<SpotifyClientAPI.Scope>()
        scope?.split(" ")?.forEach { split ->
            SpotifyClientAPI.Scope.values().forEach { if (split == it.uri) scopes.add(it) }
        }
        return scopes
    }
}

data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(val status: Int, val message: String)

class BadRequestException(error: ErrorObject) : Exception("Received Status Code ${error.status}. Error cause: ${error.message}")