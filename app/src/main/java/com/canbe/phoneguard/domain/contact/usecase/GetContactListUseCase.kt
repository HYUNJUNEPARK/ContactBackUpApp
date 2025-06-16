package com.canbe.phoneguard.domain.contact.usecase

import com.canbe.phoneguard.domain.contact.repository.ContactRepository
import com.canbe.phoneguard.domain.contact.model.ContactEntity
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun invoke(): List<ContactEntity> = repository.getContactList()
}