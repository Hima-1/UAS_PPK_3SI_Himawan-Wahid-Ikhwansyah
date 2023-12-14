package com.himawan.gymstis.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterForm(
    val name: String,
    val gender: String,
    val email: String,
    val password: String
)
