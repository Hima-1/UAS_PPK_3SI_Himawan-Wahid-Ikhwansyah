package com.himawan.gymstis.di

import com.himawan.gymstis.api.JadwalRepository
import com.himawan.gymstis.repositories.PeminjamanRepository
import com.himawan.gymstis.api.RetrofitConfig
import com.himawan.gymstis.repositories.UserRepository
import com.himawan.gymstis.api.service.JadwalService
import com.himawan.gymstis.api.service.PeminjamanService
import com.himawan.gymstis.api.service.UserService
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.1.16:8080/"
    private val retrofit = RetrofitConfig(baseUrl).createRetrofit()

    private val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    private val jadwalService: JadwalService by lazy {
        retrofit.create(JadwalService::class.java)
    }

    private val peminjamanService: PeminjamanService by lazy {
        retrofit.create(PeminjamanService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(userService)
    }

    override val jadwalRepository: JadwalRepository by lazy {
        JadwalRepository(jadwalService)
    }

    override val peminjamanRepository: PeminjamanRepository by lazy {
        PeminjamanRepository(peminjamanService)
    }
}