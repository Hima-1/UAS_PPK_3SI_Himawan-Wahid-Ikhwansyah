package com.himawan.gymstis.data

import com.himawan.gymstis.service.JadwalService
import com.himawan.gymstis.service.PeminjamanService
import com.himawan.gymstis.service.UserService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.time.format.DateTimeFormatter

interface AppContainer {
    val userRepository: UserRepository
    val jadwalRepository: JadwalRepository
    val peminjamanRepository: PeminjamanRepository
}

@ExperimentalSerializationApi
@Serializer(forClass = java.time.LocalDate::class)
object LocalDateSerializer : KSerializer<java.time.LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: java.time.LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): java.time.LocalDate {
        return java.time.LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@ExperimentalSerializationApi
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.1.16:8080/"
    private val json = Json {
        ignoreUnknownKeys = true
        serializersModule = kotlinx.serialization.modules.SerializersModule {
            contextual(java.time.LocalDate::class, LocalDateSerializer)
        }
    }


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
