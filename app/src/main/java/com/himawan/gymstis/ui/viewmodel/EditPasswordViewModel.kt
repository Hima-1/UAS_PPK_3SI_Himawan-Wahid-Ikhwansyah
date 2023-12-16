package com.himawan.gymstis.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.repositories.UserRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.PasswordChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

class EditPasswordViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var newPassword by mutableStateOf("")

    private lateinit var token: String
    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToProfile = _navigateToLogin.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    private fun validateInputs(): Boolean {
        return newPassword.isNotBlank()
    }

    fun changePassword() {
        viewModelScope.launch {
            if (!validateInputs()) {
                return@launch
            }
            try {
                userRepository.updatePassword(token, PasswordChangeRequest(newPassword))
                userPreferencesRepository.clearUserData()
                _navigateToLogin.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun resetNavigationTrigger() {
        _navigateToLogin.value = false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                val userPreferencesRepository = application.userPreferenceRepository
                EditPasswordViewModel(userPreferencesRepository, userRepository)
            }
        }
    }
}