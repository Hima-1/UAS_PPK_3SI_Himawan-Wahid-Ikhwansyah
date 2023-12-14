package com.himawan.gymstis

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.himawan.gymstis.data.AppContainer
import com.himawan.gymstis.data.DefaultAppContainer
import com.himawan.gymstis.data.repositories.AuthRepository
import com.himawan.gymstis.service.UserService

private const val LOGGED_IN_USER_PREFERENCE_NAME = "logged_in_user_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LOGGED_IN_USER_PREFERENCE_NAME
)

class GymStisApplication : Application() {
    lateinit var container: AppContainer
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        authRepository = AuthRepository(dataStore)
    }
}