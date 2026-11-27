package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.shelf.client.android.data.CatalogObject
import ru.shelf.client.android.data.mockCatalogObjects

data class CatalogUiState(
    val categories: List<String> = listOf("Все разделы", "Книги", "Манга", "Мультфильмы", "Фильмы", "Аниме", "Видеоигры", "Фигурки", "Сериалы"),
    val selectedCategory: String = "Все разделы",
    val searchQuery: String = "",
    val objects: List<CatalogObject> = emptyList(),
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val isObjectNotFound: Boolean = false,
    val errorMessage: String? = null
)

class CatalogViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadObjects()
    }

    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category, isObjectNotFound = false) }
        loadObjects()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, isObjectNotFound = false) }
        loadObjects()
    }

    fun loadNextPage() {
        if (_uiState.value.isLoadingMore || _uiState.value.isLastPage) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            // Эмуляция пагинации без кнопки "показать еще"
            _uiState.update { it.copy(isLoadingMore = false) }
        }
    }

    fun onObjectClick(objectId: String, onNavigateToObject: (String) -> Unit) {
        val found = _uiState.value.objects.any { it.id == objectId }
        if (found) {
            onNavigateToObject(objectId)
        } else {
            _uiState.update { it.copy(isObjectNotFound = true) }
        }
    }

    fun resetObjectNotFound() {
        _uiState.update { it.copy(isObjectNotFound = false) }
    }

    private fun loadObjects() {
        val all = mockCatalogObjects
        val category = _uiState.value.selectedCategory
        val query = _uiState.value.searchQuery

        val filtered = all.filter { item ->
            (category == "Все разделы" || item.category.name.equals(category, ignoreCase = true)) &&
            (query.isBlank() || item.title.contains(query, ignoreCase = true))
        }

        _uiState.update { it.copy(objects = filtered, isObjectNotFound = false) }
    }
}
