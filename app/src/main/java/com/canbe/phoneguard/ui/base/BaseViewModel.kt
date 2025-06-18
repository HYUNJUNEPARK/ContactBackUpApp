package com.canbe.phoneguard.ui.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

open class BaseViewModel: ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    fun updateUiState(uiState: UiState) {
        _uiState.value = uiState
    }

    suspend fun updateUiEvent(uiEvent: UiEvent) {
        _uiEvent.emit(uiEvent)
    }
}