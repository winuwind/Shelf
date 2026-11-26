package ru.shelf.client.android.ui.screens.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.shelf.client.android.R
import ru.shelf.client.android.data.CatalogObject
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.CatalogUiState
import ru.shelf.client.android.ui.viewModels.CatalogViewModel

@Composable
fun CatalogScreen(
    previousScreenName: String = "Лента",
    viewModel: CatalogViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToObject: (String) -> Unit = {},
    onProposeObjectClick: () -> Unit = {},
    onMyApplicationsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    CatalogContent(
        previousScreenName = previousScreenName,
        uiState = uiState,
        onBackClick = onBackClick,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        onCategorySelect = { viewModel.onCategorySelect(it) },
        onObjectClick = { objectId ->
            viewModel.onObjectClick(objectId, onNavigateToObject)
        },
        onLoadMore = { viewModel.loadNextPage() },
        onProposeObjectClick = onProposeObjectClick,
        onMyApplicationsClick = onMyApplicationsClick
    )
}

@Composable
fun CatalogContent(
    previousScreenName: String,
    uiState: CatalogUiState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onObjectClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onProposeObjectClick: () -> Unit,
    onMyApplicationsClick: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItem >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow_to_certain_screen),
                    contentDescription = "Назад",
                    tint = Color.Unspecified
                )
                Text(
                    text = previousScreenName,
                    fontSize = 14.sp,
                    color = Color(0xFFA54E32),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Каталог",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF352A26)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Поиск объектов по названию", color = Color(0xFF84736B), fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Поиск",
                        tint = Color.Unspecified
                    )
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

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.categories) { category ->
                    val isSelected = category == uiState.selectedCategory
                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFFFE8DA) else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFFFFE8DA) else Color(0xFFEEDFD6),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onCategorySelect(category) }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFFA54E32) else Color(0xFF84736B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProposeObjectClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Предложить объект",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA54E32)
                )
                Icon(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMyApplicationsClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Мои заявки",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA54E32)
                )
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.objects) { catalogObject ->
                CatalogObjectCard(
                    catalogObject = catalogObject,
                    onClick = { onObjectClick(catalogObject.id) }
                )
            }

            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFA54E32),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogObjectCard(
    catalogObject: CatalogObject,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
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
                    .size(width = 58.dp, height = 90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFE8DA)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.standard_object_cover), //TODO: загрузка по url
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = catalogObject.category.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA54E32)
                )

                Text(
                    text = catalogObject.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26),
                    lineHeight = 20.sp
                )

                if (catalogObject.rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.object_rating),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${catalogObject.rating} Полка",
                            fontSize = 12.sp,
                            color = Color(0xFFA54E32),
                            fontWeight = FontWeight.Normal
                        )
                    }
                } else {
                    Text(
                        text = "Нет оценок",
                        fontSize = 12.sp,
                        color = Color(0xFF84736B)
                    )
                }
            }
        }
    }
}

@Preview(name = "1. Каталог через Ленту", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Catalog_FromFeed() {
    ShelfTheme {
        CatalogScreen(previousScreenName = "Лента")
    }
}

@Preview(name = "2. Каталог через Поиск", showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Catalog_FromSearch() {
    ShelfTheme {
        CatalogScreen(previousScreenName = "Поиск")
    }
}
