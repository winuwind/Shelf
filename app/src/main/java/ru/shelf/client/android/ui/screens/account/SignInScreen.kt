package ru.shelf.client.android.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToRestoreAccess: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {}
) {
    var emailOrName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }
    var isPasswordVisible by remember { mutableStateOf(false) }

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
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Вход",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D2B1F),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "С возвращением :)",
            fontSize = 16.sp,
            color = Color(0xFF9C6B52),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Email или имя",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = emailOrName,
                onValueChange = { emailOrName = it },
                placeholder = { Text("Введите email или имя", color = Color(0xFFC49880)) },
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
                text = "Пароль",
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { rememberMe = !rememberMe },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFA6541F),
                    uncheckedColor = Color(0xFFF5C4A8),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Запомнить меня",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D2B1F)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onNavigateToFaq,
                modifier = Modifier
                    .width(68.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFFF8F4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5C4A8)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "FAQ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3D2B1F)
                )
            }

            Button(
                onClick = onSignInSuccess,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Войти",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Потерял доступ к аккаунту? ",
                    fontSize = 14.sp,
                    color = Color(0xFF9C6B52)
                )
                Box(modifier = Modifier.clickable { onNavigateToRestoreAccess() }) {
                    Text(
                        text = "Восстановить",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA6541F),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Новый аккаунт? ",
                    fontSize = 14.sp,
                    color = Color(0xFF9C6B52)
                )
                Box(modifier = Modifier.clickable { onNavigateToSignUp() }) {
                    Text(
                        text = "Зарегистрироваться",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA6541F),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    ShelfTheme {
        SignInScreen()
    }
}
