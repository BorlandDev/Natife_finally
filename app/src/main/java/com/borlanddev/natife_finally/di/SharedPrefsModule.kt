package com.borlanddev.natife_finally.di

import android.content.Context
import android.content.SharedPreferences
import com.borlanddev.data.consts.USERNAME_KEY_PREFS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPrefsModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            USERNAME_KEY_PREFS,
            Context.MODE_PRIVATE
        )
}

