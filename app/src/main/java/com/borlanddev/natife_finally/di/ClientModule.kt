package com.borlanddev.natife_finally.di

import com.borlanddev.data.socket.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ClientModule {

    @Singleton
    @Provides
    fun provideClient(): Client = Client()
}