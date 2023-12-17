package com.himawan.gymstis.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.model.RegisterForm
import com.himawan.gymstis.repositories.UserRepository
import com.himawan.gymstis.ui.screen.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var gender = mutableStateOf(Gender.MALE)
    var passwordVisibility = mutableStateOf(false)

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult = _registerResult.asStateFlow()

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateGender(newGender: Gender) {
        gender.value = newGender
    }

    fun togglePasswordVisibility() {
        passwordVisibility.value = !passwordVisibility.value
    }

    suspend fun register() {
        if (!validateInputs()) {
            _registerResult.value = RegisterResult.EmptyField
            return
        }
        try {
            userRepository.register(
                RegisterForm(
                    name.value,
                    gender.value.toString(),
                    email.value,
                    password.value
                )
            )
            _registerResult.value = RegisterResult.Success
        } catch (e: Exception) {
            _registerResult.value = RegisterResult.NetworkError
        }
    }

    private fun validateInputs(): Boolean {
        return name.value.isNotBlank() &&
                email.value.isNotBlank() &&
                password.value.isNotBlank()
    }

    fun resetRegisterResult() {
        _registerResult.value = null
    }

    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                RegisterViewModel(userRepository = userRepository)
            }
        }
    }
}

enum class RegisterResult {
    Success,
    EmptyField,
    NetworkError
}
