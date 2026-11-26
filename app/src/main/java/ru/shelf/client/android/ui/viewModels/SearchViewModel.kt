package ru.shelf.client.android.ui.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.Community
import ru.shelf.client.android.data.Friend
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserMaksimOutgoing

val mockSearchUsers = listOf(
    mockUserAnna,
    mockUserMaksimOutgoing,
    User(
        id = "user_3",
        name = "Екатерина Смирнова",
        bio = "Люблю классическую литературу и кино 60-х.",
        friendsPreview = listOf(Friend("1", "M", "M"))
    ),
    User(
        id = "user_4",
        name = "Иван Петров",
        bio = "Геймер, анимешник, коллекционирую фигурки.",
        friendsPreview = emptyList()
    )
)

val mockSearchCommunities = listOf(
    Community("1", "Мир книгоманов"),
    Community("2", "Фильмы Нолана"),
    Community("3", "Клуб любителей манги"),
    Community("4", "Вселенная Ведьмака")
)

enum class SearchTab(val title: String) {
    USERS("Пользователи"),
    COMMUNITIES("Сообщества")
}

data class SearchUiState(
    val selectedTab: SearchTab = SearchTab.USERS,
    val searchQuery: String = "",
    val usersResult: List<User> = emptyList(),
    val communitiesResult: List<Community> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    val isSearchStarted: Boolean
        get() = searchQuery.trim().isNotEmpty()

    val isResultsEmpty: Boolean
        get() = isSearchStarted && ((selectedTab == SearchTab.USERS && usersResult.isEmpty()) ||
                (selectedTab == SearchTab.COMMUNITIES && communitiesResult.isEmpty()))
}

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        performSearch()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, isError = false) }
        performSearch()
    }

    fun onTabSelect(tab: SearchTab) {
        _uiState.update { it.copy(selectedTab = tab, isError = false) }
        performSearch()
    }

    fun clearSearchQuery() {
        _uiState.update { it.copy(searchQuery = "", isError = false) }
        performSearch()
    }

    fun retrySearch() {
        _uiState.update { it.copy(isError = false) }
        performSearch()
    }

    private fun performSearch() {
        val query = _uiState.value.searchQuery.trim()

        if (query.isEmpty()) {
            _uiState.update {
                it.copy(
                    usersResult = emptyList(),
                    communitiesResult = emptyList()
                )
            }
            return
        }

        val filteredUsers = mockSearchUsers.filter {
            it.name.contains(query, ignoreCase = true) || it.bio.contains(query, ignoreCase = true)
        }

        val filteredCommunities = mockSearchCommunities.filter {
            it.name.contains(query, ignoreCase = true)
        }

        _uiState.update {
            it.copy(
                usersResult = filteredUsers,
                communitiesResult = filteredCommunities
            )
        }
    }
}
