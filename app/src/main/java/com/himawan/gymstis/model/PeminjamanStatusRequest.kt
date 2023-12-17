package com.himawan.gymstis.model

import kotlinx.serialization.Serializable

@Serializable
data class PeminjamanStatusRequest(
    val status: String
)