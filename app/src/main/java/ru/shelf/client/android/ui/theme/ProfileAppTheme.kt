package ru.shelf.client.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ProfileAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = ScreenBgColor,
            surface = Color.White,
            onPrimary = TextPrimary,
            onSurface = TextPrimary
        ),
        content = content
    )
}
