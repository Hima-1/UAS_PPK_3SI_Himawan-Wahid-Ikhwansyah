package com.himawan.gymstis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.model.ProfileEditRequest
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.repositories.UserRepository
import com.himawan.gymstis.ui.screen.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

class EditProfileScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var email = MutableStateFlow("")
    var name = MutableStateFlow("")
    var gender = MutableStateFlow(Gender.MALE)

    private val _profileUpdateResult = MutableStateFlow(ProfileUpdateResult.None)
    val profileUpdateResult = _profileUpdateResult.asStateFlow()

    private lateinit var token: String

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    fun updateEmail(email: String) {
        this.email.value = email
    }

    fun updateName(name: String) {
        this.name.value = name
    }

    fun updateGender(gender: Gender) {
        this.gender.value = gender
    }

    private fun validateInputs(): Boolean {
        return name.value.isNotBlank() &&
                email.value.isNotBlank()
    }

    fun updateProfile() {
        viewModelScope.launch {
            if (!validateInputs()) {
                _profileUpdateResult.value = ProfileUpdateResult.BadInput
                return@launch
            }
            try {
                val profileEditRequest = ProfileEditRequest(
                    name = name.value,
                    email = email.value,
                    gender = gender.value
                )
                userRepository.updateProfile(token, profileEditRequest)
                userPreferencesRepository.clearUserData()
                _profileUpdateResult.value = ProfileUpdateResult.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _profileUpdateResult.value = ProfileUpdateResult.Error
            }
        }
    }

    fun resetProfileUpdateResult() {
        _profileUpdateResult.value = ProfileUpdateResult.None
    }


    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                val userPreferencesRepository = application.userPreferenceRepository
                EditProfileScreenViewModel(userPreferencesRepository, userRepository)
            }
        }
    }
}

enum class ProfileUpdateResult {
    Success,
    Error,
    None,
    BadInput
}