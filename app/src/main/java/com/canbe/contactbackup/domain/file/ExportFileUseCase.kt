package com.canbe.contactbackup.domain.file

import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.toEntity
import javax.inject.Inject

class ExportFileUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(
        fileName: String,
        contactList: List<ContactUiModel>
    ) {
        val contacts = contactList.map { it.toEntity() }
        repository.exportToFile(fileName, contacts)
    }
}