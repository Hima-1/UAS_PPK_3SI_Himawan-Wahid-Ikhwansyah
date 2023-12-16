package com.himawan.gymstis.model

import com.himawan.gymstis.ui.screen.Gender
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse (
    val name: String,
    val email: String,
    val gender: Gender,
    val roles: List<String>
)