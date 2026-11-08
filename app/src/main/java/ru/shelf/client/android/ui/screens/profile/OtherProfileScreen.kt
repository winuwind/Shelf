package ru.shelf.client.android.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.CollectionVisibility
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserAnnaBlocked
import ru.shelf.client.android.data.mockUserAnnaFriend
import ru.shelf.client.android.data.mockUserAnnaPrivate
import ru.shelf.client.android.data.mockUserAnnaYouAreBlocked
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.theme.ShelfTheme

// Цвета дизайна
val ScreenBgColor = Color(0xFFFFF9F5)
val TextPrimary = Color(0xFF2C2C2C)
val TextSecondary = Color(0xFF8E8E8E)
val PeachAccent = Color(0xFFF9DCD0)
val TerracottaBadge = Color(0xFFC87A58)
val TopBarCircleBg = Color(0xFFFDECE5)
val LineDividerColor = Color(0xFFF0ECE8)
val CardBorderColor = Color(0xFFEFE8E3)
val DisabledText = Color(0xFFB8B0AA)

@Composable
fun MainAppScreen(
    user: User,
    currentUserId: String = CURRENT_USER_ID,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onUnblockClick: () -> Unit = {},
    onSendMessageClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTab = 3) },
        containerColor = ScreenBgColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OtherProfileScreen(
                user = user,
                currentUserId = currentUserId,
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                onAddFriendClick = onAddFriendClick,
                onRemoveFriendClick = onRemoveFriendClick,
                onUnblockClick = onUnblockClick,
                onSendMessageClick = onSendMessageClick
            )
        }
    }
}

@Composable
fun OtherProfileScreen(
    user: User,
    currentUserId: String = CURRENT_USER_ID,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onUnblockClick: () -> Unit = {},
    onSendMessageClick: () -> Unit = {}
) {
    val isMe = user.id == currentUserId || user.isCurrentUser
    val showMenu = isMe || user.isBlockedByMe

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBgColor)
    ) {
        // Шапка с кнопками
        TopBar(
            title = if (isMe) "Профиль" else "Профиль ${user.name}",
            showMenu = showMenu,
            onBackClick = onBackClick,
            onMenuClick = onMenuClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Шапка профиля (обложка, аватар, имя, био, статус)
            item {
                ProfileHeader(user = user, isMe = isMe)
            }

            // Кнопки действий (не показываем для своего профиля, приватного не-друга или если вы заблокированы)
            val canShowActions = !isMe && (!user.isPrivate || user.isFriend) && !user.isBlockedMe
            if (canShowActions) {
                item {
                    ActionButtons(
                        user = user,
                        onAddFriendClick = onAddFriendClick,
                        onRemoveFriendClick = onRemoveFriendClick,
                        onUnblockClick = onUnblockClick,
                        onSendMessageClick = onSendMessageClick
                    )
                }
            }

            // Секции (Друзья, Сообщества, Коллекции)
            val canShowContent = (isMe || !user.isPrivate || user.isFriend) && !user.isBlockedMe
            if (canShowContent) {
                if (user.friendsPreview.isNotEmpty()) {
                    item {
                        FriendsSection(user = user)
                    }
                }
                if (user.communitiesPreview.isNotEmpty()) {
                    item {
                        CommunitiesSection(user = user)
                    }
                }
                if (user.collectionsPreview.isNotEmpty()) {
                    item {
                        CollectionsSection(user = user)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    title: String,
    showMenu: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
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
                    .background(TopBarCircleBg)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TopBarCircleBg)
                    .clickable { onMenuClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Меню",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User, isMe: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Контейнер обложки
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE5D5CD)) // Имитация красивой обложки
        ) {
            // Если пользователь заблокирован нами, выводим плашку поверх обложки
            if (user.isBlockedByMe) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = TerracottaBadge,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Пользователь заблокирован",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Аватар
        Box(
            modifier = Modifier
                .padding(top = 100.dp, start = 16.dp)
                .size(86.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, Color.White, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF8B7E74)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(1),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Надпись статуса под аватаркой справа ("Ваш профиль" или "У вас в друзьях")
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            if (isMe) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondary
                    )
                    Text(
                        text = "Ваш профиль",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            } else if (user.isFriend && !user.isBlockedByMe) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TerracottaBadge
                    )
                    Text(
                        text = "У вас в друзьях",
                        fontSize = 12.sp,
                        color = TerracottaBadge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Имя и био
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = user.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (user.isBlockedMe) {
            Text(
                text = "Вы не можете просматривать профиль этого пользователя",
                color = TextSecondary,
                fontSize = 14.sp
            )
        } else if (user.isPrivate && !isMe && !user.isFriend) {
            Text(
                text = "Этот профиль является приватным",
                color = TextSecondary,
                fontSize = 14.sp
            )
        } else {
            Text(
                text = user.bio,
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ActionButtons(
    user: User,
    onAddFriendClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onUnblockClick: () -> Unit = {},
    onSendMessageClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (user.isBlockedByMe) {
            // Кнопка Разблокировать
            Button(
                onClick = onUnblockClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PeachAccent),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "Разблокировать",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "Вы заблокировали этого пользователя.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                color = TextSecondary,
                fontSize = 12.sp
            )
            // Кнопка Сообщение (неактивна)
            OutlinedButton(
                onClick = { },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = DisabledText
                ),
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = DisabledText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Сообщение",
                    color = DisabledText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            // Кнопка "Добавить в друзья" / "Удалить из друзей"
            if (user.isFriend) {
                Button(
                    onClick = onRemoveFriendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PeachAccent),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonRemove,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Удалить из друзей",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Button(
                    onClick = onAddFriendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PeachAccent),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAdd,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Добавить в друзья",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Кнопка Сообщение
            OutlinedButton(
                onClick = onSendMessageClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Сообщение",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!user.isFriend) {
                Text(
                    text = "Войдите, чтобы добавить ${user.name} в друзья",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

// --- Секции ---

@Composable
fun FriendsSection(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SectionHeader(title = "Друзья", count = user.friendsCount)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(user.friendsPreview.take(5)) { friend ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(friend.color)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = friend.initial,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = friend.name,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LineDividerColor, thickness = 1.dp)
    }
}

@Composable
fun CommunitiesSection(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SectionHeader(title = "Сообщества", count = user.communitiesCount)
        Spacer(modifier = Modifier.height(12.dp))

        user.communitiesPreview.take(5).forEach { community ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF333333)) // Имитация аватарки сообщества
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = community.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LineDividerColor, thickness = 1.dp)
    }
}

@Composable
fun CollectionsSection(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SectionHeader(title = "Коллекции", count = user.collectionsCount)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            user.collectionsPreview.take(5).forEach { collection ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = collection.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        when (collection.visibility) {
                            CollectionVisibility.PRIVATE -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Только я", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                            CollectionVisibility.PUBLIC -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Все", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                            CollectionVisibility.FRIENDS_ONLY -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Для друзей", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            if (count > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = count.toString(),
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

// --- Нижняя Навигационная Панель ---

@Composable
fun BottomNavigationBar(selectedTab: Int = 3) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.border(width = 1.dp, color = CardBorderColor)
    ) {
        val items = listOf("Лента", "Поиск", "Сообщения", "Профиль")
        val icons = listOf(
            Icons.Outlined.ViewAgenda,
            Icons.Outlined.Search,
            Icons.Outlined.Email,
            Icons.Default.Person
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedTab,
                onClick = { },
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TerracottaBadge,
                    selectedTextColor = TerracottaBadge,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// --- Превью ---

@Preview(name = "1. Чужой профиль (Обычный)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_Normal() {
    ShelfTheme {
        MainAppScreen(user = mockUserAnna)
    }
}

@Preview(name = "2. Ваш профиль", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_CurrentUser() {
    ShelfTheme {
        MainAppScreen(user = mockUserMe)
    }
}

@Preview(name = "3. Профиль друга", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_Friend() {
    ShelfTheme {
        MainAppScreen(user = mockUserAnnaFriend)
    }
}

@Preview(name = "4. Заблокированный профиль", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_Blocked() {
    ShelfTheme {
        MainAppScreen(user = mockUserAnnaBlocked)
    }
}

@Preview(name = "5. Приватный профиль", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_Private() {
    ShelfTheme {
        MainAppScreen(user = mockUserAnnaPrivate)
    }
}

@Preview(name = "6. Вы заблокированы", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_OtherProfile_YouAreBlocked() {
    ShelfTheme {
        MainAppScreen(user = mockUserAnnaYouAreBlocked)
    }
}
