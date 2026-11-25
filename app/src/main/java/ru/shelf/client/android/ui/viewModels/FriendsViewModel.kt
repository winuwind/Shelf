package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.Friend
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.screens.users.FriendsErrorType

data class FriendsUiState(
    val user: User = mockUserMe,
    val currentUserId: String = CURRENT_USER_ID,
    val selectedTab: Int = 0,
    val incomingRequests: List<Friend> = listOf(
        Friend("1", "Лена", "Л", 0xFFD9E0E8),
        Friend("2", "Коля", "К", 0xFFF1D0C1),
        Friend("3", "Саша", "С", 0xFFDAE2D6)
    ),
    val outgoingRequests: List<Friend> = listOf(
        Friend("1", "Мирослава", "М", 0xFFE7DCEB),
        Friend("2", "Артём", "А", 0xFFE8D6C8),
        Friend("3", "Валерия", "В", 0xFFF1D0C1),
        Friend("4", "Никита", "Н", 0xFFDAE2D6)
    ),
    val errorState: FriendsErrorType? = null,
    val bannerNotification: String? = null,
    val isLoading: Boolean = false
)

class FriendsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun acceptRequest(friend: Friend) {
        _uiState.update { currentState ->
            val updatedIncoming = currentState.incomingRequests.filter { it.id != friend.id }
            val updatedFriends = currentState.user.friendsPreview + friend
            currentState.copy(
                incomingRequests = updatedIncoming,
                user = currentState.user.copy(
                    friendsPreview = updatedFriends,
                    friendsCount = updatedFriends.size
                ),
                bannerNotification = "Заявка от ${friend.name} принята"
            )
        }
    }

    fun declineRequest(friend: Friend) {
        _uiState.update { currentState ->
            val updatedIncoming = currentState.incomingRequests.filter { it.id != friend.id }
            currentState.copy(
                incomingRequests = updatedIncoming,
                bannerNotification = "Заявка от ${friend.name} отклонена"
            )
        }
    }

    fun cancelRequest(friend: Friend) {
        _uiState.update { currentState ->
            val updatedOutgoing = currentState.outgoingRequests.filter { it.id != friend.id }
            currentState.copy(
                outgoingRequests = updatedOutgoing,
                bannerNotification = "Заявка для ${friend.name} отменена"
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
