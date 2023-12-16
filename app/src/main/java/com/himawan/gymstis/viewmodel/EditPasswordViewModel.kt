package com.himawan.gymstis.viewmodel

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
import com.himawan.gymstis.data.UserRepository
import com.himawan.gymstis.data.repositories.UserPreferencesRepository
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
    private val _navigateToProfile = MutableStateFlow(false)
    val navigateToProfile = _navigateToProfile.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    fun updateNewPassword(newPassword: String) {
        this.newPassword = newPassword
    }

    fun changePassword() {
        viewModelScope.launch {
            try {
                userRepository.updatePassword(token, PasswordChangeRequest(newPassword))
                _navigateToProfile.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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