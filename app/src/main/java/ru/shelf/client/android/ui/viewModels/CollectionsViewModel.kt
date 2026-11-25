package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.Collection
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.screens.collections.CollectionsErrorType

data class CollectionsUiState(
    val user: User = mockUserMe,
    val currentUserId: String = CURRENT_USER_ID,
    val errorState: CollectionsErrorType? = null,
    val bannerNotification: String? = null,
    val isLoading: Boolean = false
)

class CollectionsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    fun deleteCollection(collection: Collection) {
        _uiState.update { currentState ->
            val updatedCollections = currentState.user.collectionsPreview.filter { it.id != collection.id }
            currentState.copy(
                user = currentState.user.copy(
                    collectionsPreview = updatedCollections,
                    collectionsCount = updatedCollections.size
                ),
                bannerNotification = "Коллекция «${collection.name}» удалена"
            )
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(bannerNotification = null) }
    }

    fun retryError() {
        _uiState.update { it.copy(errorState = null) }
    }
}
