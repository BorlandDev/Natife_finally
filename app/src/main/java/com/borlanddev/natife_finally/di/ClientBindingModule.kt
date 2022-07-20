package com.borlanddev.natife_finally.di

import com.borlanddev.data.socket.ClientImpl
import com.borlanddev.domain.socket.Client
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ClientBindingModule {

    @Singleton
    @Binds
    fun bindClient(clientImpl: ClientImpl) : Client
}