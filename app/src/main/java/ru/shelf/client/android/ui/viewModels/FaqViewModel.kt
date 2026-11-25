package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FaqUiState(
    val expandedIndex: Int? = null,
    val searchQuery: String = ""
)

class FaqViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FaqUiState())
    val uiState: StateFlow<FaqUiState> = _uiState.asStateFlow()

    fun toggleExpand(index: Int) {
        _uiState.update { currentState ->
            val nextExpanded = if (currentState.expandedIndex == index) null else index
            currentState.copy(expandedIndex = nextExpanded)
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
