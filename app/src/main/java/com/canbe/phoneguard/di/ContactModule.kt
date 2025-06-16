package com.canbe.phoneguard.di

import android.content.ContentResolver
import android.content.Context
import com.canbe.phoneguard.data.contact.source.ContactReadDataSource
import com.canbe.phoneguard.data.contact.repository.ContactRepositoryImpl
import com.canbe.phoneguard.domain.contact.repository.ContactRepository
import com.canbe.phoneguard.domain.contact.usecase.GetContactListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ContactModule {
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver

    @Provides
    fun provideContactDataSource(contextResolver: ContentResolver): ContactReadDataSource = ContactReadDataSource(contextResolver)


    @Provides
    fun provideContactRepository(contactReadDataSource: ContactReadDataSource): ContactRepository = ContactRepositoryImpl(contactReadDataSource)

    @Provides
    fun provideGetContactUseCase(contactPRepository: ContactRepository): GetContactListUseCase = GetContactListUseCase(contactPRepository)
}