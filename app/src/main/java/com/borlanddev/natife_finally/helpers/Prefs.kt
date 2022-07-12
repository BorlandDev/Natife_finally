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
    val preferences = (context.getSharedPreferences(
        APP_PREFERENCES,
        Context.MODE_PRIVATE
    )) as SharedPreferences
}
