package ru.shelf.client.android.ui.screens.objects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.shelf.client.android.data.mockApplicationsList
import ru.shelf.client.android.ui.screens.objects.ApplicationDetail

@Composable
fun MyApplicationsScreen(
    onBackClick: () -> Unit = {},
    onProposeObjectClick: () -> Unit = {},
    onApplicationClick: (ApplicationDetail) -> Unit = {}
) {
    val applications = mockApplicationsList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F5))
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
            text = "Мои заявки",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ваши предложения и результаты рассмотрения.",
            fontSize = 14.sp,
            color = Color(0xFF84736B),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFEEDFD6), RoundedCornerShape(12.dp))
                .clickable { onProposeObjectClick() }
                .padding(horizontal = 14.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Предложить объект",
                fontSize = 14.sp,
                color = Color(0xFF352A26)
            )
            Text(
                text = "›",
                fontSize = 16.sp,
                color = Color(0xFFA6541F)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(applications) { app ->
                ApplicationCard(
                    title = app.title,
                    category = app.proposedCategory,
                    date = app.sendDate,
                    status = app.status,
                    onClick = { onApplicationClick(app) }
                )
            }
        }
    }
}

@Preview(name = "1. Список моих заявок", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewMyApplicationsScreen() {
    MyApplicationsScreen()
}