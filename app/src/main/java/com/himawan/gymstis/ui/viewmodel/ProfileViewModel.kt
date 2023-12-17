package com.himawan.gymstis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.repositories.UserRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var token: String
    private val _profile = MutableStateFlow<ProfileResponse?>(null)
    val profile = _profile.asStateFlow()

    private val _actionStatus = MutableStateFlow(ProfileActionStatus.None)
    val actionStatus = _actionStatus.asStateFlow()

    init {
        fetchUserProfile()
    }

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                try {
                    val profileResponse = userRepository.getProfile(user.token)
                    _profile.value = profileResponse
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.clearUserData()
                _actionStatus.value = ProfileActionStatus.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _actionStatus.value = ProfileActionStatus.Error
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                userRepository.deleteProfile(token)
                userPreferencesRepository.clearUserData()
                _actionStatus.value = ProfileActionStatus.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _actionStatus.value = ProfileActionStatus.Error
            }
        }
    }

    fun resetActionStatus() {
        _actionStatus.value = ProfileActionStatus.None
    }

    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                val userPreferencesRepository = application.userPreferenceRepository
                ProfileViewModel(userPreferencesRepository, userRepository)
            }
        }
    }
}

enum class ProfileActionStatus {
    Success,
    Error,
    None
}
