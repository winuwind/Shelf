package ru.shelf.client.android.ui.screens.objects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R

@Composable
fun SuggestObjectSuccessScreen(
    application: ApplicationDetail,
    onBackClick: () -> Unit = {},
    onMyApplicationsClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_button),
                contentDescription = "Назад",
                tint = Color.Unspecified,
                modifier = Modifier.size(38.dp, 36.dp)
            )
            Text(
                text = "Каталог",
                fontSize = 14.sp,
                color = Color(0xFFA54E32),
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Заявка отправлена",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26)
        )

        Spacer(modifier = Modifier.height(20.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEAF5EC), shape = RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(
                text = "Заявка на добавление объекта отправлена на модерацию",
                fontSize = 14.sp,
                color = Color(0xFF286542),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        
        ApplicationCard(
            title = application.title,
            category = application.proposedCategory,
            date = application.sendDate,
            status = application.status,
            onClick = { /* TODO: Open application details */ }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Объекта пока нет в общем каталоге. Результат рассмотрения появится в ваших заявках.",
            fontSize = 14.sp,
            color = Color(0xFF84736B),
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        
        Button(
            onClick = onMyApplicationsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Мои заявки",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

enum class ApplicationStatus(val title: String, val bgColor: Color, val textColor: Color) {
    MODERATION("На модерации", Color(0xFFFFE8DA), Color(0xFFA6541F)),
    APPROVED("Одобрена", Color(0xFFEAF5EC), Color(0xFF286542)),
    REJECTED("Отклонена", Color(0xFFFFF0EE), Color(0xFFB43D32))
}

@Composable
fun ApplicationCard(
    title: String,
    category: String,
    date: String,
    status: ApplicationStatus,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFEEDFD6), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26)
                )
                Text(
                    text = "›",
                    fontSize = 20.sp,
                    color = Color(0xFFA6541F)
                )
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    color = Color(0xFF84736B)
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color(0xFF84736B)
                )
            }

            Box(
                modifier = Modifier
                    .background(status.bgColor, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    text = status.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = status.textColor
                )
            }
        }
    }
}

@Preview(name = "1. Заявка отправлена на модерацию", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewSuggestObjectSuccessScreen() {
    val application = ApplicationDetail(
        id = "1042",
        title = "Пиранези",
        sendDate = "05.11.2026, 10:24",
        proposedCategory = "Книги",
        description = "...",
        status = ApplicationStatus.MODERATION
    )
    SuggestObjectSuccessScreen(application = application)
}