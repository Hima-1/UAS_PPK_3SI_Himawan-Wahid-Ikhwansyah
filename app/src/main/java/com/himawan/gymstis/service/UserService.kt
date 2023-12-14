package com.himawan.gymstis.service

import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.model.AuthResponse
import com.himawan.gymstis.model.User
import com.himawan.gymstis.model.RegisterForm
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header

interface UserService {
    @POST("/login")
    suspend fun login(@Body authRequest: AuthRequest): AuthResponse

    @POST("/register")
    suspend fun register(@Body registerForm: RegisterForm): User
}
