package com.himawan.gymstis.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    private val _loginResult = MutableStateFlow(LoginResult.None)
    val loginResult = _loginResult.asStateFlow()

    suspend fun login() {
        _loginResult.value = try {
            val loginResponse = userRepository.login(AuthRequest(email, password))
            userPreferencesRepository.saveToken(loginResponse.accessToken)
            userPreferencesRepository.saveEmail(loginResponse.email)
            userPreferencesRepository.saveGender(loginResponse.gender)
            userPreferencesRepository.saveIsStaff(loginResponse.roles.contains("ROLE_STAFF"))
            LoginResult.Success
        } catch (e: IOException) {
            LoginResult.NetworkError
        } catch (e: Exception) {
            LoginResult.WrongEmailOrPassword
        }
    }

    fun resetLoginResult() {
        _loginResult.value = LoginResult.None
    }

    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                LoginViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}

enum class LoginResult {
    Success,
    WrongEmailOrPassword,
    NetworkError,
    None
}
