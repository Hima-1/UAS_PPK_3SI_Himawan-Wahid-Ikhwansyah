package com.himawan.gymstis.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.data.JadwalRepository
import com.himawan.gymstis.data.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.ui.screen.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateJadwalViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jadwalRepository: JadwalRepository
) : ViewModel() {
    private lateinit var token: String
    var selectedDate by mutableStateOf(LocalDate.now())
    var gender by mutableStateOf(Gender.MALE)
    var kuota by mutableStateOf("")

    private val _navigateToJadwal = MutableStateFlow(false)
    val navigateToJadwal = _navigateToJadwal.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    fun createJadwal(): CreateJadwalResult {
        return try {
            viewModelScope.launch {
                val jadwalForm = JadwalForm(
                    date = selectedDate,
                    gender = gender,
                    kuota = kuota.toInt()
                )
                jadwalRepository.createJadwal(token, jadwalForm)
                _navigateToJadwal.value = true
            }
            CreateJadwalResult.Success
        } catch (e: Exception) {
            CreateJadwalResult.Error
        }
    }

    fun resetNavigationTrigger() {
        _navigateToJadwal.value = false
    }

    fun updateDate(newDate: LocalDate) {
        selectedDate = newDate
    }

    fun updateGender(newGender: Gender) {
        gender = newGender
    }

    fun updateKuota(newKuota: String) {
        kuota = newKuota
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val jadwalRepository = application.container.jadwalRepository
                CreateJadwalViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    jadwalRepository = jadwalRepository
                )
            }
        }
    }
}

enum class CreateJadwalResult {
    Success,
    Error
}
