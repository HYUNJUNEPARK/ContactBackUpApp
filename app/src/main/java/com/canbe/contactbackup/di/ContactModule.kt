package com.canbe.contactbackup.di

import android.content.ContentResolver
import android.content.Context
import com.canbe.contactbackup.data.contact.ContactDataSource
import com.canbe.contactbackup.data.contact.ContactRepositoryImpl
import com.canbe.contactbackup.domain.contact.ContactRepository
import com.canbe.contactbackup.domain.contact.GetContactListUseCase
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
    fun provideContactDataSource(contentResolver: ContentResolver): ContactDataSource = ContactDataSource(contentResolver)

    @Provides
    fun provideContactRepository(dataSource: ContactDataSource): ContactRepository = ContactRepositoryImpl(dataSource)

    @Provides
    fun provideGetContactUseCase(repository: ContactRepository): GetContactListUseCase = GetContactListUseCase(repository)
}