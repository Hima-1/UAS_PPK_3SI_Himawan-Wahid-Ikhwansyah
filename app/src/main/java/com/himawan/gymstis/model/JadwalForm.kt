package com.himawan.gymstis.model

import com.himawan.gymstis.ui.screen.Gender
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class JadwalForm(
    @Contextual val date: LocalDate,
    val gender: Gender,
    val kuota: Int
)
