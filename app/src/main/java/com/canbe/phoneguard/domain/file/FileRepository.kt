package com.canbe.phoneguard.domain.file

import android.net.Uri
import com.canbe.phoneguard.data.model.ContactDto

interface FileRepository {
    suspend fun exportToFile(content: String)
    suspend fun extractDataFromFile(uri: Uri): List<ContactDto>
}