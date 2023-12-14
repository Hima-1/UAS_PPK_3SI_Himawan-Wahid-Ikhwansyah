package com.himawan.gymstis.data

import com.himawan.gymstis.service.JadwalService
import com.himawan.gymstis.service.PeminjamanService
import com.himawan.gymstis.service.UserService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val userRepository: UserRepository
    val jadwalRepository: JadwalRepository
    val peminjamanRepository: PeminjamanRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.1.16:8080/"
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build())
        .build()

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
