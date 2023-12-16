package com.himawan.gymstis.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest (
    val newPassword: String
)