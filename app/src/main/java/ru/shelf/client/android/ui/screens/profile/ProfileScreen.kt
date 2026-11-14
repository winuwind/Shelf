package ru.shelf.client.android.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import ru.shelf.client.android.R
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.CollectionVisibility
import ru.shelf.client.android.data.FriendRequestStatus
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserAnnaBlocked
import ru.shelf.client.android.data.mockUserAnnaFriend
import ru.shelf.client.android.data.mockUserAnnaPrivate
import ru.shelf.client.android.data.mockUserAnnaYouAreBlocked
import ru.shelf.client.android.data.mockUserDeleted
import ru.shelf.client.android.data.mockUserMaksimIncoming
import ru.shelf.client.android.data.mockUserMaksimOutgoing
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.theme.ShelfTheme

val ScreenBgColor = Color(0xFFFFF9F5)
val TextPrimary = Color(0xFF352A26)
val TextSecondary = Color(0xFF84736B)
val PeachAccent = Color(0xFFF9DCD0)
val TerracottaBadge = Color(0xFFC87A58)
val TopBarCircleBg = Color(0xFFFDECE5)
val LineDividerColor = Color(0xFFF0ECE8)
val CardBorderColor = Color(0xFFEFE8E3)
val DisabledText = Color(0xFFA5978E)
val DisabledBg = Color(0xFFF0E8E2)

@Composable
fun ProfileScreen( //TODO: Переделать на viewModel чтобы не передавать столько функций + добавить кликабельность друзей/сообществ/коллекций на экране профиля
    user: User,
    currentUserId: String = CURRENT_USER_ID,
    bannerNotification: String? = null,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onAcceptRequestClick: () -> Unit = {},
    onDeclineRequestClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
    onUnblockClick: () -> Unit = {},
    onBlockUserClick: () -> Unit = {},
    onSendMessageClick: () -> Unit = {},
    onOpenFriendsClick: () -> Unit = {},
    onOpenCommunitiesClick: () -> Unit = {},
    onOpenCollectionsClick: () -> Unit = {}
) {
    val isMe = user.id == currentUserId || user.isCurrentUser

    var activeBanner by remember(bannerNotification) { mutableStateOf(bannerNotification) }

    LaunchedEffect(activeBanner) {
        if (activeBanner != null) {
            delay(5.seconds)
            activeBanner = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBgColor)
    ) {
        ProfileTopBar(
            title = if (isMe) "Профиль" else "Профиль ${user.name}",
            showMenu = isMe,
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = activeBanner != null,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
                ) {
                    activeBanner?.let { message ->
                        SuccessBannerNotification(message = message)
                    }
                }
            }

            item {
                ProfileHeader(user = user, isMe = isMe)
            }

            val canShowActions = !isMe && !user.isBannedOrDeleted && !user.isPrivate
            if (canShowActions) {
                item {
                    ProfileActionButtons(
                        user = user,
                        onAddFriendClick = {
                            activeBanner = "Заявка в друзья отправлена"
                            onAddFriendClick()
                        },
                        onRemoveFriendClick = {
                            activeBanner = "Пользователь удалён из друзей"
                            onRemoveFriendClick()
                        },
                        onAcceptRequestClick = {
                            activeBanner = "Заявка принята. Теперь вы друзья"
                            onAcceptRequestClick()
                        },
                        onDeclineRequestClick = {
                            activeBanner = "Заявка отклонена"
                            onDeclineRequestClick()
                        },
                        onCancelRequestClick = {
                            activeBanner = "Заявка отменена"
                            onCancelRequestClick()
                        },
                        onUnblockClick = {
                            activeBanner = "Пользователь разблокирован"
                            onUnblockClick()
                        },
                        onBlockUserClick = {
                            activeBanner = "Пользователь заблокирован"
                            onBlockUserClick()
                        },
                        onSendMessageClick = onSendMessageClick
                    )
                }
            }

            val canShowContent = !user.isBannedOrDeleted && (isMe || !user.isPrivate)
            if (canShowContent) {
                item {
                    FriendsSection(
                        user = user,
                        onOpenFriendsClick = onOpenFriendsClick
                    )
                }
                item {
                    CommunitiesSection(
                        user = user,
                        onOpenCommunitiesClick = onOpenCommunitiesClick
                    )
                }
                item {
                    CollectionsSection(
                        user = user,
                        onOpenCollectionsClick = onOpenCollectionsClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileTopBar(
    title: String,
    showMenu: Boolean = false,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
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
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Назад",
                    tint = Color.Unspecified
                )
            }
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TopBarCircleBg)
                    .clickable { onSettingsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_settings_menu),
                    contentDescription = "Настройки",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun SuccessBannerNotification(message: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F4)),
        border = BorderStroke(1.dp, CardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = null,
                tint = TerracottaBadge,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = message,
                fontSize = 13.sp,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun ProfileHeader(user: User, isMe: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE5D5CD))
        ) {
            Image(
                painter = painterResource(id = R.drawable.standard_banner), //TODO: Получиь баннер
                contentDescription = "Обложка",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 78.dp, start = 12.dp)
                .size(88.dp)
                .clip(CircleShape)
                .background(ScreenBgColor)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.standard_avatar), //TODO: Получение аватара по URL
                contentDescription = "Аватар",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 6.dp)
        ) {
            if (isMe) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_main_panel),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = TextSecondary
                    )
                    Text(
                        text = "Ваш профиль",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            } else if (user.isFriend) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = TerracottaBadge
                    )
                    Text(
                        text = "У вас в друзьях",
                        fontSize = 11.sp,
                        color = TerracottaBadge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = user.name,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))

        when {
            user.isBannedOrDeleted -> {
                Text(
                    text = "Аккаунт пользователя заблокирован или удалён",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
            user.isPrivate && !isMe -> {
                Text(
                    text = "Этот профиль является приватным",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
            user.isBioPrivate -> {
                Text(
                    text = "Пользователь скрыл описание",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
            else -> {
                Text(
                    text = user.bio,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ProfileActionButtons(
    user: User,
    onAddFriendClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onAcceptRequestClick: () -> Unit = {},
    onDeclineRequestClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
    onUnblockClick: () -> Unit = {},
    onBlockUserClick: () -> Unit = {},
    onSendMessageClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            user.isBlockedByMe -> {
                if(user.isFriend){
                    Button(
                        onClick = onRemoveFriendClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCE9DE)),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_remove_from_friends),
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Удалить из друзей", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                OutlinedButton(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        disabledContainerColor = DisabledBg,
                        disabledContentColor = DisabledText
                    ),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = DisabledText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = DisabledText, fontSize = 14.sp)
                }
                
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            user.isBlockedMe -> {
                Text(
                    text = "Пользователь вас заблокировал",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Button(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = DisabledBg,
                        disabledContentColor = DisabledText
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_add_friend),
                        contentDescription = null,
                        tint = DisabledText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Добавить в друзья", color = DisabledText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onSendMessageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ScreenBgColor),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            user.friendRequestStatus == FriendRequestStatus.OUTGOING -> {
                Button(
                    onClick = { },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = DisabledBg,
                        disabledContentColor = DisabledText
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_friend_requesr_sent),
                        contentDescription = null,
                        tint = DisabledText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Заявка отправлена", color = DisabledText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onCancelRequestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFCE9DE)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Text(text = "Отменить заявку", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onSendMessageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ScreenBgColor),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            user.friendRequestStatus == FriendRequestStatus.INCOMING -> {
                Button(
                    onClick = onAcceptRequestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5AC)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_accept_a_friend),
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Принять заявку", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onDeclineRequestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFCE9DE)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Отклонить заявку", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onSendMessageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ScreenBgColor),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            user.isFriend -> {
                Button(
                    onClick = onRemoveFriendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCE9DE)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_remove_from_friends),
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Удалить из друзей", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onSendMessageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ScreenBgColor),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            else -> {
                Button(
                    onClick = onAddFriendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7C5AC)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_add_friend),
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Добавить в друзья", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onSendMessageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ScreenBgColor),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_write_message),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Сообщение", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (!user.isBlockedByMe) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onBlockUserClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_ban),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(text = "Заблокировать", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}


@Composable
fun FriendsSection(user: User, onOpenFriendsClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        SectionHeader(
            title = "Друзья",
            count = user.friendsCount,
            onChevronClick = onOpenFriendsClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (user.isFriendsPrivate){
            Text("Данные скрыты", fontSize = 13.sp, color = TextSecondary)
        }
        else if (user.friendsPreview.isEmpty()) {
            Text("Нет друзей", fontSize = 13.sp, color = TextSecondary)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(user.friendsPreview.take(10)) { friend ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box( //TODO: иконка друга, а не генерация круга с буквой
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(friend.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = friend.initial,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        var text = friend.name
                        if (text.length > 6){
                            text = text.substring(0, 4) + "..."
                        }
                        Text(
                            text = text,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LineDividerColor, thickness = 1.dp)
    }
}

@Composable
fun CommunitiesSection(user: User, onOpenCommunitiesClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        SectionHeader(
            title = "Сообщества",
            count = user.communitiesCount,
            onChevronClick = onOpenCommunitiesClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        if(user.isCommunitiesPrivate){
            Text("Данные скрыты", fontSize = 13.sp, color = TextSecondary)
        }
        else if (user.communitiesPreview.isEmpty()) {
            Text("Нет сообществ", fontSize = 13.sp, color = TextSecondary)
        } else {
            user.communitiesPreview.take(3).forEach { community ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.community_not_found_and_standard_avatar), //TODO: Получение баннера сообщества
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    var text = community.name
                    if (text.length > 30){
                        text = text.substring(0, 30) + "..."
                    }
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LineDividerColor, thickness = 1.dp)
    }
}

@Composable
fun CollectionsSection(user: User, onOpenCollectionsClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        SectionHeader(
            title = "Коллекции",
            count = user.collectionsCount,
            onChevronClick = onOpenCollectionsClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        if(user.isCollectionsPrivate){
            Text("Данные скрыты", fontSize = 13.sp, color = TextSecondary)
        }
        else if (user.collectionsPreview.isEmpty()) {
            Text("Нет коллекций", fontSize = 13.sp, color = TextSecondary)
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(user.collectionsPreview.take(5)) { collection ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorderColor),
                        modifier = Modifier
                            .width(160.dp)
                            .height(68.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            var text = collection.name
                            if (text.length > 12){
                                text = text.substring(0, 9) + "..."
                            }
                            Text(
                                text = text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            when (collection.visibility) {
                                CollectionVisibility.PRIVATE -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_privacy_only_me),
                                            contentDescription = null,
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text("Только владелец", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                                CollectionVisibility.PUBLIC -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_privacy_everything),
                                            contentDescription = null,
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text("Для всех", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                                CollectionVisibility.FRIENDS_ONLY -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.profile_privacy_friends_only),
                                            contentDescription = null,
                                            tint = Color.Unspecified
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
}

@Composable
fun SectionHeader(
    title: String,
    count: Int,
    onChevronClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChevronClick() },
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
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = (if (count > 0) count else 0).toString(),
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview(name = "1. Свой профиль", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_Me() {
    ShelfTheme {
        ProfileScreen(user = mockUserMe)
    }
}

@Preview(name = "2. Чужой профиль (Обычный)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_Normal() {
    ShelfTheme {
        ProfileScreen(user = mockUserAnna)
    }
}

@Preview(name = "3. Профиль друга", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_Friend() {
    ShelfTheme {
        ProfileScreen(user = mockUserAnnaFriend)
    }
}

@Preview(name = "4. Заявка отправлена (Исходящая)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_OutgoingRequest() {
    ShelfTheme {
        ProfileScreen(
            user = mockUserMaksimOutgoing,
            bannerNotification = "Заявка в друзья отправлена"
        )
    }
}

@Preview(name = "5. Входящая заявка", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_IncomingRequest() {
    ShelfTheme {
        ProfileScreen(user = mockUserMaksimIncoming)
    }
}

@Preview(name = "6. Заблокированный мной", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_BlockedByMe() {
    ShelfTheme {
        ProfileScreen(user = mockUserAnnaBlocked)
    }
}

@Preview(name = "7. Меня заблокировали", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_YouAreBlocked() {
    ShelfTheme {
        ProfileScreen(user = mockUserAnnaYouAreBlocked)
    }
}

@Preview(name = "8. Приватный профиль", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_Private() {
    ShelfTheme {
        ProfileScreen(user = mockUserAnnaPrivate)
    }
}

@Preview(name = "9. Удаленный / заблокированный аккаунт", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Profile_Deleted() {
    ShelfTheme {
        ProfileScreen(user = mockUserDeleted)
    }
}
