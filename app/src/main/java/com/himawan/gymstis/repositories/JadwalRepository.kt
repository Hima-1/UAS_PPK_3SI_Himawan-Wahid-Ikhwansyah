package com.himawan.gymstis.repositories

import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.model.JadwalResponse
import com.himawan.gymstis.api.service.JadwalService
import retrofit2.Response

class JadwalRepository(private val jadwalService: JadwalService) {
    suspend fun createJadwal(token: String, jadwalForm: JadwalForm): Response<Unit> = jadwalService.createJadwal("Bearer $token", jadwalForm)
    suspend fun getAvailableJadwals(token: String): List<JadwalResponse> = jadwalService.getAvailableJadwals("Bearer $token")
    suspend fun deleteJadwal(token: String, jadwalId: Long) = jadwalService.deleteJadwal("Bearer $token", jadwalId)
}

