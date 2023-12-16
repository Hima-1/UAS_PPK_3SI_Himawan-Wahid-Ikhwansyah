package com.himawan.gymstis.service

import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.model.JadwalResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JadwalService {
    @POST("/api/jadwal")
    suspend fun createJadwal(@Header("Authorization") token: String, @Body jadwalForm: JadwalForm): JadwalResponse

    @GET("/api/jadwal")
    suspend fun getAvailableJadwals(@Header("Authorization") token: String): List<JadwalResponse>

    @PUT("/api/jadwal")
    suspend fun updateJadwal(@Header("Authorization") token: String, @Body jadwalForm: JadwalForm): JadwalResponse

    @DELETE("/api/jadwal/{id}")
    suspend fun deleteJadwal(@Header("Authorization") token: String, @Path("id") id: Long)
}
