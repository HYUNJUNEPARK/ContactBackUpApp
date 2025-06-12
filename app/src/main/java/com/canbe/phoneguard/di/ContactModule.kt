package com.canbe.phoneguard.di

import com.canbe.phoneguard.data.contact.ContactRepositoryImpl
import com.canbe.phoneguard.domain.contact.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ContactModule {
    @Provides
    fun provideContactRepository(): ContactRepository = ContactRepositoryImpl()
}