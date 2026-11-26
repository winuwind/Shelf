package ru.shelf.client.android.data

data class CatalogObject(
    val id: String,
    val category: String,
    val title: String,
    val description: String = "Это увлекательное произведение завоевало сердца миллионов читателей и зрителей по всему миру. Глубокий сюжет, проработанные персонажи и неповторимая атмосфера никого не оставят равнодушным.",
    val imageUrl: String? = null,
    val rating: Float? = 8.8f,
    val ratingsCount: Int = 1420,
    val reviewsCount: Int = 310,
    val myRating: Int? = null,
    val status: String? = null,
    val progress: String? = null
)

data class Review(
    val id: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val rating: Int? = null,
    val date: String,
    val text: String,
    val likesCount: Int = 0
)

val mockCatalogObjects = listOf(
    CatalogObject(
        id = "1",
        category = "Книги",
        title = "1984",
        rating = 8.8f,
        ratingsCount = 1420,
        reviewsCount = 310
    ),
    CatalogObject(
        id = "2",
        category = "Фильмы",
        title = "Аватар",
        rating = 8.1f,
        ratingsCount = 2100,
        reviewsCount = 450
    ),
    CatalogObject(
        id = "3",
        category = "Манга",
        title = "Акира",
        rating = 8.7f,
        ratingsCount = 890,
        reviewsCount = 120
    ),
    CatalogObject(
        id = "4",
        category = "Аниме",
        title = "Атака титанов",
        rating = 9.1f,
        ratingsCount = 3400,
        reviewsCount = 890
    ),
    CatalogObject(
        id = "5",
        category = "Видеоигры",
        title = "Ведьмак 3",
        rating = 9.4f,
        ratingsCount = 4100,
        reviewsCount = 1100
    ),
    CatalogObject(
        id = "6",
        category = "Книги",
        title = "Ведьмак. Последнее желание",
        rating = 8.9f,
        ratingsCount = 1200,
        reviewsCount = 280
    ),
    CatalogObject(
        id = "7",
        category = "Книги",
        title = "Вино из одуванчиков",
        rating = 8.7f,
        ratingsCount = 950,
        reviewsCount = 190
    ),
    CatalogObject(
        id = "8",
        category = "Фигурки",
        title = "Геральт из Ривии",
        rating = null,
        ratingsCount = 0,
        reviewsCount = 0
    ),
    CatalogObject(
        id = "9",
        category = "Книги",
        title = "Гордость и предубеждение",
        rating = 8.8f,
        ratingsCount = 1100,
        reviewsCount = 240
    ),
    CatalogObject(
        id = "10",
        category = "Книги",
        title = "Дюна",
        rating = 8.9f,
        ratingsCount = 1600,
        reviewsCount = 380
    ),
    CatalogObject(
        id = "11",
        category = "Фильмы",
        title = "Дюна",
        rating = 8.3f,
        ratingsCount = 1800,
        reviewsCount = 420
    ),
    CatalogObject(
        id = "12",
        category = "Фильмы",
        title = "Интерстеллар",
        rating = 9.2f,
        ratingsCount = 3900,
        reviewsCount = 920
    ),
    CatalogObject(
        id = "13",
        category = "Аниме",
        title = "Магическая битва",
        rating = 8.6f,
        ratingsCount = 2100,
        reviewsCount = 480
    ),
    CatalogObject(
        id = "14",
        category = "Манга",
        title = "Магическая битва",
        rating = 8.5f,
        ratingsCount = 1300,
        reviewsCount = 290
    )
)
