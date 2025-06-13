package com.canbe.phoneguard.domain.contact

interface ContactRepository {
    fun getContactList() : List<Contact>
}