package com.canbe.contactbackup.domain.file

import android.net.Uri
import com.canbe.contactbackup.domain.model.ContactEntity

interface FileRepository {
    suspend fun exportToFile(fileName:String, content: String)
    suspend fun extractDataFromFile(uri: Uri): List<ContactEntity>
}