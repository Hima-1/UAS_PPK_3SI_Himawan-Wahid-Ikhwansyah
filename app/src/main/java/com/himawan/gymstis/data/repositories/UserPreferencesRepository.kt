package com.himawan.gymstis.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("user_token")
        val NAME = stringPreferencesKey("user_name")
        val EMAIL = stringPreferencesKey("user_email")
        val GENDER = stringPreferencesKey("user_gender")
        val IS_STAFF = booleanPreferencesKey("user_is_staff")
        const val TAG = "UserPreferencesRepository"
    }

    val user: Flow<UserState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences:", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserState(
                preferences[TOKEN] ?: "",
                preferences[NAME] ?: "",
                preferences[EMAIL] ?: "",
                preferences[GENDER] ?: "",
                preferences[IS_STAFF] ?: false
            )
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }
    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun saveIsStaff(isStaff: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_STAFF] = isStaff
        }
    }

    suspend fun saveGender(gender: String) {
        dataStore.edit { preferences ->
            preferences[GENDER] = gender
        }
    }
}

data class UserState(
    val token: String,
    val name: String,
    val email: String,
    val gender: String,
    val isStaff: Boolean
)
