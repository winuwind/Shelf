package ru.shelf.client.android.ui.screens.objects

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.shelf.client.android.R
import java.util.Locale

@Composable
fun SuggestObjectScreen(
    initialCategory: String = "",
    onBackClick: () -> Unit = {},
    onSubmitClick: (title: String, category: String, desc: String, hasImage: Boolean) -> Unit = { _, _, _, _ -> },
    onSelectCategoryClick: () -> Unit = {}
) {
    var objectName by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(initialCategory) }
    var otherCategoryName by rememberSaveable { mutableStateOf("") }
    var objectDescription by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(initialCategory) {
        if (initialCategory.isNotEmpty()) {
            selectedCategory = initialCategory
        }
    }
    
    var imageUriString by rememberSaveable { mutableStateOf<String?>(null) }
    val imageUri = imageUriString?.let { Uri.parse(it) }

    var showErrors by rememberSaveable { mutableStateOf(false) }
    
    var fileName by rememberSaveable { mutableStateOf("") }
    var fileSize by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    
    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            val info = getFileInfo(context, uri)
            fileName = info.first
            fileSize = info.second
        }
    }
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) {
                imageUriString = uri.toString()
            }
        }
    )

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
            text = "Предложить объект",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF352A26)
        )

        Spacer(modifier = Modifier.height(20.dp))

        val isCategoryOther = selectedCategory == "Другой раздел"
        
        val isNameEmptyError = showErrors && objectName.isBlank()
        val isCategoryEmptyError = showErrors && selectedCategory.isBlank()
        val isOtherCategoryEmptyError = showErrors && isCategoryOther && otherCategoryName.isBlank()
        
        val isNameExceeded = objectName.length > 200
        val isOtherNameExceeded = otherCategoryName.length > 100
        val isDescExceeded = objectDescription.length > 5000
        
        val hasAnyError = isNameEmptyError || isCategoryEmptyError || isOtherCategoryEmptyError || isNameExceeded || isOtherNameExceeded || isDescExceeded

        if (hasAnyError) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF0EE), shape = RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Исправьте ошибки в полях. Введённые данные сохранены.",
                    fontSize = 14.sp,
                    color = Color(0xFFB43D32),
                    lineHeight = 20.sp
                )
            }
        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFE8DA), shape = RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Объект появится в каталоге только после одобрения администратором.",
                    fontSize = 14.sp,
                    color = Color(0xFF9C6B52),
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        SuggestObjectTextField(
            title = "Название объекта",
            isRequired = true,
            value = objectName,
            onValueChange = { 
                objectName = it 
                if (showErrors && it.isNotBlank()) showErrors = false
            },
            placeholder = "Введите название объекта",
            maxLength = 200,
            hasError = isNameEmptyError,
            errorMessage = if (isNameEmptyError) "Введите название объекта" else if (isNameExceeded) "Название объекта не должно превышать 200 символов" else null
        )

        Spacer(modifier = Modifier.height(20.dp))


        SuggestObjectSelectorField(
            title = "Раздел каталога",
            isRequired = true,
            value = selectedCategory,
            placeholder = "Выберите раздел каталога",
            onClick = onSelectCategoryClick,
            hasError = isCategoryEmptyError,
            errorMessage = if (isCategoryEmptyError) "Выберите раздел каталога" else null
        )


        if (isCategoryOther) {
            Spacer(modifier = Modifier.height(20.dp))
            
            SuggestObjectTextField(
                title = "Название нового раздела",
                isRequired = true,
                value = otherCategoryName,
                onValueChange = { 
                    otherCategoryName = it 
                    if (showErrors && it.isNotBlank()) showErrors = false
                },
                placeholder = "Введите название нового раздела",
                maxLength = 100,
                hasError = isOtherCategoryEmptyError,
                errorMessage = if (isOtherCategoryEmptyError) "Укажите название нового раздела" else if (isOtherNameExceeded) "Название раздела не должно превышать 100 символов" else null,
                helperText = "Новый раздел будет создан только при необходимости и после одобрения администратором."
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        SuggestObjectTextField(
            title = "Описание объекта",
            isRequired = false,
            value = objectDescription,
            onValueChange = { objectDescription = it },
            placeholder = "Расскажите об объекте",
            maxLength = 5000,
            minHeight = 120.dp,
            hasError = false,
            errorMessage = if (isDescExceeded) "Описание не должно превышать 5000 символов" else null,
            helperText = "Необязательно до 5000 символов"
        )

        Spacer(modifier = Modifier.height(20.dp))


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Изображение объекта",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF352A26)
            )

            if (imageUri != null) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Выбранное изображение",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 70.dp, height = 92.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    )
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = fileName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF352A26)
                        )
                        
                        Text(
                            text = fileSize,
                            fontSize = 12.sp,
                            color = Color(0xFF84736B)
                        )
                        
                        Text(
                            text = "Заменить изображение",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFA54E32),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { 
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    ) 
                                }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(172.dp)
                        .background(Color(0xFFFFF8F4), shape = RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFF5C4A8), RoundedCornerShape(12.dp))
                        .clickable { 
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) 
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Загрузить изображение",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFA6541F)
                    )
                }
                
                Text(
                    text = "Необязательно",
                    fontSize = 12.sp,
                    color = Color(0xFF84736B),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {
                val isCategoryOther = selectedCategory == "Другой раздел"
                val isOtherCategoryInvalid = isCategoryOther && otherCategoryName.isBlank()
                
                val isNameExceeded = objectName.length > 200
                val isOtherNameExceeded = otherCategoryName.length > 100
                val isDescExceeded = objectDescription.length > 5000
                
                if (objectName.isBlank() || selectedCategory.isBlank() || isOtherCategoryInvalid || isNameExceeded || isOtherNameExceeded || isDescExceeded) {
                    showErrors = true
                } else {
                    showErrors = false
                    val finalCategory = if (isCategoryOther) otherCategoryName else selectedCategory
                    onSubmitClick(objectName, finalCategory, objectDescription, imageUri != null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA6541F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Отправить на модерацию",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(1.dp, Color(0xFFF5C4A8), RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF8F4)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Отмена",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFA6541F)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SuggestObjectTextField(
    title: String,
    isRequired: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    minHeight: Dp = 52.dp,
    hasError: Boolean = false,
    errorMessage: String? = null,
    helperText: String? = null
) {
    val isLengthExceeded = value.length > maxLength
    val showRed = hasError || isLengthExceeded
    val borderColor = if (showRed) Color(0xFFB43D32) else Color(0xFFF5C4A8)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF3D2B1F))) {
                    append(title)
                }
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Color(0xFFB43D32))) {
                        append(" *")
                    }
                }
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .background(Color(0xFFFFF8F4), shape = RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF3D2B1F),
                    lineHeight = 22.sp
                ),
                cursorBrush = SolidColor(Color(0xFFA6541F)),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFFC49880),
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        )
                    }
                    innerTextField()
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            if (showRed && errorMessage != null) {
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    color = Color(0xFFB43D32),
                    modifier = Modifier.weight(1f)
                )
            } else if (helperText != null) {
                Text(
                    text = helperText,
                    fontSize = 12.sp,
                    color = Color(0xFF84736B),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = "${value.length} / $maxLength",
                fontSize = 12.sp,
                color = if (isLengthExceeded) Color(0xFFB43D32) else Color(0xFF84736B),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun SuggestObjectSelectorField(
    title: String,
    isRequired: Boolean,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    hasError: Boolean = false,
    errorMessage: String? = null
) {
    val borderColor = if (hasError) Color(0xFFB43D32) else Color(0xFFF5C4A8)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF3D2B1F))) {
                    append(title)
                }
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Color(0xFFB43D32))) {
                        append(" *")
                    }
                }
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(Color(0xFFFFF8F4), shape = RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value.ifEmpty { placeholder },
                color = if (value.isEmpty()) Color(0xFFC49880) else Color(0xFF3D2B1F),
                fontSize = 16.sp
            )
            
            Text(
                text = "⌄",
                color = Color(0xFF9C6B52),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        if (hasError && errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFB43D32)
            )
        }
    }
}

fun getFileInfo(context: Context, uri: Uri): Pair<String, String> {
    var fileName = "unknown"
    var fileSize = "Размер неизвестен"
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst()) {
            if (nameIndex != -1) fileName = cursor.getString(nameIndex)
            if (sizeIndex != -1) {
                val sizeBytes = cursor.getLong(sizeIndex)
                if (sizeBytes > 0) {
                    val sizeKb = sizeBytes / 1024
                    val sizeMb = sizeKb / 1024.0
                    fileSize = if (sizeMb >= 1.0) {
                        String.format(Locale.getDefault(), "%.1f МБ", sizeMb)
                    } else {
                        "$sizeKb КБ"
                    }
                }
            }
        }
    }
    return Pair(fileName, fileSize)
}

@Preview(name = "1. Пустая форма", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewSuggestObjectScreenEmpty() {
    SuggestObjectScreen()
}

@Preview(name = "2. Заполненная форма", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewSuggestObjectScreenFilled() {
    SuggestObjectScreen(initialCategory = "Книги")
}

@Preview(name = "3. Ошибка валидации", showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewSuggestObjectScreenError() {
    SuggestObjectScreen() // Without hoisting state, previewing pure error requires clicking "Submit" in interactive mode
}
