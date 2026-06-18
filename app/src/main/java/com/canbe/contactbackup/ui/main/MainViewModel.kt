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
import com.canbe.contactbackup.ui.model.toEntity
import com.canbe.contactbackup.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContactListUseCase: GetContactListUseCase,
    private val exportFileUseCase: ExportFileUseCase,
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun getContacts() = launchInViewModelScope {
        Timber.d("getContacts(): ${contactList.value}")
        updateUiState(UiState.ProgressLoading)

        _contactList.value = getContactListUseCase().map { it.toUiModel() }

        updateUiState(UiState.FinishLoading)
    }

    fun exportToFile(fileName: String) = launchInViewModelScope {
        Timber.d("exportToFile(): ${contactList.value}")
        if (contactList.value.isEmpty()) {
            Timber.e("exportToFile() contactList is empty")
            throw NoContactsInDevice()
        }
        updateUiState(UiState.ProgressLoading)

        val entities = contactList.value.map { it.toEntity() }
        exportFileUseCase(fileName, entities)

        updateUiState(UiState.FinishLoading)
        updateUiEvent(UiEvent.ShowToast(R.string.success_save_contact_file))
    }
}
