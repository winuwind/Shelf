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
import ru.shelf.client.android.ui.screens.account.ChangePasswordScreen
import ru.shelf.client.android.ui.screens.account.RestoreAccessScreen
import ru.shelf.client.android.ui.screens.account.SignInScreen
import ru.shelf.client.android.ui.screens.help.FaqScreen
import ru.shelf.client.android.ui.screens.profile.ProfileScreen
import ru.shelf.client.android.ui.screens.registration.EmailSentErrorScreen
import ru.shelf.client.android.ui.screens.registration.EmailSentSuccessScreen
import ru.shelf.client.android.ui.screens.registration.EmailVerifyErrorScreen
import ru.shelf.client.android.ui.screens.registration.EmailVerifySuccessScreen
import ru.shelf.client.android.ui.screens.registration.RegistrationScreen
import ru.shelf.client.android.ui.screens.search.SearchScreen
import ru.shelf.client.android.ui.screens.users.FriendsScreen

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
                composable(Routes.Catalog.route) {
                    ru.shelf.client.android.ui.screens.objects.CatalogScreen(
                        previousScreenName = "Лента",
                        onBackClick = { navController.popBackStack() },
                        onNavigateToObject = { objectId ->
                            navController.navigate(Routes.ObjectDetail.createRoute(objectId))
                        }
                    )
                }

                composable(Routes.ObjectDetail.route) { backStackEntry ->
                    val objectId = backStackEntry.arguments?.getString("objectId") ?: "1"
                    ru.shelf.client.android.ui.screens.objects.ObjectDetailScreen(
                        objectId = objectId,
                        isAuthorized = isAuthorized,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Routes.Search.route) {
                    SearchScreen(
                        onNavigateToUser = { userId ->
                            navController.navigate(Routes.Profile.route)
                        },
                        onNavigateToCommunity = { communityId ->
                            navController.navigate(Routes.Communities.route)
                        },
                        onNavigateToCatalog = {
                            navController.navigate(Routes.Catalog.route)
                        }
                    )
                }

                composable(Routes.Messages.route) {
                    MessagesPlaceholderScreen()
                }

                composable(Routes.Profile.route) {
                    ProfileScreen(
                        user = mockUserMe,
                        onBackClick = { navController.popBackStack() },
                        onOpenFriendsClick = {
                            navController.navigate(Routes.Friends.route)
                        },
                        onOpenCommunitiesClick = {
                            navController.navigate(Routes.Communities.route)
                        },
                        onOpenCollectionsClick = {
                            navController.navigate(Routes.Collections.route)
                        }
                    )
                }

                composable(Routes.Friends.route) {
                    FriendsScreen(
                        user = mockUserMe,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Routes.Communities.route) {
                    ru.shelf.client.android.ui.screens.communities.CommunitiesScreen(
                        user = mockUserMe,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Routes.Collections.route) {
                    ru.shelf.client.android.ui.screens.collections.CollectionsScreen(
                        user = mockUserMe,
                        onBackClick = { navController.popBackStack() }
                    )
                }

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
                        },
                        onNavigateToRestoreAccess = {
                            navController.navigate(Routes.RestoreAccess.route)
                        },
                        onNavigateToFaq = {
                            navController.navigate(Routes.Faq.route)
                        }
                    )
                }

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

                composable(Routes.RestoreAccess.route) {
                    RestoreAccessScreen(
                        onSendRestoreEmailClick = {
                            navController.navigate(Routes.EmailSentSuccess.route)
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.ChangePassword.route) {
                    ChangePasswordScreen(
                        onChangePasswordSuccess = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        },
                        onNavigateToSignIn = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.Faq.route) {
                    FaqScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToRestoreAccess = {
                            navController.navigate(Routes.RestoreAccess.route)
                        },
                        onNavigateToSupport = {
                            navController.navigate(Routes.ChangePassword.route) // TODO: изменить навигацию на другой экран
                        }
                    )
                }

                composable(Routes.EmailSentSuccess.route) {
                    EmailSentSuccessScreen(
                        time = remember { mutableStateOf("01:23:45") },
                        onCloseClick = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.EmailSentError.route) {
                    EmailSentErrorScreen(
                        onOkClick = {
                            navController.navigate(Routes.SignIn.route) {
                                 popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Routes.EmailVerifySuccess.route) {
                    EmailVerifySuccessScreen(
                        onNavigateToSignIn = {
                            navController.navigate(Routes.SignIn.route) {
                                popUpTo(Routes.SignIn.route) { inclusive = true }
                            }
                        }
                    )
                }

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
