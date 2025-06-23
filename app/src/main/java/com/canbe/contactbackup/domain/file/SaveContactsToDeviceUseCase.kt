package com.canbe.contactbackup.domain.file

import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.toEntity
import javax.inject.Inject

class SaveContactsToDeviceUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(contactList: List<ContactUiModel>) {
        val contacts = contactList.map { it.toEntity() }
        repository.saveContactsToDevice(contacts)
    }
}