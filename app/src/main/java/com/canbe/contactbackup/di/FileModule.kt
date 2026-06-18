package com.canbe.contactbackup.di

import com.canbe.contactbackup.data.file.FileRepositoryImpl
import com.canbe.contactbackup.domain.file.FileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileModule {
    @Binds
    abstract fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}
