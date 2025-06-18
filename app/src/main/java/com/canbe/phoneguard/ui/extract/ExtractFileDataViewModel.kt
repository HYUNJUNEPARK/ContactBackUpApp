package com.canbe.phoneguard.ui.extract

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.domain.file.ExtractFileDataUseCase
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExtractFileDataViewModel @Inject constructor(
    private val extractFileDataUseCase: ExtractFileDataUseCase
) : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun extractFromFile(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("extractFromFile(): $uri")
        _uiState.value = UiState.Loading

        val fileContent = extractFileDataUseCase(uri)
        Timber.d("extractFromFile() fileContent: $fileContent")

        _contactList.value = fileContent

        _uiState.value = UiState.Success
    }
}
