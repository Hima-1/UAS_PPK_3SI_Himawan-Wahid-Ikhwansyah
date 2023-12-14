package com.himawan.gymstis.data

import com.himawan.gymstis.model.PeminjamanUserRequest
import com.himawan.gymstis.model.PeminjamanUserResponse
import com.himawan.gymstis.service.PeminjamanService

class PeminjamanRepository(private val peminjamanService: PeminjamanService) {
    suspend fun createPeminjaman(token: String, peminjamanRequest: PeminjamanUserRequest): PeminjamanUserResponse = peminjamanService.createPeminjaman("Bearer $token", peminjamanRequest)
    suspend fun getPeminjamanByUser(token: String, userId: Long): List<PeminjamanUserResponse> = peminjamanService.getPeminjamanByUser("Bearer $token", userId)
}
