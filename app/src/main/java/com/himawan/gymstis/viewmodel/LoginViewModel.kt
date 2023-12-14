package com.himawan.gymstis.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.data.UserRepository
import com.himawan.gymstis.data.repositories.AuthRepository
import com.himawan.gymstis.model.AuthRequest
import retrofit2.HttpException

private const val TAG = "LoginViewModel"

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var emailField by mutableStateOf("")
        private set

    var passwordField by mutableStateOf("")
        private set

    fun updateEmail(email: String) {
        emailField = email
    }

    fun updatePassword(password: String) {
        passwordField = password
    }

    suspend fun attemptLogin(): LoginResult {
        try {
            val authRequest = AuthRequest(emailField, passwordField)
            val loginResponse = userRepository.login(authRequest)
            Log.d(TAG, "accessToken: ${loginResponse.accessToken}")

            authRepository.saveToken(loginResponse.accessToken)
            authRepository.saveEmail(loginResponse.email)
            authRepository.saveIsAdmin(loginResponse.roles.contains("ROLE_ADMIN"))
            authRepository.saveGender(loginResponse.gender)

        } catch (e: HttpException) {
            return when (e.code()) {
                400 -> {
                    Log.d(TAG, "bad input")
                    LoginResult.BadInput
                }

                401 -> {
                    Log.d(TAG, "Wrong email or password")
                    LoginResult.WrongEmailOrPassword
                }

                else -> {
                    Log.e(TAG, "Http exception: (${e.javaClass}) ${e.message}")
                    LoginResult.NetworkError
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can't login: (${e.javaClass}) ${e.message}")
            return LoginResult.NetworkError
        }

        return LoginResult.Success
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                LoginViewModel(
                    authRepository = application.authRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}

enum class LoginResult {
    Success,
    BadInput,
    WrongEmailOrPassword,
    NetworkError
}
