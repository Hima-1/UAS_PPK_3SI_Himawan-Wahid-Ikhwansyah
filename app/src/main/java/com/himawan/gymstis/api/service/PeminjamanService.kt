package com.himawan.gymstis.api.service

import com.himawan.gymstis.model.PeminjamanEditStatus
import com.himawan.gymstis.model.PeminjamanRequest
import com.himawan.gymstis.model.PeminjamanResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PeminjamanService {
    @POST("/api/peminjaman")
    suspend fun createPeminjaman(@Header("Authorization") token: String, @Body peminjamanRequest: PeminjamanRequest): PeminjamanResponse

    @GET("/api/peminjaman")
    suspend fun getAllPeminjaman(@Header("Authorization") token: String): List<PeminjamanResponse>

    @PATCH("/api/peminjaman/{id}/status")
    suspend fun updateStatusPeminjaman(@Header("Authorization") token: String, @Path("id") id: Long, @Body peminjamanEditStatus: PeminjamanEditStatus): PeminjamanResponse
}
