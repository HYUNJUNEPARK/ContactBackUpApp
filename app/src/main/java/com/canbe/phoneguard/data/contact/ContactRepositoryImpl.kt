package com.canbe.phoneguard.data.contact

import com.canbe.phoneguard.data.model.toEntity
import com.canbe.phoneguard.domain.contact.ContactEntity
import com.canbe.phoneguard.domain.contact.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource
) : ContactRepository {
    override suspend fun getContactList(): List<ContactEntity> {
        return contactDataSource.getContactList().map {
            it.toEntity()
        }
    }
}