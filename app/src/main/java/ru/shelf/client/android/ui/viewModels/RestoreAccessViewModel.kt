package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RestoreAccessUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSentSuccess: Boolean = false
)

class RestoreAccessViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RestoreAccessUiState())
    val uiState: StateFlow<RestoreAccessUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun sendRestoreEmail(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите email") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        _uiState.update { it.copy(isLoading = false, isSentSuccess = true) }
        onSuccess()
    }
}
