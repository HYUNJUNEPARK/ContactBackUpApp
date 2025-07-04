package com.canbe.contactbackup.ui.model

sealed class UiState {
    data object ProgressLoading : UiState()
    data object FinishLoading : UiState()
}

sealed class UiEvent {
    data class ShowToast(val messageResId: Int) : UiEvent()
    data class ShowDialog(val dialogEventType: DialogEventType, val e: Exception? = null) : UiEvent()
}

enum class DialogEventType {
    REQUEST_EXPORT,
    SUCCESS_GET_CONTACTS,
    ERROR
}
