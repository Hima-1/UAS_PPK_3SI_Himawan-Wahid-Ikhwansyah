package com.himawan.gymstis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.model.JadwalResponse
import com.himawan.gymstis.model.PeminjamanRequest
import com.himawan.gymstis.repositories.JadwalRepository
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.ui.screen.FilterCriteriaJadwal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDate

class JadwalViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jadwalRepository: JadwalRepository,
    private val peminjamanRepository: PeminjamanRepository
) : ViewModel() {

    private lateinit var token: String
    private val _jadwals = MutableStateFlow<List<JadwalResponse>>(emptyList())
    val jadwals = _jadwals.asStateFlow()

    private val _deleteStatus = MutableStateFlow(ActionStatus.None)
    val deleteStatus = _deleteStatus.asStateFlow()

    private val _applyStatus = MutableStateFlow(ActionStatus.None)
    val applyStatus = _applyStatus.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Set<FilterCriteriaJadwal>>(emptySet())
    val filteredJadwals: StateFlow<List<JadwalResponse>> = _jadwals
        .combine(_selectedFilters) { jadwals, filters ->
            if (filters.isEmpty()) jadwals
            else jadwals.filter { jadwal -> matchesFilters(jadwal, filters) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                refreshJadwals()
            }
        }
    }

    private fun refreshJadwals() {
        viewModelScope.launch {
            try {
                val jadwalList = jadwalRepository.getAvailableJadwals(token)
                _jadwals.value = jadwalList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteJadwal(jadwalId: Long) {
        viewModelScope.launch {
            try {
                jadwalRepository.deleteJadwal(token, jadwalId)
                refreshJadwals()
                _deleteStatus.value = ActionStatus.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _deleteStatus.value = ActionStatus.Error
            }
        }
    }

    fun applyForJadwal(date: LocalDate) {
        viewModelScope.launch {
            try {
                peminjamanRepository.createPeminjaman(
                    token,
                    PeminjamanRequest(date)
                )
                _applyStatus.value = ActionStatus.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _applyStatus.value = ActionStatus.Error
            }
        }
    }

    fun resetActionStatus() {
        _deleteStatus.value = ActionStatus.None
        _applyStatus.value = ActionStatus.None
    }

    fun setSelectedFilters(filters: Set<FilterCriteriaJadwal>) {
        _selectedFilters.value = filters
    }

    private fun matchesFilters(
        jadwal: JadwalResponse,
        filters: Set<FilterCriteriaJadwal>
    ): Boolean {
        val isOngoing = jadwal.date >= LocalDate.now()
        val isCompleted = jadwal.date < LocalDate.now()
        val isMale = jadwal.gender == "MALE"
        val isFemale = jadwal.gender == "FEMALE"

        return filters.all { filter ->
            when (filter) {
                FilterCriteriaJadwal.Ongoing -> isOngoing
                FilterCriteriaJadwal.Completed -> isCompleted
                FilterCriteriaJadwal.Male -> isMale
                FilterCriteriaJadwal.Female -> isFemale
            }
        }
    }

    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val jadwalRepository = application.container.jadwalRepository
                val userPreferencesRepository = application.userPreferenceRepository
                val peminjamanRepository = application.container.peminjamanRepository
                JadwalViewModel(userPreferencesRepository, jadwalRepository, peminjamanRepository)
            }
        }
    }
}

enum class ActionStatus {
    Success,
    Error,
    None
}