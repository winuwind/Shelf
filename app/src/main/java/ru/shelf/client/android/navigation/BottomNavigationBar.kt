package ru.shelf.client.android.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R

data class BottomNavItem(
    val title: String,
    val route: String,
    @DrawableRes val normalIconRes: Int,
    @DrawableRes val activeIconRes: Int,
    val requiresAuth: Boolean = false
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Каталог",
        route = Routes.Catalog.route,
        normalIconRes = R.drawable.feed_main_panel,
        activeIconRes = R.drawable.highlighted_feed_main_panel,
        requiresAuth = false
    ),
    BottomNavItem(
        title = "Поиск",
        route = Routes.Search.route,
        normalIconRes = R.drawable.feed_search,
        activeIconRes = R.drawable.highlighted_search_main_panel,
        requiresAuth = false
    ),
    BottomNavItem(
        title = "Сообщения",
        route = Routes.Messages.route,
        normalIconRes = R.drawable.messages_main_panel,
        activeIconRes = R.drawable.highlighted_messages_main_panel,
        requiresAuth = true
    ),
    BottomNavItem(
        title = "Профиль",
        route = Routes.Profile.route,
        normalIconRes = R.drawable.profile_main_panel,
        activeIconRes = R.drawable.highlighted_profile_main_panel,
        requiresAuth = true
    )
)

val ActiveTabColor = Color(0xFFA6541F)
val InactiveTabColor = Color(0xFF8A776C)
val NavBorderColor = Color(0xFFEEDFD6)

@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    pendingTargetRoute: String? = null,
    onTabSelected: (BottomNavItem) -> Unit
) {
    val activeRoute = when (currentRoute) {
        Routes.SignIn.route,
        Routes.SignUp.route,
        Routes.Registration.route,
        Routes.RestoreAccess.route,
        Routes.ChangePassword.route,
        Routes.Faq.route,
        Routes.EmailSentSuccess.route,
        Routes.EmailSentError.route,
        Routes.EmailVerifySuccess.route,
        Routes.EmailVerifyError.route -> pendingTargetRoute ?: Routes.Profile.route

        else -> currentRoute ?: Routes.Catalog.route
    }

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = NavBorderColor)
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = activeRoute == item.route
            val iconRes = if (isSelected) item.activeIconRes else item.normalIconRes

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = item.title,
                        tint = Color.Unspecified
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        color = if (isSelected) ActiveTabColor else InactiveTabColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ActiveTabColor,
                    selectedTextColor = ActiveTabColor,
                    unselectedIconColor = InactiveTabColor,
                    unselectedTextColor = InactiveTabColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview()
@Composable
fun PreviewNavBar(){
    AppBottomNavigationBar("") { }
}