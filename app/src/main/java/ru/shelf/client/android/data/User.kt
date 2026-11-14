package ru.shelf.client.android.data

enum class CollectionVisibility {
    PUBLIC,
    PRIVATE,
    FRIENDS_ONLY
}

enum class FriendRequestStatus {
    NONE,
    OUTGOING,
    INCOMING
}

data class Friend(
    val id: String = "",
    val name: String,
    val initial: String,
    val color: Long = 0xFFE8D5D5,
    val avatarUrl: String? = null
)

data class Community(
    val id: String = "",
    val name: String,
    val iconRes: Int = 0,
    val imageUrl: String? = null
)

data class Collection(
    val id: String = "",
    val name: String,
    val isPrivate: Boolean = false,
    val visibility: CollectionVisibility = if (isPrivate) CollectionVisibility.PRIVATE else CollectionVisibility.PUBLIC
)

const val CURRENT_USER_ID = "current_user_id"

data class User(
    val id: String,
    val name: String,
    val bio: String,
    val avatarUrl: String? = null,
    val coverUrl: String? = null,
    val isCurrentUser: Boolean = (id == CURRENT_USER_ID),
    val isFriend: Boolean = false,
    val friendRequestStatus: FriendRequestStatus = FriendRequestStatus.NONE,
    val isBlockedByMe: Boolean = false,
    val isBlockedMe: Boolean = false,
    val isPrivate: Boolean = false,
    val isBannedOrDeleted: Boolean = false,
    val isBioPrivate: Boolean = false,
    val isFriendsPrivate: Boolean = false,
    val isCommunitiesPrivate: Boolean = false,
    val isCollectionsPrivate: Boolean = false,
    val friendsCount: Int = 0,
    val friendsPreview: List<Friend> = emptyList(),
    val communitiesCount: Int = 0,
    val communitiesPreview: List<Community> = emptyList(),
    val collectionsCount: Int = 0,
    val collectionsPreview: List<Collection> = emptyList()
)

val mockUserAnna = User(
    id = "user_anna_123",
    name = "Anna",
    bio = "Не всё интересное можно рассказать словами.",
    avatarUrl = null,
    coverUrl = null,
    isCurrentUser = false,
    isFriend = false,
    isBlockedByMe = false,
    isBlockedMe = false,
    isPrivate = false,
    friendsCount = 5,
    friendsPreview = listOf(
        Friend("1", "Маша", "М", 0xFFE5D5D0),
        Friend("2", "Илья", "И", 0xFFD8E5DC),
        Friend("3", "Соня", "С", 0xFFE8DAD0),
        Friend("4", "Дима", "Д", 0xFFD8E0E8),
        Friend("5", "Алиса", "А", 0xFFE5D5E8)
    ),
    communitiesCount = 2,
    communitiesPreview = listOf(
        Community("1", "Мир книгоманов"),
        Community("2", "Фильмы Нолана")
    ),
    collectionsCount = 1,
    collectionsPreview = listOf(
        Collection("1", "Книги", visibility = CollectionVisibility.PUBLIC)
    )
)

val mockUserMe = mockUserAnna.copy(
    id = CURRENT_USER_ID,
    isCurrentUser = true,
    collectionsCount = 3,
    collectionsPreview = listOf(
        Collection("1", "Книги", visibility = CollectionVisibility.PUBLIC),
        Collection("2", "Фильмы", visibility = CollectionVisibility.PRIVATE),
        Collection("3", "Новые сериалы", visibility = CollectionVisibility.PUBLIC)
    )
)

val mockUserAnnaFriend = mockUserAnna.copy(
    isFriend = true,
    collectionsCount = 2,
    collectionsPreview = listOf(
        Collection("1", "Книги", visibility = CollectionVisibility.PUBLIC),
        Collection("2", "Сериалы", visibility = CollectionVisibility.PUBLIC)
    )
)

val mockUserMaksimOutgoing = mockUserAnna.copy(
    id = "user_maksim_456",
    name = "Maksim",
    bio = "Ищу красоту в мелочах и истории в случайных встречах.",
    friendRequestStatus = FriendRequestStatus.OUTGOING
)

val mockUserMaksimIncoming = mockUserMaksimOutgoing.copy(
    friendRequestStatus = FriendRequestStatus.INCOMING
)

val mockUserAnnaBlocked = mockUserAnna.copy(
    isBlockedByMe = true,
    isFriend = true,
    collectionsCount = 1,
    collectionsPreview = listOf(
        Collection("1", "Книги", visibility = CollectionVisibility.PUBLIC)
    )
)

val mockUserAnnaPrivate = mockUserAnna.copy(
    isPrivate = true
)

val mockUserAnnaYouAreBlocked = mockUserAnna.copy(
    isBlockedMe = true
)

val mockUserDeleted = mockUserAnna.copy(
    isBannedOrDeleted = true,
    name = "Удаленный аккаунт",
    bio = ""
)
