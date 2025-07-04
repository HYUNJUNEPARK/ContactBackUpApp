package com.canbe.contactbackup.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.canbe.contactbackup.R
import com.canbe.contactbackup.domain.contact.GetContactListUseCase
import com.canbe.contactbackup.domain.file.ExportFileUseCase
import com.canbe.contactbackup.exception.NoContactsInDevice
import com.canbe.contactbackup.ui.base.BaseViewModel
import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.UiEvent
import com.canbe.contactbackup.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUserCase: GetContactListUseCase,
    private val exportFileUseCase: ExportFileUseCase,
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun getContacts() = launchInViewModelScope {
        Timber.d("getContacts(): ${contactList.value}")
        updateUiState(UiState.ProgressLoading)

        _contactList.value = getContactListUserCase.invoke()

        updateUiState(UiState.FinishLoading)
    }

    fun exportToFile(fileName: String) = launchInViewModelScope {
        Timber.d("exportToFile(): ${contactList.value}")
        if (contactList.value.isEmpty()) {
            Timber.e("exportToFile() contactList is empty")
            //updateUiEvent(UiEvent.ShowDialog(EventType.ERROR, NoContactsInDevice()))
            throw NoContactsInDevice()
            //return@launchInViewModelScope
        }
        updateUiState(UiState.ProgressLoading)

        exportFileUseCase(fileName, contactList.value)

        updateUiState(UiState.FinishLoading)
        updateUiEvent(UiEvent.ShowToast(R.string.success_save_contact_file))
    }
}
