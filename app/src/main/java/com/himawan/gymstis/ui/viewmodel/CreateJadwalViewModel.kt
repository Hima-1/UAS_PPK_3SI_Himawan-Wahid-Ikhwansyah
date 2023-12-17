package com.himawan.gymstis.ui.viewmodel

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
import com.himawan.gymstis.model.JadwalForm
import com.himawan.gymstis.repositories.JadwalRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.ui.screen.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDate

class CreateJadwalViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jadwalRepository: JadwalRepository
) : ViewModel() {
    private lateinit var token: String
    private var selectedDate: LocalDate? by mutableStateOf(LocalDate.now())
    var gender by mutableStateOf(Gender.MALE)
    var kuota by mutableStateOf("")

    private val _jadwalCreationResult =
        MutableStateFlow<JadwalCreationResult>(JadwalCreationResult.None)
    val jadwalCreationResult = _jadwalCreationResult.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    fun createJadwal() {
        try {
            if (!validateJadwalForm()) {
                _jadwalCreationResult.value = JadwalCreationResult.Error
                return
            }
            viewModelScope.launch {
                val jadwalForm = selectedDate?.let {
                    JadwalForm(
                        date = it,
                        gender = gender,
                        kuota = kuota.toInt()
                    )
                }
                jadwalForm?.let { jadwalRepository.createJadwal(token, it) }
                _jadwalCreationResult.value = JadwalCreationResult.Success
            }
        } catch (e: Exception) {
            _jadwalCreationResult.value = JadwalCreationResult.Error
        }
    }

    fun validateJadwalForm(): Boolean {
        return selectedDate != null && kuota.isNotEmpty()
    }

    fun resetCreationResult() {
        _jadwalCreationResult.value = JadwalCreationResult.None
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

    @ExperimentalSerializationApi
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

enum class JadwalCreationResult {
    Success,
    Error,
    None
}