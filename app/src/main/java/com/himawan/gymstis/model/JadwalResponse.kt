package com.himawan.gymstis.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class JadwalResponse(
    val hari: String,
    @Contextual val date: LocalDate,
    val gender: String,
    val peminjam: Int,
    val kuota: Int
)
