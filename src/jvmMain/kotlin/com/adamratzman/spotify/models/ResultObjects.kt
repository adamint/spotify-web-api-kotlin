package com.adamratzman.spotify.models

import com.squareup.moshi.Json

data class AuthenticationErrorResponse(
    val error: String,
    @Json(name = "error_description") val description: String
)