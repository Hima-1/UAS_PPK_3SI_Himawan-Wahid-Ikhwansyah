package com.himawan.gymstis.di

import com.himawan.gymstis.api.JadwalRepository
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.repositories.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val jadwalRepository: JadwalRepository
    val peminjamanRepository: PeminjamanRepository
}