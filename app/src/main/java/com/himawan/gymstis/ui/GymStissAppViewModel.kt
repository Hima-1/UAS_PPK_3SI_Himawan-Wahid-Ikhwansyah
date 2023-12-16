package com.himawan.gymstis.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.data.repositories.UserPreferencesRepository
import com.himawan.gymstis.data.repositories.UserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GymStisAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val userState: StateFlow<UserState> = userPreferencesRepository.user.map { user ->
        user
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserState(
            "",
            "",
            "",
            "",
            isStaff = false,
        )
    )
    
    suspend fun logout() {
        userPreferencesRepository.saveToken("")
        userPreferencesRepository.saveName("")
        userPreferencesRepository.saveEmail("")
        userPreferencesRepository.saveGender("")
        userPreferencesRepository.saveIsStaff(false)
    }
    
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                GymStisAppViewModel(
                    userPreferencesRepository = application.userPreferenceRepository
                )
            }
        }
    }
}
