package com.canbe.contactbackup.ui.extract

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.canbe.contactbackup.R
import com.canbe.contactbackup.domain.file.ExtractFileDataUseCase
import com.canbe.contactbackup.domain.file.SaveContactsToDeviceUseCase
import com.canbe.contactbackup.ui.base.BaseViewModel
import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.DialogEventType
import com.canbe.contactbackup.ui.model.UiEvent
import com.canbe.contactbackup.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExtractFileDataViewModel @Inject constructor(
    private val extractFileDataUseCase: ExtractFileDataUseCase,
    private val saveContactsToDeviceUseCase: SaveContactsToDeviceUseCase
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    /**
     * JSON 파일에서 컨텐츠를 추출해, 연락처 리스트를 뷰에 업데이트한다.
     */
    fun extractFromFile(uri: Uri) = launchInViewModelScope {
        Timber.d("extractFromFile(): $uri")
        updateUiState(UiState.Loading)

        val fileContent = extractFileDataUseCase(uri)
        Timber.d("extractFromFile() fileContent: $fileContent")

        _contactList.value = fileContent

        updateUiState(UiState.Success)
        updateUiEvent(UiEvent.ShowDialog(DialogEventType.SUCCESS_GET_CONTACTS))
    }

    /**
     * 연락처 리스트를 디바이스에 저장한다.
     */
    fun saveContactsToDevice() = launchInViewModelScope {
        Timber.d("saveContactsToDevice()")
        updateUiState(UiState.Loading)

        saveContactsToDeviceUseCase(contactList.value)

        updateUiState(UiState.Success)
        updateUiEvent(UiEvent.ShowToast(R.string.success_restore_contact))
    }
}
