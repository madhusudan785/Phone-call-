package com.example.phonecall.callList.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class AppPreferences(private val context: Context) {
    private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")

    suspend fun isFirstTime(): Boolean {
        return context.dataStore.data.map { it[FIRST_TIME_KEY] ?: true }.first()
    }

    suspend fun setFirstTime(value: Boolean) {
        context.dataStore.edit { it[FIRST_TIME_KEY] = value }
    }
}
