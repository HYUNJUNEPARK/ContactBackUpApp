package com.canbe.contactbackup.data.file

import android.net.Uri
import com.canbe.contactbackup.data.model.toEntity
import com.canbe.contactbackup.domain.file.FileRepository
import com.canbe.contactbackup.domain.model.ContactEntity
import com.canbe.contactbackup.domain.model.toDto
import com.google.gson.Gson
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileDataSource: FileDataSource
) : FileRepository {
    override suspend fun exportToFile(fileName: String, contactList: List<ContactEntity>) {
        val contacts = contactList.map { it.toDto() }
        val fileContent = Gson().toJson(contacts)
        fileDataSource.exportToFile(fileName = fileName, fileContent = fileContent)
    }

    override suspend fun extractDataFromFile(uri: Uri): List<ContactEntity> {
        return fileDataSource.extractDataFromFile(uri).map { it.toEntity() }
    }

    override suspend fun saveContactsToDevice(contactList: List<ContactEntity>) {
        val contacts = contactList.map { it.toDto() }
        fileDataSource.saveContactsToDevice(contacts)
    }
}