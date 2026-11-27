package ru.shelf.client.android.data

import androidx.compose.runtime.mutableStateListOf
import ru.shelf.client.android.ui.screens.objects.ApplicationDetail

val mockApplicationPiranesi = ApplicationDetail(
    id = "1042",
    title = "Пиранези",
    sendDate = "05.11.2026, 10:24",
    proposedCategory = "Книги",
    description = "Пиранези живёт в бесконечном Доме, где залы полны статуй, а по лестницам поднимаются океанские приливы. Роман Сюзанны Кларк о памяти и поиске истины.",
    status = ApplicationStatus.MODERATION,
    fileName = "piranesi.jpg"
)

val mockApplicationDune = ApplicationDetail(
    id = "1031",
    title = "Дюна",
    sendDate = "01.11.2026, 09:15",
    proposedCategory = "Книги",
    description = "Научно-фантастический роман Фрэнка Герберта.",
    status = ApplicationStatus.REJECTED,
    reviewDate = "02.11.2026",
    rejectionReason = "Объект уже есть в каталоге: «Дюна», раздел «Книги». Пожалуйста, используйте существующую карточку."
)

val mockApplicationGardens = ApplicationDetail(
    id = "1038",
    title = "Сады Луны",
    sendDate = "03.11.2026, 12:40",
    proposedCategory = "Манга",
    description = "Первый роман фэнтезийного цикла Стивена Эриксона «Малазанская книга павших».",
    status = ApplicationStatus.APPROVED,
    resultDescription = "Объект добавлен в каталог. Фактический раздел: Книги.",
    reviewDate = "04.11.2026",
    objectId = "2041"
)

val mockApplicationsList = mutableStateListOf(
    mockApplicationPiranesi,
    mockApplicationGardens,
    mockApplicationDune
)
