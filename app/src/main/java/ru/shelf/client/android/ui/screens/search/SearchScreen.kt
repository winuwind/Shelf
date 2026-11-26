package ru.shelf.client.android.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import ru.shelf.client.android.data.Community
import ru.shelf.client.android.data.User
import ru.shelf.client.android.data.mockUserAnna
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.SearchTab
import ru.shelf.client.android.ui.viewModels.SearchUiState
import ru.shelf.client.android.ui.viewModels.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onNavigateToUser: (String) -> Unit = {},
    onNavigateToCommunity: (String) -> Unit = {},
    onNavigateToCatalog: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchContent(
        uiState = uiState,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        onTabSelect = { viewModel.onTabSelect(it) },
        onClearQuery = { viewModel.clearSearchQuery() },
        onRetry = { viewModel.retrySearch() },
        onNavigateToUser = onNavigateToUser,
        onNavigateToCommunity = onNavigateToCommunity,
        onNavigateToCatalog = onNavigateToCatalog
    )
}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    onSearchQueryChange: (String) -> Unit,
    onTabSelect: (SearchTab) -> Unit,
    onClearQuery: () -> Unit,
    onRetry: () -> Unit,
    onNavigateToUser: (String) -> Unit,
    onNavigateToCommunity: (String) -> Unit,
    onNavigateToCatalog: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Поиск",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF352A26)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFE8DA), shape = RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                SearchTab.entries.forEach { tab ->
                    val isSelected = tab == uiState.selectedTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { onTabSelect(tab) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFFA54E32) else Color(0xFF84736B)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    val hintText = if (uiState.selectedTab == SearchTab.USERS) {
                        "Поиск пользователей по имени"
                    } else {
                        "Поиск сообществ"
                    }
                    Text(
                        text = hintText,
                        color = Color(0xFF84736B),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Поиск",
                        tint = Color.Unspecified
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Очистить",
                                tint = Color(0xFF84736B)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFFEEDFD6),
                    unfocusedBorderColor = Color(0xFFEEDFD6),
                    focusedTextColor = Color(0xFF352A26),
                    unfocusedTextColor = Color(0xFF352A26)
                ),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToCatalog() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Перейти в каталог",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA54E32)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        when {
            uiState.isError -> {
                SearchErrorView(onRetry = onRetry)
            }
            !uiState.isSearchStarted -> {
                SearchInitialStateView(selectedTab = uiState.selectedTab)
            }
            uiState.isResultsEmpty -> {
                SearchEmptyResultsView(selectedTab = uiState.selectedTab)
            }
            uiState.selectedTab == SearchTab.USERS -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.usersResult) { user ->
                        SearchUserCard(user = user, onClick = { onNavigateToUser(user.id) })
                    }
                }
            }
            uiState.selectedTab == SearchTab.COMMUNITIES -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.communitiesResult) { community ->
                        SearchCommunityCard(community = community, onClick = { onNavigateToCommunity(community.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchInitialStateView(
    selectedTab: SearchTab
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconRes = if (selectedTab == SearchTab.USERS) {
            R.drawable.search_username_status
        } else {
            R.drawable.search // Or another icon if needed, like community_not_found
        }

        val hintText = if (selectedTab == SearchTab.USERS) {
            "Введите имя пользователя для поиска"
        } else {
            "Введите название сообщества для поиска"
        }
        
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFFCE9DE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = hintText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

@Composable
fun SearchUserCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8D5D5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(1).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF352A26)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = user.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26)
                )

                if (user.bio.isNotEmpty()) {
                    Text(
                        text = user.bio,
                        fontSize = 13.sp,
                        color = Color(0xFF84736B),
                        maxLines = 1
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun SearchCommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE8DA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.community_not_found_and_standard_avatar),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = community.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26)
                )
                Text(
                    text = "Сообщество",
                    fontSize = 12.sp,
                    color = Color(0xFF84736B)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun SearchEmptyResultsView(
    selectedTab: SearchTab = SearchTab.USERS
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconRes = if (selectedTab == SearchTab.USERS) {
            R.drawable.search_no_users_found
        } else {
            R.drawable.search_no_users_found // Assuming we reuse or have a similar one
        }

        val emptyText = if (selectedTab == SearchTab.USERS) {
            "Пользователи по вашему запросу не найдены"
        } else {
            "Сообщества по вашему запросу не найдены"
        }
        
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F1F1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = emptyText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

@Composable
fun SearchErrorView(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search_and_collection_errors),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Не удалось выполнить поиск. Попробуйте позже",
            fontSize = 14.sp,
            color = Color(0xFF84736B),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFFE8DA))
                .clickable { onRetry() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Повторить",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFA54E32)
            )
        }
    }
}

@Preview(name = "1. Поиск (начальное состояние — ничего не введено)", showBackground = true)
@Composable
fun Preview_SearchScreen_Initial() {
    ShelfTheme {
        SearchScreen()
    }
}

@Preview(name = "2. Поиск (есть результаты)", showBackground = true)
@Composable
fun Preview_SearchScreen_WithResults() {
    ShelfTheme {
        SearchContent(
            uiState = SearchUiState(
                searchQuery = "Anna",
                usersResult = listOf(mockUserAnna)
            ),
            onSearchQueryChange = {},
            onTabSelect = {},
            onClearQuery = {},
            onRetry = {},
            onNavigateToUser = {},
            onNavigateToCommunity = {}
        )
    }
}

@Preview(name = "3. Поиск (пустой результат)", showBackground = true)
@Composable
fun Preview_SearchScreen_Empty() {
    ShelfTheme {
        SearchContent(
            uiState = SearchUiState(
                searchQuery = "Несуществующий пользователь"
            ),
            onSearchQueryChange = {},
            onTabSelect = {},
            onClearQuery = {},
            onRetry = {},
            onNavigateToUser = {},
            onNavigateToCommunity = {}
        )
    }
}

@Preview(name = "4. Поиск (ошибка)", showBackground = true)
@Composable
fun Preview_SearchScreen_Error() {
    ShelfTheme {
        SearchContent(
            uiState = SearchUiState(isError = true),
            onSearchQueryChange = {},
            onTabSelect = {},
            onClearQuery = {},
            onRetry = {},
            onNavigateToUser = {},
            onNavigateToCommunity = {}
        )
    }
}
