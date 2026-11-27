package ru.shelf.client.android.ui.screens.profile;

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun ProfileSettingsScreen(
    name: String = "Пользователь",
    bio: String = "",
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onExit: () -> Unit = {}
) {
    var displayName by remember { mutableStateOf(name) }
    var about by remember { mutableStateOf(bio) }
    var profilePrivacy by remember { mutableStateOf("Все") }
    var friendsPrivacy by remember { mutableStateOf("Все") }
    var communitiesPrivacy by remember { mutableStateOf("Все") }
    var descriptionPrivacy by remember { mutableStateOf("Все") }

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
                .padding(top = 12.dp, bottom = 20.dp)
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
            Spacer(modifier = Modifier.width(12.dp));
            Text(
                text = "Профиль",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF352A26)
            )
        }

        Text(
            text = "Баннер",
            color = Color(0xFF352A26),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF4D5C5)),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                painterResource(R.drawable.settings_edit_banner),
                null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(10.dp)
            )
        }

        Text(
            text = "Информация о вас",
            color = Color(0xFF352A26),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )

        OutlinedTextField(
            displayName,
            { displayName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            placeholder = {
                Text(
                    text = "Имя пользователя"
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = profileFieldColors()
        )

        OutlinedTextField(
            about,
            { about = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = {
                Text(
                    text = "Расскажите о себе"
                )
            },
            minLines = 2,
            shape = RoundedCornerShape(12.dp),
            colors = profileFieldColors()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Приватность",
                color = Color(0xFFB64D27),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            PrivacyChoice("Профиль целиком", profilePrivacy)
            {
                profilePrivacy = it
            }
            HorizontalDivider(color = Color(0xFFF0E6DF))
            PrivacyChoice("Описание пользователя", descriptionPrivacy)
            {
                descriptionPrivacy = it
            }
            HorizontalDivider(color = Color(0xFFF0E6DF))
            PrivacyChoice("Друзья", friendsPrivacy) {
                friendsPrivacy = it
            }
            HorizontalDivider(color = Color(0xFFF0E6DF))
            PrivacyChoice("Сообщества", communitiesPrivacy)
            {
                communitiesPrivacy = it
            }
        }

        Button(
            onClick = {
                onSaveClick(displayName, about, profilePrivacy, friendsPrivacy) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB64D27)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Сохранить",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 20.dp)
                .clickable { onExit()},
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.settings_ending_sessions),
                contentDescription = "Выход",
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Выход",
                color = Color(0xFFB64D27),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun profileFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFF5C4A8),
        unfocusedBorderColor = Color(0xFFF5C4A8),
        focusedContainerColor = Color(0xFFFFF8F4),
        unfocusedContainerColor = Color(0xFFFFF8F4)
    )

@Composable
private fun PrivacyChoice(label: String, value: String, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp)
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color(0xFF352A26), fontSize = 13.sp
        )
        Text(
            text = value,
            color = Color(0xFFB64D27),
            fontSize = 13.sp
        )
        Icon(
            painterResource(if (expanded) R.drawable.up_arrow else R.drawable.down_arrow),
            null,
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
        DropdownMenu(
            expanded, { expanded = false }
        ) {
            listOf("Все", "Только друзья", "Только я").forEach {
                option -> DropdownMenuItem(
                text = { Text(option) },
                onClick = { onChange(option);
                    expanded = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingsScreenPreview() {
    ShelfTheme {
        ProfileSettingsScreen()
    }
}