package com.canbe.phoneguard.ui.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel: ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    fun updateUiState(uiState: UiState) {
        Timber.d("updateUiState() $uiState")
        _uiState.value = uiState
    }

    fun updateUiEvent(uiEvent: UiEvent) = viewModelScope.launch {
        Timber.d("updateUiEvent() $uiEvent")
        _uiEvent.emit(uiEvent)
    }
}