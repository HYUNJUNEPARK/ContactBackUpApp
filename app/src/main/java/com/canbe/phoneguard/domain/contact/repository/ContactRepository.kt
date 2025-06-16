package com.canbe.phoneguard.domain.contact.repository

import com.canbe.phoneguard.domain.contact.model.ContactEntity

interface ContactRepository {
    suspend fun getContactList() : List<ContactEntity>
}