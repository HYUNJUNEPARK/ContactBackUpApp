package com.canbe.phoneguard.data.contact

import com.canbe.phoneguard.domain.contact.Contact
import com.canbe.phoneguard.domain.contact.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource
) : ContactRepository {
    override fun getContactList(): List<Contact> {
        return contactDataSource.getContactList().map {
            Contact(
                id = it.contactId,
                name = it.displayNamePrimary ?: "UNKNOWN",
                number = it.phoneNumbers,
                emails = it.emailAddresses,
                organization = it.organizationCompany,
                note = it.notes
            )
        }
    }
}