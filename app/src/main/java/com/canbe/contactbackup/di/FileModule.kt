package com.canbe.contactbackup.di

import android.content.ContentResolver
import com.canbe.contactbackup.data.file.FileDataSource
import com.canbe.contactbackup.data.file.FileRepositoryImpl
import com.canbe.contactbackup.domain.file.ExportFileUseCase
import com.canbe.contactbackup.domain.file.ExtractFileDataUseCase
import com.canbe.contactbackup.domain.file.FileRepository
import com.canbe.contactbackup.domain.file.SaveContactsToDeviceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    fun provideFileDataSource(contentResolver: ContentResolver): FileDataSource = FileDataSource(contentResolver)

    @Provides
    fun provideFileRepository(dataSource: FileDataSource): FileRepository = FileRepositoryImpl(dataSource)

    @Provides
    fun provideExportFileUseCase(repository: FileRepository): ExportFileUseCase = ExportFileUseCase(repository)

    @Provides
    fun provideExtractDataFromFileUseCase(repository: FileRepository): ExtractFileDataUseCase = ExtractFileDataUseCase(repository)

    @Provides
    fun provideSaveContactsToDevice(repository: FileRepository): SaveContactsToDeviceUseCase = SaveContactsToDeviceUseCase(repository)
}