package com.himawan.gymstis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.model.PeminjamanResponse
import com.himawan.gymstis.model.PeminjamanStatusRequest
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.ui.screen.FilterCriteriaPeminjaman
import com.himawan.gymstis.ui.screen.Gender
import com.himawan.gymstis.ui.screen.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDate

class PeminjamanViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val peminjamanRepository: PeminjamanRepository
) : ViewModel() {

    private lateinit var token: String
    private val _peminjamanItems = MutableStateFlow<List<PeminjamanResponse>>(emptyList())
    val peminjamanItems = _peminjamanItems.asStateFlow()

    private val _updateStatusResult = MutableStateFlow(UpdateStatusResult.None)
    val updateStatusResult = _updateStatusResult.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Set<FilterCriteriaPeminjaman>>(emptySet())
    val filteredPeminjamanItems: StateFlow<List<PeminjamanResponse>> = _peminjamanItems
        .combine(_selectedFilters) { items, filters ->
            if (filters.isEmpty()) items
            else items.filter { matchesFilters(it, filters) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchPeminjaman()
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    private fun fetchPeminjaman() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.user.collect { user ->
                    val token = user.token
                    val peminjamanData = peminjamanRepository.getPeminjaman(token)
                    _peminjamanItems.value = peminjamanData
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePeminjamanStatus(peminjamanId: Long, newStatus: Status) {
        viewModelScope.launch {
            try {
                val editStatus = PeminjamanStatusRequest(newStatus.name)
                peminjamanRepository.updateStatusPeminjaman(token, peminjamanId, editStatus)
                fetchPeminjaman()
                _updateStatusResult.value = UpdateStatusResult.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _updateStatusResult.value = UpdateStatusResult.Error
            }
        }
    }

    fun resetUpdateStatusResult() {
        _updateStatusResult.value = UpdateStatusResult.None
    }

    private fun matchesFilters(
        item: PeminjamanResponse,
        filters: Set<FilterCriteriaPeminjaman>
    ): Boolean {
        val isOngoing = item.date >= LocalDate.now() // Adjust if necessary
        val isCompleted = item.date < LocalDate.now() // Adjust if necessary
        val isMale = item.gender == Gender.MALE
        val isFemale = item.gender == Gender.FEMALE
        val isPending = item.status == Status.PENDING.value
        val isApproved = item.status == Status.APPROVED.value
        val isDenied = item.status == Status.DENIED.value

        return filters.all { filter ->
            when (filter) {
                FilterCriteriaPeminjaman.Ongoing -> isOngoing
                FilterCriteriaPeminjaman.Completed -> isCompleted
                FilterCriteriaPeminjaman.Male -> isMale
                FilterCriteriaPeminjaman.Female -> isFemale
                FilterCriteriaPeminjaman.Pending -> isPending
                FilterCriteriaPeminjaman.Approved -> isApproved
                FilterCriteriaPeminjaman.Denied -> isDenied
            }
        }
    }

    fun setSelectedFilters(filters: Set<FilterCriteriaPeminjaman>) {
        _selectedFilters.value = filters
    }

    @ExperimentalSerializationApi
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GymStisApplication)
                val peminjamanRepository = application.container.peminjamanRepository
                val userPreferencesRepository = application.userPreferenceRepository
                PeminjamanViewModel(userPreferencesRepository, peminjamanRepository)
            }
        }
    }
}

enum class UpdateStatusResult {
    Success,
    Error,
    None
}
