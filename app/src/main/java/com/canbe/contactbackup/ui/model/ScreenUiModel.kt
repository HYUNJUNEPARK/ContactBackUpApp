package com.canbe.contactbackup.ui.model

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    //data class Error(val exception: Exception) : UiState()
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowDialog(val eventType: EventType, val e: Exception? = null) : UiEvent()
}

enum class EventType {
    EXPORT, ERROR
}
