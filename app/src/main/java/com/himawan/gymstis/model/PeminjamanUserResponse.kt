package com.himawan.gymstis.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PeminjamanUserResponse(
    @Contextual val date: LocalDate,
    val status: String
)
