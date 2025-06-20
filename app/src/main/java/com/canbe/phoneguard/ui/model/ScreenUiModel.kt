package com.canbe.phoneguard.ui.model

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val exception: Exception) : UiState()
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowDialog(val eventType: EventType) : UiEvent()
}

enum class EventType {
    EXPORT
}
