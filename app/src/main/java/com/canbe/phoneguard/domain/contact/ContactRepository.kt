package com.canbe.phoneguard.domain.contact

interface ContactRepository {
    suspend fun getContactList() : List<ContactEntity>
}