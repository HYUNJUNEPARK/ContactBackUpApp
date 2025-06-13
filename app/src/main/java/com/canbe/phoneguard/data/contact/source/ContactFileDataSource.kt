package com.canbe.phoneguard.data.contact.source

import android.content.Context
import com.canbe.phoneguard.domain.contact.model.Contact
import javax.inject.Inject

class ContactFileDataSource @Inject constructor(
    private val context: Context
) {
    fun saveToFile(contactList: List<Contact>) {


    }

    fun restoreFromFile() {

    }
}