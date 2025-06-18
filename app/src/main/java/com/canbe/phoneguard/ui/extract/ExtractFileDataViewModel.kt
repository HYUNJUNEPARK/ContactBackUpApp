package com.canbe.phoneguard.ui.extract

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.file.ExtractFileDataUseCase
import com.canbe.phoneguard.ui.base.BaseViewModel
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExtractFileDataViewModel @Inject constructor(
    private val extractFileDataUseCase: ExtractFileDataUseCase
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
}
