package com.borlanddev.natife_finally.helpers

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun putUsername(username: String) {
        preferences.edit().putString(USERNAME_KEY_PREFS, username).apply()
    }

    fun getUsername(): String = preferences.getString(
        USERNAME_KEY_PREFS, DEFAULT_NAME_PREFS
    ) ?: DEFAULT_NAME_PREFS
}
