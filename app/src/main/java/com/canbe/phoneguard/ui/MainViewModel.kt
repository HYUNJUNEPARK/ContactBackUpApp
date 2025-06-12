package com.canbe.phoneguard.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.canbe.phoneguard.domain.contact.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _contacts = mutableStateOf<List<String>>(emptyList())
    val contacts: State<List<String>> = _contacts

    fun loadContacts(context: Context) {
        _contacts.value = contactRepository.getContacts(context)
    }
}
