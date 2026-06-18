package com.canbe.contactbackup.domain.file

import com.canbe.contactbackup.domain.model.ContactEntity
import javax.inject.Inject

class ExportFileUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(
        fileName: String,
        contactList: List<ContactEntity>
    ) {
        repository.exportToFile(fileName, contactList)
    }
}
