package com.canbe.contactbackup.domain.file

import com.canbe.contactbackup.domain.model.ContactEntity
import javax.inject.Inject

class SaveContactsToDeviceUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(contactList: List<ContactEntity>) {
        repository.saveContactsToDevice(contactList)
    }
}
