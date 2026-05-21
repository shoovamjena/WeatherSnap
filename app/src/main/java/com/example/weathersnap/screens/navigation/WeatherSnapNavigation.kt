package com.example.weathersnap.screens.navigation

import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weathersnap.screens.camera.CustomCameraScreen
import com.example.weathersnap.screens.create_report.CreateReportScreen
import com.example.weathersnap.screens.create_report.CreateReportViewModel
import com.example.weathersnap.screens.saved_reports.SavedReportsScreen
import com.example.weathersnap.screens.weather_search.WeatherData
import com.example.weathersnap.screens.weather_search.WeatherScreen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSnapNavigation() {
    val navController = rememberNavController()
    var capturedImagePath by rememberSaveable { mutableStateOf<String?>(null) }
    var originalImageSizeMb by rememberSaveable { mutableStateOf<Double?>(null) }
    var compressedImageSizeMb by rememberSaveable { mutableStateOf<Double?>(null) }

    NavHost(
        navController = navController,
        startDestination = "weather_search",
        enterTransition = {
            fadeIn(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 1.08f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + scaleIn(
                initialScale = 1.08f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 0.92f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        }
    ) {
        composable("weather_search") {
            WeatherScreen(
                onNavigateToCreateReport = { weatherData ->
                    capturedImagePath = null
                    originalImageSizeMb = null
                    compressedImageSizeMb = null
                    val city = Uri.encode(weatherData.cityName)
                    val temp = Uri.encode(weatherData.temperature)
                    val condition = Uri.encode(weatherData.condition)
                    val hum = Uri.encode(weatherData.humidity)
                    val wind = Uri.encode(weatherData.windSpeed)
                    val press = Uri.encode(weatherData.pressure)

                    navController.navigate(
                        "create_report/$city/$temp/$condition/$hum/$wind/$press"
                    )
                },
                onNavigateToSavedReports = {
                    navController.navigate("saved_reports")
                }
            )
        }

        composable(
            route = "create_report/{cityName}/{temp}/{condition}/{humidity}/{wind}/{pressure}",
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType },
                navArgument("temp") { type = NavType.StringType },
                navArgument("condition") { type = NavType.StringType },
                navArgument("humidity") { type = NavType.StringType },
                navArgument("wind") { type = NavType.StringType },
                navArgument("pressure") { type = NavType.StringType },
            )
        ) { backStackEntry ->

            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            val temp = backStackEntry.arguments?.getString("temp") ?: ""
            val condition = backStackEntry.arguments?.getString("condition") ?: ""
            val humidity = backStackEntry.arguments?.getString("humidity") ?: ""
            val wind = backStackEntry.arguments?.getString("wind") ?: ""
            val pressure = backStackEntry.arguments?.getString("pressure") ?: ""

            val frozenWeatherData = WeatherData(
                cityName = cityName,
                temperature = temp,
                condition = condition,
                humidity = humidity,
                windSpeed = wind,
                pressure = pressure
            )

            val createViewModel: CreateReportViewModel = hiltViewModel()
            val capturedImageUri = capturedImagePath?.toUri()

            CreateReportScreen(
                weatherData = frozenWeatherData,
                capturedImageUri = capturedImageUri,
                originalImageSizeMb = originalImageSizeMb,
                compressedImageSizeMb = compressedImageSizeMb,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate("camera") },
                onSaveReport = { notes ->
                    val reportToSave = com.example.weathersnap.screens.saved_reports.SavedReport(
                        cityName = frozenWeatherData.cityName,
                        temperature = frozenWeatherData.temperature,
                        condition = frozenWeatherData.condition,
                        humidity = frozenWeatherData.humidity,
                        windSpeed = frozenWeatherData.windSpeed,
                        pressure = frozenWeatherData.pressure,
                        notes = notes,
                        imagePath = capturedImagePath ?: "",
                        originalSizeMb = String.format(Locale.US, "%.2f", originalImageSizeMb ?: 0.0),
                        compressedSizeMb = String.format(Locale.US, "%.2f", compressedImageSizeMb ?: 0.0),
                        timestamp = System.currentTimeMillis()
                    )

                    createViewModel.saveReport(reportToSave) {
                        navController.navigate("saved_reports") {
                            popUpTo("weather_search")
                        }
                    }
                }
            )
        }

        composable("camera") {
            CustomCameraScreen(
                onImageCaptured = { uri, originalSize, compressedSize ->
                    capturedImagePath = uri.toString()
                    originalImageSizeMb = originalSize
                    compressedImageSizeMb = compressedSize
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }

        composable("saved_reports") {
            SavedReportsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}