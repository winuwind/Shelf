package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.FriendRequestStatus
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.components.BannerType

data class ProfileUiState(
    val user: User = mockUserMe,
    val currentUserId: String = CURRENT_USER_ID,
    val activeBanner: String? = null,
    val bannerType: BannerType = BannerType.Success,
    val showBlockDialog: Boolean = false,
    val isMenuExpanded: Boolean = false,
    val isLoading: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun setUser(user: User) {
        _uiState.update { it.copy(user = user) }
    }

    fun showBlockDialog() {
        _uiState.update { it.copy(showBlockDialog = true) }
    }

    fun dismissBlockDialog() {
        _uiState.update { it.copy(showBlockDialog = false) }
    }

    fun confirmBlockUser() {
        _uiState.update { currentState ->
            currentState.copy(
                showBlockDialog = false,
                user = currentState.user.copy(isBlockedByMe = true),
                activeBanner = "Пользователь заблокирован",
                bannerType = BannerType.Blocked
            )
        }
    }

    fun unblockUser() {
        _uiState.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(isBlockedByMe = false),
                activeBanner = "Пользователь разблокирован",
                bannerType = BannerType.Success
            )
        }
    }

    fun addFriend() {
        _uiState.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(friendRequestStatus = FriendRequestStatus.OUTGOING),
                activeBanner = "Заявка в друзья отправлена",
                bannerType = BannerType.Success
            )
        }
    }

    fun removeFriend() {
        _uiState.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(friendRequestStatus = FriendRequestStatus.NONE),
                activeBanner = "Пользователь удален из друзей",
                bannerType = BannerType.Success
            )
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(activeBanner = null) }
    }
}
