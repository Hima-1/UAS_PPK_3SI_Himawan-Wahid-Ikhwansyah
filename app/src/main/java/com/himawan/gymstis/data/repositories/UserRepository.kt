package com.himawan.gymstis.data

import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.model.AuthResponse
import com.himawan.gymstis.model.PasswordChangeRequest
import com.himawan.gymstis.model.ProfileEditRequest
import com.himawan.gymstis.model.ProfileResponse
import com.himawan.gymstis.model.User
import com.himawan.gymstis.service.UserService
import com.himawan.gymstis.model.RegisterForm

class UserRepository(private val userService: UserService) {
    suspend fun login(authRequest: AuthRequest): AuthResponse = userService.login(authRequest)
    suspend fun register(registerForm: RegisterForm): User = userService.register(registerForm)
    suspend fun getProfile(token: String): ProfileResponse = userService.getProfile("Bearer $token")
    suspend fun deleteProfile(token: String) = userService.deleteProfile("Bearer $token")
    suspend fun updateProfile(token: String, profileEditRequest: ProfileEditRequest): ProfileResponse = userService.updateProfile("Bearer $token", profileEditRequest)
    suspend fun updatePassword(token: String, passwordChangeRequest: PasswordChangeRequest): ProfileResponse = userService.updatePassword("Bearer $token", passwordChangeRequest)
}

