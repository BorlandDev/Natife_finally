package com.borlanddev.natife_finally.di

import com.borlanddev.data.socket.ClientImpl
import com.borlanddev.data.storage.PrefsImpl
import com.borlanddev.domain.socket.Client
import com.borlanddev.domain.storage.Prefs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBindingModule {

    @Singleton
    @Binds
    fun bindPrefs(prefsImpl: PrefsImpl): Prefs

    @Singleton
    @Binds
    fun bindClient(clientImpl: ClientImpl): Client
}
