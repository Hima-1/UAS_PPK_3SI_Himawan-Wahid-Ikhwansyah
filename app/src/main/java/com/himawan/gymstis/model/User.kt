package com.himawan.gymstis.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Long?,
    val name: String,
    val password: String,
    val email: String,
    val roles: List<Role>?,
    val gender: String
)