package ru.shelf.client.android.ui.screens.registration

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit = {},
    onRegisterError: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val inputTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        color = Color(0xFF3D2B1F)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3EC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Регистрация",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D2B1F),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Добро пожаловать!",
            fontSize = 16.sp,
            color = Color(0xFF9C6B52),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                placeholder = { Text("Введите email", color = Color(0xFFC49880)) },
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

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("Имя пользователя ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Введите имя пользователя", color = Color(0xFFC49880)) },
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

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = buildAnnotatedString {
                    append("Пароль ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Введите пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(if (isPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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
                    append("Подтверждение пароля ")
                    withStyle(SpanStyle(color = Color(0xFFB64D27))) { append("*") }
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Повторите пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(if (isConfirmPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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
            onClick = onRegisterSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Зарегистрироваться",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Уже есть аккаунт? ",
                fontSize = 14.sp,
                color = Color(0xFF9C6B52)
            )
            Box(
                modifier = Modifier.clickable { onNavigateToSignIn() }
            ) {
                Text(
                    text = "Войти",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA6541F),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    ShelfTheme {
        RegistrationScreen()
    }
}
