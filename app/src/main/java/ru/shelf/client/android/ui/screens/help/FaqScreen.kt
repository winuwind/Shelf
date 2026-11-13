package ru.shelf.client.android.ui.screens.help

import android.text.Html
import android.text.style.URLSpan
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shelf.client.android.R
import ru.shelf.client.android.ui.theme.ShelfTheme

data class FaqItemData(
    val title: String,
    val answer: String
)

fun parseHtmlToAnnotatedString(
    htmlText: String,
    onNavigateToRestoreAccess: () -> Unit,
    onNavigateToSupport: () -> Unit
): AnnotatedString {
    val spanned = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    return buildAnnotatedString {
        append(spanned.toString())
        val urlSpans = spanned.getSpans(0, spanned.length, URLSpan::class.java)
        for (span in urlSpans) {
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            val url = span.url ?: ""

            val linkAnnotation = LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color(0xFFA6541F),
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    ),
                    hoveredStyle = SpanStyle(
                        color = Color(0xFFD36F30),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ),
                    pressedStyle = SpanStyle(
                        color = Color(0xFF6B310E),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ),
                    focusedStyle = SpanStyle(
                        color = Color(0xFFD36F30),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { link ->
                    when ((link as? LinkAnnotation.Url)?.url) {
                        "restore_access" -> onNavigateToRestoreAccess()
                        "support" -> onNavigateToSupport()
                    }
                }
            )

            addLink(
                url = linkAnnotation,
                start = start,
                end = end
            )
        }
    }
}

@Composable
fun FaqScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToRestoreAccess: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    val questions = stringArrayResource(id = R.array.faq_questions)
    val answers = stringArrayResource(id = R.array.faq_answers)

    val faqList = remember(questions, answers) {
        questions.zip(answers) { question, answer ->
            FaqItemData(title = question, answer = answer)
        }
    }

    val expandedIndices = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3EC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp)
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
                text = "Назад ко входу",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFA6541F)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "FAQ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D2B1F),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            faqList.forEachIndexed { index, item ->
                val isExpanded = expandedIndices.contains(index)

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F4)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5C4A8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isExpanded) {
                                        expandedIndices.remove(index)
                                    } else {
                                        expandedIndices.add(index)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA6541F),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = if (isExpanded) R.drawable.up_arrow else R.drawable.down_arrow),
                                contentDescription = null,
                                tint = Color(0xFFA6541F),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))

                            val annotatedAnswer = remember(item.answer) {
                                parseHtmlToAnnotatedString(
                                    htmlText = item.answer,
                                    onNavigateToRestoreAccess = onNavigateToRestoreAccess,
                                    onNavigateToSupport = onNavigateToSupport
                                )
                            }

                            Text(
                                text = annotatedAnswer,
                                fontSize = 15.sp,
                                color = Color(0xFF3D2B1F),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FaqScreenPreview() {
    var lastClickedAction by remember { mutableStateOf<String?>(null) }

    ShelfTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FaqScreen(
                onNavigateBack = {
                    lastClickedAction = "Нажата кнопка «Назад ко входу»"
                },
                onNavigateToRestoreAccess = {
                    lastClickedAction = "Клик по ссылке: Восстановить доступ"
                },
                onNavigateToSupport = {
                    lastClickedAction = "Клик по ссылке: Служба поддержки"
                }
            )

            if (lastClickedAction != null) {
                Surface(
                    color = Color(0xFFA6541F),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = lastClickedAction!!,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}
