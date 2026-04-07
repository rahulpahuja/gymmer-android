package com.m1x.gymmer.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gymmer_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val userToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN]
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
            preferences[IS_LOGGED_IN] = false
        }
    }
}
