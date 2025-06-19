package com.canbe.phoneguard.domain.contact

import com.canbe.phoneguard.domain.model.ContactEntity

interface ContactRepository {
    suspend fun getContactList() : List<ContactEntity>
}