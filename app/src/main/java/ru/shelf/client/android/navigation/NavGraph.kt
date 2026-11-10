package ru.shelf.client.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.shelf.client.android.data.mockUserMe
import ru.shelf.client.android.ui.screens.account.SignInScreen
import ru.shelf.client.android.ui.screens.profile.OtherProfileScreen
import ru.shelf.client.android.ui.screens.registration.EmailSentErrorScreen
import ru.shelf.client.android.ui.screens.registration.EmailSentSuccessScreen
import ru.shelf.client.android.ui.screens.registration.EmailVerifyErrorScreen
import ru.shelf.client.android.ui.screens.registration.EmailVerifySuccessScreen
import ru.shelf.client.android.ui.screens.registration.RegistrationScreen

@Composable
fun AppNavGraph(
    initialIsAuthorized: Boolean = false
) {
    val navController = rememberNavController()

    var isAuthorized by remember { mutableStateOf(initialIsAuthorized) }

    var pendingTargetRoute by remember { mutableStateOf<String?>(null) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.Catalog.route

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = currentRoute,
                pendingTargetRoute = pendingTargetRoute,
                onTabSelected = { item ->
                    if (item.requiresAuth && !isAuthorized) {
                        pendingTargetRoute = item.route
                        navController.navigate(Routes.SignIn.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        pendingTargetRoute = null
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFFFF9F5)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Catalog.route
            ) {
                // 1. Каталог
                composable(Routes.Catalog.route) {
                    CatalogPlaceholderScreen()
                }

                // 2. Поиск
                composable(Routes.Search.route) {
                    SearchPlaceholderScreen()
                }

                // 3. Сообщения (Требует авторизации)
                composable(Routes.Messages.route) {
                    MessagesPlaceholderScreen()
                }

                // 4. Профиль (Требует авторизации)
                composable(Routes.Profile.route) {
                    OtherProfileScreen(
                        user = mockUserMe,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // 5. Вход в систему
                composable(Routes.SignIn.route) {
                    SignInScreen(
                        onSignInSuccess = {
                            isAuthorized = true
                            val target = pendingTargetRoute ?: Routes.Profile.route
                            pendingTargetRoute = null
                            navController.navigate(target) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSignUp = {
                            navController.navigate(Routes.Registration.route)
                        }
                    )
                }

                // 6. Экран регистрации
                composable(Routes.Registration.route) {
                    RegistrationScreen(
                        onRegisterSuccess = {
                            navController.navigate(Routes.EmailSentSuccess.route)
                        },
                        onRegisterError = {
                            navController.navigate(Routes.EmailSentError.route)
                        },
                        onNavigateToSignIn = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 7. Подтверждение отправки письма (Успех)
                composable(Routes.EmailSentSuccess.route) {
                    EmailSentSuccessScreen(
                        onCloseClick = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 8. Ошибка отправки письма
                composable(Routes.EmailSentError.route) {
                    EmailSentErrorScreen(
                        onOkClick = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // 9. Переход по ссылке (Успех)
                composable(Routes.EmailVerifySuccess.route) {
                    EmailVerifySuccessScreen(
                        onNavigateToSignIn = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 10. Переход по ссылке (Неудача)
                composable(Routes.EmailVerifyError.route) {
                    EmailVerifyErrorScreen(
                        onResendEmailClick = {
                            navController.navigate(Routes.Registration.route) {
                                popUpTo(Routes.Registration.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

// --- Временные заглушки экранов ---

@Composable
fun CatalogPlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Экран Каталога",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26)
        )
    }
}

@Composable
fun SearchPlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Экран Поиска",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26)
        )
    }
}

@Composable
fun MessagesPlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Экран Сообщений",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF352A26)
        )
    }
}
