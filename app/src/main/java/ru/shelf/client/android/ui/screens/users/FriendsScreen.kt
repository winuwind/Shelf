package ru.shelf.client.android.ui.screens.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.shelf.client.android.R
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.Friend
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.components.BannerErrorType
import ru.shelf.client.android.ui.components.BannerType
import ru.shelf.client.android.ui.components.FloatingNotificationBannerHost
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.FriendsViewModel

enum class FriendsErrorType(val message: String) {
    LOAD_FRIENDS_FAILED("Не удалось загрузить список друзей. Попробуйте позже"),
    LOAD_INCOMING_FAILED("Не удалось загрузить входящие заявки. Попробуйте позже"),
    CANCEL_REQUEST_FAILED("Не удалось отменить заявку. Попробуйте ещё раз")
}

@Composable
fun FriendsScreen(
    viewModel: FriendsViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onUserClick: (Friend) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    FriendsScreen(
        user = uiState.user,
        currentUserId = uiState.currentUserId,
        initialTab = uiState.selectedTab,
        incomingRequests = uiState.incomingRequests,
        outgoingRequests = uiState.outgoingRequests,
        errorState = uiState.errorState,
        bannerNotification = uiState.bannerNotification,
        onBackClick = onBackClick,
        onUserClick = onUserClick,
        onAcceptRequestClick = { viewModel.acceptRequest(it) },
        onDeclineRequestClick = { viewModel.declineRequest(it) },
        onCancelRequestClick = { viewModel.cancelRequest(it) },
        onRetryErrorClick = { viewModel.retryError() }
    )
}

@Composable
fun FriendsScreen(
    user: User = mockUserMe,
    currentUserId: String = CURRENT_USER_ID,
    initialTab: Int = 0,
    incomingRequests: List<Friend> = listOf(
        Friend("1", "Лена", "Л", 0xFFD9E0E8),
        Friend("2", "Коля", "К", 0xFFF1D0C1),
        Friend("3", "Саша", "С", 0xFFDAE2D6)
    ),
    outgoingRequests: List<Friend> = listOf(
        Friend("1", "Мирослава", "М", 0xFFE7DCEB),
        Friend("2", "Артём", "А", 0xFFE8D6C8),
        Friend("3", "Валерия", "В", 0xFFF1D0C1),
        Friend("4", "Никита", "Н", 0xFFDAE2D6)
    ),
    errorState: FriendsErrorType? = null,
    bannerNotification: String? = null,
    onBackClick: () -> Unit = {},
    onUserClick: (Friend) -> Unit = {},
    onAcceptRequestClick: (Friend) -> Unit = {},
    onDeclineRequestClick: (Friend) -> Unit = {},
    onCancelRequestClick: (Friend) -> Unit = {},
    onRetryErrorClick: () -> Unit = {}
) {
    val isMe = user.id == currentUserId || user.isCurrentUser
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    var activeBanner by remember(bannerNotification) { mutableStateOf(bannerNotification) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        FriendsTopBar(
            title = "Друзья",
            onBackClick = onBackClick
        )

        FloatingNotificationBannerHost(
            message = activeBanner,
            typeError = BannerType.Failed,
            onDismiss = { activeBanner = null }
        )

        if (errorState != null) {
            FriendsErrorBanner(
                errorMessage = errorState.message,
                onRetryClick = onRetryErrorClick
            )
        }

        if (isMe) {
            FriendsTabs(
                selectedTab = selectedTab,
                incomingCount = incomingRequests.size,
                onTabSelected = { selectedTab = it }
            )

            when (selectedTab) {
                0 -> MyFriendsTabContent(
                    friends = user.friendsPreview,
                    onUserClick = onUserClick
                )
                1 -> IncomingRequestsTabContent(
                    requests = incomingRequests,
                    onAccept = { friend ->
                        onAcceptRequestClick(friend)
                    },
                    onDecline = { friend ->
                        onDeclineRequestClick(friend)
                    },
                    onUserClick = onUserClick
                )
                2 -> OutgoingRequestsTabContent(
                    requests = outgoingRequests,
                    onCancel = { friend ->
                        onCancelRequestClick(friend)
                    },
                    onUserClick = onUserClick
                )
            }
        } else {
            OtherUserFriendsContent(
                user = user,
                onUserClick = onUserClick
            )
        }
    }
}

@Composable
fun FriendsTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE8DA))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow_to_certain_screen),
                contentDescription = "Назад",
                tint = Color.Unspecified
            )
        }
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26)
        )
    }
}

@Composable
fun FriendsErrorBanner(
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F4)),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color(0xFFA54E32),
                lineHeight = 22.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE8DA)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Повторить",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA54E32)
                    )
                }
            }
        }
    }
}

@Composable
fun FriendsTabs(
    selectedTab: Int,
    incomingCount: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val tab0Active = selectedTab == 0
        Box(
            modifier = Modifier
                .weight(1f)
                .height(58.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (tab0Active) Color(0xFFFFE8DA) else Color(0xFFFFF9F5))
                .clickable { onTabSelected(0) },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Мои",
                    fontSize = 12.sp,
                    fontWeight = if (tab0Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab0Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
                Text(
                    text = "друзья",
                    fontSize = 12.sp,
                    fontWeight = if (tab0Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab0Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
            }
        }

        val tab1Active = selectedTab == 1
        Box(
            modifier = Modifier
                .weight(1f)
                .height(58.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (tab1Active) Color(0xFFFFE8DA) else Color(0xFFFFF9F5))
                .clickable { onTabSelected(1) },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Входящие",
                    fontSize = 12.sp,
                    fontWeight = if (tab1Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab1Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
                Text(
                    text = "заявки",
                    fontSize = 12.sp,
                    fontWeight = if (tab1Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab1Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
            }
        }

        val tab2Active = selectedTab == 2
        Box(
            modifier = Modifier
                .weight(1f)
                .height(58.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (tab2Active) Color(0xFFFFE8DA) else Color(0xFFFFF9F5))
                .clickable { onTabSelected(2) },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Исходящие",
                    fontSize = 12.sp,
                    fontWeight = if (tab2Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab2Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
                Text(
                    text = "заявки",
                    fontSize = 12.sp,
                    fontWeight = if (tab2Active) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (tab2Active) Color(0xFFA54E32) else Color(0xFF84736B)
                )
            }
        }
    }
}

@Composable
fun MyFriendsTabContent(
    friends: List<Friend>,
    onUserClick: (Friend) -> Unit
) {
    if (friends.isEmpty()) {
        EmptyFriendsView(
            iconRes = R.drawable.friend_user_has_no_friends,
            title = "У вас пока нет друзей",
            subtitle = "Добавляйте людей в друзья, чтобы делиться коллекциями."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Мои друзья",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF352A26)
                    )
                    Text(
                        text = friends.size.toString(),
                        fontSize = 13.sp,
                        color = Color(0xFF84736B)
                    )
                }
            }

            items(friends) { friend ->
                FriendRowItem(friend = friend, onClick = { onUserClick(friend) })
            }
        }
    }
}

@Composable
fun IncomingRequestsTabContent(
    requests: List<Friend>,
    onAccept: (Friend) -> Unit,
    onDecline: (Friend) -> Unit,
    onUserClick: (Friend) -> Unit
) {
    if (requests.isEmpty()) {
        EmptyFriendsView(
            iconRes = R.drawable.friend_no_incoming_friend_requests,
            title = "Нет входящих заявок",
            subtitle = "Когда кто-то захочет добавить вас в друзья, заявка появится здесь."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Ожидают вашего ответа",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF352A26)
                    )
                    Text(
                        text = requests.size.toString(),
                        fontSize = 13.sp,
                        color = Color(0xFF84736B)
                    )
                }
            }

            items(requests) { friend ->
                IncomingRequestRowItem(
                    friend = friend,
                    onAccept = { onAccept(friend) },
                    onDecline = { onDecline(friend) },
                    onUserClick = { onUserClick(friend) }
                )
            }
        }
    }
}

@Composable
fun OutgoingRequestsTabContent(
    requests: List<Friend>,
    onCancel: (Friend) -> Unit,
    onUserClick: (Friend) -> Unit
) {
    if (requests.isEmpty()) {
        EmptyFriendsView(
            iconRes = R.drawable.friend_no_friend_requests_sent,
            title = "Нет отправленных заявок",
            subtitle = "Здесь будут ваши заявки в друзья, на которые ещё не ответили."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Отправленные заявки",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF352A26)
                    )
                    Text(
                        text = requests.size.toString(),
                        fontSize = 13.sp,
                        color = Color(0xFF84736B)
                    )
                }
            }

            items(requests) { friend ->
                OutgoingRequestRowItem(
                    friend = friend,
                    onCancel = { onCancel(friend) },
                    onUserClick = { onUserClick(friend) }
                )
            }
        }
    }
}

@Composable
fun OtherUserFriendsContent(
    user: User,
    onUserClick: (Friend) -> Unit
) {
    when {
        user.isFriendsPrivate -> {
            EmptyFriendsView(
                iconRes = R.drawable.friend_user_has_no_friends,
                title = "Пользователь ${user.name} скрыл список своих друзей",
                subtitle = ""
            )
        }
        user.friendsPreview.isEmpty() -> {
            EmptyFriendsView(
                iconRes = R.drawable.friend_user_has_no_friends,
                title = "У пользователя ${user.name} нет друзей",
                subtitle = ""
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(user.friendsPreview) { friend ->
                    FriendRowItem(friend = friend, onClick = { onUserClick(friend) })
                }
            }
        }
    }
}

@Composable
fun FriendRowItem(
    friend: Friend,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(friend.color)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.initial, //TODO: иконка пользователя если есть
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF352A26)
                )
            }
            Text(
                text = friend.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )
    }
    HorizontalDivider(color = Color(0xFFEEDFD6), thickness = 1.dp)
}

@Composable
fun IncomingRequestRowItem(
    friend: Friend,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(friend.color))
                .clickable { onUserClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friend.initial, //TODO: Получение иконки человека
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = friend.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26),
                modifier = Modifier.clickable { onUserClick() }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5AC)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Принять",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF352A26)
                    )
                }

                OutlinedButton(
                    onClick = onDecline,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFFF9F5)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Отклонить",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFA54E32)
                    )
                }
            }
        }
    }
    HorizontalDivider(color = Color(0xFFEEDFD6), thickness = 1.dp)
}

@Composable
fun OutgoingRequestRowItem(
    friend: Friend,
    onCancel: () -> Unit,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(friend.color))
                .clickable { onUserClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friend.initial, //TODO: Получить иконку
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = friend.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26),
                modifier = Modifier.clickable { onUserClick() }
            )
            Text(
                text = "Ожидается ответ",
                fontSize = 12.sp,
                color = Color(0xFF84736B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = onCancel,
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFFF9F5)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "Отменить заявку",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA54E32)
                )
            }
        }
    }
    HorizontalDivider(color = Color(0xFFEEDFD6), thickness = 1.dp)
}

@Composable
fun EmptyFriendsView(
    iconRes: Int,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE8DA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26),
            textAlign = TextAlign.Center
        )
        if (subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF84736B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(name = "1. Свои друзья - вкладка Мои", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_MyFriends() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 0
        )
    }
}

@Preview(name = "2. Свои друзья - Входящие заявки", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_Incoming() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 1
        )
    }
}

@Preview(name = "3. Свои друзья - Исходящие заявки", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_Outgoing() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 2
        )
    }
}

@Preview(name = "4. Пустой список своих друзей", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_EmptyMyFriends() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe.copy(friendsPreview = emptyList()),
            initialTab = 0
        )
    }
}

@Preview(name = "5. Нет входящих заявок", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_EmptyIncoming() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 1,
            incomingRequests = emptyList()
        )
    }
}

@Preview(name = "6. Нет исходящих заявок", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_EmptyOutgoing() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 2,
            outgoingRequests = emptyList()
        )
    }
}

@Preview(name = "7. Ошибка загрузки (плашка)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_ErrorBanner() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 1,
            errorState = FriendsErrorType.LOAD_INCOMING_FAILED
        )
    }
}

@Preview(name = "8. Всплывающая плашка ошибки на 5 сек", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_FloatingErrorBanner() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserMe,
            initialTab = 1,
            bannerNotification = BannerErrorType.DECLINE_REQUEST_FAILED.message
        )
    }
}

@Preview(name = "9. Друзья другого пользователя", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_OtherUser() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserAnna.copy(isFriendsPrivate = false)
        )
    }
}

@Preview(name = "10. Друзья другого пользователя скрыты (Private)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Friends_OtherUserPrivate() {
    ShelfTheme {
        FriendsScreen(
            user = mockUserAnna.copy(isFriendsPrivate = true)
        )
    }
}