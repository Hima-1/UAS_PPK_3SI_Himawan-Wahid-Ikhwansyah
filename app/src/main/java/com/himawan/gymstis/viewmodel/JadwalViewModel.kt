package com.himawan.gymstis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.data.JadwalRepository
import com.himawan.gymstis.data.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.JadwalResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JadwalViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jadwalRepository: JadwalRepository
) : ViewModel() {

    private lateinit var token: String
    private val _jadwals = MutableStateFlow<List<JadwalResponse>>(emptyList())
    val jadwals = _jadwals.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                refreshJadwals()
            }
        }
    }

    fun refreshJadwals() {
        viewModelScope.launch {
            try {
                val jadwalList = jadwalRepository.getAvailableJadwals(token)
                _jadwals.value = jadwalList
            } catch (e: Exception) {
                e.printStackTrace() // or handle the exception as needed
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val jadwalRepository = application.container.jadwalRepository
                val userPreferencesRepository = application.userPreferenceRepository
                JadwalViewModel(userPreferencesRepository, jadwalRepository)
            }
        }
    }
}