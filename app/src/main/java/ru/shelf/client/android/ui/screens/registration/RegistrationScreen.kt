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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme
import ru.shelf.client.android.ui.viewModels.RegistrationViewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {},
    onRegisterError: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    RegistrationContent(
        email = uiState.email,
        username = uiState.username,
        password = uiState.password,
        confirmPassword = uiState.confirmPassword,
        isPasswordVisible = uiState.isPasswordVisible,
        isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
        errorMessage = uiState.errorMessage,
        onEmailChange = { viewModel.onEmailChange(it) },
        onUsernameChange = { viewModel.onUsernameChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onConfirmPasswordChange = { viewModel.onConfirmPasswordChange(it) },
        onPasswordVisibilityToggle = { viewModel.togglePasswordVisibility() },
        onConfirmPasswordVisibilityToggle = { viewModel.toggleConfirmPasswordVisibility() },
        onRegisterClick = { viewModel.register(onRegisterSuccess, onRegisterError) },
        onNavigateToSignIn = onNavigateToSignIn
    )
}

@Composable
fun RegistrationContent(
    email: String = "",
    username: String = "",
    password: String = "",
    confirmPassword: String = "",
    isPasswordVisible: Boolean = false,
    isConfirmPasswordVisible: Boolean = false,
    errorMessage: String? = null,
    onEmailChange: (String) -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onPasswordVisibilityToggle: () -> Unit = {},
    onConfirmPasswordVisibilityToggle: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}
) {
    var internalEmail by remember(email) { mutableStateOf(email) }
    var internalUsername by remember(username) { mutableStateOf(username) }
    var internalPassword by remember(password) { mutableStateOf(password) }
    var internalConfirmPassword by remember(confirmPassword) { mutableStateOf(confirmPassword) }
    var internalIsPasswordVisible by remember(isPasswordVisible) { mutableStateOf(isPasswordVisible) }
    var internalIsConfirmPasswordVisible by remember(isConfirmPasswordVisible) { mutableStateOf(isConfirmPasswordVisible) }

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
                value = internalEmail,
                onValueChange = {
                    internalEmail = it
                    onEmailChange(it)
                },
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
                value = internalUsername,
                onValueChange = {
                    internalUsername = it
                    onUsernameChange(it)
                },
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
                value = internalPassword,
                onValueChange = {
                    internalPassword = it
                    onPasswordChange(it)
                },
                placeholder = { Text("Введите пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = {
                        internalIsPasswordVisible = !internalIsPasswordVisible
                        onPasswordVisibilityToggle()
                    }) {
                        Icon(
                            painter = painterResource(if (internalIsPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (internalIsPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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
                value = internalConfirmPassword,
                onValueChange = {
                    internalConfirmPassword = it
                    onConfirmPasswordChange(it)
                },
                placeholder = { Text("Повторите пароль", color = Color(0xFFC49880)) },
                textStyle = inputTextStyle,
                trailingIcon = {
                    IconButton(onClick = {
                        internalIsConfirmPasswordVisible = !internalIsConfirmPasswordVisible
                        onConfirmPasswordVisibilityToggle()
                    }) {
                        Icon(
                            painter = painterResource(if (internalIsConfirmPasswordVisible) R.drawable.show_password else R.drawable.hide_password_or_message),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                visualTransformation = if (internalIsConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '●'),
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

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFB44736)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onRegisterClick,
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
