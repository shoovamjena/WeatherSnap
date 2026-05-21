package com.example.weathersnap.screens.saved_reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SavedReport(
    val id: Int = 0,
    val cityName: String,
    val temperature: String,
    val condition: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String,
    val notes: String,
    val imagePath: String,
    val originalSizeMb: String,
    val compressedSizeMb: String,
    val timestamp: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedReportsScreen(
    viewModel: SavedReportsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val reports by viewModel.reports.collectAsState()

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
                    .padding(horizontal = 20.dp),
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
                                text = "Saved Reports",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            AutoSizeText(
                                text = "${reports.size} saved reports locally",
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

                if (reports.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📂",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AutoSizeText(
                                text = "No reports saved yet.",
                                color = Color.White.copy(0.5f),
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(reports) { report ->
                            ReportCard(report = report)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: SavedReport) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff35352d), RoundedCornerShape(15.dp))
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = report.imagePath),
                contentDescription = "Report Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AutoSizeText(
                    text = report.cityName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                val dateString = sdf.format(Date(report.timestamp))

                AutoSizeText(
                    text = "${report.condition} • $dateString",
                    color = Color.White.copy(0.5f),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            AutoSizeText(
                text = "${report.temperature}°C",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xffbfcd81)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MiniStatBox("Humidity", "${report.humidity}%", Color(0xff2e7964), Modifier.weight(1f))
            MiniStatBox("Wind", "${report.windSpeed} m/s", Color(0xff335c8f), Modifier.weight(1f))
            MiniStatBox("Pressure", report.pressure, Color(0xff775218), Modifier.weight(1f))
        }

        if (report.notes.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(0.2f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    AutoSizeText("Field Notes", color = Color.White.copy(0.5f), fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.notes,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoSizeText(
                text = "Original File: ${report.originalSizeMb} MB",
                color = Color.White.copy(0.3f),
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            AutoSizeText(
                text = "Compressed File: ${report.compressedSizeMb} MB",
                color = Color(0xffbfcd81),
                fontSize = 10.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MiniStatBox(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color.copy(0.15f), RoundedCornerShape(10.dp))
    ) {
        Column(Modifier.padding(10.dp)) {
            AutoSizeText(label, color = Color.White.copy(0.5f), fontSize = 10.sp)
            AutoSizeText(value, color = color, fontSize = 14.sp)
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