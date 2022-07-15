package com.borlanddev.natife_finally.helpers

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences = (context.getSharedPreferences(
        APP_PREFERENCES,
        Context.MODE_PRIVATE
    )) as SharedPreferences

    fun putUsername(username: String) {
        preferences.edit().putString(APP_PREFERENCES, username).apply()
    }

    fun getUsername(): String = preferences.getString(
        APP_PREFERENCES, ""
    ).toString()

    fun deleteUsername() {
        preferences.edit().putString(APP_PREFERENCES, "").apply()
    }
}
