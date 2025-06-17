package com.canbe.phoneguard.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.contact.GetContactListUseCase
import com.canbe.phoneguard.domain.file.ExportFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase,
    private val exportFileUseCase: ExportFileUseCase
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) {
        isLoading = true
        _contactList.value = getContactListUserCase.invoke()
        Timber.d("getContacts(): ${contactList.value}")
        isLoading = false
    }

    fun exportToFile(callback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        if (contactList.value.isEmpty()) {
            Timber.e("exportToFile() contactList is empty")
            return@launch
        }
        Timber.d("exportToFile(): ${contactList.value}")
        exportFileUseCase.invoke(contactList.value)

        withContext(Dispatchers.Main) {
            callback()
        }
    }
}
