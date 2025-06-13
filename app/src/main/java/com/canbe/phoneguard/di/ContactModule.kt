package com.canbe.phoneguard.di

import android.content.ContentResolver
import android.content.Context
import com.canbe.phoneguard.data.contact.ContactDataSource
import com.canbe.phoneguard.data.contact.ContactRepositoryImpl
import com.canbe.phoneguard.domain.contact.ContactRepository
import com.canbe.phoneguard.domain.contact.GetContactListUseCase
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
    fun provideContactDataSource(contextResolver: ContentResolver): ContactDataSource = ContactDataSource(contextResolver)

    @Provides
    fun provideContactRepository(contactDataSource: ContactDataSource): ContactRepository = ContactRepositoryImpl(contactDataSource)

    @Provides
    fun provideGetContactUseCase(contactPRepository: ContactRepository): GetContactListUseCase = GetContactListUseCase(contactPRepository)
}