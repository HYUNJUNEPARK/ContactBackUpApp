package com.canbe.phoneguard.domain.contact

import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun invoke(): List<ContactEntity> = repository.getContactList()
}