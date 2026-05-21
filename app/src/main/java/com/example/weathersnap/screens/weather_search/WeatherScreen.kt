package com.example.weathersnap.screens.weather_search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weathersnap.screen.weather_search.WeatherViewModel



data class WeatherData(
    val cityName: String,
    val temperature: String,
    val condition: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String
)

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@ExperimentalMaterial3Api
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onNavigateToCreateReport:(WeatherData) -> Unit,
    onNavigateToSavedReports:() -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.citySuggestions.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xff2b3009)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0.5f to Color(0xffbfcd81),
                                0.9f to Color(0xffa3d0c0)
                            )
                        ), RoundedCornerShape(15)
                    )
            ) {
                Row(
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WeatherSnap",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Text(
                            "Live weather reports with camera evidence",
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                    }
                    Button(
                        onClick = { onNavigateToSavedReports() },
                        colors = ButtonDefaults.buttonColors(Color(0xff2d3400)),
                        shape = RoundedCornerShape(25)
                    ) {
                        Text(
                            "Reports",
                            color = Color(0xffbfcd81),
                            fontSize = 10.sp
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
            ) {
                val focusManager = LocalFocusManager.current // Used to hide keyboard on click

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black, RoundedCornerShape(15))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = query,
                        onValueChange = { viewModel.onQueryChanged(it) },
                        label = {
                            Text(
                                "City",
                                color = Color(0xffbfcd81),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        viewModel.onQueryChanged("") // Empties the text field
                                        viewModel.clearSuggestions() // Closes the dropdown
                                    }
                                ) {
                                    Text(
                                        text = "✕",
                                        color = Color(0xffbfcd81),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text("Enter city", color = Color(0xffbfcd81).copy(0.5f))
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Search // Changes Enter key to a Search magnifying glass
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus() // Hide keyboard
                                viewModel.onManualSearch() // Trigger search
                            }
                        ),
                        shape = RoundedCornerShape(15),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(0.5f)
                        ),
                        supportingText = if (query.trim().length <= 2) {
                            {
                                Text(
                                    "Enter more than 2 letters to start city suggestion",
                                    fontSize = 9.sp,
                                    color = Color(0xffbfcd81)
                                )
                            }
                        } else null
                    )
                    Spacer(Modifier.width(20.dp))
                    Button(
                        onClick = {
                            focusManager.clearFocus() // Hide keyboard
                            viewModel.onManualSearch() // Trigger search
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xffbfcd81)),
                        shape = RoundedCornerShape(50),
                    ) {
                        Text("Search", color = Color.Black)
                    }
                }

                val isDropdownVisible = suggestions.isNotEmpty()


                AnimatedVisibility(
                    visible = isDropdownVisible,
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, 0) {
                            placeable.placeRelative(0, 0)
                        }
                    },
                    enter = fadeIn(tween(100)),
                    exit = fadeOut(tween(100))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .padding(top = 8.dp)
                    ) {
                        suggestions.forEachIndexed { index, city ->

                            key(city.id) {
                                var isItemPopVisible by remember { mutableStateOf(false) }


                                LaunchedEffect(Unit) {
                                    isItemPopVisible = true
                                }


                                val scale by animateFloatAsState(
                                    targetValue = if (isItemPopVisible) 1f else 0.5f,
                                    animationSpec = tween(durationMillis = 350, delayMillis = index * 60),
                                    label = "scale"
                                )
                                val alpha by animateFloatAsState(
                                    targetValue = if (isItemPopVisible) 1f else 0f,
                                    animationSpec = tween(durationMillis = 350, delayMillis = index * 60),
                                    label = "alpha"
                                )

                                val currentShape = dropdownItemShape(index, suggestions.size)

                                Column(
                                    modifier = Modifier.graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = alpha
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xffbfcd81),
                                                shape = currentShape
                                            )
                                            .clip(currentShape)
                                            .clickable {
                                                focusManager.clearFocus() // Hide keyboard!
                                                viewModel.onCitySelected(city)
                                            }
                                            .padding(horizontal = 16.dp, vertical = 14.dp)
                                    ) {
                                        Text(
                                            text = "${city.name}, ${city.country}",
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (index < suggestions.lastIndex) {
                                        HorizontalDivider(
                                            thickness = 2.dp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff35352d), RoundedCornerShape(15.dp))
                    .padding(20.dp)
                    .zIndex(0f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                when (weatherState) {
                    is WeatherUiState.Idle -> {
                        Text(
                            text = "Search for a city to see weather data.",
                            color = Color.White.copy(0.5f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is WeatherUiState.Loading -> {
                        ContainedLoadingIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            containerColor = Color(0xffbfcd81),
                            indicatorColor = Color(0xff2d3400)
                        )
                    }
                    is WeatherUiState.Error -> {
                        Text(
                            text = (weatherState as WeatherUiState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is WeatherUiState.Success -> {
                        val weather = (weatherState as WeatherUiState.Success).data

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = weather.cityName,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = weather.condition,
                                    color = Color.White.copy(0.5f)
                                )
                            }
                            Box(
                                Modifier.background(
                                    Color(0xff424b07),
                                    RoundedCornerShape(15)
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${weather.temperature}°C",
                                    fontSize = 24.sp,
                                    color = Color(0xffbfcd81),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Box(
                                Modifier
                                    .weight(1f)
                                    .background(
                                        Color(0xff2e7964).copy(0.15f),
                                        RoundedCornerShape(15)
                                    )
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(
                                        "Humidity",
                                        color = Color.White.copy(0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "${weather.humidity}%",
                                        color = Color(0xff2e7964),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Box(
                                Modifier
                                    .weight(1f)
                                    .background(
                                        Color(0xff335c8f).copy(0.15f),
                                        RoundedCornerShape(15)
                                    )
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(
                                        "Wind",
                                        color = Color.White.copy(0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "${weather.windSpeed} m/s",
                                        color = Color(0xff335c8f),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Box(
                                Modifier
                                    .weight(1f)
                                    .background(
                                        Color(0xff775218).copy(0.15f),
                                        RoundedCornerShape(15)
                                    )
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(
                                        "Pressure",
                                        color = Color.White.copy(0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = weather.pressure,
                                        color = Color(0xff775218),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xff424338), RoundedCornerShape(15))
                        .padding(20.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Report readiness",
                            color = Color.White.copy(0.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            "Camera and Room DB enabled",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }

                Button(
                    onClick = {
                        if (weatherState is WeatherUiState.Success) {
                            val currentData = (weatherState as WeatherUiState.Success).data
                            onNavigateToCreateReport(currentData)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xffbfcd81)),
                    enabled = weatherState is WeatherUiState.Success
                ) {
                    Text(
                        "Create Report",
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun dropdownItemShape(index: Int, size: Int): androidx.compose.ui.graphics.Shape {
    return when {
        size == 1 -> RoundedCornerShape(20.dp)
        index == 0 -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        index == size - 1 -> RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
        else -> androidx.compose.ui.graphics.RectangleShape
    }
}