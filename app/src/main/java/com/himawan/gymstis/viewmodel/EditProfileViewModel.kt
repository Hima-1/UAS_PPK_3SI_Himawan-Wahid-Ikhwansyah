package com.himawan.gymstis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.data.UserRepository
import com.himawan.gymstis.data.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.ProfileEditRequest
import com.himawan.gymstis.ui.screen.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditProfileScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var email = MutableStateFlow("")
    var name = MutableStateFlow("")
    var gender = MutableStateFlow<Gender>(Gender.MALE)

    private val _profileUpdated = MutableStateFlow(false)
    val profileUpdated = _profileUpdated.asStateFlow()

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

    fun updateProfile() {
        viewModelScope.launch {
            try {
                val profileEditRequest = ProfileEditRequest(
                    name = name.value,
                    email = email.value,
                    gender = gender.value
                )
                userRepository.updateProfile(token, profileEditRequest)
                userPreferencesRepository.clearUserData()
                _profileUpdated.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetNavigationTrigger() {
        _profileUpdated.value = false
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymStisApplication)
                val userRepository = application.container.userRepository
                val userPreferencesRepository = application.userPreferenceRepository
                EditProfileScreenViewModel(userPreferencesRepository, userRepository)
            }
        }
    }
}