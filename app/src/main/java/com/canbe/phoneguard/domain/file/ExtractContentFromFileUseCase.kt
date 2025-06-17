package com.canbe.phoneguard.domain.file

import android.net.Uri
import javax.inject.Inject

class ExtractContentFromFileUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend fun invoke(uri: Uri) {
        repository.extractContentFromFile(uri)
    }
}