package ru.shelf.client.android.ui.viewModels

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.shelf.client.android.data.CatalogObject
import ru.shelf.client.android.data.Rating
import ru.shelf.client.android.data.Review
import ru.shelf.client.android.data.mockCatalogObjects

data class UserCollectionSelection(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)

data class ObjectDetailUiState(
    val catalogObject: CatalogObject = mockCatalogObjects.first(),
    val isAuthorized: Boolean = true,
    val userStatus: String? = null,
    val userProgress: String? = null,
    val userRating: Int? = null,
    val reviews: List<Review> = listOf(
        Review("1", "Анна Смирнова", rating = 9, dateCreated = "24 октября 2026", dateChanged = null, text = "Великолепное произведение! Захватывает с первых страниц и держит в напряжении до самого конца.", likesCount = 14),
        Review("2", "Максим Петров", rating = 8, dateCreated = "20 октября 2026", dateChanged = "30 октября 2026", text = "Очень глубокий смысл, заставляет задуматься о многом. Рекомендую к прочтению каждому.", likesCount = 8)
    ),
    val ratings: List<Rating> = listOf(
        Rating(rating = 8.8, "Кинопоиск"),
        Rating(rating = 6.1, "IMDb")
    ),
    val isAddCollectionDialogShown: Boolean = false,
    val isRateDialogShown: Boolean = false,
    val isAddReviewDialogShown: Boolean = false,
    val bannerNotification: String? = null,
    val selectedRatingInDialog: Int = 8,
    val draftReviewText: String = "",
    val draftReviewRating: Int = 9,
    val userCollections: List<UserCollectionSelection> = listOf(
        UserCollectionSelection("1", "Избранное", isSelected = true),
        UserCollectionSelection("2", "Любимые книги", isSelected = false),
        UserCollectionSelection("3", "Хочу посмотреть", isSelected = false)
    )
)

class ObjectViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ObjectDetailUiState())
    val uiState: StateFlow<ObjectDetailUiState> = _uiState.asStateFlow()

    fun loadObject(objectId: String) {
        val found = mockCatalogObjects.find { it.id == objectId } ?: mockCatalogObjects.first()
        _uiState.update { it.copy(catalogObject = found) }
    }

    fun setAuthorized(authorized: Boolean) {
        _uiState.update { it.copy(isAuthorized = authorized) }
    }

    fun toggleUserStatus(status: String) {
        _uiState.update { currentState ->
            val newStatus = if (currentState.userStatus == status) null else status
            val bannerMsg = newStatus?.let { "Статус изменен на «$it»" } ?: "Статус убран"
            currentState.copy(userStatus = newStatus, bannerNotification = bannerMsg)
        }
    }

    fun setUserProgress(progress: String?) {
        _uiState.update { it.copy(userProgress = progress) }
    }

    fun openRateDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedRatingInDialog = currentState.userRating ?: 8,
                isRateDialogShown = true
            )
        }
    }

    fun removeRating() {
        _uiState.update { currentState ->
            currentState.copy(
                userRating = null
            )
        }
    }

    fun closeRateDialog() {
        _uiState.update { it.copy(isRateDialogShown = false) }
    }

    fun selectRatingInDialog(rating: Int) {
        _uiState.update { it.copy(selectedRatingInDialog = rating) }
    }

    fun confirmRating() {
        _uiState.update { currentState ->
            val rating = currentState.selectedRatingInDialog
            currentState.copy(
                userRating = rating,
                isRateDialogShown = false,
                bannerNotification = "Ваша оценка: $rating из 10"
            )
        }
    }

    fun openReviewDialog() {
        _uiState.update { it.copy(isAddReviewDialogShown = true, draftReviewText = "", draftReviewRating = 9) }
    }

    fun closeReviewDialog() {
        _uiState.update { it.copy(isAddReviewDialogShown = false) }
    }

    fun onDraftReviewTextChange(text: String) {
        _uiState.update { it.copy(draftReviewText = text) }
    }

    fun onDraftReviewRatingChange(rating: Int) {
        _uiState.update { it.copy(draftReviewRating = rating) }
    }

    fun confirmAddReview() {
        val currentState = _uiState.value
        val text = currentState.draftReviewText.ifBlank { "Отличное произведение!" }
        val newReview = Review(
            id = System.currentTimeMillis().toString(),
            authorName = "Вы",
            rating = currentState.draftReviewRating,
            dateCreated = "Сегодня",
            dateChanged = null,
            text = text,
            likesCount = 0
        )
        _uiState.update { state ->
            state.copy(
                reviews = listOf(newReview) + state.reviews,
                isAddReviewDialogShown = false,
                draftReviewText = "",
                bannerNotification = "Отзыв успешно добавлен"
            )
        }
    }

    fun openCollectionDialog() {
        _uiState.update { it.copy(isAddCollectionDialogShown = true) }
    }

    fun closeCollectionDialog() {
        _uiState.update { it.copy(isAddCollectionDialogShown = false) }
    }

    fun toggleCollectionSelection(collectionId: String) {
        _uiState.update { currentState ->
            val updated = currentState.userCollections.map { col ->
                if (col.id == collectionId) col.copy(isSelected = !col.isSelected) else col
            }
            currentState.copy(userCollections = updated)
        }
    }

    fun confirmCollectionSelection() {
        _uiState.update { currentState ->
            currentState.copy(
                isAddCollectionDialogShown = false,
                bannerNotification = "Список коллекций обновлен"
            )
        }
    }

    fun toggleLikeReview(reviewId: String) {
        _uiState.update { currentState ->
            val updatedReviews = currentState.reviews.map { review ->
                if (review.id == reviewId) {
                    review.copy(likesCount = review.likesCount + 1)
                } else review
            }
            currentState.copy(reviews = updatedReviews)
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(bannerNotification = null) }
    }
}
