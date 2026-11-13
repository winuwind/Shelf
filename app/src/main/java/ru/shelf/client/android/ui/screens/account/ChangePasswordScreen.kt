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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun ChangePasswordScreen(
    onChangePasswordSuccess: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmNewPasswordVisible by remember { mutableStateOf(false) }

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
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onNavigateToSignIn() }
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
                text = "Назад к входу",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFA6541F)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Изменение пароля",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D2B1F),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Придумайте новый пароль",
            fontSize = 16.sp,
            color = Color(0xFF9C6B52),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("Новый пароль ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("Введите новый пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                        Icon(
                            painter = painterResource(if (isNewPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("Подтверждение нового пароля ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                placeholder = { Text("Повторите новый пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = { isConfirmNewPasswordVisible = !isConfirmNewPasswordVisible }) {
                        Icon(
                            painter = painterResource(if (isConfirmNewPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (isConfirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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
            onClick = onChangePasswordSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Изменить пароль",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ShelfTheme {
        ChangePasswordScreen()
    }
}
