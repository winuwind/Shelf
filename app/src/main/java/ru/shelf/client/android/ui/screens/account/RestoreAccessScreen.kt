package ru.shelf.client.android.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.R

@Composable
fun RestoreAccessScreen(
    onSendRestoreEmailClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    val inputTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF3D2B1F)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3EC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onNavigateBack() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow_to_certain_screen),
                contentDescription = "Назад",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Назад",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFA6541F)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Восстановление доступа",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D2B1F),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Отправьте письмо для восстановления доступа к аккаунту",
            fontSize = 16.sp,
            color = Color(0xFF9C6B52),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("Электронная почта ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Введите адрес электронной почты", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFF5C4A8),
                    unfocusedBorderColor = Color(0xFFF5C4A8),
                    focusedContainerColor = Color(0xFFFFF8F4),
                    unfocusedContainerColor = Color(0xFFFFF8F4),
                    focusedTextColor = Color(0xFF3D2B1F),
                    unfocusedTextColor = Color(0xFF3D2B1F)
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onSendRestoreEmailClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Отправить письмо",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestoreAccessScreenPreview() {
    ShelfTheme {
        RestoreAccessScreen()
    }
}
