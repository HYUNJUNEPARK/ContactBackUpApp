package com.canbe.contactbackup.ui.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canbe.contactbackup.BuildConfig
import com.canbe.contactbackup.exception.UnknownException
import com.canbe.contactbackup.ui.base.BaseActivity.Companion.LOG_TAG_LIFE_CYCLE
import com.canbe.contactbackup.ui.model.DialogEventType
import com.canbe.contactbackup.ui.model.UiEvent
import com.canbe.contactbackup.ui.model.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    init {
        Timber.tag(LOG_TAG_LIFE_CYCLE).i("${javaClass.simpleName} init()")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(LOG_TAG_LIFE_CYCLE).i("${javaClass.simpleName} onCleared()")
    }

    private val _uiState = mutableStateOf<UiState?>(null)
    val uiState: State<UiState?> = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        Timber.e("ExceptionHandler throwable: $e")

        if (BuildConfig.DEBUG) e.printStackTrace()

        val exception = e as? Exception ?: UnknownException("Unknown Throwable: $e")

        updateUiEvent(UiEvent.ShowDialog(DialogEventType.ERROR, exception))
    }

    /**
     * Dispatchers + CoroutineExceptionHandler
     */
    protected fun launchInViewModelScope(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            block()
        }
    }

    fun updateUiState(uiState: UiState) {
        Timber.d("updateUiState() $uiState")
        _uiState.value = uiState
    }

    fun updateUiEvent(uiEvent: UiEvent) = viewModelScope.launch {
        Timber.d("updateUiEvent() $uiEvent")
        _uiEvent.emit(uiEvent)
    }
}