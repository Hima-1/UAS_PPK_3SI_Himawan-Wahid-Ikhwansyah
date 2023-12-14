package com.himawan.gymstis.data

import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.model.AuthResponse
import com.himawan.gymstis.model.User
import com.himawan.gymstis.service.UserService
import com.himawan.gymstis.model.RegisterForm

class UserRepository(private val userService: UserService) {
    suspend fun login(authRequest: AuthRequest): AuthResponse = userService.login(authRequest)
    suspend fun register(registerForm: RegisterForm): User = userService.register(registerForm)
}
