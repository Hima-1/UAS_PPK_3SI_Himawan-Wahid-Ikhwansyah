package com.himawan.gymstis.service

import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.model.JadwalResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface JadwalService {
    @POST("/jadwal")
    suspend fun createJadwal(@Header("Authorization") token: String, @Body jadwalForm: JadwalForm): JadwalResponse

    @GET("/jadwal")
    suspend fun getAvailableJadwals(@Header("Authorization") token: String): List<JadwalResponse>

    @PUT("/jadwal")
    suspend fun updateJadwal(@Header("Authorization") token: String, @Body jadwalForm: JadwalForm): JadwalResponse
}
