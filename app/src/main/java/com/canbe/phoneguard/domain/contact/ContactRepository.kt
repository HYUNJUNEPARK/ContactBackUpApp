package com.canbe.phoneguard.domain.contact

import android.content.Context

interface ContactRepository {
    fun getContacts(context: Context) : List<String>
}