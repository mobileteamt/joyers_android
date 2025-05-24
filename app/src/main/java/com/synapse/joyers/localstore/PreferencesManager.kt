package com.synapse.joyers.localstore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferencesManager(private val context: Context) {

    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val USER_ID = stringPreferencesKey("user_id")


    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                set(ACCESS_TOKEN, accessToken)
            }
        }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN]
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.first()[USER_ID]
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                set(USER_ID, userId)
            }
        }
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

