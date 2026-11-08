package ru.shelf.client.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph() {
    val navController = rememberNavController();

    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route){

        }
    }
}