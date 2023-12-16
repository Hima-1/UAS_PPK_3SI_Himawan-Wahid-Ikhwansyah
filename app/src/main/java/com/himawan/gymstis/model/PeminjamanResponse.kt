package com.himawan.gymstis.model

import com.himawan.gymstis.ui.screen.Gender
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PeminjamanResponse(
    val id: Long,
    @Contextual val date: LocalDate,
    val status: String,
    val peminjam: Int,
    val kuota: Int,
    val gender: Gender,
    @SerialName("hari")
    val dayName: String
)
