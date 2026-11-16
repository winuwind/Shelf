package ru.shelf.client.android.ui.screens.collections

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
import ru.shelf.client.android.R
import ru.shelf.client.android.data.CURRENT_USER_ID
import ru.shelf.client.android.data.Collection
import ru.shelf.client.android.data.CollectionVisibility
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.theme.ShelfTheme

enum class CollectionsErrorType(val message: String) {
    LOAD_COLLECTIONS_FAILED("Не удалось загрузить коллекции. Попробуйте позже"),
    DELETE_COLLECTION_FAILED("Не удалось удалить коллекцию. Попробуйте позже"),
    PRIVACY_RESTRICTED("Список коллекций недоступен")
}

@Composable
fun CollectionsScreen(
    user: User = mockUserMe,
    currentUserId: String = CURRENT_USER_ID,
    errorState: CollectionsErrorType? = null,
    onBackClick: () -> Unit = {},
    onCreateCollectionClick: () -> Unit = {},
    onCollectionClick: (Collection) -> Unit = {},
    onRetryErrorClick: () -> Unit = {}
) {
    val isMe = user.id == currentUserId || user.isCurrentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        CollectionsTopBar(
            title = "Коллекции",
            count = user.collectionsCount,
            onBackClick = onBackClick
        )

        if (errorState != null) {
            CollectionsErrorCard(
                errorMessage = errorState.message,
                onRetryClick = onRetryErrorClick
            )
        }

        if (isMe) {
            Text(
                text = "Ваши коллекции",
                fontSize = 13.sp,
                color = Color(0xFF84736B),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        when {
            !isMe && user.isCollectionsPrivate -> {
                EmptyCollectionsView(
                    title = "Нет доступных коллекций",
                    showCreateButton = false
                )
            }
            user.collectionsPreview.isEmpty() -> {
                EmptyCollectionsView(
                    title = if (isMe) "У вас пока нет коллекций" else "Нет доступных коллекций",
                    showCreateButton = isMe,
                    onCreateClick = onCreateCollectionClick
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(user.collectionsPreview) { collection ->
                        CollectionRowItem(
                            collection = collection,
                            isMe = isMe,
                            onClick = { onCollectionClick(collection) }
                        )
                    }

                    if (isMe) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = onCreateCollectionClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE8DA)),
                                    shape = RoundedCornerShape(20.dp),
                                    elevation = ButtonDefaults.buttonElevation(0.dp),
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.plus),
                                        contentDescription = null,
                                        tint = Color(0xFFD4623A),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Создать коллекцию",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFD4623A)
                                    )
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
fun CollectionsTopBar(
    title: String,
    count: Int,
    onBackClick: () -> Unit
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
                    .clip(CircleShape)
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
                color = Color(0xFF352A26)
            )
        }

        if (count > 0) {
            Text(
                text = count.toString(),
                fontSize = 14.sp,
                color = Color(0xFF84736B)
            )
        }
    }
}

@Composable
fun CollectionsErrorCard(
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
fun CollectionRowItem(
    collection: Collection,
    isMe: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = collection.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF352A26)
                )

                if (isMe) {
                    val visibilityText = when (collection.visibility) {
                        CollectionVisibility.PUBLIC -> "Все"
                        CollectionVisibility.PRIVATE -> "Только я"
                        CollectionVisibility.FRIENDS_ONLY -> "Только друзья"
                    }
                    Text(
                        text = visibilityText,
                        fontSize = 10.sp,
                        color = Color(0xFF84736B)
                    )
                }
            }

            Text(
                text = collection.dateCreate,
                fontSize = 11.sp,
                color = Color(0xFF84736B)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

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
fun EmptyCollectionsView(
    title: String,
    showCreateButton: Boolean = false,
    onCreateClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color(0xFF84736B),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        if (showCreateButton) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE8DA)),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.height(44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null,
                    tint = Color(0xFFD4623A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Создать коллекцию",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD4623A)
                )
            }
        }
    }
}

@Preview(name = "1. Свои коллекции", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_MyList() {
    ShelfTheme {
        CollectionsScreen(user = mockUserMe)
    }
}

@Preview(name = "2. Свой пустой список коллекций", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_EmptyMe() {
    ShelfTheme {
        CollectionsScreen(user = mockUserMe.copy(collectionsPreview = emptyList(), collectionsCount = 0))
    }
}

@Preview(name = "3. Коллекции чужого пользователя", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_OtherUser() {
    ShelfTheme {
        CollectionsScreen(user = mockUserAnna.copy(isCollectionsPrivate = false))
    }
}

@Preview(name = "4. Нет доступных коллекций (чужой)", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_EmptyOtherUser() {
    ShelfTheme {
        CollectionsScreen(user = mockUserAnna.copy(isCollectionsPrivate = false, collectionsPreview = emptyList(), collectionsCount = 0))
    }
}

@Preview(name = "5. Ошибка загрузки коллекций", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_ErrorLoad() {
    ShelfTheme {
        CollectionsScreen(
            user = mockUserMe,
            errorState = CollectionsErrorType.LOAD_COLLECTIONS_FAILED
        )
    }
}

@Preview(name = "6. Ошибка удаления коллекции", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Collections_ErrorDelete() {
    ShelfTheme {
        CollectionsScreen(
            user = mockUserMe,
            errorState = CollectionsErrorType.DELETE_COLLECTION_FAILED
        )
    }
}
