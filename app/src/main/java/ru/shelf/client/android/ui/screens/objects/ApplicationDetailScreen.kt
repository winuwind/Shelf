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
import ru.shelf.client.android.data.ApplicationStatus

data class ApplicationDetail(
    val id: String,
    val title: String,
    val sendDate: String,
    val proposedCategory: String,
    val description: String,
    val status: ApplicationStatus,
    val fileName: String? = null,
    val resultDescription: String? = null,
    val reviewDate: String? = null,
    val objectId: String? = null,
    val rejectionReason: String? = null
)

@Composable
fun ApplicationDetailScreen(
    application: ApplicationDetail,
    onBackClick: () -> Unit = {},
    onNavigateToObject: (String) -> Unit = {}
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
                text = "Мои заявки",
                fontSize = 14.sp,
                color = Color(0xFFA54E32),
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Моя заявка",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Заявка № ${application.id}",
                fontSize = 12.sp,
                color = Color(0xFF84736B)
            )

            Box(
                modifier = Modifier
                    .background(application.status.bgColor, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    text = application.status.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = application.status.textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(18.dp))
                .border(1.dp, Color(0xFFEEDFD6), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = application.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26)
                )

                Text(
                    text = "Отправлена ${application.sendDate}",
                    fontSize = 12.sp,
                    color = Color(0xFF84736B)
                )

                Text(
                    text = "Предложенный раздел: ${application.proposedCategory}",
                    fontSize = 14.sp,
                    color = Color(0xFF352A26)
                )

                Text(
                    text = application.description,
                    fontSize = 14.sp,
                    color = Color(0xFF352A26),
                    lineHeight = 20.sp
                )
                if (application.fileName != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Изображение объекта",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF352A26)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFEEDFD6), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp, 92.dp)
                                    .background(Color.LightGray, shape = RoundedCornerShape(6.dp))
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = application.fileName,
                                    fontSize = 14.sp,
                                    color = Color(0xFF352A26)
                                )
                                Text(
                                    text = "Приложено к заявке",
                                    fontSize = 12.sp,
                                    color = Color(0xFF84736B)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Conditional bottom blocks depending on status
        when (application.status) {
            ApplicationStatus.MODERATION -> {
                Text(
                    text = "Заявка ожидает рассмотрения. Объект появится в каталоге после одобрения администратором.",
                    fontSize = 14.sp,
                    color = Color(0xFF84736B),
                    lineHeight = 20.sp
                )
            }
            ApplicationStatus.APPROVED -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFFEEDFD6), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Результат рассмотрения",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF352A26)
                        )
                        Text(
                            text = application.resultDescription ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF352A26),
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "Рассмотрена ${application.reviewDate} Объект № ${application.objectId}",
                            fontSize = 12.sp,
                            color = Color(0xFF84736B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { application.objectId?.let { onNavigateToObject(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Перейти к объекту в каталоге",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
            ApplicationStatus.REJECTED -> {
                Text(
                    text = "Причина отклонения",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF352A26)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF0EE), shape = RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = application.rejectionReason ?: "",
                        fontSize = 14.sp,
                        color = Color(0xFFB43D32),
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Рассмотрена ${application.reviewDate}. Новый объект в каталоге не создан.",
                    fontSize = 12.sp,
                    color = Color(0xFF84736B),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(name = "1. Заявка на модерации", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewApplicationDetailModeration() {
    ApplicationDetailScreen(
        application = ApplicationDetail(
            id = "1042",
            title = "Пиранези",
            sendDate = "05.11.2026, 10:24",
            proposedCategory = "Книги",
            description = "Пиранези живёт в бесконечном Доме...",
            status = ApplicationStatus.MODERATION,
            fileName = "piranesi.jpg"
        )
    )
}

@Preview(name = "2. Заявка одобрена", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewApplicationDetailApproved() {
    ApplicationDetailScreen(
        application = ApplicationDetail(
            id = "1038",
            title = "Сады Луны",
            sendDate = "03.11.2026, 12:40",
            proposedCategory = "Манга",
            description = "Первый роман фэнтезийного цикла Стивена Эриксона...",
            status = ApplicationStatus.APPROVED,
            resultDescription = "Объект добавлен в каталог. Фактический раздел: Книги.",
            reviewDate = "04.11.2026",
            objectId = "2041"
        )
    )
}

@Preview(name = "3. Заявка отклонена", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewApplicationDetailRejected() {
    ApplicationDetailScreen(
        application = ApplicationDetail(
            id = "1031",
            title = "Дюна",
            sendDate = "01.11.2026, 09:15",
            proposedCategory = "Книги",
            description = "Научно-фантастический роман Фрэнка Герберта.",
            status = ApplicationStatus.REJECTED,
            reviewDate = "02.11.2026",
            rejectionReason = "Объект уже есть в каталоге: «Дюна», раздел «Книги». Пожалуйста, используйте существующую карточку."
        )
    )
}