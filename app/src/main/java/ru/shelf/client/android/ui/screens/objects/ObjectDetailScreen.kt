package ru.shelf.client.android.ui.screens.objects

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.shelf.client.android.R
import ru.shelf.client.android.data.ApplicationStatus
import ru.shelf.client.android.data.CatalogObject
import ru.shelf.client.android.data.Rating
import ru.shelf.client.android.data.Review
import ru.shelf.client.android.data.mockCatalogObjects
import ru.shelf.client.android.ui.components.BannerType
import ru.shelf.client.android.ui.components.FloatingNotificationBannerHost
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.ObjectViewModel
import ru.shelf.client.android.ui.viewModels.UserCollectionSelection
import kotlin.collections.List

@Composable
fun ObjectDetailScreen(
    previousScreenName: String = "Лента",
    objectId: String = "1",
    isAuthorized: Boolean = true,
    viewModel: ObjectViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(objectId, isAuthorized) {
        viewModel.loadObject(objectId)
        viewModel.setAuthorized(isAuthorized)
    }

    ObjectDetailContent(
        previousScreenName = previousScreenName,
        catalogObject = uiState.catalogObject,
        isAuthorized = uiState.isAuthorized,
        userStatus = uiState.userStatus,
        userProgress = uiState.userProgress,
        userRating = uiState.userRating,
        reviews = uiState.reviews,
        ratings = uiState.ratings,
        bannerNotification = uiState.bannerNotification,
        isAddCollectionDialogShown = uiState.isAddCollectionDialogShown,
        isRateDialogShown = uiState.isRateDialogShown,
        isUpdateStatusShown = uiState.isStatusChangeShown,
        isDialogRemoveStatusShown = uiState.isStatusRemoveDialogShown,
        isUpdateProgressShown = uiState.isProgressChangeShown,
        isAddReviewDialogShown = uiState.isAddReviewDialogShown,
        selectedRatingInDialog = uiState.selectedRatingInDialog,
        draftReviewText = uiState.draftReviewText,
        userCollections = uiState.userCollections,
        onBackClick = onBackClick,
        onStatusUpdate = { viewModel.onUpdateUserStatus() },
        onDismissStatusUpdate = { viewModel.onUpdateUserStatusDismiss() },
        onStatusToggle = { viewModel.toggleUserStatus(it) },
        onSetStatus = { viewModel.onSetStatus(it) },
        onRemoveStatusDialog = { viewModel.onRemoveStatusDialog() },
        onProgressUpdate = { viewModel.onUpdateProgress() },
        onDismissProgressUpdate = { viewModel.onUpdateProgressDismiss() },
        onSetProgress = { viewModel.setUserProgress(it) },
        onRatingClick = { viewModel.openRateDialog() },
        onRemoveRatingClick = { viewModel.removeRating() },
        onAddCollectionClick = { viewModel.openCollectionDialog() },
        onAddReviewClick = { viewModel.openReviewDialog() },
        onDismissBanner = { viewModel.dismissBanner() },
        onDismissCollectionDialog = { viewModel.closeCollectionDialog() },
        onDismissRateDialog = { viewModel.closeRateDialog() },
        onDismissReviewDialog = { viewModel.closeReviewDialog() },
        onSelectRatingInDialog = { viewModel.selectRatingInDialog(it) },
        onConfirmRating = { viewModel.confirmRating() },
        onDraftReviewTextChange = { viewModel.onDraftReviewTextChange(it) },
        onConfirmReview = { viewModel.confirmAddReview() },
        onToggleCollectionSelection = { viewModel.toggleCollectionSelection(it) },
        onConfirmCollectionSelection = { viewModel.confirmCollectionSelection() },
        onNumberException = { viewModel.onNumberException() }
    )
}

@Composable
fun ObjectDetailContent(
    previousScreenName: String = "Лента",
    catalogObject: CatalogObject,
    isAuthorized: Boolean,
    userStatus: String?,
    userProgress: Int?,
    userRating: Int?,
    reviews: List<Review>,
    ratings: List<Rating>,
    bannerNotification: String?,
    isAddCollectionDialogShown: Boolean,
    isRateDialogShown: Boolean,
    isUpdateStatusShown: Boolean,
    isDialogRemoveStatusShown: Boolean,
    isUpdateProgressShown: Boolean,
    isAddReviewDialogShown: Boolean,
    selectedRatingInDialog: Int,
    draftReviewText: String,
    userCollections: List<UserCollectionSelection>,
    onBackClick: () -> Unit,
    onStatusUpdate: () -> Unit,
    onDismissStatusUpdate: () -> Unit,
    onStatusToggle: (String) -> Unit,
    onSetStatus: (String?) -> Unit,
    onRemoveStatusDialog: () -> Unit,
    onProgressUpdate: () -> Unit,
    onDismissProgressUpdate: () -> Unit,
    onSetProgress: (Int) -> Unit,
    onRatingClick: () -> Unit,
    onRemoveRatingClick: () -> Unit,
    onAddCollectionClick: () -> Unit,
    onAddReviewClick: () -> Unit,
    onDismissBanner: () -> Unit,
    onDismissCollectionDialog: () -> Unit,
    onDismissRateDialog: () -> Unit,
    onDismissReviewDialog: () -> Unit,
    onSelectRatingInDialog: (Int) -> Unit,
    onConfirmRating: () -> Unit,
    onDraftReviewTextChange: (String) -> Unit,
    onConfirmReview: () -> Unit,
    onToggleCollectionSelection: (String) -> Unit,
    onConfirmCollectionSelection: () -> Unit,
    onNumberException: () -> Unit
) {
    var activeBanner by remember(bannerNotification) { mutableStateOf(bannerNotification) }

    if (isAddCollectionDialogShown) {
        AddCollectionDialog(
            userCollections = userCollections,
            onDismiss = onDismissCollectionDialog,
            onToggleCollection = onToggleCollectionSelection,
            onConfirm = onConfirmCollectionSelection
        )
    }

    if (isRateDialogShown) {
        RateObjectDialog(
            selectedRating = selectedRatingInDialog,
            onSelectRating = onSelectRatingInDialog,
            onDismiss = onDismissRateDialog,
            onConfirm = onConfirmRating
        )
    }

    if (isAddReviewDialogShown) {
        AddReviewDialog(
            reviewText = draftReviewText,
            onReviewTextChange = onDraftReviewTextChange,
            onDismiss = onDismissReviewDialog,
            onConfirm = onConfirmReview
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
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
            text = "Об объекте",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26)
        )

        Spacer(modifier = Modifier.height(16.dp))

        FloatingNotificationBannerHost(
            message = activeBanner,
            typeError = BannerType.Success,
            onDismiss = {
                activeBanner = null
                onDismissBanner()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.standard_object_cover),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                    )
                    {
                        Text(
                            text = catalogObject.category.name.uppercase(),
                            fontSize = 14.sp,
                            color = Color(0xFFA54E32),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = catalogObject.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF352A26),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Text(
                    text = catalogObject.description,
                    fontSize = 14.sp,
                    color = Color(0xFF5A4A42),
                    lineHeight = 20.sp
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                ) {
                    if (catalogObject.rating != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.object_rating),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )

                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                val formattedRating =
                                    catalogObject.rating.toString().replace('.', ',')

                                Text(
                                    text = "$formattedRating / 10",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF352A26),
                                )
                                Text(
                                    text = "Полка - ${catalogObject.ratingsCount} действующих оценок",
                                    fontSize = 12.sp,
                                    color = Color(0xFF84736B)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.object_rating),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )

                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "Нет оценок",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF352A26),
                                )
                                Text(
                                    text = "Станьте первым, кто оценит",
                                    fontSize = 12.sp,
                                    color = Color(0xFF84736B)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFDE6B3B)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onAddCollectionClick() })
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.Center,

                        ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }


                        Text(
                            "Добавить в коллекцию",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (isAuthorized) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.object_rating),
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )

                                Column(
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = "Моя оценка",
                                        fontSize = 12.sp,
                                        color = Color(0xFF84736B)
                                    )
                                    var rating = ""
                                    var colorRating = Color.White

                                    if (userRating != null) {
                                        rating = "$userRating / 10"
                                        colorRating = Color(0xFFA54E32)
                                    } else {
                                        rating = "Не оценено"
                                        colorRating = Color(0xFF84736B)
                                    }
                                    Text(
                                        text = rating,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorRating,
                                    )
                                }
                            }

                            if (userRating != null) {
                                Button(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RoundedCornerShape(16.dp),
                                    onClick = onRatingClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFFE8DA
                                        )
                                    )
                                ) {
                                    Text(
                                        "Изменить оценку",
                                        color = Color(0xFFA54E32),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Button(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RoundedCornerShape(16.dp),
                                    onClick = onRemoveRatingClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFFE8DA
                                        )
                                    )
                                ) {
                                    Text(
                                        "Удалить оценку",
                                        color = Color(0xFFA54E32),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            } else {
                                Button(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = onRatingClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFFE8DA
                                        )
                                    )
                                ) {
                                    Text(
                                        "Оценить объект",
                                        color = Color(0xFFA54E32),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        Text(
                            "Внешние оценки",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF352A26)
                        )

                        LazyRow(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(ratings) { rating ->
                                RatingCard(rating)
                            }
                        }
                    }
                }
            }
            if (isAuthorized) {
                if (catalogObject.category.listAvailableStatus.isNotEmpty()) {
                    if (isUpdateStatusShown) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(all = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Что-то поменялось? Выберите новый статус:",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF84736B)
                                    )

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        catalogObject.category.listAvailableStatus.forEach { status ->
                                            StatusItem(
                                                status = status,
                                                isSelected = status == userStatus,
                                                onSelect = onStatusToggle
                                            )
                                        }
                                    }

                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = { onSetStatus(userStatus) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFFFFE8DA
                                            )
                                        )
                                    ) {
                                        Text(
                                            "Изменить статус",
                                            color = Color(0xFFA54E32),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = onRemoveStatusDialog,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFFFFE8DA
                                            )
                                        )
                                    ) {
                                        Text(
                                            "Убрать статус",
                                            color = Color(0xFFA54E32),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        if (userStatus != null) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            "Мой статус",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color(0xFF84736B)
                                        )

                                        Text(
                                            userStatus,
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFA54E32)
                                        )

                                        Button(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .height(50.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = onStatusUpdate,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(
                                                    0xFFFFE8DA
                                                )
                                            )
                                        ) {
                                            Text(
                                                "Изменить статус",
                                                color = Color(0xFFA54E32),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        Button(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .height(50.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = onRemoveStatusDialog,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(
                                                    0xFFFFE8DA
                                                )
                                            )
                                        ) {
                                            Text(
                                                "Убрать статус",
                                                color = Color(0xFFA54E32),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            "Мой статус",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color(0xFF84736B)
                                        )

                                        Text(
                                            "Не установлен",
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF84736B)
                                        )

                                        Button(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .height(50.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = onStatusUpdate,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(
                                                    0xFFFFE8DA
                                                )
                                            )
                                        ) {
                                            Text(
                                                "Изменить статус",
                                                color = Color(0xFFA54E32),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (catalogObject.category.isProgressAvailable) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFEEDFD6))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Текущий прогресс",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF84736B)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {
                                    Text(
                                        "$userProgress из ${catalogObject.limitProgress} ${catalogObject.category.unitProgress}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF352A26)
                                    )

                                    Text(
                                        "${(userProgress ?: 0) * 100 / (catalogObject.limitProgress ?: 100)}%",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF84736B)
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = {
                                        (userProgress ?: 0).toFloat() / (catalogObject.limitProgress
                                            ?: 100).toFloat()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(50)),
                                    color = Color(0xFFDE6B3B),
                                    trackColor = Color(0xFFFFE8DA),
                                    gapSize = 0.dp,
                                    drawStopIndicator = {}
                                )

                                if(isUpdateProgressShown){
                                    Text(
                                        "Установите прогресс",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF352A26)
                                    )
                                    var chosen by rememberSaveable { mutableStateOf(false) }
                                    var progressPercent by rememberSaveable { mutableStateOf(((userProgress ?: 0) * 100  / (catalogObject.limitProgress ?: 100))) }
                                    var progress by rememberSaveable{ mutableStateOf(userProgress ?: 0) }

                                    var line by rememberSaveable { mutableStateOf(if (chosen) progressPercent.toString() else progress.toString()) }


                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),

                                    ){
                                        Button(
                                            modifier = Modifier
                                                .weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = {
                                                if(chosen)
                                                {
                                                    chosen = false
                                                    line = progress.toString()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (chosen) Color.White else Color (0xFFFFE8DA)
                                            )
                                        ) {
                                            Text(
                                                "Единицы",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = Color(0xFFA54E32)
                                            )
                                        }
                                        Button(
                                            modifier = Modifier
                                                .weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            onClick = {
                                                if(!chosen){
                                                    line = progressPercent.toString()
                                                    chosen = true
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (!chosen) Color.White else Color (0xFFFFE8DA)
                                            )
                                        ) {
                                            Text(
                                                "Проценты",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = Color(0xFFA54E32)
                                            )
                                        }
                                    }

                                    Text(
                                        "Прогресс, ${if (chosen) "%" else catalogObject.category.unitProgress}",
                                        color = Color(0xFF352A26),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )


                                    val inputTextStyle = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF352A26)
                                    )

                                    OutlinedTextField(
                                        value = line,
                                        onValueChange = {
                                            try {
                                                var x = if(it.isNotEmpty()) it.toInt() else 0
                                                
                                                if (chosen) {
                                                    progressPercent = x
                                                    progress = (catalogObject.limitProgress
                                                        ?: 100) * progressPercent / 100
                                                } else {
                                                    progress = x
                                                    progressPercent = (progress
                                                         * 100 / (catalogObject.limitProgress
                                                        ?: 100))
                                                }

                                                if(progress > (catalogObject.limitProgress
                                                        ?: 100) || progress < 0 || progressPercent > 100 || progressPercent < 0
                                                ){
                                                    x = line.toInt()
                                                    if (chosen) {
                                                        progressPercent = x
                                                        progress = (catalogObject.limitProgress
                                                            ?: 100) * progressPercent / 100
                                                    } else {
                                                        progress = x
                                                        progressPercent = (progress
                                                                * 100 / (catalogObject.limitProgress
                                                            ?: 100))
                                                    }
                                                }
                                                else{
                                                    line = it
                                                }
                                            }
                                            catch (_: NumberFormatException){
                                                if(it.isNotEmpty()){
                                                    onNumberException()
                                                }
                                            }
                                        },
                                        placeholder = { Text("Введите число от 0 до ${if(chosen) 100 else catalogObject.limitProgress ?: 100}", color = Color(0xFF84736B)) },
                                        textStyle = inputTextStyle,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFF5C4A8),
                                            unfocusedBorderColor = Color(0xFFF5C4A8),
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White,
                                            focusedTextColor = Color(0xFF352A26),
                                            unfocusedTextColor = Color(0xFF352A26)
                                        ),
                                        singleLine = true
                                    )

                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = { onSetProgress(progress) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFFDE6B3B
                                            )
                                        )
                                    ) {
                                        Text(
                                            "Сохранить прогресс",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = onDismissProgressUpdate,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFFFFE8DA
                                            )
                                        )
                                    ) {
                                        Text(
                                            "Отмена",
                                            color = Color(0xFFA54E32),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                else{
                                    Button(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        onClick = onProgressUpdate,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFFFFE8DA
                                            )
                                        )
                                    ) {
                                        Text(
                                            "Изменить прогресс",
                                            color = Color(0xFFA54E32),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Отзывы",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF352A26)
                    )
                    Text(
                        text = "${reviews.size}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF84736B)
                    )
                }
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = onAddReviewClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFFDE6B3B
                        )
                    )
                ) {
                    Text(
                        text = "Написать отзыв",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            items(reviews) { review ->
                ReviewCard(
                    review = review
                )
            }
        }
    }
}

@Composable
fun StatusItem(
    status: String,
    isSelected: Boolean,
    onSelect: (String) -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFFF1E8) else Color.White
    val borderColor = if (isSelected) Color(0xFFDE6B3B) else Color(0xFFEEDFD6)
    val contentColor = Color(0xFF352A26)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect(status) },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onSelect(status) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF352A26),
                    unselectedColor = Color(0xFF352A26)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = status,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Composable
fun RatingCard(
    rating: Rating
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1E8)),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),

            ) {
            Text(
                rating.platformName,
                color = Color(0xFFA54E32),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            Text(
                "${rating.rating}/10",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF352A26)
            )

            Text(
                "Шкала от 0 до 10",
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF84736B)
            )
        }
    }
}

@Composable
fun ReviewCard(
    review: Review
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.standard_avatar), //TODO: Загрузка картинки
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(34.dp)
                    )

                    Text(
                        text = review.authorName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA54E32)
                    )
                }

                if (review.rating != null) {
                    Text(
                        text = "${review.rating}/10",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA54E32)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF5A4A42)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Опубликовано ${review.dateCreated}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF84736B),
            )

            if (review.dateChanged != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Изменено ${review.dateChanged}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF84736B)
                )
            }
        }
    }
}

@Composable
fun AddCollectionDialog(
    userCollections: List<UserCollectionSelection>,
    onDismiss: () -> Unit,
    onToggleCollection: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9F5))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Добавить в коллекцию",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF352A26)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    userCollections.forEach { col ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onToggleCollection(col.id) }
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = col.isSelected,
                                onCheckedChange = { onToggleCollection(col.id) },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFA6541F))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(col.name, fontSize = 14.sp, color = Color(0xFF352A26))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Готово", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun RateObjectDialog(
    selectedRating: Int,
    onSelectRating: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9F5))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Оцените произведение",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF352A26)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$selectedRating / 10",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA6541F)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (1..5).forEach { i ->
                        FilledTonalButton(
                            onClick = { onSelectRating(i) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (selectedRating == i) Color(0xFFA6541F) else Color(
                                    0xFFFDECE5
                                )
                            )
                        ) {
                            Text(
                                "$i",
                                color = if (selectedRating == i) Color.White else Color(0xFF352A26)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (6..10).forEach { i ->
                        FilledTonalButton(
                            onClick = { onSelectRating(i) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (selectedRating == i) Color(0xFFA6541F) else Color(
                                    0xFFFDECE5
                                )
                            )
                        ) {
                            Text(
                                "$i",
                                color = if (selectedRating == i) Color.White else Color(0xFF352A26)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить оценку", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddReviewDialog(
    reviewText: String,
    onReviewTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9F5))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Написать отзыв",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF352A26)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = onReviewTextChange,
                    placeholder = { Text("Поделитесь своими впечатлениями...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Опубликовать", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ObjectNotFoundScreen(
    namePreviousScreen: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBackClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow_to_certain_screen),
                contentDescription = "Назад",
                tint = Color.Unspecified
            )
            Text(
                text = namePreviousScreen,
                fontSize = 14.sp,
                color = Color(0xFFA54E32),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_not_found),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            Text(
                text = "Объект не найден",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE8DA)),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = "Вернуться в каталог",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFA54E32)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

//@Preview(name = "1. Authorized User", showBackground = true, heightDp = 2000)
//@Composable
//fun Preview_ObjectDetail_Authorized() {
//    ShelfTheme {
//        ObjectDetailContent(
//            catalogObject = mockCatalogObjects[0],
//            isAuthorized = true,
//            userStatus = "Читаю",
//            userProgress = 32,
//            userRating = 9,
//            reviews = listOf(
//                Review(
//                    "1",
//                    "Анна Смирнова",
//                    rating = 9,
//                    dateCreated = "24 октября 2026",
//                    dateChanged = null,
//                    text = "Великолепное произведение! Захватывает с первых страниц и держит в напряжении до самого конца.",
//                    likesCount = 14
//                ),
//                Review(
//                    "2",
//                    "Максим Петров",
//                    rating = 8,
//                    dateCreated = "20 октября 2026",
//                    dateChanged = "30 октября 2026",
//                    text = "Очень глубокий смысл, заставляет задуматься о многом. Рекомендую к прочтению каждому.",
//                    likesCount = 8
//                )
//            ),
//            ratings = listOf(
//                Rating(rating = 8.8, "Кинопоиск"),
//                Rating(rating = 6.1, "IMDb")
//            ),
//            bannerNotification = null,
//            isAddCollectionDialogShown = false,
//            isRateDialogShown = false,
//            isAddReviewDialogShown = false,
//            selectedRatingInDialog = 9,
//            draftReviewText = "",
//            userCollections = emptyList(),
//            onBackClick = {},
//            onStatusToggle = {},
//            onRatingClick = {},
//            onRemoveRatingClick = {},
//            onAddCollectionClick = {},
//            onAddReviewClick = {},
//            onDismissBanner = {},
//            onDismissCollectionDialog = {},
//            onDismissRateDialog = {},
//            onDismissReviewDialog = {},
//            onSelectRatingInDialog = {},
//            onConfirmRating = {},
//            onDraftReviewTextChange = {},
//            onConfirmReview = {},
//            onToggleCollectionSelection = {},
//            onConfirmCollectionSelection = {}
//        )
//    }
//}
//
//@Preview(name = "2. Unauthorized User", showBackground = true)
//@Composable
//fun Preview_ObjectDetail_Unauthorized() {
//    ShelfTheme {
//        ObjectDetailContent(
//            catalogObject = mockCatalogObjects[0],
//            isAuthorized = false,
//            userStatus = null,
//            userProgress = null,
//            userRating = null,
//            reviews = emptyList(),
//            ratings = emptyList(),
//            bannerNotification = null,
//            isAddCollectionDialogShown = true,
//            isRateDialogShown = false,
//            isAddReviewDialogShown = false,
//            selectedRatingInDialog = 8,
//            draftReviewText = "",
//            userCollections = emptyList(),
//            onBackClick = {},
//            onStatusToggle = {},
//            onRatingClick = {},
//            onRemoveRatingClick = {},
//            onAddCollectionClick = {},
//            onAddReviewClick = {},
//            onDismissBanner = {},
//            onDismissCollectionDialog = {},
//            onDismissRateDialog = {},
//            onDismissReviewDialog = {},
//            onSelectRatingInDialog = {},
//            onConfirmRating = {},
//            onDraftReviewTextChange = {},
//            onConfirmReview = {},
//            onToggleCollectionSelection = {},
//            onConfirmCollectionSelection = {}
//        )
//    }
//}
//
//@Preview(name = "3. Экран: Объект не найден", showBackground = true)
//@Composable
//fun Preview_ObjectNotFound() {
//    ShelfTheme {
//        ObjectNotFoundScreen("Каталог", onBackClick = {})
//    }
//}

@Preview(name = "4. Превью с viewModel", showBackground = true, heightDp = 2000)
@Composable
fun Preview_All() {
    ShelfTheme {
        ObjectDetailScreen {

        }
    }
}