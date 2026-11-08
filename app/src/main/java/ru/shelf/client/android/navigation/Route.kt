package ru.shelf.client.android.navigation

sealed class Routes (val route: String) {
    object Home: Routes("home")

}