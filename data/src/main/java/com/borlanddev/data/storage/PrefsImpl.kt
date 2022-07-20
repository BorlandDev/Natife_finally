package com.borlanddev.data.storage

import android.content.SharedPreferences
import com.borlanddev.data.consts.DEFAULT_NAME_PREFS
import com.borlanddev.data.consts.USERNAME_KEY_PREFS
import com.borlanddev.domain.storage.Prefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsImpl @Inject constructor(
    private val preferences: SharedPreferences
) : Prefs {

    override fun putUsername(username: String) {
        preferences.edit().putString(USERNAME_KEY_PREFS, username).apply()
    }

    override fun getUsername(): String = preferences.getString(
        USERNAME_KEY_PREFS, DEFAULT_NAME_PREFS
    ) ?: DEFAULT_NAME_PREFS
}
