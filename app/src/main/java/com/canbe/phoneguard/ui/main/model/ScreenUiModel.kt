package com.canbe.phoneguard.ui.main.model

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val exception: Exception) : UiState()
}

sealed class UiEvent {
    //data object ShowToast : UiEvent()
    data class ShowSuccessDialog(val event: DialogEvent) : UiEvent()
}

enum class DialogEvent {
    EXPORT
}
