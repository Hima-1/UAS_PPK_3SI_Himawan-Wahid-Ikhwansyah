package com.himawan.gymstis.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class JadwalForm(
    @Contextual val date: LocalDate,
    val gender: String,
    val kuota: Int
)
