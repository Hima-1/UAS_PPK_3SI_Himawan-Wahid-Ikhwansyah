package com.himawan.gymstis.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val email: String,
    val accessToken: String,
    val roles: List<String>,
    val gender: String
)
