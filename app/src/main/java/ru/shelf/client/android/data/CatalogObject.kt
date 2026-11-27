package ru.shelf.client.android.data

data class Category(
    val id: String,
    val name: String,
    val listAvailableStatus: List<String>,
    val isProgressAvailable: Boolean,
    val unitProgress: String?
)

data class CatalogObject(
    val id: String,
    val category: Category,
    val title: String,
    val description: String = "Это увлекательное произведение завоевало сердца миллионов читателей и зрителей по всему миру. Глубокий сюжет, проработанные персонажи и неповторимая атмосфера никого не оставят равнодушным.",
    val imageUrl: String? = null,
    val rating: Float? = 8.8f,
    val ratingsCount: Int = 1420,
    val reviewsCount: Int = 310,
    var myRating: Int? = null,
    var status: String? = null,
    var progress: Int? = null,
    val limitProgress: Int? = null
)

data class Review(
    val id: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val rating: Int? = null,
    val dateCreated: String,
    val dateChanged: String?,
    val text: String,
    val likesCount: Int = 0
)

data class Rating(
    val rating: Double,
    val platformName: String
)

val mockCategory = listOf(
    Category(
        id = "1",
        name = "Книги",
        listAvailableStatus = listOf(
            "Не прочитана", "Читаю", "Отложена", "Прочитана",
        ),
        isProgressAvailable = true,
        unitProgress = "стр."
    ),
    Category(
        id = "2",
        name = "Фильмы",
        listAvailableStatus = listOf(
            "Не просмотрен", "Не досмотрен", "Просмотрен"
        ),
        isProgressAvailable = true,
        unitProgress = "мин."
    ),
    Category(
        id = "3",
        name = "Манга",
        listAvailableStatus = listOf(
            "Не прочитана", "Читаю", "Отложена", "Прочитана",
        ),
        isProgressAvailable = true,
        unitProgress = "стр."
    ),
    Category(
        id = "4",
        name = "Аниме",
        listAvailableStatus = listOf(
            "Не просмотрено", "Не досмотрено", "Просмотрено"
        ),
        isProgressAvailable = true,
        unitProgress = "мин."
    ),
    Category(
        id = "5",
        name = "Видеоигры",
        listAvailableStatus = listOf(
            "Не пройдена", "Прохожу", "Пройдена"
        ),
        isProgressAvailable = true,
        unitProgress = "этапов"
    ),
    Category(
        id = "6",
        name = "Фигурки",
        listAvailableStatus = listOf(),
        isProgressAvailable = false,
        unitProgress = null
    )
)

val mockCatalogObjects = listOf(
    CatalogObject(
        id = "1",
        category = mockCategory[0],
        title = "1984",
        rating = 8.8f,
        ratingsCount = 1420,
        reviewsCount = 310,
        status = mockCategory[0].listAvailableStatus[1],
        progress = 44,
        limitProgress = 96
    ),
    CatalogObject(
        id = "2",
        category = mockCategory[1],
        title = "Аватар",
        rating = 8.1f,
        ratingsCount = 2100,
        reviewsCount = 450,
        status = mockCategory[1].listAvailableStatus[1],
        progress = 23,
        limitProgress = 143
    ),
    CatalogObject(
        id = "3",
        category = mockCategory[2],
        title = "Акира",
        rating = 8.7f,
        ratingsCount = 890,
        reviewsCount = 120,
        status = mockCategory[2].listAvailableStatus[0],
        progress = 0,
        limitProgress = 42
    ),
    CatalogObject(
        id = "4",
        category = mockCategory[3],
        title = "Атака титанов",
        rating = 9.1f,
        ratingsCount = 3400,
        reviewsCount = 890,
        status = mockCategory[3].listAvailableStatus[2],
        progress = 7,
        limitProgress = 7
    ),
    CatalogObject(
        id = "5",
        category = mockCategory[4],
        title = "Ведьмак 3",
        rating = 9.4f,
        ratingsCount = 4100,
        reviewsCount = 1100,
        status = mockCategory[4].listAvailableStatus[0],
        progress = 0,
        limitProgress = 9
    ),
    CatalogObject(
        id = "6",
        category = mockCategory[0],
        title = "Ведьмак. Последнее желание",
        rating = 8.9f,
        ratingsCount = 1200,
        reviewsCount = 280,
        status = mockCategory[0].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "7",
        category = mockCategory[0],
        title = "Вино из одуванчиков",
        rating = 8.7f,
        ratingsCount = 950,
        reviewsCount = 190,
        status = mockCategory[0].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "8",
        category = mockCategory[5],
        title = "Геральт из Ривии",
        rating = null,
        ratingsCount = 0,
        reviewsCount = 0
    ),
    CatalogObject(
        id = "9",
        category = mockCategory[0],
        title = "Гордость и предубеждение",
        rating = 8.8f,
        ratingsCount = 1100,
        reviewsCount = 240,
        status = mockCategory[0].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "10",
        category = mockCategory[0],
        title = "Дюна",
        rating = 8.9f,
        ratingsCount = 1600,
        reviewsCount = 380,
        status = mockCategory[0].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "11",
        category = mockCategory[1],
        title = "Дюна",
        rating = 8.3f,
        ratingsCount = 1800,
        reviewsCount = 420,
        status = mockCategory[1].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "12",
        category = mockCategory[1],
        title = "Интерстеллар",
        rating = 9.2f,
        ratingsCount = 3900,
        reviewsCount = 920,
        status = mockCategory[1].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "13",
        category = mockCategory[3],
        title = "Магическая битва",
        rating = 8.6f,
        ratingsCount = 2100,
        reviewsCount = 480,
        status = mockCategory[3].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    ),
    CatalogObject(
        id = "14",
        category = mockCategory[2],
        title = "Магическая битва",
        rating = 8.5f,
        ratingsCount = 1300,
        reviewsCount = 290,
        status = mockCategory[2].listAvailableStatus[0],
        progress = 0,
        limitProgress = 100
    )
)
