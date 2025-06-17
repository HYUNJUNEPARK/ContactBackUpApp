package com.canbe.phoneguard.data.file

import android.net.Uri
import com.canbe.phoneguard.domain.file.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileDataSource: FileDataSource
) : FileRepository {
    override suspend fun exportToFile(content: String) {
        fileDataSource.exportToFile(fileContent = content)
    }

    override suspend fun extractContentFromFile(uri: Uri): String {
        return fileDataSource.extractDataFromFile(uri)
    }
}