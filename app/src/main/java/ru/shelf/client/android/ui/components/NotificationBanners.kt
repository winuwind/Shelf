package ru.shelf.client.android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme
import kotlin.time.Duration.Companion.seconds

enum class BannerErrorType(val message: String) {
    SEND_REQUEST_FAILED("Не удалось отправить заявку. Попробуйте позже"),
    ACCEPT_REQUEST_FAILED("Не удалось принять заявку. Попробуйте позже"),
    CANCEL_REQUEST_FAILED("Не удалось отменить заявку"),
    REQUEST_EXPIRED("Заявка больше не актуальна"),
    DECLINE_REQUEST_FAILED("Не удалось отклонить заявку. Попробуйте позже"),
    REMOVE_FRIEND_FAILED("Не удалось удалить пользователя из друзей"),
    MESSAGING_BLOCKED("Обмен сообщениями недоступен из-за блокировки")
}

enum class BannerType{
    Success,
    Failed,
    Blocked,
}

@Composable
fun NotificationBanner(
    message: String,
    typeError: BannerType = BannerType.Failed
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (typeError == BannerType.Blocked) Color(0xFFFFE8DA) else Color(0xFFFFF8F4)),
        border = BorderStroke(1.dp, Color(0xFFEEDFD6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var color = Color(0xFFA54E32);
            if (typeError == BannerType.Success) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    tint = Color(0xFFC87A58),
                    modifier = Modifier.size(18.dp)
                )
                color = Color(0xFF352A26)
            }
            else if (typeError == BannerType.Blocked){
                Icon(
                    painter = painterResource(id = R.drawable.collection_access_settings),
                    contentDescription = null,
                    tint = Color(0xFFC87A58),
                    modifier = Modifier.size(18.dp)
                )
                color = Color(0xFFA54E32)
            }

            Text(
                text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = color,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun FloatingNotificationBannerHost(
    message: String?,
    typeError: BannerType = BannerType.Failed,
    onDismiss: () -> Unit = {}
) {
    var visibleMessage by remember(message) { mutableStateOf(message) }

    LaunchedEffect(visibleMessage) {
        if (visibleMessage != null) {
            delay(5.seconds)
            visibleMessage = null
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visibleMessage != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        visibleMessage?.let { msg ->
            NotificationBanner(
                message = msg,
                typeError = typeError
            )
        }
    }
}

@Preview(name = "1. Ошибка отправки заявки", showBackground = true)
@Composable
fun Preview_Banner_SendFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.SEND_REQUEST_FAILED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "2. Ошибка принятия заявки", showBackground = true)
@Composable
fun Preview_Banner_AcceptFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.ACCEPT_REQUEST_FAILED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "3. Ошибка отмены заявки", showBackground = true)
@Composable
fun Preview_Banner_CancelFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.CANCEL_REQUEST_FAILED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "4. Заявка больше не актуальна", showBackground = true)
@Composable
fun Preview_Banner_Expired() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.REQUEST_EXPIRED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "5. Ошибка удаления из друзей", showBackground = true)
@Composable
fun Preview_Banner_RemoveFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.REMOVE_FRIEND_FAILED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "6. Ошибка отклонения заявки", showBackground = true)
@Composable
fun Preview_Banner_DeclineFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.DECLINE_REQUEST_FAILED.message, typeError = BannerType.Failed)
    }
}

@Preview(name = "7. Ошибка отправления сообщения", showBackground = true)
@Composable
fun Preview_Banner_SendMessageFailed() {
    ShelfTheme {
        NotificationBanner(message = BannerErrorType.MESSAGING_BLOCKED.message, typeError = BannerType.Blocked)
    }
}
