package com.himawan.gymstis.service

import com.himawan.gymstis.model.PeminjamanUserRequest
import com.himawan.gymstis.model.PeminjamanUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PeminjamanService {
    @POST("/peminjaman")
    suspend fun createPeminjaman(@Header("Authorization") token: String, @Body peminjamanRequest: PeminjamanUserRequest): PeminjamanUserResponse

    @GET("/peminjaman/{userId}")
    suspend fun getPeminjamanByUser(@Header("Authorization") token: String, @Path("userId") userId: Long): List<PeminjamanUserResponse>
}
