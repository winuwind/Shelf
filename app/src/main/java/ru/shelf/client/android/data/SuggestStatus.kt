package ru.shelf.client.android.data

import androidx.compose.ui.graphics.Color

enum class ApplicationStatus(val title: String, val bgColor: Color, val textColor: Color) {
    MODERATION("На модерации", Color(0xFFFFE8DA), Color(0xFFA6541F)),
    APPROVED("Одобрена", Color(0xFFEAF5EC), Color(0xFF286542)),
    REJECTED("Отклонена", Color(0xFFFFF0EE), Color(0xFFB43D32))
}
