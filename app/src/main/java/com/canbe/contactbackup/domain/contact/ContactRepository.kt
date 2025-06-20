package com.canbe.contactbackup.domain.contact

import com.canbe.contactbackup.domain.model.ContactEntity

interface ContactRepository {
    suspend fun getContactList() : List<ContactEntity>
}