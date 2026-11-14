package ru.shelf.client.android.ui.screens.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

@Composable
fun EmailSentSuccessScreen(
    time: MutableState<String>,
    onCloseClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3EC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Проверьте почту",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D2B1F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.checking_mail),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "На указанный адрес электронной почты отправлено письмо с подтверждением.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D2B1F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Перейдите по ссылке в письме, чтобы подтвердить адрес.",
                fontSize = 15.sp,
                color = Color(0xFF3D2B1F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ссылка действительна в течение 1 часа.",
                fontSize = 15.sp,
                color = Color(0xFF3D2B1F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFFFF8F4), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFA6541F), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = time.value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D2B1F)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = buildAnnotatedString {
                    append("Если адрес не будет подтверждён в течение 7 дней, учётная запись будет ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("удалена")
                    }
                    append(".")
                },
                fontSize = 15.sp,
                color = Color(0xFF3D2B1F),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB64D27)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Закрыть",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailSentSuccessScreenPreview() {
    ShelfTheme {
        EmailSentSuccessScreen(time = remember {  mutableStateOf("01:23:45") })
    }
}
