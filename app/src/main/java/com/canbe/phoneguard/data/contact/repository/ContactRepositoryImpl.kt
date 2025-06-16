package com.canbe.phoneguard.data.contact.repository

import com.canbe.phoneguard.data.contact.source.ContactReadDataSource
import com.canbe.phoneguard.domain.contact.model.Contact
import com.canbe.phoneguard.domain.contact.repository.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactReadDataSource: ContactReadDataSource
) : ContactRepository {
    override suspend fun getContactList(): List<Contact> {
        return contactReadDataSource.getContactList().map {
            Contact(
                id = it.contactId,
                name = it.displayNamePrimary ?: "UNKNOWN",
                number = it.phoneNumbers,
                emails = it.emailAddresses,
                organization = it.organizationCompany,
                note = it.notes,
                profileUri = it.contactPhotoUri
            )
        }
    }
}