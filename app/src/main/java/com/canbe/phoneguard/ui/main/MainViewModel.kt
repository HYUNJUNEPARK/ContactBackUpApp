package com.canbe.phoneguard.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.contact.GetContactListUseCase
import com.canbe.phoneguard.domain.file.ExportFileUseCase
import com.canbe.phoneguard.ui.base.BaseViewModel
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.DialogEvent
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase,
    private val exportFileUseCase: ExportFileUseCase,
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("getContacts(): ${contactList.value}")
        updateUiState(UiState.Loading)

        _contactList.value = getContactListUserCase.invoke()

        updateUiState(UiState.Success)
    }

    fun exportToFile() = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("exportToFile(): ${contactList.value}")
        if (contactList.value.isEmpty()) {
            Timber.e("exportToFile() contactList is empty")
            return@launch
        }
        updateUiState(UiState.Loading)

        exportFileUseCase(contactList.value)

        updateUiState(UiState.Success)
        updateUiEvent(UiEvent.ShowSuccessDialog(DialogEvent.EXPORT))
    }
}
