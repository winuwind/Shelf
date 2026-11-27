package ru.shelf.client.android.ui.screens.account

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.data.*
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun SecuritySettingsScreen(
    email: String = "anna@example.com", //вынести в моки
    onBackClick: () -> Unit = {},
    onChangeEmailClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onEndSessionsClick: () -> Unit = {},
    onEndSession: (UserSession) -> Unit = {},
    onBlockedUsersClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.
        fillMaxSize().
        background(Color(0xFFFFF9F5)).
        verticalScroll(rememberScrollState()).
        padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
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
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Безопасность",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF352A26)
            )
        }

        Text(
            text = "Безопасность",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB64D27)
        )

        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(14.dp)
                ) {
            SecurityCard(
                "Пароль",
                "••••••••••",
                "Изменить пароль",
                R.drawable.settings_password_security,
                onChangePasswordClick
            )
            HorizontalDivider(color = Color(0xFFF0E6DF))
            SecurityCard(
                "Почта для входа",
                email,
                "Изменить почту",
                R.drawable.settings_email_security,
                onChangeEmailClick
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Заблокированные пользователи",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB64D27)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(14.dp)
        ) {
            BlockedUsers(
                "Управление списком блокировок",
                R.drawable.settings_blocked_users,
                onBlockedUsersClick
            )
        }

        val sessions = mockSessions

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Активные сессии",
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB64D27)
            )

            Text(
                text = sessions.size.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFB64D27)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(14.dp)
        ) {
            sessions.forEachIndexed { index, session ->
                SessionRow(
                    session = session,
                    onEndSessionClick = { onEndSession(session) }
                )

                if (index != sessions.lastIndex) {
                    HorizontalDivider(
                        color = Color(0xFFF0E6DF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onEndSessionsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Завершить все сессии",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SecurityCard(title: String, value: String, action: String,  icon: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = title,
                color = Color(0xFF352A26),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .weight(1f),
                color = Color(0xFF9C8173),
                fontSize = 14.sp
            )
            Text(
                text = action,
                modifier = Modifier
                    .clickable(onClick = onClick),
                color = Color(0xFFB64D27),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}
@Composable
private fun SessionRow(session: UserSession, onEndSessionClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    getDeviceIcon(session.deviceType)
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = session.deviceName,
                        color = Color(0xFF352A26),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (session.isCurrent) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Текущее",
                            color = Color(0xFFB64D27),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = session.browser,
                    color = Color(0xFF9C8173),
                    fontSize = 12.sp
                )
            }

            if (!session.isCurrent) {
                Text(
                    text = "Завершить",
                    modifier = Modifier
                        .clickable(onClick = onEndSessionClick)
                        .padding(start = 8.dp),
                    color = Color(0xFFB64D27),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Text(
            text = session.date,
            modifier = Modifier.padding(
                start = 44.dp,
                top = 4.dp
            ),
            color = Color(0xFF9C8173),
            fontSize = 12.sp
        )
    }
}
@Composable
private fun BlockedUsers(title: String, icon: Int, onBlockedClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onBlockedClick)
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Color(0xFF352A26),
            fontSize = 14.sp
        )

        Icon(
            painter = painterResource(R.drawable.right_arrow),
            contentDescription = "Перейти к заблокированным пользователям",
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )
    }
}
@DrawableRes
private fun getDeviceIcon(type: DeviceType): Int {
    return when (type) {
        DeviceType.COMPUTER ->
            R.drawable.settings_device_type_computer

        DeviceType.PHONE ->
            R.drawable.settings_device_type_phone

        DeviceType.TABLET ->
            R.drawable.settings_device_type_tablet

        DeviceType.UNKNOWN ->
            R.drawable.settings_device_type_unknown
    }
}

@Preview(showBackground = true)
@Composable
fun SecuritySettingsScreenPreview() {
    ShelfTheme {
        SecuritySettingsScreen()
    }
}