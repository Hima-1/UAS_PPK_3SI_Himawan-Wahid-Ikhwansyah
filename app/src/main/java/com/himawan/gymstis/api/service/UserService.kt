package com.himawan.gymstis.api.service

import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.model.AuthResponse
import com.himawan.gymstis.model.PasswordChangeRequest
import com.himawan.gymstis.model.ProfileEditRequest
import com.himawan.gymstis.model.ProfileResponse
import com.himawan.gymstis.model.RegisterForm
import com.himawan.gymstis.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST("/login")
    suspend fun login(@Body authRequest: AuthRequest): AuthResponse

    @POST("/register")
    suspend fun register(@Body registerForm: RegisterForm): User

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse

    @DELETE("/profile")
    suspend fun deleteProfile(@Header("Authorization") token: String)

    @PUT("/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body profileEditRequest: ProfileEditRequest
    ): ProfileResponse

    @PATCH("/profile/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body passwordChangeRequest: PasswordChangeRequest
    ): ProfileResponse
}
