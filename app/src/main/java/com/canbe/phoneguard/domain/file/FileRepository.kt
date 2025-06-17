package com.canbe.phoneguard.domain.file

import android.net.Uri

interface FileRepository {
    suspend fun exportToFile(content: String)
    suspend fun extractContentFromFile(uri: Uri)
}