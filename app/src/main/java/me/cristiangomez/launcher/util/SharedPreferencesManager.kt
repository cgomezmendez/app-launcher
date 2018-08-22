package me.cristiangomez.launcher.util

import android.content.Context

class SharedPreferencesManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE)

    var didPopulateDatabase: Boolean
        get() {
            return sharedPreferences.getBoolean(PREF_DID_POPULATE_DATABASE, false)
        }
        set(value) {
            sharedPreferences.edit().putBoolean(PREF_DID_POPULATE_DATABASE, value).apply()
        }

    companion object {
        const val SHARED_PREFERENCES_NAME = "me.cristiangomez.launcher"
        const val PREF_DID_POPULATE_DATABASE = "DID_POPULATE_DATABASE"
    }
}