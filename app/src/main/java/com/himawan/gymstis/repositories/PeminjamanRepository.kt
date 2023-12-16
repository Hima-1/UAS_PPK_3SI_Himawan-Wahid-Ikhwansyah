package com.himawan.gymstis.repositories

import com.himawan.gymstis.model.PeminjamanEditStatus
import com.himawan.gymstis.model.PeminjamanRequest
import com.himawan.gymstis.model.PeminjamanResponse
import com.himawan.gymstis.api.service.PeminjamanService

class PeminjamanRepository(private val peminjamanService: PeminjamanService) {
    suspend fun createPeminjaman(token: String, peminjamanRequest: PeminjamanRequest): PeminjamanResponse = peminjamanService.createPeminjaman("Bearer $token", peminjamanRequest)
    suspend fun getPeminjaman(token: String): List<PeminjamanResponse> = peminjamanService.getAllPeminjaman("Bearer $token")
    suspend fun updateStatusPeminjaman(token: String, peminjamanId: Long, peminjamanEditStatus: PeminjamanEditStatus): PeminjamanResponse = peminjamanService.updateStatusPeminjaman("Bearer $token", peminjamanId, peminjamanEditStatus)
}

