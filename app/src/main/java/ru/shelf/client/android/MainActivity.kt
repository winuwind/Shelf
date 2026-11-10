package ru.shelf.client.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.shelf.client.android.navigation.AppNavGraph
import ru.shelf.client.android.ui.theme.ShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShelfTheme {
                AppNavGraph(initialIsAuthorized = false)
            }
        }
    }
}
