package com.himawan.gymstis.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest (
    @SerialName("password")
    val newPassword: String
)