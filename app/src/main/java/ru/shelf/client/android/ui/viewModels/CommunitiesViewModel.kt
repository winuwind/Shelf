package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.Community
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.screens.communities.CommunitiesErrorType

data class CommunitiesUiState(
    val user: User = mockUserMe,
    val currentUserId: String = CURRENT_USER_ID,
    val errorState: CommunitiesErrorType? = null,
    val bannerNotification: String? = null,
    val isLoading: Boolean = false
)

class CommunitiesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CommunitiesUiState())
    val uiState: StateFlow<CommunitiesUiState> = _uiState.asStateFlow()

    fun leaveCommunity(community: Community) {
        _uiState.update { currentState ->
            val updatedCommunities = currentState.user.communitiesPreview.filter { it.id != community.id }
            currentState.copy(
                user = currentState.user.copy(
                    communitiesPreview = updatedCommunities,
                    communitiesCount = updatedCommunities.size
                ),
                bannerNotification = "Вы вышли из сообщества ${community.name}"
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
