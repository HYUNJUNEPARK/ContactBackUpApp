package com.canbe.phoneguard.domain.contact.repository

import com.canbe.phoneguard.domain.contact.model.Contact

interface ContactRepository {
    suspend fun getContactList() : List<Contact>
    suspend fun saveContactListToFile(contactList: List<Contact>)
    suspend fun restoreContactListFromFile()
}