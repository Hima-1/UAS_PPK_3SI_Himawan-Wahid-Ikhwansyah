package com.himawan.gymstis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.PeminjamanEditStatus
import com.himawan.gymstis.model.PeminjamanResponse
import com.himawan.gymstis.ui.screen.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeminjamanViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val peminjamanRepository: PeminjamanRepository
) : ViewModel() {

    private lateinit var token: String
    private val _peminjamanItems = MutableStateFlow<List<PeminjamanResponse>>(emptyList())
    val peminjamanItems = _peminjamanItems.asStateFlow()

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
                val editStatus = PeminjamanEditStatus(newStatus.name)
                peminjamanRepository.updateStatusPeminjaman(token, peminjamanId, editStatus)
                fetchPeminjaman()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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
