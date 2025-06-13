package com.canbe.phoneguard.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.contact.Contact
import com.canbe.phoneguard.domain.contact.GetContactListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase
) : ViewModel() {
    private val _contactList = mutableStateOf<List<Contact>>(emptyList())
    val contactList: State<List<Contact>> = _contactList

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) {
        _contactList.value = getContactListUserCase.invoke()
        Timber.d("contactList: ${contactList.value}")
    }
}
