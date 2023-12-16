package com.himawan.gymstis.model

import com.himawan.gymstis.ui.screen.Gender
import kotlinx.serialization.Serializable

@Serializable
data class ProfileEditRequest(
    val name: String,
    val email: String,
    val gender: Gender
)