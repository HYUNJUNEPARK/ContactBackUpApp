package com.canbe.contactbackup.domain.file

import android.net.Uri
import com.canbe.contactbackup.domain.model.ContactEntity
import javax.inject.Inject

class ExtractFileDataUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): List<ContactEntity> {
        return repository.extractDataFromFile(uri)
    }
}
