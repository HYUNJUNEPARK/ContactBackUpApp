package com.canbe.contactbackup.domain.contact

import com.canbe.contactbackup.domain.model.ContactEntity
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(): List<ContactEntity> {
        return repository.getContactList()
    }
}
