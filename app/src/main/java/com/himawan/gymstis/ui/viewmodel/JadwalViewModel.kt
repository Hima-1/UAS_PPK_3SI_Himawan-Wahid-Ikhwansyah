package com.himawan.gymstis.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.himawan.gymstis.GymStisApplication
import com.himawan.gymstis.repositories.JadwalRepository
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.repositories.UserPreferencesRepository
import com.himawan.gymstis.model.JadwalResponse
import com.himawan.gymstis.model.PeminjamanRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class JadwalViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jadwalRepository: JadwalRepository,
    private val peminjamanRepository: PeminjamanRepository
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
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("JadwalViewModel", "deleteJadwal: ${e.message}")
                refreshJadwals()
            }
        }
    }

    fun applyForJadwal(jadwalId: Long, date: LocalDate) {
        viewModelScope.launch {
            try {
                peminjamanRepository.createPeminjaman(
                    token,
                    PeminjamanRequest(date)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("JadwalViewModel", "applyForJadwal: ${e.message}")
            }
        }
    }

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