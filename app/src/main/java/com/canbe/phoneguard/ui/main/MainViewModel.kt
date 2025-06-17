package com.canbe.phoneguard.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.contact.GetContactListUseCase
import com.canbe.phoneguard.domain.file.ExportFileUseCase
import com.canbe.phoneguard.ui.main.model.ContactUiModel
import com.canbe.phoneguard.ui.main.model.DialogEvent
import com.canbe.phoneguard.ui.main.model.UiEvent
import com.canbe.phoneguard.ui.main.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase,
    private val exportFileUseCase: ExportFileUseCase
) : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("getContacts(): ${contactList.value}")
        _uiState.value = UiState.Loading

        _contactList.value = getContactListUserCase.invoke()

        _uiState.value = UiState.Success
    }

    fun exportToFile() = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("exportToFile(): ${contactList.value}")
        if (contactList.value.isEmpty()) {
            Timber.e("exportToFile() contactList is empty")
            return@launch
        }
        _uiState.value = UiState.Loading

        exportFileUseCase.invoke(contactList.value)

        _uiState.value = UiState.Success
        _uiEvent.emit(UiEvent.ShowSuccessDialog(DialogEvent.EXPORT))
    }
}
