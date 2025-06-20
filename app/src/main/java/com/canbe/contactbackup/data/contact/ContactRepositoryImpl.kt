package com.canbe.contactbackup.data.contact

import com.canbe.contactbackup.data.model.toEntity
import com.canbe.contactbackup.domain.model.ContactEntity
import com.canbe.contactbackup.domain.contact.ContactRepository
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