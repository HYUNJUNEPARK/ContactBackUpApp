package com.canbe.phoneguard.data.contact.repository

import com.canbe.phoneguard.data.contact.model.toEntity
import com.canbe.phoneguard.data.contact.source.ContactReadDataSource
import com.canbe.phoneguard.domain.contact.model.ContactEntity
import com.canbe.phoneguard.domain.contact.repository.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactReadDataSource: ContactReadDataSource
) : ContactRepository {
    override suspend fun getContactList(): List<ContactEntity> {
        return contactReadDataSource.getContactList().map {
            it.toEntity()
        }
    }
}