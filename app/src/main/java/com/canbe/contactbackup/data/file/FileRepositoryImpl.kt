package com.canbe.contactbackup.data.file

import android.net.Uri
import com.canbe.contactbackup.data.model.toEntity
import com.canbe.contactbackup.domain.model.ContactEntity
import com.canbe.contactbackup.domain.file.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileDataSource: FileDataSource
) : FileRepository {
    override suspend fun exportToFile(fileName: String, content: String) {
        fileDataSource.exportToFile(fileName = fileName, fileContent = content)
    }

    override suspend fun extractDataFromFile(uri: Uri): List<ContactEntity> {
        return fileDataSource.extractDataFromFile(uri).map { it.toEntity() }
    }
}