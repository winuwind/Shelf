package ru.shelf.client.android.ui.screens.communities

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
import ru.shelf.client.android.data.Community
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.components.BannerType
import ru.shelf.client.android.ui.components.FloatingNotificationBannerHost
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.CommunitiesViewModel

enum class CommunitiesErrorType(val message: String) {
    LOAD_COMMUNITIES_FAILED("Не удалось загрузить список сообществ. Попробуйте позже")
}

@Composable
fun CommunitiesScreen(
    viewModel: CommunitiesViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onCommunityClick: (Community) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    CommunitiesScreen(
        user = uiState.user,
        currentUserId = uiState.currentUserId,
        errorState = uiState.errorState,
        bannerNotification = uiState.bannerNotification,
        onBackClick = onBackClick,
        onCommunityClick = onCommunityClick,
        onRetryErrorClick = { viewModel.retryError() }
    )
}

@Composable
fun CommunitiesScreen(
    user: User = mockUserMe,
    currentUserId: String = CURRENT_USER_ID,
    errorState: CommunitiesErrorType? = null,
    bannerNotification: String? = null,
    onBackClick: () -> Unit = {},
    onCommunityClick: (Community) -> Unit = {},
    onRetryErrorClick: () -> Unit = {}
) {
    val isMe = user.id == currentUserId || user.isCurrentUser
    var activeBanner by remember(bannerNotification) { mutableStateOf(bannerNotification) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        CommunitiesTopBar(
            title = "Сообщества",
            onBackClick = onBackClick
        )

        FloatingNotificationBannerHost(
            message = activeBanner,
            typeError = BannerType.Failed,
            onDismiss = { activeBanner = null }
        )

        if (errorState != null) {
            CommunitiesErrorCard(
                errorMessage = errorState.message,
                onRetryClick = onRetryErrorClick
            )
        }

        when {
            !isMe && user.isCommunitiesPrivate -> {
                EmptyCommunitiesView(
                    title = "Пользователь ${user.name} скрыл сообщества, на которые подписан"
                )
            }
            user.communitiesPreview.isEmpty() -> {
                EmptyCommunitiesView(
                    title = if (isMe) "Вы не подписаны ни на одно сообщество" else "Пользователь не подписан ни на одно сообщество"
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(user.communitiesPreview) { community ->
                        CommunityRowItem(
                            community = community,
                            onClick = { onCommunityClick(community) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommunitiesTopBar(
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
fun CommunitiesErrorCard(
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
fun CommunityRowItem(
    community: Community,
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFE8DA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.community_not_found_and_standard_avatar), //TODO: Получение аватара сообщества
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = community.name,
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
fun EmptyCommunitiesView(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color(0xFF84736B),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}


@Preview(name = "1. Список сообществ (Свой)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Communities_MyList() {
    ShelfTheme {
        CommunitiesScreen(user = mockUserMe)
    }
}

@Preview(name = "2. Пустой список сообществ", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Communities_Empty() {
    ShelfTheme {
        CommunitiesScreen(user = mockUserMe.copy(communitiesPreview = emptyList()))
    }
}

@Preview(name = "3. Скрытые сообщества другого пользователя", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Communities_Private() {
    ShelfTheme {
        CommunitiesScreen(user = mockUserAnna.copy(isCommunitiesPrivate = true))
    }
}

@Preview(name = "4. Ошибка загрузки сообществ", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Communities_Error() {
    ShelfTheme {
        CommunitiesScreen(
            user = mockUserMe,
            errorState = CommunitiesErrorType.LOAD_COMMUNITIES_FAILED
        )
    }
}