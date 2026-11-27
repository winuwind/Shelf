package ru.shelf.client.android.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import androidx.compose.ui.res.painterResource
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun AccountSettingsScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 22.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE8DA))
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(R.drawable.back_arrow_to_certain_screen),
                    null,
                    tint = Color.Unspecified
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Настройки учетной записи",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF352A26)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF4D5C5))
            )

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.BottomStart)
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE8DA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.standard_avatar),
                    contentDescription = "Аватар профиля",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(27.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE8DA))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.settings_edit_avatar),
                        contentDescription = "Изменить аватар",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        SettingsRow(
            "Профиль",
            "Изменение личной информации и настроек приватности",
            R.drawable.profile_settings,
            onProfileClick
        )
        SettingsRow(
            "Безопасность",
            "Пароль, электронная почта, заблокированные пользователи, активные сесии",
            R.drawable.seetings_security,
            onSecurityClick)
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFE8DA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(icon),
                null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF352A26)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF9C8173),
                lineHeight = 16.sp
            )
        }
        Icon(
            painterResource(R.drawable.right_arrow),
            null,
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountSettingsScreenPreview() {
    ShelfTheme {
        AccountSettingsScreen()
    }
}