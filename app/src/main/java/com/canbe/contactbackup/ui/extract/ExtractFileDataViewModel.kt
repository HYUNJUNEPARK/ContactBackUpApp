package com.canbe.contactbackup.ui.extract

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.canbe.contactbackup.domain.file.ExtractFileDataUseCase
import com.canbe.contactbackup.domain.file.SaveContactsToDeviceUseCase
import com.canbe.contactbackup.ui.base.BaseViewModel
import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExtractFileDataViewModel @Inject constructor(
    private val extractFileDataUseCase: ExtractFileDataUseCase,
    private val saveContactsToDeviceUseCase: SaveContactsToDeviceUseCase
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun extractFromFile(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("extractFromFile(): $uri")
        updateUiState(UiState.Loading)

        val fileContent = extractFileDataUseCase(uri)
        Timber.d("extractFromFile() fileContent: $fileContent")

        _contactList.value = fileContent

        updateUiState(UiState.Success)
    }

    fun saveContactsToDevice() = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("saveContactsToDevice()")
        saveContactsToDeviceUseCase(contactList.value)
    }
}
