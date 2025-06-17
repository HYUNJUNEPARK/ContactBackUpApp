package com.canbe.phoneguard.di

import android.content.ContentResolver
import com.canbe.phoneguard.data.file.FileDataSource
import com.canbe.phoneguard.data.file.FileRepositoryImpl
import com.canbe.phoneguard.domain.file.ExportFileUseCase
import com.canbe.phoneguard.domain.file.ExtractFileDataUseCase
import com.canbe.phoneguard.domain.file.FileRepository
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
}