package com.canbe.phoneguard.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.contact.model.Contact
import com.canbe.phoneguard.domain.contact.usecase.GetContactListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    private val _contactList = mutableStateOf<List<Contact>>(emptyList())
    val contactList: State<List<Contact>> = _contactList

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) {
        isLoading = true
        _contactList.value = getContactListUserCase.invoke()
        Timber.d("contactList: ${contactList.value}")
        isLoading = false
    }
}
