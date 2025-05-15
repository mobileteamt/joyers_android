package com.synapse.joyers.localstore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferencesManager(private val context: Context) {

    private val KEY_USERNAME = stringPreferencesKey("key_username")

    val usernameFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USERNAME]
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                set(KEY_USERNAME, username)
            }
        }
    }

    suspend fun getUsername(): String? {
        return context.dataStore.data.first()[KEY_USERNAME]
    }

    suspend fun clear() {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                //remove(KEY_USERNAME)
                clear()
            }


        }
    }
}

