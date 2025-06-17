package com.canbe.phoneguard.domain.file

import android.net.Uri
import javax.inject.Inject

class ExtractFileDataUseCase @Inject constructor(
    private val repository: FileRepository
) {
    //TODO 여기서 데이터 변환 할 것 !
    suspend fun invoke(uri: Uri): String {
        val fileContentString = repository.extractContentFromFile(uri)
        return fileContentString
    }
}