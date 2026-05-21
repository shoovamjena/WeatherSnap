package com.example.weathersnap.screens.create_report

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weathersnap.screens.weather_search.WeatherData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    weatherData: WeatherData,
    capturedImageUri: Uri?,
    originalImageSizeMb: Double? = null,
    compressedImageSizeMb: Double? = null,
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onSaveReport: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var notes by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xff2b3009),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xffbfcd81), RoundedCornerShape(15.dp))
                ) {
                    Row(
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            AutoSizeText(
                                text = "Create Report",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            AutoSizeText(
                                text = "Capture evidence and save to local DB",
                                color = Color.Black.copy(0.7f),
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(Color.Black),
                            shape = RoundedCornerShape(25)
                        ) {
                            AutoSizeText(
                                text = "Back",
                                color = Color(0xffbfcd81),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xff35352d), RoundedCornerShape(15.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            AutoSizeText(
                                text = weatherData.cityName,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            AutoSizeText(
                                text = weatherData.condition,
                                color = Color.White.copy(0.5f),
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        AutoSizeText(
                            text = "${weatherData.temperature}°C",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            Modifier
                                .weight(1f)
                                .background(Color(0xff2e7964).copy(0.15f), RoundedCornerShape(10.dp))
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                AutoSizeText("Humidity", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                AutoSizeText(
                                    "${weatherData.humidity}%",
                                    color = Color(0xff2e7964),
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .background(Color(0xff335c8f).copy(0.15f), RoundedCornerShape(10.dp))
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                AutoSizeText("Wind", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                AutoSizeText("${weatherData.windSpeed} m/s", color = Color(0xff335c8f), fontSize = 14.sp)
                            }
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .background(Color(0xff775218).copy(0.15f), RoundedCornerShape(10.dp))
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                AutoSizeText("Pressure", color = Color.White.copy(0.5f), fontSize = 10.sp)
                                AutoSizeText(weatherData.pressure, color = Color(0xff775218), fontSize = 14.sp)
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xff35352d), RoundedCornerShape(15.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(0xff424338))
                            .border(
                                width = 1.dp,
                                color = if (capturedImageUri == null) Color.White.copy(0.1f) else Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = capturedImageUri,
                            transitionSpec = {
                                fadeIn(tween(400)) togetherWith fadeOut(tween(400))
                            },
                            label = "ImagePreviewTransition"
                        ) { targetUri ->
                            if (targetUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = targetUri),
                                    contentDescription = "Captured Evidence",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AutoSizeText(
                                    text = "Photo preview",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    if (originalImageSizeMb != null && compressedImageSizeMb != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AutoSizeText(
                                text = "Original: ${String.format("%.2f", originalImageSizeMb)} MB",
                                color = Color.White.copy(0.5f),
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AutoSizeText(
                                text = "Compressed: ${String.format("%.2f", compressedImageSizeMb)} MB",
                                color = Color(0xffbfcd81),
                                fontSize = 10.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onNavigateToCamera()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xffbfcd81)),
                        shape = RoundedCornerShape(50)
                    ) {
                        AutoSizeText(
                            text = if (capturedImageUri == null) "Capture Photo" else "Retake Photo",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xff35352d), RoundedCornerShape(15.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    AutoSizeText(
                        text = "Field Notes",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = {
                            Text("Notes...", color = Color.White.copy(0.3f), maxLines = 1)
                        },
                        shape = RoundedCornerShape(10.dp),
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xffbfcd81),
                            unfocusedBorderColor = Color.White.copy(0.1f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(0.8f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onSaveReport(notes)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xffbfcd81)),
                        shape = RoundedCornerShape(50),
                        enabled = capturedImageUri != null
                    ) {
                        AutoSizeText(
                            text = "Save Report",
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 4.dp),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    minFontSize: TextUnit = 8.sp
) {
    var scaledTextStyle by remember {
        mutableStateOf(TextStyle(fontSize = fontSize, fontWeight = fontWeight, textAlign = textAlign?: TextAlign.Center))
    }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        color = color,
        maxLines = 1,
        style = scaledTextStyle,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth || textLayoutResult.didOverflowHeight) {
                if (scaledTextStyle.fontSize.value > minFontSize.value) {
                    scaledTextStyle = scaledTextStyle.copy(fontSize = (scaledTextStyle.fontSize.value * 0.9f).sp)
                } else {
                    readyToDraw = true
                }
            } else {
                readyToDraw = true
            }
        }
    )
}