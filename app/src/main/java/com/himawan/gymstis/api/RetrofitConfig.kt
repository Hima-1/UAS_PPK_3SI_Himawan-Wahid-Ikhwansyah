package com.himawan.gymstis.api


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate

@ExperimentalSerializationApi
class RetrofitConfig(private val baseUrl: String) {
    private val json = Json {
        ignoreUnknownKeys = true
        serializersModule = kotlinx.serialization.modules.SerializersModule {
            contextual(LocalDate::class, LocalDateSerializer)
        }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(provideOkHttpClient())
            .build()
    }
}
