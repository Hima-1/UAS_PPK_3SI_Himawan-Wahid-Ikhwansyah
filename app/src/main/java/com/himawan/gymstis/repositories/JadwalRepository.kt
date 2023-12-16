package com.himawan.gymstis.repositories

import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.model.JadwalResponse
import com.himawan.gymstis.api.service.JadwalService

class JadwalRepository(private val jadwalService: JadwalService) {
    suspend fun createJadwal(token: String, jadwalForm: JadwalForm): JadwalResponse = jadwalService.createJadwal("Bearer $token", jadwalForm)
    suspend fun getAvailableJadwals(token: String): List<JadwalResponse> = jadwalService.getAvailableJadwals("Bearer $token")
    suspend fun deleteJadwal(token: String, jadwalId: Long) = jadwalService.deleteJadwal("Bearer $token", jadwalId)
}

