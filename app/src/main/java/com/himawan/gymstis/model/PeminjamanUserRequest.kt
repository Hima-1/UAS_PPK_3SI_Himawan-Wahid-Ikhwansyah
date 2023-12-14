package com.himawan.gymstis.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PeminjamanUserRequest(
    @Contextual val date: LocalDate
)
